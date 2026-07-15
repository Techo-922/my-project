from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import psycopg2
from fastapi.middleware.cors import CORSMiddleware
from app_init import app
from connect import get_conn

# 请求体模型
class LoginRequest(BaseModel):
    username: str
    password: str

# 登录接口
@app.post("/login")
def login(data: LoginRequest):
    conn = get_conn()
    cur = conn.cursor()
    cur.execute("SELECT txx_username, txx_role, txx_name FROM Tianxx_users WHERE txx_username=%s AND txx_password=%s",
                (data.username, data.password))
    result = cur.fetchone()
    cur.close()
    conn.close()

    if result:
        return {
            "code": 0,
            "msg": "登录成功",
            "data": {
                "username": result[0],
                "role": result[1],
                "name": result[2],
                "token": "mock_token_" + result[0]
            }
        }
    else:
        raise HTTPException(status_code=401, detail="用户名或密码错误")
