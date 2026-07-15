-- 学生学分统计存储过程
CREATE OR REPLACE PROCEDURE sp_calculate_student_credits(
    IN student_no CHAR(8),
    OUT total_credits INT,
    OUT remaining_credits INT
)
AS
DECLARE
    required_credits INT := 160; -- 假设毕业需要160学分
BEGIN
    -- 计算已获得的总学分（及格课程）
    SELECT COALESCE(SUM(c.txx_CreditHour), 0)
    INTO total_credits
    FROM Tianxx_Reports r
    JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
    JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
    WHERE r.txx_Sno = student_no 
      AND r.txx_Grade >= 60;
    
    -- 计算剩余所需学分
    remaining_credits := GREATEST(required_credits - total_credits, 0);
    
    -- 更新学生表中的学分字段
    UPDATE Tianxx_Students
    SET txx_CreditHours = total_credits
    WHERE txx_Sno = student_no;
END;

-- 教师课程管理存储过程
CREATE OR REPLACE PROCEDURE sp_teacher_course_management(
    IN teacher_no CHAR(6),
    IN term VARCHAR(20),
    OUT course_count INT,
    OUT student_count INT
)
AS
BEGIN
    -- 统计教师某学期教授的课程数量
    SELECT COUNT(DISTINCT t.txx_Cno)
    INTO course_count
    FROM Tianxx_Teaching t
    WHERE t.txx_Tno = teacher_no 
      AND t.txx_Term = term;
    
    -- 统计教师某学期教授的学生总数
    SELECT COUNT(DISTINCT s.txx_Sno)
    INTO student_count
    FROM Tianxx_Teaching t
    JOIN Tianxx_Classes c ON t.txx_ClassID = c.txx_ClassID
    JOIN Tianxx_Students s ON s.txx_ClassID = c.txx_ClassID
    WHERE t.txx_Tno = teacher_no 
      AND t.txx_Term = term;
    
    -- 使用GaussDB兼容的输出方式
    RAISE NOTICE '教师 % 在 % 学期教授 % 门课程，共 % 名学生', 
        teacher_no, term, course_count, student_count;
END;

-- 学生成绩分析存储过程
CREATE OR REPLACE PROCEDURE sp_analyze_student_performance(
    IN student_no CHAR(8),
    OUT avg_grade DECIMAL(5,2),
    OUT highest_grade DECIMAL(5,2),
    OUT lowest_grade DECIMAL(5,2),
    OUT passed_courses INT,
    OUT failed_courses INT
)
AS
BEGIN
    -- 计算平均成绩（排除NULL值）
    SELECT ROUND(AVG(txx_Grade), 2)
    INTO avg_grade
    FROM Tianxx_Reports
    WHERE txx_Sno = student_no 
      AND txx_Grade IS NOT NULL;
    
    -- 获取最高分（包含所有成绩）
    SELECT MAX(txx_Grade)
    INTO highest_grade
    FROM Tianxx_Reports
    WHERE txx_Sno = student_no;
    
    -- 获取最低分（排除NULL和负值）
    SELECT MIN(txx_Grade)
    INTO lowest_grade
    FROM Tianxx_Reports
    WHERE txx_Sno = student_no 
      AND txx_Grade >= 0;
    
    -- 统计及格课程数（≥60分）
    SELECT COUNT(*)
    INTO passed_courses
    FROM Tianxx_Reports
    WHERE txx_Sno = student_no 
      AND txx_Grade >= 60;
    
    -- 统计不及格课程数（0≤分数<60）
    SELECT COUNT(*)
    INTO failed_courses
    FROM Tianxx_Reports
    WHERE txx_Sno = student_no 
      AND txx_Grade < 60 
      AND txx_Grade >= 0;
    
    -- GaussDB兼容的输出方式
    RAISE NOTICE '学号 % 的成绩分析: 平均分 %, 最高分 %, 最低分 %, 及格课程 %, 不及格课程 %',
        student_no, 
        COALESCE(avg_grade, 0), 
        COALESCE(highest_grade, 0), 
        COALESCE(lowest_grade, 0), 
        COALESCE(passed_courses, 0), 
        COALESCE(failed_courses, 0);
END;
