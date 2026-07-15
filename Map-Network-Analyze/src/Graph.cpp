#include "Graph.h"  //包含自定义头文件Graph.h，其中声明了类Graph的定义和方法。
#include <queue>  // 包含C++标准库头文件queue，用于队列操作

Graph::Graph() {
    n = 0;  // 初始化节点数为0
    e = 0;  // 初始化边数为0
    // 初始化邻接矩阵
    for (int i = 0; i < maxv; i++) {
        for (int j = 0; j < maxv; j++) {
            edges[i][j] = 0;  // 初始化邻接矩阵中的每个值为0，表示无边
        }
    }
}

void Graph::CreatGraph(int a[][maxv], int n, int e) {  //根据给定的邻接矩阵数组a创建图
    this->n = n;  // 设置节点数
    this->e = e;  // 设置边数
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            edges[i][j] = a[i][j];  // 复制邻接矩阵
        }
    }
}

void Graph::addEdge(int u, int v) {  // 添加无向边
    edges[u][v] = 1;  // 在邻接矩阵中添加边 (u, v)
    edges[v][u] = 1;  // 添加对称边 (v, u)（无向图）
    e++;   // 边数加1

    // 更新文件
    updateFile("D:/vscode_wenjian/SFML/user.txt");
}

void Graph::removeEdge(int u, int v) {  // 删除无向边
    edges[u][v] = 0;  // 删除边 (u, v)
    edges[v][u] = 0;  // 删除对称边 (v, u)
    e--;  // 边数减1

    // 更新文件
    updateFile("D:/vscode_wenjian/SFML/user.txt");
}

void Graph::updateFile(const string& filename) {  
    ifstream readFile(filename);  // 以读取模式打开文件，读取第一行
    if (!readFile.is_open()) {
        cerr << "Failed to open file for reading!" << endl;  // 打印错误信息
        return;
    }
    string firstLine;
    getline(readFile, firstLine); // 读取第一行内容

    readFile.close(); // 关闭读取模式的文件

    ofstream writeFile(filename);   // 以写入模式打开文件
    if (!writeFile.is_open()) {
        cerr << "Failed to open file for updating!" << endl;  // 打印错误信息
        return;
    }

    writeFile << firstLine << endl;  // 写入第一行（保持原样）

    writeFile << n << " " << e << endl;  // 写入节点数和边数，从第二行开始

    // 写入邻接矩阵
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            writeFile << edges[i][j] << " ";
        }
        writeFile << endl;
    }

    writeFile.close(); // 关闭文件
}

string Graph::shortestPath(int start, int end) {  //使用Dijkstra算法计算两点之间的最短路径。
    int dist[maxv];  // 距离数组，存储到每个节点的最短路径长度
    int prev[maxv];  // 前驱节点数组，存储最短路径的前驱节点
    bool visited[maxv] = {false};  // 记录节点是否已访问
    fill(dist, dist + maxv, numeric_limits<int>::max());  // 距离初始化为无穷大
    dist[start] = 0;  // 起点到自身距离为0

    // Dijkstra 算法的实现
    for (int i = 0; i < n; i++) {
        int u = -1;  
        int minDist = numeric_limits<int>::max();
        for (int j = 0; j < n; j++) {
            if (!visited[j] && dist[j] < minDist) {
                u = j;  // 找到距离最近且未访问的节点
                minDist = dist[j];
            }
        }

        if (u == -1) break;  // 没有可达节点
        visited[u] = true;  // 标记为访问

        for (int v = 0; v < n; v++) {
            if (edges[u][v] != 0 && !visited[v] && dist[u] + edges[u][v] < dist[v]) {
                dist[v] = dist[u] + edges[u][v];  // 更新距离
                prev[v] = u;  // 记录前驱节点
            }
        }
    }

    // 回溯找到最短路径
    string path;
    int current = end;
    while (current != start) {
        path = to_string(current) + path;  // 将当前节点添加到路径字符串
        if (current != start) {
            path = " -> " + path;  // 添加箭头
        }
        current = prev[current];  // 回溯到前驱节点
    }
    path = to_string(start) + path;

    // 如果路径不存在
    if (path.empty() || prev[end] == -1) {
        return "No path found";  // 返回错误信息，无路径
    }

    // return最短路径和长度
    stringstream ss;
    ss << "Path: " << path << "\nLength: " << dist[end];
    return ss.str();
}

string Graph::adjacentVertices(int vertex) {
    stringstream ss; 
    ss<<"";  // 初始化空字符串
    cout << "Adjacent vertices to " << vertex << ": ";
    for (int i = 0; i < n; i++) {  // 遍历节点
        if (edges[vertex][i] != 0) {  // 如果存在边，说明是邻接节点
            ss << i << " ";  // 将邻接节点加入结果
        }
    }
    return ss.str();  // return邻接节点字符串
}

string Graph::searchAllPaths(int start, int end) {
    const int maxPathLength = 100; // 假设路径长度不超过100
    char paths[maxv][maxPathLength]; // 存储所有路径
    int pathIndex[maxv]; // 存储当前路径的索引
    int count = 0; // 统计路径数
    bool visited[maxv] = {false}; // 标记数组，记录节点是否已访问

    // 调用DFS来生成所有路径
    DFS(start, end, pathIndex, 0, paths, count,visited); // 深度优先搜索

    //拼接所有路径为字符串返回
    stringstream sss;
    sss<<"";
    for (int i = 0; i < count; ++i) {
        sss << "Path " << i + 1 << ": " << paths[i] << "\n";  // 将每条路径写入
    }

    if (count == 0) {  // 如果没有路径
        sss << "No path found from " << start << " to " << end ;
    }
    return sss.str();
}

void Graph::DFS(int v, int end, int pathIndex[], int index, char paths[maxv][100], int &count,bool visited[]) {
    visited[v] = true;  // 标记当前节点已访问
    pathIndex[index] = v; // 将当前节点加入路径

    //如果当前节点是终点
    if (v == end) { // 到达终点
        char path[100];  // 将路径转换为字符串
        path[0] = '\0'; // 初始化为空字符串
        for (int i = 0; i <= index; ++i) {  // 遍历路径索引
            int node = pathIndex[i];
            if (i > 0) {
                strcat(path, " -> ");  // 添加箭头符
            }
            char buffer[10];
            sprintf(buffer, "%d", node);  // 将节点编号转为字符串
            strcat(path, buffer);  // 拼接节点编号到路径
        }
        strcpy(paths[count++], path);  // 将路径保存到`paths`数组
    } 
    //如果当前节点不是终点，递归访问所有邻接且未访问的节点
    else {
        for (int i = 0; i < n; i++) {
            if (edges[v][i] != 0 && !visited[i]) {  // 如果有边且未访问
                DFS(i, end, pathIndex, index + 1, paths, count,visited);   // 递归调用
            }
        }
    }
    visited[v] = false; // 回溯时取消访问标记
}

void Graph::BFS(int v) {  //广度优先搜索，按层次遍历图
    bool visited[maxv] = {false};  // 记录访问过的节点
    queue<int> q;

    visited[v] = true;  // 标记起始节点已访问
    q.push(v);  // 将起始节点入队
    
    //遍历队列
    while (!q.empty()) {
        int u = q.front();  // 取队首元素
        q.pop();  // 出队
        cout << vexs[u] << " ";  // 打印当前节点
        
        //遍历所有邻接节点
        for (int i = 0; i < n; i++) {
            if (edges[u][i] != 0 && !visited[i]) {  // 如果有边且未访问
                visited[i] = true;  // 标记为已访问
                q.push(i);  // 入队
            }
        }
    }
}

bool Graph::addDot(int u) {  //向图中添加一个新节点
    for (int i = 0; i < n; i++) {
        if (vexs[i] == to_string(u)) {  // 检查是否已存在该节点
            return false; // 节点已存在，返回失败
        }
    }
    if (n < maxv) {  // 如果节点数未达到上限
        vexs[n] = to_string(u);  // 将新节点编号加入节点数组
        for (int i = 0; i < maxv; i++) {
            edges[n][i] = 0;  // 初始化新节点的行
            edges[i][n] = 0;  // 初始化新节点的列
        }
        n++;  // 更新节点数
        return true;
    }

    // 更新文件
    updateFile("D:/vscode_wenjian/SFML/user.txt");

    return false; //节点数已达上限，返回失败
}

bool Graph::deleteNode(int index) {  //从图中删除指定节点
    if (index < 0 || index >= n) {  // 检查节点索引是否有效
        cout << "Node index is out of range." << endl;
        return false;
    }

    // 删除与该节点相关的所有边
    for (int i = 0; i < n; i++) {
        edges[index][i] = 0;  // 删除行
        edges[i][index] = 0;  // 删除列
    }
    n--;  // 节点数减少

    //删除节点后，重新排列节点
    for (int i = index; i < n; i++) {  // 从删除的索引开始，向前覆盖
        vexs[i] = vexs[i + 1];  // 节点数组左移
        for (int j = 0; j < maxv; j++) {
            edges[i][j] = edges[i + 1][j];  // 邻接矩阵左移
            edges[j][i] = edges[j][i + 1];  //邻接矩阵上移
        }
    }
    return true;
    updateFile("D:/vscode_wenjian/SFML/user.txt");  // 更新文件
}

void Graph::loadGraphFromFile(const string& filename) {  //从文件加载图数据
    ifstream file(filename);  // 打开文件
    if (!file.is_open()) {
        cerr << "Failed to open file for loading graph!" << endl;
        return;
    }

    // 读取节点数和边数
    file >> n >> e;  // 从文件中读取节点数和边数

    // 读取邻接矩阵
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            file >> edges[i][j];
        }
    }
    file.close();  // 关闭文件
}