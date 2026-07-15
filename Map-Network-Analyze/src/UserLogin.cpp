#include "UserLogin.h"
#include <fstream>
#include <sstream>
#include <iostream>

using namespace std;

UserLogin::UserLogin(int maxUsers) : maxUsers(maxUsers) {
    // 初始化用户数为0
}

bool UserLogin::createUser(const string& username, const string& password) {
    if (users.size() < maxUsers) {  // 判断当前用户数量是否小于最大用户数
        if (users.find(username) == users.end()) {  // 确保用户名唯一
            users[username] = password;  // 添加用户

            // 打开（或创建）用户文件
            ofstream file("D:/vscode_wenjian/SFML/" + username + ".txt");

            if (!file.is_open()) {
                cerr << "Failed to create user file.\n";
                return false;
            }

            // 写入密码到文件
            file << password << "\n";  // 第1行：密码

            // 跳过第二行
            file << "\n";  // 第二行，内容为空，直接跳过

            file.close();  // 关闭文件
            cout << "User created successfully.\n";
            return true;
        } else {
            cerr << "User already exists.\n";
        }
    }
    return false;  // 用户已存在或已达到最大用户数
}

bool UserLogin::verifyUser(const string& username, const string& password) {
    auto it = users.find(username);
    if (it != users.end() && it->second == password) {
        return true;
    }
    return false;  // 用户名或密码不匹配
}

int UserLogin::readMap(int adjacencyMatrix[][100], int maxNodes) {
    ifstream file("user.txt");  
    if (!file.is_open()) {
        cerr << "Failed to open map file!" << endl;
        return -1;
    }

    string line;
    int row = 0;

    while (getline(file, line) && row < maxNodes) {
        stringstream ss(line);
        int value;
        int col = 0;

        while (ss >> value && col < maxNodes) {
            adjacencyMatrix[row][col] = value;
            col++;
        }
        row++;
    }

    file.close();
    return 1;
}