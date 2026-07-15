#ifndef EMPLOYEENODE_H
#define EMPLOYEENODE_H

#include "Employee.h"

class EmployeeNode {
public:
    Employee data;
    EmployeeNode *next;

    EmployeeNode(const Employee &emp);
};

#endif // EMPLOYEENODE_H