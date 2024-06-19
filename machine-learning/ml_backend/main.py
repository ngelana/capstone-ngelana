import orjson
import uvicorn

from typing import Any
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException
from fastapi.encoders import jsonable_encoder

from fastapi_cache import FastAPICache, Coder
from fastapi_cache.decorator import cache
from fastapi_cache.backends.inmemory import InMemoryBackend

from content import content_based


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
        similar_places = content_based(place_id)
        return similar_places
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {e}")


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
