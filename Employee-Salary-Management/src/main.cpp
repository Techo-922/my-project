#include "class/EmployeeList.h"
#include <iostream>
#include <limits>

// 显示菜单
void displayMenu() {
    cout << "\n职工工资管理系统" << endl;
    cout << "1. 添加职工" << endl;
    cout << "2. 查询职工" << endl;
    cout << "3. 按科室查询职工" << endl;
    cout << "4. 按工资排序输出" << endl;
    cout << "5. 修改职工信息" << endl;
    cout << "6. 删除职工信息" << endl;
    cout << "7. 显示所有职工" << endl;
    cout << "8. 科室工资统计" << endl;
    cout << "0. 退出系统" << endl;
    cout << "请选择操作: ";
}

// 添加职工
void addEmployee(EmployeeList &list) {
    string name, gender, id, phone, department;
    double salary;

    cout << "请输入职工信息:" << endl;
    cout << "姓名: ";
    cin >> name;
    cout << "性别: ";
    cin >> gender;
    cout << "工号: ";
    cin >> id;
    cout << "电话: ";
    cin >> phone;
    cout << "科室: ";
    cin >> department;
    cout << "工资: ";
    cin >> salary;

    try {
        Employee emp(name, gender, id, phone, department, salary);
        list.addEmployee(emp);
        cout << "职工添加成功!" << endl;
    } catch (const invalid_argument &e) {
        cout << "输入无效: " << e.what() << endl;
    }
}

// 查询职工
void searchEmployee(EmployeeList &list) {
    int choice;
    cout << "1. 按工号查询" << endl;
    cout << "2. 按姓名查询(精确)" << endl;
    cout << "3. 按姓名查询(模糊)" << endl;
    cout << "请选择查询方式: ";
    cin >> choice;

    string input;
    vector<Employee*> result;

    switch (choice) {
        case 1:
            cout << "请输入工号: ";
            cin >> input;
            if (Employee* emp = list.findEmployeeById(input)) {
                cout << left << setw(10) << "工号" 
                         << setw(10) << "姓名" 
                         << setw(8) << "性别" 
                         << setw(15) << "电话" 
                         << setw(15) << "科室" 
                         << setw(10) << "工资" << endl;
                cout << "------------------------------------------------------------" << endl;
                emp->display();
            } else {
                cout << "未找到该职工!" << endl;
            }
            break;
        case 2:
            cout << "请输入姓名: ";
            cin >> input;
            result = list.findEmployeesByName(input, true);
            break;
        case 3:
            cout << "请输入姓名(部分): ";
            cin >> input;
            result = list.findEmployeesByName(input, false);
            break;
        default:
            cout << "无效选择!" << endl;
            return;
    }

    if (choice == 2 || choice == 3) {
        if (result.empty()) {
            cout << "未找到匹配的职工!" << endl;
        } else {
            cout << left << setw(10) << "工号" 
                     << setw(10) << "姓名" 
                     << setw(8) << "性别" 
                     << setw(15) << "电话" 
                     << setw(15) << "科室" 
                     << setw(10) << "工资" << endl;
            cout << "------------------------------------------------------------" << endl;
            for (auto emp : result) {
                emp->display();
            }
        }
    }
}

// 按科室查询职工
void searchByDepartment(EmployeeList &list) {
    int choice;
    cout << "1. 精确查询" << endl;
    cout << "2. 模糊查询" << endl;
    cout << "请选择查询方式: ";
    cin >> choice;

    string dept;
    cout << "请输入科室名称: ";
    cin >> dept;

    vector<Employee*> result = list.findEmployeesByDepartment(dept, choice == 1);

    if (result.empty()) {
        cout << "未找到该科室的职工!" << endl;
    } else {
        cout << left << setw(10) << "工号" 
                 << setw(10) << "姓名" 
                 << setw(8) << "性别" 
                 << setw(15) << "电话" 
                 << setw(15) << "科室" 
                 << setw(10) << "工资" << endl;
        cout << "------------------------------------------------------------" << endl;
        for (auto emp : result) {
            emp->display();
        }
    }
}

// 修改职工信息
void updateEmployee(EmployeeList &list) {
    string id;
    cout << "请输入要修改的职工工号: ";
    cin >> id;

    Employee* emp = list.findEmployeeById(id);
    if (emp == nullptr) {
        cout << "未找到该职工!" << endl;
        return;
    }

    cout << "当前职工信息:" << endl;
    cout << left << setw(10) << "工号" 
             << setw(10) << "姓名" 
             << setw(8) << "性别" 
             << setw(15) << "电话" 
             << setw(15) << "科室" 
             << setw(10) << "工资" << endl;
    cout << "------------------------------------------------------------" << endl;
    emp->display();

    string name, gender, newId, phone, department;
    double salary;

    cout << "请输入新的职工信息(不修改请直接回车):" << endl;
    cout << "姓名(" << emp->getName() << "): ";
    cin.ignore();
    getline(cin, name);
    if (name.empty()) name = emp->getName();

    cout << "性别(" << emp->getGender() << "): ";
    getline(cin, gender);
    if (gender.empty()) gender = emp->getGender();

    cout << "工号(" << emp->getId() << "): ";
    getline(cin, newId);
    if (newId.empty()) newId = emp->getId();

    cout << "电话(" << emp->getPhone() << "): ";
    getline(cin, phone);
    if (phone.empty()) phone = emp->getPhone();

    cout << "科室(" << emp->getDepartment() << "): ";
    getline(cin, department);
    if (department.empty()) department = emp->getDepartment();

    cout << "工资(" << emp->getSalary() << "): ";
    string salaryStr;
    getline(cin, salaryStr);
    if (salaryStr.empty()) {
        salary = emp->getSalary();
    } else {
        salary = stod(salaryStr);
    }

    try {
        Employee newEmp(name, gender, newId, phone, department, salary);
        if (list.updateEmployeeById(id, newEmp)) {
            cout << "职工信息修改成功!" << endl;
        }
    } catch (const invalid_argument &e) {
        cout << "输入无效: " << e.what() << endl;
    }
}

// 删除职工
void deleteEmployee(EmployeeList &list) {
    string id;
    cout << "请输入要删除的职工工号: ";
    cin >> id;

    if (list.deleteEmployeeById(id)) {
        cout << "职工删除成功!" << endl;
    } else {
        cout << "未找到该职工!" << endl;
    }
}

int main() {
    EmployeeList employeeList;
    int choice;

    cout << "============================================" << endl;
    cout << "      欢迎使用职工工资管理系统" << endl;
    cout << "============================================" << endl;
    cout << "系统已启动，已从文件加载 " << employeeList.getSize() << " 条职工记录" << endl;
    cout << "请根据菜单选择操作：" << endl;

    do {
        displayMenu();
        
        // 添加输入提示
        cout << "请输入选项数字(0-8): ";
        while (!(cin >> choice)) {
            cin.clear(); // 清除错误状态
            cin.ignore(numeric_limits<streamsize>::max(), '\n'); // 忽略错误输入
            cout << "输入无效，请输入数字(0-8): ";
        }

        switch (choice) {
            case 1:
                cout << "\n--- 添加职工 ---" << endl;
                addEmployee(employeeList);
                break;
            case 2:
                cout << "\n--- 查询职工 ---" << endl;
                searchEmployee(employeeList);
                break;
            case 3:
                cout << "\n--- 按科室查询 ---" << endl;
                searchByDepartment(employeeList);
                break;
            case 4:
                cout << "\n--- 按工资排序输出 ---" << endl;
                employeeList.sortBySalary();
                employeeList.displayAll();
                break;
            case 5:
                cout << "\n--- 修改职工信息 ---" << endl;
                updateEmployee(employeeList);
                break;
            case 6:
                cout << "\n--- 删除职工信息 ---" << endl;
                deleteEmployee(employeeList);
                break;
            case 7:
                cout << "\n--- 显示所有职工 ---" << endl;
                employeeList.displayAll();
                break;
            case 8:
                cout << "\n--- 科室工资统计 ---" << endl;
                employeeList.calculateDepartmentAvgSalary();
                break;
            case 0:
                cout << "\n正在退出系统..." << endl;
                cout << "已保存 " << employeeList.getSize() << " 条职工记录到文件" << endl;
                cout << "感谢使用，再见!" << endl;
                break;
            default:
                cout << "无效选择，请输入0-8之间的数字!" << endl;
        }
    } while (choice != 0);

    return 0;
}