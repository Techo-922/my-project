from fastapi.responses import JSONResponse


from dashboard import *
from student_manager import *
from teacher_manager import *
from course_manage import *
from department_manage import *
from grade_manager import *
from qa_lc import *
from login import *



for route in app.routes:
    print(f"Path: {route.path}, Method: {route.methods}, Name: {route.name}")

@app.get("/")
def init():
    return
