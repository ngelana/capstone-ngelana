import os
import logging
import numpy as np
import pandas as pd

from tensorflow.keras.models import load_model
from sklearn.preprocessing import StandardScaler

from typing import Tuple, List, Optional
from functools import cache
from pathlib import Path

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

base_dir = Path(__file__).resolve().parents[1]
model_dir_path = base_dir / 'ml_resources' / 'keras_model'
data_dir_path = base_dir / 'ml_resources' / 'other'


@cache
def load_keras_model():
    model_path = os.path.join(model_dir_path, 'user_pref.keras')
    try:
        model = load_model(model_path)
        return model
    except FileNotFoundError as e:
        logger.error(msg=f"Keras model not found: {e}", exc_info=True)
        raise FileNotFoundError("Keras model not found.")
    except Exception as e:
        logger.error(msg=f"An error occurred: {e}", exc_info=True)
        raise Exception("An error occurred")


@cache
def load_data() -> Tuple[List[str], pd.DataFrame, pd.DataFrame, pd.DataFrame]:
    path_place_types = os.path.join(data_dir_path, 'place_types.csv')
    path_place_reviews = os.path.join(data_dir_path, 'place_reviews.csv')
    path_user_preferences = os.path.join(data_dir_path, 'user_preferences.csv')
    path_merged_data = os.path.join(data_dir_path, 'merged_data.csv')
    try:
        data_place_types = pd.read_csv(path_place_types, header=None)[0].tolist()
        data_place_reviews = pd.read_csv(path_place_reviews)
        data_user_preferences = pd.read_csv(path_user_preferences)
        data_merged_data = pd.read_csv(path_merged_data)
    except FileNotFoundError as e:
        logger.error(msg=f"Data file not found: {e}", exc_info=True)
        raise FileNotFoundError("Data file not found")
    except Exception as e:
        logger.error(msg=f"An error occurred: {e}", exc_info=True)
        raise Exception("An error occurred")

    return data_place_types, data_place_reviews, data_user_preferences, data_merged_data


@cache
def prepare_system() -> Tuple[List[str], pd.DataFrame, pd.DataFrame, pd.DataFrame, StandardScaler]:
    try:
        place_types, place_reviews, user_preferences, merged_data = load_data()

        user_features = merged_data[[f'{ptype}_user' for ptype in place_types]].values
        place_features = merged_data[place_types].values

        scaler = StandardScaler()
        _ = scaler.fit_transform(user_features)
        _ = scaler.fit_transform(place_features)

        return place_types, place_reviews, user_preferences, merged_data, scaler
    except Exception as e:
        logger.error(msg=f"An error occurred: {e}", exc_info=True)
        raise Exception("An error occurred")


def user_based(input_pref: List[str], user_pref: Optional[List[str]] = None, top_n: int = 20) -> List[str]:
    place_types, place_reviews, user_preferences, merged_data, scaler = prepare_system()
    model = load_keras_model()

    try:
        if user_pref is None:
            user_pref_data = np.random.randint(2, size=(1, len(place_types)))
        else:
            user_pref_data = pd.DataFrame(data=[[0] * len(place_types)], columns=place_types)
            user_pref_data.loc[:, input_pref] = 1
            user_pref_data = user_pref_data.values

        filtered_place = place_reviews[place_reviews[input_pref].any(axis=1)]

        place_features = filtered_place[place_types].drop_duplicates().values
        place_ids = filtered_place['id'].drop_duplicates().values

        place_features_scaled = scaler.transform(place_features)
        user_pref_scaled = scaler.transform(np.repeat(user_pref_data, len(place_features), axis=0))

        predictions = model.predict([user_pref_scaled, place_features_scaled])

        top_indices = np.argsort(predictions[:, 0])[-top_n:][::-1]
        recommended_places_ids = place_ids[top_indices]
        recommended_places = filtered_place[filtered_place['id'].isin(recommended_places_ids)]

        sorted_recommended_places = recommended_places.sort_values(by='rating', ascending=False)
        sorted_recommended_places = sorted_recommended_places.drop_duplicates(subset=['name'])

        return sorted_recommended_places['id'].values.tolist()
    except Exception as e:
        logger.error(msg=f"An error occurred: {e}", exc_info=True)
        raise Exception("An error occurred")
