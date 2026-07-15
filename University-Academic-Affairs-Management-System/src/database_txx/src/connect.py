import psycopg2
# 数据库连接信息
conn_info = {
    "dbname": "db_zjut",
    "user": "db_user33",
    "password": "db_user33@123",
    "host": "110.41.127.77",
    "port": "8000"
}

def get_conn():
    return psycopg2.connect(**conn_info)