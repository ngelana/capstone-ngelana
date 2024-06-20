import numpy as np
import pandas as pd

from tensorflow.keras.models import load_model
from sklearn.preprocessing import StandardScaler

from functools import cache


@cache
def load_keras_model():
    try:
        model = load_model('user_pref_model.keras')
        return model
    except FileNotFoundError:
        raise FileNotFoundError("Keras model not found.")


@cache
def load_data():
    try:
        user_pref_path = 'ml_dataset/user_preferences.csv'
        user_pref_data = pd.read_csv(user_pref_path)
    except FileNotFoundError:
        raise FileNotFoundError("User reviews data not found.")

    try:
        place_types_path = 'ml_dataset/place_types.csv'
        place_types_data = pd.read_csv(place_types_path)
    except FileNotFoundError:
        raise FileNotFoundError("Place types data not found.")

    try:
        user_review_path = 'ml_dataset/user_reviews.csv'
        user_review_data = pd.read_csv(user_review_path)
    except FileNotFoundError:
        raise FileNotFoundError("User reviews data not found.")

    return user_pref_data, place_types_data, user_review_data


@cache
def user_based(input_pref: list, user_pref: list | None = None, top_n: int = 10):
    user_pref_data, place_types_data, user_review_data = load_data()
    model = load_keras_model()
    scaler = StandardScaler()
    try:
        if user_pref:
            # One row of user preferences
            user_pref = np.array(user_pref)
        else:
            user_pref = np.random.randint(2, size=(1, len(place_types_data)))

        filtered_places = user_review_data[user_review_data[input_pref].any(axis=1)]

        place_features = filtered_places[place_types_data].drop_duplicates().values
        place_ids = filtered_places['id'].drop_duplicates().values

        place_features_scaled = scaler.transform(place_features)
        user_pref_scaled = scaler.transform(np.repeat(user_pref, len(place_features), axis=0))

        predictions = model.predict([place_features_scaled, user_pref_scaled])

        top_indices = np.argsort(predictions[:, 0])[-top_n:][::-1]
        recommend_places_ids = place_ids[top_indices]

        unique_recommendations = set(recommend_places_ids)
        recommend_places = filtered_places[filtered_places['id'].isin(unique_recommendations)]
        sorted_recommendations = recommend_places.sort_values(by='rating', ascending=False)
        sorted_recommendations = sorted_recommendations.drop_duplicates(subset=['name'])
        return sorted_recommendations[['id']].to_string(index=False).split()
    except Exception as e:
        raise Exception(f"An error occurred: {e}")
