#include "EmployeeList.h"
#include <iostream>
#include <iomanip>
#include <algorithm>

// 从文件中读取数据
void EmployeeList::loadFromFile() {
    ifstream inFile("employees.dat", ios::binary);
    if (!inFile) return;

    Employee emp;
    while (inFile.read(reinterpret_cast<char*>(&emp), sizeof(Employee))) {
        addEmployee(emp);
    }

    inFile.close();
}

// 保存数据到文件
void EmployeeList::saveToFile() {
    ofstream outFile("employees.dat", ios::binary | ios::trunc);
    if (!outFile) {
        cerr << "无法打开文件进行保存!" << endl;
        return;
    }

    EmployeeNode *current = head;
    while (current != nullptr) {
        outFile.write(reinterpret_cast<const char*>(&current->data), sizeof(Employee));
        current = current->next;
    }

    outFile.close();
}

EmployeeList::EmployeeList() : head(nullptr), size(0) {
    loadFromFile();
}

EmployeeList::~EmployeeList() {
    saveToFile();
    clear();
}

// 添加职工
void EmployeeList::addEmployee(const Employee &emp) {
    EmployeeNode *newNode = new EmployeeNode(emp);
    
    // 检查工号是否已存在
    if (findEmployeeById(emp.getId()) != nullptr) {
        cout << "工号已存在，无法添加!" << endl;
        delete newNode;
        return;
    }

    if (head == nullptr) {
        head = newNode;
    } else {
        EmployeeNode *current = head;
        while (current->next != nullptr) {
            current = current->next;
        }
        current->next = newNode;
    }
    size++;
}

// 根据工号查找职工
Employee* EmployeeList::findEmployeeById(const string &id) {
    EmployeeNode *current = head;
    while (current != nullptr) {
        if (current->data.getId() == id) {
            return &(current->data);
        }
        current = current->next;
    }
    return nullptr;
}

// 根据姓名查找职工（精确或模糊）
vector<Employee*> EmployeeList::findEmployeesByName(const string &name, bool exactMatch) {
    vector<Employee*> result;
    EmployeeNode *current = head;
    
    while (current != nullptr) {
        if (exactMatch) {
            if (current->data.getName() == name) {
                result.push_back(&(current->data));
            }
        } else {
            if (current->data.getName().find(name) != string::npos) {
                result.push_back(&(current->data));
            }
        }
        current = current->next;
    }
    
    return result;
}

// 根据科室查找职工（精确或模糊）
vector<Employee*> EmployeeList::findEmployeesByDepartment(const string &dept, bool exactMatch) {
    vector<Employee*> result;
    EmployeeNode *current = head;
    
    while (current != nullptr) {
        if (exactMatch) {
            if (current->data.getDepartment() == dept) {
                result.push_back(&(current->data));
            }
        } else {
            if (current->data.getDepartment().find(dept) != string::npos) {
                result.push_back(&(current->data));
            }
        }
        current = current->next;
    }
    
    return result;
}

// 根据工号删除职工
bool EmployeeList::deleteEmployeeById(const string &id) {
    if (head == nullptr) return false;

    if (head->data.getId() == id) {
        EmployeeNode *temp = head;
        head = head->next;
        delete temp;
        size--;
        return true;
    }

    EmployeeNode *current = head;
    while (current->next != nullptr) {
        if (current->next->data.getId() == id) {
            EmployeeNode *temp = current->next;
            current->next = temp->next;
            delete temp;
            size--;
            return true;
        }
        current = current->next;
    }

    return false;
}

// 根据工号修改职工信息
bool EmployeeList::updateEmployeeById(const string &id, const Employee &newData) {
    Employee* emp = findEmployeeById(id);
    if (emp == nullptr) return false;

    // 检查新工号是否与其他职工冲突（如果工号被修改）
    if (id != newData.getId() && findEmployeeById(newData.getId()) != nullptr) {
        cout << "新工号已存在，无法修改!" << endl;
        return false;
    }

    *emp = newData;
    return true;
}

// 按工资排序
void EmployeeList::sortBySalary() {
    if (head == nullptr || head->next == nullptr) return;

    vector<Employee> employees;
    EmployeeNode *current = head;
    while (current != nullptr) {
        employees.push_back(current->data);
        current = current->next;
    }

    sort(employees.begin(), employees.end());

    clear();

    for (const auto &emp : employees) {
        addEmployee(emp);
    }
}

// 计算科室平均工资
void EmployeeList::calculateDepartmentAvgSalary() {
    if (head == nullptr) {
        cout << "没有职工数据!" << endl;
        return;
    }

    // 收集所有科室
    vector<string> departments;
    EmployeeNode *current = head;
    while (current != nullptr) {
        if (find(departments.begin(), departments.end(), current->data.getDepartment()) == departments.end()) {
            departments.push_back(current->data.getDepartment());
        }
        current = current->next;
    }

    // 计算每个科室的平均工资
    cout << "科室工资统计:" << endl;
    cout << "----------------------------------------" << endl;
    cout << left << setw(15) << "科室" << setw(15) << "人数" << setw(15) << "平均工资" << endl;
    
    for (const auto &dept : departments) {
        int count = 0;
        double total = 0.0;
        
        current = head;
        while (current != nullptr) {
            if (current->data.getDepartment() == dept) {
                count++;
                total += current->data.getSalary();
            }
            current = current->next;
        }
        
        cout << left << setw(15) << dept 
                 << setw(15) << count 
                 << setw(15) << fixed << setprecision(2) << (total / count) << endl;
    }
}

// 显示所有职工
void EmployeeList::displayAll() const {
    if (head == nullptr) {
        cout << "没有职工数据!" << endl;
        return;
    }

    cout << left << setw(10) << "工号" 
              << setw(10) << "姓名" 
              << setw(8) << "性别" 
              << setw(15) << "电话" 
              << setw(15) << "科室" 
              << setw(10) << "工资" << endl;
     cout << "------------------------------------------------------------" <<  endl;

    EmployeeNode *current = head;
    while (current != nullptr) {
        current->data.display();
        current = current->next;
    }
}

// 清空链表
void EmployeeList::clear() {
    while (head != nullptr) {
        EmployeeNode *temp = head;
        head = head->next;
        delete temp;
    }
    size = 0;
}

int EmployeeList::getSize() const { return size; }