from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 或指定你的前端 URL
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)