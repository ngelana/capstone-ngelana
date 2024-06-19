import orjson
import uvicorn
import numpy as np

from typing import Any
from pydantic import BaseModel
from contextlib import asynccontextmanager
from similar_places_data import reviews_data, X
from sklearn.metrics.pairwise import cosine_similarity
from fastapi import FastAPI, Response, HTTPException
from fastapi.encoders import jsonable_encoder

from fastapi_cache import FastAPICache, Coder
from fastapi_cache.backends.inmemory import InMemoryBackend
from fastapi_cache.decorator import cache


class ORJsonCoder(Coder):
    @classmethod
    def encode(cls, value: Any) -> bytes:
        return orjson.dumps(
            value,
            default=jsonable_encoder
        )

    @classmethod
    def decode(cls, value: bytes) -> Any:
        return orjson.loads(value)


class UserPreferences(BaseModel):
    input: str
    types: list


@asynccontextmanager
async def lifespan(api: FastAPI):
    FastAPICache.init(InMemoryBackend())
    yield
    await FastAPICache.clear()

app = FastAPI(lifespan=lifespan)


# For similar place recommendation
# Example: ip:port/similar-places/ChIJwZQeZ7vZwokR5jX6v7JZ3Zg
# Return : Response JSON body with 10 similar places
@app.get("/similar-places/{place_id}")
@cache(namespace="similar-places", expire=300)
async def content_based(place_id: str):
    try:
        place_idx = reviews_data[reviews_data['id'] == place_id].index[0]
        place_review = X['review'][place_idx]
        place_types = X['types'][place_idx]
        place_vector = np.concatenate([place_review, [place_types]])
        all_vectors = np.hstack([X['review'], X['types'].reshape(-1, 1)])
        similarities = cosine_similarity([place_vector], all_vectors)[0]
        similar_indices = np.argsort(similarities)[-10:][::-1]
        similar_places = reviews_data['id'].iloc[similar_indices].to_string(index=False).split()
        return similar_places

    except IndexError:
        raise HTTPException(status_code=404, detail="Place ID not found")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal Server Error: {e}")


@app.post("/places-generate/")
async def generate_places(user_preferences: UserPreferences):
    try:
        print("hello")
        test = user_preferences.input
        result_json = orjson.dumps(test)
        return Response(content=result_json, media_type="application/json")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal Server Error: {e}")


if __name__ == '__main__':
    uvicorn.run(app, host='0.0.0.0', port=8000)
