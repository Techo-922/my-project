#include "Employee.h"

// 构造函数
Employee::Employee() : salary(0) {}

Employee::Employee(string n, string g, string i, string p, string d, double s)
    : name(n), gender(g), id(i), phone(p), department(d), salary(s) {}

// 获取和设置成员变量的方法
string Employee::getName() const { return name; }
void Employee::setName(const string &n) { name = n; }

string Employee::getGender() const { return gender; }
void Employee::setGender(const string &g) { gender = g; }

string Employee::getId() const { return id; }
void Employee::setId(const string &i) { id = i; }

string Employee::getPhone() const { return phone; }
void Employee::setPhone(const string &p) {
    // 简单的电话格式验证
    if (p.length() != 11 || p.find_first_not_of("0123456789") != string::npos) {
        throw invalid_argument("电话必须是11位数字");
    }
    phone = p;
}

string Employee::getDepartment() const { return department; }
void Employee::setDepartment(const string &d) { department = d; }

double Employee::getSalary() const { return salary; }
void Employee::setSalary(double s) {
    if (s < 0) {
        throw invalid_argument("工资不能为负数");
    }
    salary = s;
}

// 显示职工信息
void Employee::display() const {
    cout << left << setw(10) << id 
                 << setw(10) << name 
                 << setw(8) << gender 
                 << setw(15) << phone 
                 << setw(15) << department 
                 << setw(10) << fixed << setprecision(2) << salary << endl;
}

// 重载比较运算符，用于排序
bool Employee::operator<(const Employee &other) const {
    return salary < other.salary;
}