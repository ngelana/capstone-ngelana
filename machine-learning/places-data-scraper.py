# This script is created to create a dataset for the machine learning model.

import os
import csv
import time
import requests
import logging
import pandas as pd

from datetime import datetime
from google.auth import default
from google.auth.transport.requests import Request
from requests.exceptions import RequestException

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def get_gcloud_credential():
    creds, _ = default()
    if not creds.valid:
        if creds.expired and creds.refresh_token:
            creds.refresh(Request())
    return creds


fields = ','.join([
    'places.id',
    'places.displayName',
    'places.formattedAddress',
    'places.googleMapsUri',
    'places.location',
    'places.primaryType',
    'places.types',
    'places.businessStatus',
    'places.internationalPhoneNumber',
    'places.priceLevel',
    'places.rating',
    'places.userRatingCount',
])

types = [
    'car_rental', 'art_gallery', 'museum', 'performing_arts_theater', 'amusement_park', 'cultural_center',
    'hiking_area', 'historical_landmark', 'national_park', 'night_club', 'park', 'tourist_attraction', 'zoo',
    'american_restaurant', 'bar', 'barbecue_restaurant', 'brazilian_restaurant', 'cafe', 'chinese_restaurant',
    'coffee_shop', 'fast_food_restaurant', 'french_restaurant', 'greek_restaurant', 'indian_restaurant',
    'indonesian_restaurant', 'italian_restaurant', 'japanese_restaurant', 'korean_restaurant',
    'lebanese_restaurant', 'mediterranean_restaurant', 'mexican_restaurant', 'middle_eastern_restaurant',
    'restaurant', 'seafood_restaurant', 'spanish_restaurant', 'steak_house', 'sushi_restaurant', 'thai_restaurant',
    'turkish_restaurant', 'vietnamese_restaurant', 'camping_cabin', 'cottage', 'guest_house', 'hostel', 'hotel',
    'lodging', 'motel', 'private_guest_room', 'resort_hotel'
]


def circle_search(cred, lat, lon, rad):
    api_url = 'https://places.googleapis.com/v1/places:searchNearby'

    header = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {cred.token}',
        'X-Goog-FieldMask': fields,
        'X-Goog-User-Project': cred.quota_project_id
    }

    payload = {
        'locationRestriction': {
            'circle': {
                'center': {
                    'latitude': lat,
                    'longitude': lon
                },
                'radius': rad
            }
        },
        'includedTypes': types
    }

    try:
        response = requests.post(api_url, headers=header, json=payload)
        response.raise_for_status()
        return response.json()
    except RequestException as error:
        logger.error(f'API error: {error}')
        raise
    except Exception as error:
        logger.error(f'Unexpected error: {error}')
        raise


def dataset_maker(data):
    now = datetime.now().strftime('%d-%m-%y_%H%M%S')
    banned_types = ['point_of_interest', 'establishment']
    file_path = f'scraping-output/export_{now}.csv'
    dataset = []

    for item in data.get('places', []):
        filtered_types = [t for t in item.get('types', []) if t not in banned_types]
        row = {
            'id': item.get('id', ),
            'name': item.get('displayName', {}).get('text', ),
            'latitude': item.get('location', {}).get('latitude', ),
            'longitude': item.get('location', {}).get('longitude', ),
            'address': item.get('formattedAddress', ),
            'url': item.get('googleMapsUri', ),
            'status': item.get('businessStatus', ),
            'phone': item.get('internationalPhoneNumber', ),
            'primary-type': item.get('primaryType', ),
            'types': ', '.join(filtered_types),
            'rating': item.get('rating', ),
            'rating-count': item.get('userRatingCount', ),
            'price-level': item.get('priceLevel', )
        }
        dataset.append(row)

    if dataset:
        os.makedirs('scraping-output', exist_ok=True)
        with open(file_path, mode='w', newline='', encoding='utf-8') as file:
            writer = csv.DictWriter(file, fieldnames=dataset[0].keys())
            writer.writeheader()
            writer.writerows(dataset)
        logger.info(f'Dataset saved to {file_path}')
    else:
        logger.warning('No data to save.')


def main():
    checkpoint_path = 'checkpoint.csv'
    if os.path.exists(checkpoint_path):
        checkpoint_df = pd.read_csv(checkpoint_path)
    else:
        checkpoint_df = pd.DataFrame(columns=['last_index'])
        checkpoint_df.to_csv(checkpoint_path, index=False)

    if not checkpoint_df.empty:
        start_index = checkpoint_df['last_index'].iloc[-1] + 1
    else:
        start_index = 0

    try:
        coordinates = pd.read_csv('coordinates/bali_island_clean.csv')
    except FileNotFoundError:
        logger.error('Coordinates file not found.')
        return
    except pd.errors.EmptyDataError:
        logger.error('Coordinates file is empty or invalid.')
        return

    credential = get_gcloud_credential()
    radius = 500

    for index in range(start_index, len(coordinates)):
        row = coordinates.iloc[index]
        lat = row['latitude']
        lon = row['longitude']

        try:
            data = circle_search(credential, lat, lon, radius)
            dataset_maker(data)
            logger.info(f'Scraped coordinate {lat}, {lon} successfully')

            checkpoint_df = pd.DataFrame({'last_index': [index]})
            checkpoint_df.to_csv(checkpoint_path, index=False)

            time.sleep(2)
        except Exception as error:
            logger.error(f'Error on coordinate {lat}, {lon}: {error}')
            checkpoint_df = pd.DataFrame({'last_index': [index - 1]})
            checkpoint_df.to_csv(checkpoint_path, index=False)
            break


if __name__ == '__main__':
    main()

