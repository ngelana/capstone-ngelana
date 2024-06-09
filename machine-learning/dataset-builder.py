# The script below is for compliling 3k places data from Google Places API scraping into 1 dataset.

import os
import pandas as pd


def main():
    # Load the dataset
    dataset_path = 'scraping-output'
    dataset_files = [f for f in os.listdir(dataset_path) if f.endswith('.csv')]

    #  Get the headers
    first_file = pd.read_csv(f'{dataset_path}/{dataset_files[0]}')

    # Read the rest of the files without headers
    other_files = [pd.read_csv(f'{dataset_path}/{f}', header=0) for f in dataset_files[1:]]

    # Concatenate the files
    dataset = pd.concat([first_file] + other_files, ignore_index=True)

    # Save the combined dataset
    combined_path = 'combined-dataset/combined_dataset.csv'
    dataset.to_csv(combined_path, index=False)
    print(f'Compiled dataset saved to {combined_path}')


if __name__ == '__main__':
    main()
