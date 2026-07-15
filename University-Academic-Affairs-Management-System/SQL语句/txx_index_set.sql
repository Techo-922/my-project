--班级表
CREATE INDEX idx_classes_deptno ON
 Tianxx_Classes(txx_DeptNo);

--学生表
CREATE INDEX idx_students_regionid ON
 Tianxx_Students(txx_RegionID);
 CREATE INDEX idx_students_deptno ON
 Tianxx_Students(txx_DeptNo);
 CREATE INDEX idx_students_classid ON
 Tianxx_Students(txx_ClassID);

--教学安排表
CREATE INDEX idx_teaching_teacher_class_term
 ON Tianxx_Teaching(txx_Tno, txx_ClassID,
 txx_Term)

--成绩表
CREATE INDEX idx_reports_student ON
 Tianxx_Reports(txx_Sno);
 CREATE INDEX idx_reports_teaching ON
 Tianxx_Reports(txx_TeachingID);