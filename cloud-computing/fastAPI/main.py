import numpy as np

from pydantic import BaseModel
from similar_places_data import reviews_data, X
from sklearn.metrics.pairwise import cosine_similarity
from fastapi import FastAPI, Response, HTTPException


class UserPreferences(BaseModel):
    input: str
    types: list


app = FastAPI()


# For similar place recommendation
# Example: ip:port/similar-places/ChIJwZQeZ7vZwokR5jX6v7JZ3Zg
# Return : Response JSON body with 10 similar places
@app.get("/similar-places/{place_id}")
async def content_based(place_id: str):
    try:
        place_idx = reviews_data[reviews_data['id'] == place_id].index[0]
        place_review = X['review'][place_idx]
        place_types = X['types'][place_idx]

        place_vector = np.concatenate([place_review, [place_types]])
        all_vectors = np.hstack([X['review'], X['types'].reshape(-1, 1)])
        similarities = cosine_similarity([place_vector], all_vectors)[0]

        similar_indices = np.argsort(similarities)[-10:][::-1]
        similar_places = reviews_data['id'].iloc[similar_indices]
        result_json = similar_places.to_json(orient='values')

        return Response(content=result_json, media_type="application/json")

    except IndexError:
        raise HTTPException(status_code=404, detail="Place ID not found")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal Server Error: {e}")

@app.post("/places-generate/")
async def generate_places(user_preferences: UserPreferences):
    try:
        print("hello")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal Server Error: {e}")
