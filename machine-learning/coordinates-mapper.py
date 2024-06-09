# This script is created to create coordinate list for data scraping.

import os
import csv
import math
import folium
import pandas as pd
import geopandas as gpd

from shapely.geometry import Point
from geopy.distance import geodesic


# Function to map coordinates for Bali Island in hexagonal packing
def get_row(start_coord, radius, count):
    coord_data = []
    for _ in range(count):
        next_coord = geodesic(meters=radius * 2).destination(start_coord, 90)

        coord_data.append([next_coord.latitude, next_coord.longitude])
        start_coord = next_coord
    return coord_data


def coord_mapper(start_coord, radius, row_count, height_count):
    coord_data = []
    height = radius * math.sqrt(3)
    for _ in range(height_count):
        direction = 90 if _ % 2 == 0 else 270
        next_coord = geodesic(meters=radius).destination(
            geodesic(meters=height).destination(start_coord, 180), direction
        )
        coord_data.extend(get_row(next_coord, radius, row_count))
        start_coord = next_coord
    return coord_data


# Function to export coordinates to csv
def coord_to_csv(data, filename):
    file_path = 'coordinates/bali_island_dirty.csv'
    with open(file_path, mode='w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(['latitude', 'longitude'])
        writer.writerows(data)


# Function to filter points based on land and forest
def land_filter(shape_gdf, points_df):
    geometry = [Point(xy) for xy in zip(points_df['longitude'], points_df['latitude'])]
    points_gdf = gpd.GeoDataFrame(points_df, geometry=geometry)
    points_gdf.set_crs(crs=shape_gdf.crs, allow_override=True, inplace=True)
    points_inland = gpd.sjoin(points_gdf, shape_gdf, how='inner', predicate='within')
    filtered_df = points_df[points_df.index.isin(points_inland.index)]
    filtered_df.reset_index(drop=True, inplace=True)
    return filtered_df


def forest_filter(shape_gdf, points_df):
    geometry = [Point(xy) for xy in zip(points_df['longitude'], points_df['latitude'])]
    points_gdf = gpd.GeoDataFrame(points_df, geometry=geometry)
    points_gdf.set_crs(crs=shape_gdf.crs, allow_override=True, inplace=True)
    points_inforest = gpd.sjoin(points_gdf, shape_gdf, how='left', predicate='within')
    points_outforest = points_inforest[points_inforest['index_right'].isna()]
    filtered_df = points_df[points_df.index.isin(points_outforest.index)]
    filtered_df.reset_index(drop=True, inplace=True)
    return filtered_df


# Function to export map to html
def map_exporter(data):
    map_export = folium.Map(location=[data['latitude'][0], data['longitude'][0]], zoom_start=10)
    for latitude, longitude in data.values:
        folium.Marker([latitude, longitude]).add_to(map_export)
    map_export.save('map-export/bali_island_filtered.html')
    return


def main():
    point_radius = 500
    start_point = (-8.096568, 114.437477)
    map_length, map_height = 140, 100
    dirty_coordinates = coord_mapper(start_point, point_radius, map_length, map_height)
    coord_to_csv(dirty_coordinates, 'bali_island_dirty')

    file_path = 'shape-filter'
    shapefiles = [file for file in os.listdir(file_path) if file.endswith('.shp')]

    forest_gdf = [gpd.read_file(os.path.join(file_path, file)) for file in shapefiles]
    forest_gdf = gpd.GeoDataFrame(pd.concat(forest_gdf, ignore_index=True))

    land_gdf = gpd.read_file('land-filter/indonesia_province.shp')
    coord_df = pd.read_csv('coordinates/bali_island_dirty.csv')

    land_filtered = land_filter(land_gdf, coord_df)
    forest_filtered = forest_filter(forest_gdf, land_filtered)

    map_exporter(forest_filtered)
    forest_filtered.to_csv('coordinates/bali_island_clean.csv', index=False)


if __name__ == '__main__':
    main()
