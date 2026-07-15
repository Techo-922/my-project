#ifndef EMPLOYEE_H
#define EMPLOYEE_H

#include <string>
#include <iomanip>
#include <iostream>

using namespace std;

class Employee {
private:
    string name;        // 姓名
    string gender;      // 性别
    string id;          // 工号
    string phone;       // 电话
    string department;  // 所在科室
    double salary;          // 工资

public:
    // 构造函数
    Employee();
    Employee(string n, string g, string i, string p, string d, double s);

    // 获取和设置成员变量的方法
    string getName() const;
    void setName(const string &n);

    string getGender() const;
    void setGender(const string &g);

    string getId() const;
    void setId(const string &i);

    string getPhone() const;
    void setPhone(const string &p);

    string getDepartment() const;
    void setDepartment(const string &d);

    double getSalary() const;
    void setSalary(double s);

    // 显示职工信息
    void display() const;

    // 重载比较运算符，用于排序
    bool operator<(const Employee &other) const;
};

#endif // EMPLOYEE_H