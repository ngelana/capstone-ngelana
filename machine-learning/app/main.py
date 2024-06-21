import orjson
import uvicorn

from typing import Any
from pydantic import BaseModel
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException
from fastapi.encoders import jsonable_encoder

from fastapi_cache import FastAPICache, Coder
from fastapi_cache.decorator import cache
from fastapi_cache.backends.inmemory import InMemoryBackend

from .ml_model.collaborative_based import collaborative_based
from .ml_model.content_based import content_based
from .ml_model.user_based import user_based


class Preferences(BaseModel):
    user: list | None = None
    input: list


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
    FastAPICache.init(InMemoryBackend())
    yield
    await FastAPICache.clear()


app = FastAPI(lifespan=lifespan)


@app.get("/similar-places/{place_id}")
@cache(namespace="similar-places", expire=300, coder=CustomCoder)
async def get_similar_places(place_id: str):
    try:
        similar_places = content_based(place_id=place_id)
        return similar_places
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Layer main: {e}")


@app.post("/recommend-places/")
@cache(namespace="recommend-places", expire=300, coder=CustomCoder)
async def get_recommend_places(pref: Preferences):
    try:
        input_pref = pref.input
        user_pref = pref.user
        # TODO: Find out what the correct type passed to the user_based function should be
        recommend_places = user_based(input_pref, user_pref)
        return recommend_places
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {e}")


# TODO: @Antonius Miquel, Implement the user collaborative filtering function
@app.get("/user-collaborative/{user_id}")
@cache(namespace="user-collaborative", expire=300, coder=CustomCoder)
async def get_user_collaborative(user_id: str):
    try:
        result = collaborative_based(user_id)
        print(result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {e}")


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

# NOTE: If need to run using commandline use this command
# uvicorn app.main:app --reload

