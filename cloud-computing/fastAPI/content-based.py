import pandas as pd
from sklearn.preprocessing import LabelEncoder
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences


def load_data(file_path: str):
    try:
        reviews_df = pd.read_csv(file_path)

        if 'types' not in reviews_df.columns or 'review' not in reviews_df.columns:
            raise ValueError("The dataset must contain 'types' and 'review' columns.")

        label_encoder = LabelEncoder()
        reviews_df['types_encoded'] = label_encoder.fit_transform(reviews_df['types'])

        tokenizer = Tokenizer()
        tokenizer.fit_on_texts(reviews_df['review'])
        sequences = tokenizer.texts_to_sequences(reviews_df['review'])

        max_sequence_length = max(len(seq) for seq in sequences)
        padded_sequences = pad_sequences(sequences, maxlen=max_sequence_length)

        features = {
            'review': padded_sequences,
            'types': reviews_df['types_encoded'].values
        }

        return reviews_df, features

    except FileNotFoundError:
        raise FileNotFoundError(f"The file at {file_path} was not found.")
    except pd.errors.EmptyDataError:
        raise ValueError("The CSV file is empty.")
    except ValueError as ve:
        raise ValueError(f"Value error: {ve}")
    except Exception as e:
        raise Exception(f"An unexpected error occurred: {e}")


reviews_file_path = 'final-dataset/review_dataset.csv'

def similar_places():
try:
    reviews_data, X = load_and_prepare_data(reviews_file_path)
except Exception as error:
    print(f"Error in data preparation: {error}")
    reviews_data, X = None, None
