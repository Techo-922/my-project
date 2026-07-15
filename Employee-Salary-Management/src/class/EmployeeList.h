#ifndef EMPLOYEELIST_H
#define EMPLOYEELIST_H

#include "EmployeeNode.h"
#include <vector>
#include <algorithm>
#include <fstream>

using namespace std;

class EmployeeList {
private:
    EmployeeNode *head;
    int size;

    // 从文件中读取数据
    void loadFromFile();
    // 保存数据到文件
    void saveToFile();

public:
    EmployeeList();
    ~EmployeeList();

    // 添加职工
    void addEmployee(const Employee &emp);
    // 根据工号查找职工
    Employee* findEmployeeById(const string &id);
    // 根据姓名查找职工（精确或模糊）
    vector<Employee*> findEmployeesByName(const string &name, bool exactMatch = true);
    // 根据科室查找职工（精确或模糊）
    vector<Employee*> findEmployeesByDepartment(const string &dept, bool exactMatch = true);
    // 根据工号删除职工
    bool deleteEmployeeById(const string &id);
    // 根据工号修改职工信息
    bool updateEmployeeById(const string &id, const Employee &newData);
    // 按工资排序
    void sortBySalary();
    // 计算科室平均工资
    void calculateDepartmentAvgSalary();
    // 显示所有职工
    void displayAll() const;
    // 清空链表
    void clear();
    
    int getSize() const;
};

#endif // EMPLOYEELIST_H