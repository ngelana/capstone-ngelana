import os
import numpy as np
import pandas as pd

from sklearn.preprocessing import LabelEncoder
from sklearn.metrics.pairwise import cosine_similarity
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences

from functools import cache


@cache
def load_reviews_data():
    base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    csv_path = os.path.join(base_dir, 'ml_resources', 'dataset', 'dataset_reviews.csv')
    try:
        data = pd.read_csv(csv_path)
        encoder = LabelEncoder()
        types_encoded = encoder.fit_transform(data['types'])
        tokenizer = Tokenizer()
        tokenizer.fit_on_texts(data['review'])
        sequences = tokenizer.texts_to_sequences(data['review'])
        max_length = max(map(len, sequences), default=0)
        padded_sequences = pad_sequences(sequences, maxlen=max_length)
        x_review = padded_sequences
        x_types = types_encoded
        return data, x_review, x_types

    except FileNotFoundError:
        raise FileNotFoundError("Reviews data not found.")
    except Exception as e:
        raise Exception(f"An error occurred: {e}")


@cache
def content_based(place_id: str, top_n: int = 10):
    try:
        data, x_review, x_types = load_reviews_data()
        place_index = data['id'].eq(place_id).idxmax()
        place_review = x_review[place_index]
        place_types = x_types[place_index]
        place_vector = np.concatenate([place_review, [place_types]])
        all_vectors = np.hstack([x_review, x_types.reshape(-1, 1)])
        similarities = cosine_similarity([place_vector], all_vectors)[0]
        similar_indices = np.argsort(similarities)[-top_n:][::-1]
        similar_indices = similar_indices[similar_indices != place_index][:top_n]
        similar_places = data['id'].iloc[similar_indices]
        return similar_places.to_string(index=False).split()

    except Exception as e:
        raise Exception(f"An error occurred: {e}")
