#ifndef GRAPH_H
#define GRAPH_H

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <limits>
#include <cstring>

using namespace std;

const int maxv = 100;   // 最大节点数

class Graph {
public:
    int edges[maxv][maxv];  // 邻接矩阵
    int n, e;  // 图的节点数和边数
    string vexs[maxv];  // 存储节点名称

    Graph();  // 构造函数
    void CreatGraph(int a[][maxv], int n, int e);  // 创建图的邻接矩阵
    void addEdge(int u, int v);  // 添加边
    void removeEdge(int u, int v);  // 删除边
    bool addDot(int u);  // 添加节点
    bool deleteNode(int index);  // 删除节点
    string shortestPath(int start, int end);  // 查找最短路径
    string searchAllPaths(int start, int end);  // 查找所有路径
    string adjacentVertices(int vertex);  // 查找邻接节点
    void updateFile(const string& filename);  // 更新文件中的图数据
    void DFS(int v, int end, int pathIndex[], int index, char paths[maxv][100], int &count,bool visited[]) ;  // 深度优先搜索
    void BFS(int v);  // 广度优先搜索
    void loadGraphFromFile(const string& filename);
};

#endif