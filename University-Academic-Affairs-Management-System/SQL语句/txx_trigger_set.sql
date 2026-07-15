CREATE OR REPLACE FUNCTION update_student_credits()
RETURNS TRIGGER AS $$
DECLARE
    total_credits INT;
BEGIN
    -- 计算该学生所有及格课程学分总和
    SELECT COALESCE(SUM(c.txx_CreditHour), 0)
    INTO total_credits
    FROM Tianxx_Reports r
    JOIN Tianxx_Teaching t ON r.txx_TeachingID = t.txx_TeachingID
    JOIN Tianxx_Courses c ON t.txx_Cno = c.txx_Cno
    WHERE r.txx_Sno = NEW.txx_Sno AND r.txx_Grade >= 60;

    -- 更新学生的已修学分
    UPDATE Tianxx_Students
    SET txx_CreditHours = total_credits
    WHERE txx_Sno = NEW.txx_Sno;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_update_credits_after_report_change
AFTER INSERT OR UPDATE OF txx_Grade ON Tianxx_Reports
FOR EACH ROW
EXECUTE PROCEDURE update_student_credits();



CREATE OR REPLACE FUNCTION delete_student_related_records()
RETURNS TRIGGER AS $$
BEGIN
    -- 删除学生相关成绩
    DELETE FROM Tianxx_Reports WHERE txx_Sno = OLD.txx_Sno;

    -- 如果还有其它关联表需要删除，也写在这里

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_delete_student_cascade
BEFORE DELETE ON Tianxx_Students
FOR EACH ROW
EXECUTE PROCEDURE delete_student_related_records();

CREATE OR REPLACE FUNCTION create_student_user()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO Tianxx_users (txx_username, txx_password, txx_role, txx_name)
  VALUES (
    NEW.txx_Sno,             -- 用户名为学号
    '123456',                  -- 默认密码（建议后续修改）
    'student',                 -- 身份角色
    NEW.txx_Sname            -- 显示姓名
  );
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_create_user_after_student_insert
AFTER INSERT ON Tianxx_Students
FOR EACH ROW
EXECUTE PROCEDURE create_student_user();


-- 增强版教师级联删除触发器
CREATE OR REPLACE FUNCTION delete_teacher_related_records()
RETURNS TRIGGER AS $$
DECLARE
    teaching_ids INT[];
    delete_count INT;
BEGIN
    -- 获取该教师的所有教学记录ID
    SELECT ARRAY_AGG(txx_TeachingID)
    INTO teaching_ids
    FROM Tianxx_Teaching
    WHERE txx_Tno = OLD.txx_Tno;

    -- 如果没有教学记录，直接返回
    IF teaching_ids IS NULL THEN
        RAISE NOTICE '教师 % 无教学记录，直接删除', OLD.txx_Tno;
        RETURN OLD;
    END IF;

    -- 记录将要删除的数据量（可选，用于日志）
    SELECT COUNT(*) INTO delete_count 
    FROM Tianxx_Reports 
    WHERE txx_TeachingID = ANY(teaching_ids);
    
    RAISE NOTICE '正在删除教师 % 的 % 条成绩记录和 % 条教学记录', 
        OLD.txx_Tno, 
        delete_count, 
        array_length(teaching_ids, 1);

    -- 删除关联的成绩记录
    DELETE FROM Tianxx_Reports
    WHERE txx_TeachingID = ANY(teaching_ids);

    -- 删除教学记录
    DELETE FROM Tianxx_Teaching
    WHERE txx_Tno = OLD.txx_Tno;

    RAISE NOTICE '教师 % 的相关记录已删除完毕', OLD.txx_Tno;
    RETURN OLD;
    
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION '删除教师 % 相关记录时出错: %', OLD.txx_Tno, SQLERRM;
        RETURN NULL; -- 这将阻止删除操作
END;
$$ LANGUAGE plpgsql;

-- 创建触发器
CREATE TRIGGER trg_delete_teacher_cascade
BEFORE DELETE ON Tianxx_Teachers
FOR EACH ROW
EXECUTE PROCEDURE delete_teacher_related_records();