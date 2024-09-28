import orjson
import logging

from typing import Any, List, Optional
from pydantic import BaseModel, Field
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException
from fastapi.encoders import jsonable_encoder

from fastapi_cache import FastAPICache, Coder
from fastapi_cache.decorator import cache
from fastapi_cache.backends.inmemory import InMemoryBackend

from .ml_model.content_based import content_based
from .ml_model.user_based import user_based

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class Preferences(BaseModel):
    userPreferences: Optional[List[str]] = Field(default=None, description="User account preferences")
    inputPreferences: List[str] = Field(default=..., description="Input preferences for recommendation")


class CustomCoder(Coder):
    @classmethod
    def encode(cls, value: Any) -> bytes:
        return orjson.dumps(
            value,
            default=jsonable_encoder
        )

    @classmethod
    def decode(cls, value: bytes) -> Any:
        return orjson.loads(value)


@asynccontextmanager
async def lifespan(api: FastAPI):
    FastAPICache.init(InMemoryBackend(), prefix="fastapi-cache")
    yield
    await FastAPICache.clear()


app = FastAPI(lifespan=lifespan)


@app.get("/")
async def root():
    """
    Root endpoint to test the API.
    """
    return {"message": "Hello, this is Machine Learning API!"}


@app.get(path="/similar-places/{place_id}", summary="Get similar places", response_model=List[str])
@cache(namespace="similar-places", expire=300, coder=CustomCoder)
async def get_similar_places(place_id: str) -> List[str]:
    """
    **Get similar places based on content-based filtering**
    - Input: String of place id for which similar places are to be fetched.
    - Output: List of similar place ids.
    """
    try:
        similar_places = content_based(place_id)
        return similar_places
    except ValueError as e:
        logger.error(f"Invalid place ID in /similar-places: {e}", exc_info=True)
        raise HTTPException(status_code=400, detail=f"Invalid place id.")
    except Exception as e:
        logger.error(f"Error in /similar-places: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"An error occurred while fetching similar places.")


@app.post(path="/recommend-places/", summary="Get recommended places", response_model=List[str])
async def get_recommend_places(data: Preferences) -> List[str]:
    """
    **Get recommended places based on user preferences**
    - Input: Request body of user preferences and input preferences for recommendation.
    - Output: List of recommended place ids.
    """
    try:
        recommend_places = user_based(data.inputPreferences, data.userPreferences or [])
        return recommend_places
    except ValueError as e:
        logger.error(f"Invalid preferences in /recommend-places: {e}", exc_info=True)
        raise HTTPException(status_code=400, detail=f"Invalid input preferences.")
    except Exception as e:
        logger.error(f"Error in /recommend-places: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"An error occurred while fetching recommended places.")
