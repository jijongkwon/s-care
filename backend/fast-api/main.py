from fastapi import FastAPI
from routers import stress

app = FastAPI()

app.include_router(stress.router)
