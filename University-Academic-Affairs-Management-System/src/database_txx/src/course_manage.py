from app_init import app
from connect import get_conn
from fastapi import HTTPException,UploadFile, File,Query,Body
from typing import Dict, List,Optional
from pydantic import BaseModel
import openpyxl
from psycopg2 import sql
from fastapi.responses import JSONResponse

@app.get("/courses")
def get_courses(
    page: int = 1,
    size: int = 20,
    keyword: Optional[str] = Query("", description="课程名称模糊搜索")
):
    conn = get_conn()
    cursor = conn.cursor()
    offset = (page - 1) * size

    query = """
        SELECT txx_Cno, txx_Cname, txx_CreditHour, txx_Hour, txx_AssessType
        FROM Tianxx_Courses
        WHERE 1=1
    """
    params = []

    if keyword:
        query += " AND txx_Cname ILIKE %s"
        params.append(f"%{keyword}%")

    query += " ORDER BY txx_Cno LIMIT %s OFFSET %s"
    params.extend([size, offset])

    cursor.execute(query, params)
    rows = cursor.fetchall()

    cursor.execute("SELECT COUNT(*) FROM Tianxx_Courses WHERE txx_Cname ILIKE %s", (f"%{keyword}%",))
    total = cursor.fetchone()[0]

    cursor.close()
    conn.close()

    return {
        "code": 200,
        "message": "查询成功",
        "data": [
            {
                "cno": row[0],
                "cname": row[1],
                "credit_hour": row[2],
                "hour": row[3],
                "assess_type": row[4]
            } for row in rows
        ],
        "total": total
    }


@app.post("/courses_add")
def add_course(course: dict = Body(...)):
    conn = get_conn()
    cursor = conn.cursor()

    cno = course.get("cno")
    cname = course.get("cname")
    credit_hour = course.get("credit_hour")
    hour = course.get("hour")
    assess_type = course.get("assess_type")

    # 查重
    cursor.execute("SELECT 1 FROM Tianxx_Courses WHERE txx_Cno = %s", (cno,))
    if cursor.fetchone():
        return JSONResponse(status_code=400, content={"message": "课程编号已存在"})

    cursor.execute("""
        INSERT INTO Tianxx_Courses (txx_Cno, txx_Cname, txx_CreditHour, txx_Hour, txx_AssessType)
        VALUES (%s, %s, %s, %s, %s)
    """, (cno, cname, credit_hour, hour, assess_type))

    conn.commit()
    cursor.close()
    conn.close()

    return {"code": 200, "message": "添加成功"}


@app.put("/courses/{cno}")
def update_course(cno: str, course: dict = Body(...)):
    conn = get_conn()
    cursor = conn.cursor()

    cursor.execute("SELECT 1 FROM Tianxx_Courses WHERE txx_Cno = %s", (cno,))
    if not cursor.fetchone():
        return JSONResponse(status_code=404, content={"message": "课程不存在"})

    cursor.execute("""
        UPDATE Tianxx_Courses
        SET txx_Cname=%s, txx_CreditHour=%s, txx_Hour=%s, txx_AssessType=%s
        WHERE txx_Cno = %s
    """, (
        course.get("cname"),
        course.get("credit_hour"),
        course.get("hour"),
        course.get("assess_type"),
        cno
    ))

    conn.commit()
    cursor.close()
    conn.close()

    return {"code": 200, "message": "更新成功"}

@app.delete("/courses_delete/{cno}")
def delete_course(cno: str):
    conn = get_conn()
    cursor = conn.cursor()

    cursor.execute("SELECT 1 FROM Tianxx_Courses WHERE txx_Cno = %s", (cno,))
    if not cursor.fetchone():
        return JSONResponse(status_code=404, content={"message": "课程不存在"})

    cursor.execute("DELETE FROM Tianxx_Courses WHERE txx_Cno = %s", (cno,))
    conn.commit()
    cursor.close()
    conn.close()

    return {"code": 200, "message": "删除成功"}


@app.get("/course_teaching_classes")
def get_course_teaching_classes(cno: str):
    try:
        conn = get_conn()
        cursor = conn.cursor()
        sql = """
            SELECT 
                cls.txx_ClassName AS class_name,
                t.txx_Tname AS teacher_name,
                t.txx_Tno AS teacher_tno,
                teaching.txx_Term AS term
            FROM Tianxx_Teaching teaching
            JOIN Tianxx_Classes cls ON teaching.txx_ClassID = cls.txx_ClassID
            JOIN Tianxx_Teachers t ON teaching.txx_Tno = t.txx_Tno
            WHERE teaching.txx_Cno = %s
            ORDER BY teaching.txx_Term DESC, cls.txx_ClassName
        """
        cursor.execute(sql, (cno,))
        rows = cursor.fetchall()
        data = [{
            "class_name": row[0],
            "teacher_name": row[1],
            "teacher_tno": row[2],
            "term": row[3]
        } for row in rows]
        cursor.close()
        conn.close()
        return {"code": 200, "data": data}
    except Exception as e:
        print("查询课程教学班级失败:", e)
        return {"code": 500, "message": "查询失败"}


class CourseEdit(BaseModel):
    cno: str  # 课程编号
    cname: str
    credit_hour: int
    total_hours: Optional[int] = 0
    assess_type: Optional[str] = ""

@app.post("/courses_edit")
def edit_course(course: CourseEdit):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        sql = """
            UPDATE Tianxx_Courses
            SET txx_Cname = %s,
                txx_CreditHour = %s,
                txx_Hour = %s,
                txx_AssessType = %s
            WHERE txx_Cno = %s
        """
        cursor.execute(sql, (
            course.cname,
            course.credit_hour,
            course.total_hours,
            course.assess_type,
            course.cno
        ))

        conn.commit()
        cursor.close()
        conn.close()
        return {"code": 200, "message": "课程信息更新成功"}
    except Exception as e:
        print("课程更新失败：", e)
        return {"code": 500, "message": "课程更新失败"}


@app.get("/courses/avg_grades")
def get_course_avg_grades(cno: str = Query(..., description="课程编号")):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        cursor.execute("""
            SELECT
                t.txx_Term AS term,
                ROUND(AVG(r.txx_Grade), 2) AS avg_grade
            FROM Tianxx_Reports r
            JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
            WHERE t.txx_Cno = %s AND r.txx_Grade IS NOT NULL
            GROUP BY t.txx_Term
            ORDER BY t.txx_Term DESC
        """, (cno,))

        results = [{"term": row[0], "avg_grade": float(row[1])} for row in cursor.fetchall()]
        return {"code": 200, "data": results}
    except Exception as e:
        print("课程平均分查询失败：", e)
        return {"code": 500, "message": "服务器错误"}

from fastapi.responses import StreamingResponse
from io import BytesIO
import openpyxl

@app.get("/courses/export")
def export_courses():
    try:
        conn = get_conn()
        cursor = conn.cursor()

        cursor.execute("""
            SELECT
                txx_Cno,
                txx_Cname,
                txx_CreditHour,
                txx_Hour,
                txx_AssessType
            FROM Tianxx_Courses
        """)
        rows = cursor.fetchall()

        wb = openpyxl.Workbook()
        ws = wb.active
        ws.title = "课程数据"

        headers = ["课程编号", "课程名称", "学分", "课时", "考核方式"]
        ws.append(headers)

        for row in rows:
            ws.append(list(row))

        stream = BytesIO()
        wb.save(stream)
        stream.seek(0)

        return StreamingResponse(
            stream,
            media_type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            headers={"Content-Disposition": "attachment; filename=courses.xlsx"}
        )
    except Exception as e:
        print("导出课程数据失败：", e)
        return {"code": 500, "message": "导出失败"}


from fastapi import APIRouter, Query
from typing import Optional

@app.get("/courses/available")
def get_available_courses(
        sno: str,
        page: int = 1,
        size: int = 10,
        keyword: Optional[str] = Query("", description="课程名称关键词")
):
    conn = get_conn()
    cursor = conn.cursor()

    # 查询该学生所属班级
    cursor.execute("SELECT txx_ClassID FROM Tianxx_Students WHERE txx_Sno = %s", (sno,))
    row = cursor.fetchone()
    if not row:
        cursor.close()
        conn.close()
        return {"code": 404, "message": "未找到该学生", "data": []}

    class_id = row[0]

    offset = (page - 1) * size

    # 主查询：教学安排 + 课程 + 教师
    query = """
        SELECT t.txx_TeachingID, c.txx_Cno, c.txx_Cname, c.txx_CreditHour,
               te.txx_Tname, t.txx_Term
        FROM Tianxx_Teaching t
        JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
        JOIN Tianxx_Teachers te ON t.txx_Tno = te.txx_Tno
        WHERE t.txx_ClassID = %s
    """
    params = [class_id]

    if keyword:
        query += " AND c.txx_Cname ILIKE %s"
        params.append(f"%{keyword}%")

    query += " ORDER BY t.txx_Term DESC, c.txx_Cno LIMIT %s OFFSET %s"
    params.extend([size, offset])

    cursor.execute(query, params)
    rows = cursor.fetchall()

    # 查询总数
    count_query = """
        SELECT COUNT(*) 
        FROM Tianxx_Teaching t
        JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
        WHERE t.txx_ClassID = %s
    """
    count_params = [class_id]
    if keyword:
        count_query += " AND c.txx_Cname ILIKE %s"
        count_params.append(f"%{keyword}%")

    cursor.execute(count_query, count_params)
    total = cursor.fetchone()[0]

    cursor.close()
    conn.close()

    return {
        "code": 200,
        "message": "查询成功",
        "data": {
            "courses": [
                {
                    "teaching_id": row[0],
                    "course_id": row[1],
                    "course_name": row[2],
                    "credit": row[3],
                    "teacher_name": row[4],
                    "term": row[5],
                }
                for row in rows
            ],
            "total": total
        }
    }

