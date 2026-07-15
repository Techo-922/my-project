from typing import Dict, List, Optional
from pydantic import BaseModel
from app_init import app
from connect import get_conn

@app.get("/dashboard/stats")
def get_dashboard_stats_main() -> Dict[str, int]:

    conn = get_conn()
    cursor = conn.cursor()

    cursor.execute("SELECT COUNT(*) FROM Tianxx_Students")
    student_count = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM Tianxx_Teachers")
    teacher_count = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM Tianxx_Courses")
    course_count = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM Tianxx_depts")
    department_count = cursor.fetchone()[0]

    cursor.close()
    conn.close()

    return {
        "student_count": student_count,
        "teacher_count": teacher_count,
        "course_count": course_count,
        "department_count": department_count
    }

class DeptStudentCount(BaseModel):
    dept_name: str
    student_count: int

#统计学生专业
@app.get("/dashboard/charts/students_by_dept", response_model=List[DeptStudentCount])
def get_students_by_dept():
    conn = get_conn()
    cursor = conn.cursor()

    query = """
    SELECT d.txx_DeptName AS dept_name, COUNT(s.txx_Sno) AS student_count
    FROM Tianxx_Students s
    LEFT JOIN Tianxx_Depts d ON s.txx_DeptNo = d.txx_DeptNo
    GROUP BY d.txx_DeptName
    ORDER BY student_count DESC
    """
    cursor.execute(query)
    rows = cursor.fetchall()

    result = [{"dept_name": row[0], "student_count": row[1]} for row in rows]

    cursor.close()
    conn.close()
    return result

class CourseAvgGrade(BaseModel):
    course_name: str
    avg_grade: float

class Department(BaseModel):
    id: str
    name: str
class DepartmentListResponse(BaseModel):
    code: int
    success: bool
    message: str
    data: List[Department]

@app.get("/departments", response_model=DepartmentListResponse)
def get_departments():
    conn = get_conn()
    cursor = conn.cursor()

    cursor.execute("""
        SELECT txx_DeptNo AS id, txx_DeptName AS name
        FROM Tianxx_Depts
        ORDER BY txx_DeptNo
    """)

    rows = cursor.fetchall()
    result = [{"id": row[0], "name": row[1]} for row in rows]

    cursor.close()
    conn.close()

    return {
        "code": 200,
        "success": True,
        "message": "获取院系列表成功",
        "data": result
    }



#专业分布图
@app.get("/dashboard/charts/major")
def get_major_chart():
    return get_students_by_dept()


class GradeDistribution(BaseModel):
    range: str
    count: int

#成绩分布
@app.get("/dashboard/charts/grade_distribution", response_model=List[GradeDistribution])
def get_grade_distribution():
    conn = get_conn()
    cursor = conn.cursor()

    query = """
    SELECT
        CASE
            WHEN txx_Grade >= 90 THEN '90-100'
            WHEN txx_Grade >= 80 THEN '80-89'
            WHEN txx_Grade >= 70 THEN '70-79'
            WHEN txx_Grade >= 60 THEN '60-69'
            ELSE '60以下'
        END AS grade_range,
        COUNT(*) AS student_count
    FROM Tianxx_Reports
    WHERE txx_Grade IS NOT NULL
    GROUP BY grade_range
    ORDER BY
        CASE grade_range
            WHEN '90-100' THEN 1
            WHEN '80-89' THEN 2
            WHEN '70-79' THEN 3
            WHEN '60-69' THEN 4
            WHEN '60以下' THEN 5
        END
    """

    cursor.execute(query)
    rows = cursor.fetchall()

    result = [{"range": row[0], "count": row[1]} for row in rows]

    cursor.close()
    conn.close()
    return result

class HometownDistribution(BaseModel):
    name: str
    value: int

#家乡分布
@app.get("/dashboard/charts/hometown_distribution", response_model=List[HometownDistribution])
def get_hometown_distribution():
    conn = get_conn()
    cursor = conn.cursor()

    query = """
    SELECT
        r.txx_RegionName AS region_name,
        COUNT(s.txx_Sno) AS student_count
    FROM Tianxx_Students s
    LEFT JOIN Tianxx_Regions r ON s.txx_RegionID = r.txx_RegionID
    GROUP BY r.txx_RegionName
    ORDER BY student_count DESC
    """

    cursor.execute(query)
    rows = cursor.fetchall()

    # 处理可能出现的 NULL 区域名，转成 '其他'
    result = []
    for row in rows:
        name = row[0] if row[0] else '其他'
        value = row[1]
        result.append({"name": name, "value": value})

    cursor.close()
    conn.close()
    return result


class StudentOut(BaseModel):
    sno: str
    sname: str
    sex: Optional[str]
    age: Optional[int]
    dept_no: Optional[str]
    class_id: Optional[str]
    start_date: Optional[str]
    credit_hours: Optional[int]


