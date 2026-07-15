#ifndef USERLOGIN_H
#define USERLOGIN_H

#include <string>
#include <map>

using namespace std;

class UserLogin {
public:
    UserLogin(int maxUsers);  // 构造函数，可以传入最大用户数量
    
    int readMap(int adjacencyMatrix[][100], int maxNodes);  //从文件中读取地图数据到邻接矩阵，假设最大节点数为10
    
    bool createUser(const string& username, const string& password); // 创建新用户（将用户名和密码存入用户数据结构）
    
    bool verifyUser(const string& username, const string& password);  // 验证用户名和密码是否匹配

private:
    map<string, string> users;  // 使用 map 来存储用户名和密码对
    
    int maxUsers; // 最大用户数
};

#endif