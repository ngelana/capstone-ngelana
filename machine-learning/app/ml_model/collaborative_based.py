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
def collaborative_based(user_id: str, top_n: int = 10):
    try:
        return print("Collaborative based")
    except Exception as e:
        raise Exception(f"An error occurred: {e}")
