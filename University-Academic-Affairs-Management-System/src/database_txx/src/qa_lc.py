import os
import urllib.parse
from sqlalchemy import create_engine
from langchain_community.llms import Tongyi
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnablePassthrough
from app_init import app
from pydantic import BaseModel

# 连接信息（如果你还需要engine可以保留）
db_user = "db_user33"
db_password = "db_user33@123"
db_host = "127.0.0.1"
db_port = "8000"
db_name = "mis11"

encoded_password = urllib.parse.quote_plus(db_password)
engine = create_engine(f"postgresql+psycopg2://{db_user}:{encoded_password}@{db_host}:{db_port}/{db_name}")

# 初始化 LLM
os.environ["DASHSCOPE_API_KEY"] = "sk-5223dbde908343888cbd1377e707d38b"
llm_tongyi = Tongyi(model="qwen-plus")

# 你已有的数据库连接接口
def get_conn():
    import psycopg2
    return psycopg2.connect(
        user=db_user,
        password=db_password,
        host=db_host,
        port=db_port,
        database=db_name
    )

# 你需要实现一个获取数据库schema信息的函数，可以用SQL查询系统表来实现
def get_schema(_):
    # 举例查询表和字段名（请根据你的数据库调整）
    with get_conn() as conn:
        with conn.cursor() as cur:
            cur.execute("""
            SELECT table_name, column_name, data_type
            FROM information_schema.columns
            WHERE table_schema = 'public'
            ORDER BY table_name, ordinal_position
            """)
            rows = cur.fetchall()
            # 简单拼成字符串返回，方便后续LLM读取
            schema_info = ""
            current_table = None
            for table, column, dtype in rows:
                if table != current_table:
                    schema_info += f"\n表：{table}\n"
                    current_table = table
                schema_info += f"  字段：{column} 类型：{dtype}\n"
            return schema_info

class QARequest(BaseModel):
    question: str

@app.post("/langchain_qa")
def langchain_qa_api(data: QARequest):
    question = data.question

    # 构建生成 SQL 查询的 Prompt
    template = """根据所输入的数据库架构（包含表名、字段名及其描述），编写一个SQL查询来回答用户问题。查询内容可以用 * 表示。
数据库结构：
{schema}

问题: {question}
SQL查询格式：
"""

    prompt = ChatPromptTemplate.from_messages([
        ("system", "将用户的问题转换为 SQL 查询语句，只返回纯 SQL，无 markdown 格式"),
        ("human", template)
    ])

    sql_response = (
            RunnablePassthrough.assign(schema=get_schema)
            | prompt
            | llm_tongyi.bind(stop=["\nSQLResult:"])
            | StrOutputParser()
    )

    query = sql_response.invoke({"question": question})

    # 执行 SQL 查询
    def execute_query(sql):
        with get_conn() as conn:
            with conn.cursor() as cur:
                cur.execute(sql)
                try:
                    rows = cur.fetchall()
                    return str(rows)
                except Exception:
                    return "无返回结果"

    response = execute_query(query)

    # 构建生成自然语言答案的 Prompt
    template1 = """根据下表架构、问题、SQL查询和SQL响应，生成问题答案，返回纯文本：
{schema}

问题：{question}
SQL查询:{query}
SQL响应：{response}
答案：
"""

    prompt_response = ChatPromptTemplate.from_messages([
        ("system", "根据 SQL 响应，输出自然语言答案（无前缀）"),
        ("human", template1)
    ])

    answer_chain = (
            RunnablePassthrough.assign(
                schema=get_schema,
                question=lambda x: question,
                query=lambda x: query,
                response=lambda x: response
            )
            | prompt_response
            | llm_tongyi
    )

    answer = answer_chain.invoke({})
    return {"answer": answer}