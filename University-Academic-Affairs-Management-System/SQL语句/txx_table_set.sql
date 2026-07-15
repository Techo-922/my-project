
-- 地区表
CREATE TABLE Tianxx_Regions (
  txx_RegionID CHAR(4) PRIMARY KEY,
  txx_RegionName VARCHAR(100) NOT NULL
);

-- 系表
CREATE TABLE Tianxx_Depts (
  txx_DeptNo CHAR(4) PRIMARY KEY,
  txx_DeptName VARCHAR(50) NOT NULL
);

-- 班级表
CREATE TABLE Tianxx_Classes (
  txx_ClassID CHAR(6) PRIMARY KEY,
  txx_DeptNo CHAR(4),
  txx_ClassName VARCHAR(50) NOT NULL,
  FOREIGN KEY (txx_DeptNo) REFERENCES Tianxx_Depts(txx_DeptNo)
);

-- 教师表
CREATE TABLE Tianxx_Teachers (
  txx_Tno CHAR(6) PRIMARY KEY,
  txx_Tname VARCHAR(50) NOT NULL,
  txx_Sex CHAR(2),
  txx_Age INT,
  txx_Title VARCHAR(50),
  txx_Phone VARCHAR(20)
);

-- 学生表
CREATE TABLE Tianxx_Students (
  txx_Sno CHAR(8) PRIMARY KEY,
  txx_Sname VARCHAR(50) NOT NULL,
  txx_Sex CHAR(2),
  txx_Age INT,
  txx_RegionID CHAR(4),
  txx_DeptNo CHAR(4),
  txx_ClassID CHAR(6),
  txx_StartDate DATE NOT NULL,
  txx_CreditHours INT DEFAULT 0,
  FOREIGN KEY (txx_RegionID) REFERENCES Tianxx_Regions(txx_RegionID),
  FOREIGN KEY (txx_DeptNo) REFERENCES Tianxx_Depts(txx_DeptNo),
  FOREIGN KEY (txx_ClassID) REFERENCES Tianxx_Classes(txx_ClassID)
);

-- 课程表（不再含教师、班级、学期字段）
CREATE TABLE Tianxx_Courses (
  txx_Cno CHAR(6) PRIMARY KEY,
  txx_Cname VARCHAR(100) NOT NULL,
  txx_CreditHour INT NOT NULL,
  txx_Hour INT,
  txx_AssessType VARCHAR(10)
);

-- 教学表：建立课程-教师-班级-学期多对多关系
CREATE TABLE Tianxx_Teaching (
  txx_TeachingID SERIAL PRIMARY KEY,
  txx_Cno CHAR(6) NOT NULL,
  txx_Tno CHAR(6) NOT NULL,
  txx_ClassID CHAR(6) NOT NULL,
  txx_Term VARCHAR(20) NOT NULL,
  FOREIGN KEY (txx_Cno) REFERENCES Tianxx_Courses(txx_Cno),
  FOREIGN KEY (txx_Tno) REFERENCES Tianxx_Teachers(txx_Tno),
  FOREIGN KEY (txx_ClassID) REFERENCES Tianxx_Classes(txx_ClassID),
  UNIQUE (txx_Cno, txx_Tno, txx_ClassID, txx_Term)
);

-- 成绩表，引用教学ID
CREATE TABLE Tianxx_Reports (
  txx_Sno CHAR(8) NOT NULL,
  txx_TeachingID INT NOT NULL,
  txx_Grade DECIMAL(5,2),
  PRIMARY KEY (txx_Sno, txx_TeachingID),
  FOREIGN KEY (txx_Sno) REFERENCES Tianxx_Students(txx_Sno),
  FOREIGN KEY (txx_TeachingID) REFERENCES Tianxx_Teaching(txx_TeachingID)
);

--用户表
CREATE TABLE Tianxx_users (
    txx_id SERIAL PRIMARY KEY,
    txx_username VARCHAR(50) UNIQUE NOT NULL,
    txx_password VARCHAR(100) NOT NULL,
    txx_role VARCHAR(20) NOT NULL,  -- admin / teacher / student / staff
    txx_name VARCHAR(50) NOT NULL,  -- 显示名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Tianxx_users (txx_username, txx_password, txx_role, txx_name) VALUES
('T003',  '123456',  'teacher', '教师3'),
('S000005',  '123456',  'student', '学生5');


ALTER TABLE Tianxx_Students
ADD COLUMN txx_Phone VARCHAR(20),
ADD COLUMN txx_Address VARCHAR(200);

ALTER TABLE Tianxx_Courses
ADD CONSTRAINT chk_credit_hour_range
CHECK (txx_CreditHour BETWEEN 1 AND 10);

ALTER TABLE Tianxx_Reports
ADD CONSTRAINT chk_grade_range
CHECK (txx_Grade BETWEEN 0 AND 100);

ALTER TABLE Tianxx_Students
ADD CONSTRAINT chk_sex_male_female
CHECK (txx_Sex IN ('男', '女'));
