from app_init import app
from connect import get_conn
from fastapi import HTTPException,UploadFile, File,Query,Body
from typing import Dict, List,Optional
from pydantic import BaseModel
import openpyxl
from psycopg2 import sql
from fastapi.responses import JSONResponse


@app.get("/departments")
def get_departments():
    conn = get_conn()
    cursor = conn.cursor()
    cursor.execute("""
        SELECT txx_DeptNo, txx_DeptName FROM Tianxx_Depts ORDER BY txx_DeptName
    """)
    rows = cursor.fetchall()
    cursor.close()
    conn.close()

    return {
        "code": 200,
        "message": "查询成功",
        "data": [
            {"id": row[0], "name": row[1]}
            for row in rows
        ]
    }

@app.get("/departments_get")
def get_departments(
    page: int = 1,
    size: int = 10,
    keyword: Optional[str] = Query("", description="专业名称或编号模糊搜索")
):
    conn = get_conn()
    cursor = conn.cursor()

    offset = (page - 1) * size
    keyword_pattern = f"%{keyword}%"

    # 查询数据
    query = """
        SELECT txx_DeptNo, txx_DeptName
        FROM Tianxx_Depts
        WHERE txx_DeptNo ILIKE %s OR txx_DeptName ILIKE %s
        ORDER BY txx_DeptNo
        LIMIT %s OFFSET %s
    """
    cursor.execute(query, (keyword_pattern, keyword_pattern, size, offset))
    rows = cursor.fetchall()

    # 查询总数
    cursor.execute("""
        SELECT COUNT(*) FROM Tianxx_Depts
        WHERE txx_DeptNo ILIKE %s OR txx_DeptName ILIKE %s
    """, (keyword_pattern, keyword_pattern))
    total = cursor.fetchone()[0]

    cursor.close()
    conn.close()

    return {
        "code": 200,
        "message": "查询成功",
        "data": [
            {
                "dept_no": row[0],
                "dept_name": row[1]
            } for row in rows
        ],
        "total": total
    }

class AddDeptForm(BaseModel):
    dept_no: str
    dept_name: str


@app.post("/departments_add")
def add_department(dept: dict):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        sql = """
            INSERT INTO Tianxx_Depts (txx_DeptNo, txx_DeptName)
            VALUES (%s, %s)
        """
        cursor.execute(sql, (dept['dept_no'], dept['dept_name']))
        conn.commit()

        cursor.close()
        conn.close()
        return {"code": 200, "message": "添加成功"}
    except Exception as e:
        print("添加失败：", e)
        return {"code": 500, "message": "添加失败"}


@app.put("/departments_update")
def update_department(dept: dict):
    try:
        conn = get_conn()
        cursor = conn.cursor()
        sql = """
            UPDATE Tianxx_Depts
            SET txx_DeptName = %s
            WHERE txx_DeptNo = %s
        """
        cursor.execute(sql, (dept["dept_name"], dept["dept_no"]))
        conn.commit()
        cursor.close()
        conn.close()
        return {"code": 200, "message": "修改成功"}
    except Exception as e:
        print("修改失败：", e)
        return {"code": 500, "message": "修改失败"}


@app.get("/departments/{dept_no}/classes")
def get_classes_by_department(dept_no: str):
    conn = get_conn()
    cursor = conn.cursor()
    cursor.execute("""
        SELECT txx_ClassID AS class_id, txx_ClassName AS class_name
        FROM Tianxx_Classes
        WHERE txx_DeptNo = %s
    """, (dept_no,))
    classes = [{"class_id": row[0], "class_name": row[1]} for row in cursor.fetchall()]
    cursor.close()
    conn.close()
    return {
        "code": 200,
        "classes": classes
    }

@app.delete("/departments_delete/{dept_no}")
def delete_department(dept_no: str):
    conn = get_conn()
    cursor = conn.cursor()

    try:
        # 先检查是否有班级依赖该专业，防止违反外键约束
        cursor.execute("SELECT COUNT(*) FROM Tianxx_Classes WHERE txx_DeptNo = %s", (dept_no,))
        count = cursor.fetchone()[0]
        if count > 0:
            raise HTTPException(status_code=400, detail="该专业下存在班级，不能删除")

        cursor.execute("DELETE FROM Tianxx_Depts WHERE txx_DeptNo = %s", (dept_no,))
        conn.commit()
        return {"code": 200, "message": "删除成功"}
    except Exception as e:
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        cursor.close()
        conn.close()

