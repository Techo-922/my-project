from app_init import app
from connect import get_conn
from fastapi import Body,Query,HTTPException
from pydantic import BaseModel

@app.get("/grades/year_summary")
def get_yearly_grade_summary(
    keyword: str = "",
    grade_year: str = "",
    page: int = 1,
    size: int = 20,
    sort_prop: str = "sno",   # 新增排序字段参数，默认按学号排序
    sort_order: str = "asc"   # 新增排序顺序，asc 或 desc，默认升序
):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        # 校验排序字段，避免SQL注入
        valid_sort_columns = {"dept_no", "dept_name", "sno", "sname", "year", "avg_grade"}
        if sort_prop not in valid_sort_columns:
            sort_prop = "avg_grade"
        sort_dir = "ASC" if sort_order.lower() == "asc" else "DESC"

        offset = (page - 1) * size
        sql = f"""
            SELECT dept_no, dept_name, sno, sname, year, avg_grade
            FROM v_student_yearly_avg_scores
            WHERE (%s = '' OR sno ILIKE %s OR sname ILIKE %s)
              AND (%s = '' OR year = %s)
            ORDER BY {sort_prop} {sort_dir}
            LIMIT %s OFFSET %s
        """
        params = (keyword, f"%{keyword}%", f"%{keyword}%", grade_year, grade_year, size, offset)
        cursor.execute(sql, params)
        rows = cursor.fetchall()

        result = [{
            "dept_no": row[0],
            "dept_name": row[1],
            "sno": row[2],
            "sname": row[3],
            "year": row[4],
            "year_grade": float(row[5]) if row[5] is not None else None
        } for row in rows]

        # 查询总数
        cursor.execute("""
            SELECT COUNT(*) FROM v_student_yearly_avg_scores
            WHERE (%s = '' OR sno ILIKE %s OR sname ILIKE %s)
              AND (%s = '' OR year = %s)
        """, (keyword, f"%{keyword}%", f"%{keyword}%", grade_year, grade_year))
        total = cursor.fetchone()[0]

        return {"code": 200, "data": result, "total": total}
    except Exception as e:
        print("学年成绩查询失败：", e)
        return {"code": 500, "message": "查询失败"}


@app.post("/grades/add_by_name")
def add_grade_by_name(data: dict = Body(...)):
    try:
        cname = data.get("cname")
        sname = data.get("sname")
        grade = data.get("grade")

        if not cname or not sname or grade is None:
            return {"code": 400, "message": "参数不完整"}

        conn = get_conn()
        cursor = conn.cursor()

        # 获取 teaching_id 和 sno
        cursor.execute("""
            SELECT t.txx_TeachingID, s.txx_Sno
            FROM Tianxx_Teaching t
            JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
            JOIN Tianxx_Students s ON s.txx_Sname = %s
            WHERE c.txx_Cname = %s
            LIMIT 1
        """, (sname, cname))
        result = cursor.fetchone()
        if not result:
            return {"code": 404, "message": "未找到对应的课程或学生"}

        teaching_id, sno = result

        # 判断是否已存在成绩记录
        cursor.execute("""
            SELECT 1 FROM Tianxx_Reports
            WHERE txx_Sno = %s AND txx_TeachingID = %s
        """, (sno, teaching_id))
        exists = cursor.fetchone()

        if exists:
            # 更新成绩
            cursor.execute("""
                UPDATE Tianxx_Reports
                SET txx_Grade = %s
                WHERE txx_Sno = %s AND txx_TeachingID = %s
            """, (grade, sno, teaching_id))
        else:
            # 插入新成绩
            cursor.execute("""
                INSERT INTO Tianxx_Reports (txx_Sno, txx_TeachingID, txx_Grade)
                VALUES (%s, %s, %s)
            """, (sno, teaching_id, grade))

        conn.commit()
        cursor.close()
        conn.close()

        return {"code": 200, "message": "成绩保存成功"}

    except Exception as e:
        print("添加成绩失败：", e)
        return {"code": 500, "message": "系统错误"}

@app.get("/grades/earned_credits")
def get_earned_credits(sname: str):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        # 计算学生已修学分总数
        # 假设已修学分统计规则为：
        # 学生成绩≥60分的课程对应学分累计
        cursor.execute("""
            SELECT COALESCE(SUM(c.txx_CreditHour), 0)
            FROM Tianxx_Reports r
            JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
            JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
            JOIN Tianxx_Students s ON r.txx_Sno = s.txx_Sno
            WHERE s.txx_Sname = %s AND r.txx_Grade >= 60
        """, (sname,))
        result = cursor.fetchone()
        credits = result[0] if result else 0

        cursor.close()
        conn.close()

        return {"code": 200, "credits": credits}
    except Exception as e:
        print("查询已修学分失败：", e)
        return {"code": 500, "message": "查询失败"}


@app.get("/students/detail_with_grades")
def get_student_detail_with_grades(sno: str = Query(..., description="学生学号")):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        # 查询学生基本信息 + 院系 + 班级 + 生源地
        cursor.execute("""
            SELECT
                s.txx_Sno,
                s.txx_Sname,
                s.txx_Sex,
                s.txx_Age,
                r.txx_RegionName,
                d.txx_DeptName,
                s.txx_ClassID,
                s.txx_StartDate
            FROM Tianxx_Students s
            LEFT JOIN Tianxx_Regions r ON s.txx_RegionID = r.txx_RegionID
            LEFT JOIN Tianxx_Depts d ON s.txx_DeptNo = d.txx_DeptNo
            WHERE s.txx_Sno = %s
        """, (sno,))
        row = cursor.fetchone()
        if not row:
            return {"code": 404, "message": "未找到学生"}

        student = {
            "sno": row[0],
            "sname": row[1],
            "sex": row[2],
            "age": row[3],
            "region_name": row[4],
            "dept_name": row[5],
            "class_id": row[6],
            "start_date": row[7],
        }

        # 查询已修学分
        cursor.execute("""
            SELECT COALESCE(SUM(c.txx_CreditHour), 0)
            FROM Tianxx_Reports r
            JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
            JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
            WHERE r.txx_Sno = %s AND r.txx_Grade IS NOT NULL
        """, (sno,))
        credit = cursor.fetchone()[0]
        student["credit_hours"] = credit

        # 查询成绩记录（修复教师名连接）
        cursor.execute("""
            SELECT
                c.txx_Cno,
                c.txx_Cname,
                tea.txx_Tname,
                t.txx_Term,
                cls.txx_ClassName,
                r.txx_Grade
            FROM Tianxx_Reports r
            JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
            JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
            JOIN Tianxx_Teachers tea ON t.txx_Tno = tea.txx_Tno
            LEFT JOIN Tianxx_Classes cls ON t.txx_ClassID = cls.txx_ClassID
            WHERE r.txx_Sno = %s
            ORDER BY t.txx_Term DESC
        """, (sno,))
        grades = [{
            "course_id": row[0],
            "course_name": row[1],
            "teacher_name": row[2],
            "term": row[3],
            "class_name": row[4],
            "grade": row[5]
        } for row in cursor.fetchall()]

        return {
            "code": 200,
            "student": student,
            "grades": grades
        }

    except Exception as e:
        print("获取学生详情失败：", e)
        return {"code": 500, "message": "查询失败"}


class GradeUpdate(BaseModel):
    sno: str
    teaching_id: int
    grade: float

@app.post("/grades/update")
def update_grade(payload: GradeUpdate):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        cursor.execute("""
            UPDATE Tianxx_Reports
            SET txx_Grade = %s
            WHERE txx_Sno = %s AND txx_TeachingID = %s
        """, (payload.grade, payload.sno, payload.teaching_id))

        if cursor.rowcount == 0:
            raise HTTPException(status_code=404, detail="成绩记录不存在")

        conn.commit()
        return {"code": 200, "message": "更新成功"}

    except Exception as e:
        print("成绩更新失败：", e)
        raise HTTPException(status_code=500, detail="服务器内部错误")

    finally:
        cursor.close()
        conn.close()


from fastapi.responses import StreamingResponse
from io import BytesIO
import openpyxl

@app.get("/grades/export")
def export_grades(keyword: str = "", grade_year: str = ""):
    try:
        conn = get_conn()
        cursor = conn.cursor()

        sql = """
            SELECT 
                s.txx_Sno,
                s.txx_Sname,
                d.txx_DeptName,
                c.txx_Cname,
                t.txx_Term,
                r.txx_Grade
            FROM Tianxx_Reports r
            JOIN Tianxx_Students s ON r.txx_Sno = s.txx_Sno
            JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
            JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
            JOIN Tianxx_Depts d ON s.txx_DeptNo = d.txx_DeptNo
            WHERE (%s = '' OR s.txx_Sname ILIKE %s OR s.txx_Sno ILIKE %s)
              AND (%s = '' OR t.txx_Term = %s)
            ORDER BY s.txx_Sno, t.txx_Term
        """
        params = (keyword, f"%{keyword}%", f"%{keyword}%", grade_year, grade_year)
        cursor.execute(sql, params)
        rows = cursor.fetchall()

        wb = openpyxl.Workbook()
        ws = wb.active
        ws.title = "成绩数据"

        headers = ["学号", "学生名", "专业", "课程名", "学期", "成绩"]
        ws.append(headers)

        for row in rows:
            ws.append(list(row))

        stream = BytesIO()
        wb.save(stream)
        stream.seek(0)

        return StreamingResponse(
            stream,
            media_type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            headers={"Content-Disposition": "attachment; filename=grades.xlsx"}
        )
    except Exception as e:
        print("导出成绩失败：", e)
        return {"code": 500, "message": "导出失败"}


