#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <iostream>
#include <sstream>
#include <fstream>
#include "Graph.h"
#include "UserLogin.h"

using namespace std;

void drawMatrix(const Graph& graph,sf::RenderWindow& window,int n) {
    // 每个点的半径
    const float radius = 5.0f;

    // 点之间的间距
    const float spacing = 20.0f;

    // 创建点的形状
    sf::CircleShape point(radius);

    // 遍历邻接矩阵，绘制每个点
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            // 设置点的颜色
            if (graph.edges[i][j] == 1) {
                point.setFillColor(sf::Color::Green);  // 绿色表示 1
            } else {
                point.setFillColor(sf::Color::Red);    // 红色表示 0
            }

            // 计算点的位置
            float xPos = 100.f + j * spacing;  // X轴位置，按列排列
            float yPos = 100.f + i * spacing;  // Y轴位置，按行排列

            point.setPosition(xPos, yPos);  // 设置点的位置

            // 绘制点
            window.draw(point);
        }
    }
}

int loadMapFromFile(const string& filename, Graph& graph) {
    ifstream file(filename);  // 打开文件
    if (!file.is_open()) {
        cerr << "Failed to open file!" << endl;
        return -1;  // 返回失败标识
    }

    string line;
    int row = 0;

    // 跳过前两行
    getline(file, line);  // 第一行
    getline(file, line);  // 第二行，读取成一维数组

    // 将第二行读取到一维数组
    int secondLineArray[graph.n];
    stringstream ss(line);
    int col = 0;
    while (ss >> secondLineArray[col] && col < graph.n) {
        col++;
    }
    
    // 打印第二行数组（用于调试）
    for (int i = 0; i < col; ++i) {
        cout << secondLineArray[i] << " ";
    }
    cout << endl;

    // 从第三行开始读取邻接矩阵数据
    while (getline(file, line) && row < graph.n) {
        stringstream ss(line);
        int col = 0;

        // 逐个读取行中的数字
        while (ss >> graph.edges[row][col] && col < graph.n) {
            col++;
        }
        row++;
    }

    file.close();  // 关闭文件
    return 1;  // 成功读取
}

void createErrorWindow(const string& errorMessage) {
    sf::RenderWindow errorWindow(sf::VideoMode(200, 100), "Error", sf::Style::Close);
    
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    sf::Text errorText(errorMessage, font, 18);
    errorText.setFillColor(sf::Color::Red);
    errorText.setPosition(20, 30);

    while (errorWindow.isOpen()) {
        sf::Event event;
        while (errorWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                errorWindow.close();
            }
        }

        errorWindow.clear(sf::Color::White);
        errorWindow.draw(errorText);
        errorWindow.display();
    }
}

void createLoginWindow(UserLogin& userLogin,sf::RenderWindow& window,Graph& graph) {
    sf::RenderWindow loginWindow(sf::VideoMode(400, 300), "Login", sf::Style::Close);
    
    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return ;
    }

    // 创建文本框
    sf::RectangleShape usernameBox(sf::Vector2f(250, 30));
    usernameBox.setFillColor(sf::Color::White);
    usernameBox.setPosition(75, 50);

    sf::RectangleShape passwordBox(sf::Vector2f(250, 30));
    passwordBox.setFillColor(sf::Color::White);
    passwordBox.setPosition(75, 100);

    // 创建按钮
    sf::RectangleShape loginButton(sf::Vector2f(150, 40));
    loginButton.setFillColor(sf::Color::Green);
    loginButton.setPosition(50, 200);

    sf::RectangleShape createAccountButton(sf::Vector2f(150, 40));
    createAccountButton.setFillColor(sf::Color::Cyan);
    createAccountButton.setPosition(205, 200);

    // 创建按钮文字
    sf::Text loginText("Login", font, 18);
    loginText.setFillColor(sf::Color::Black);
    loginText.setPosition(100, 210);

    sf::Text createAccountText("Create Account", font, 18);
    createAccountText.setFillColor(sf::Color::Black);
    createAccountText.setPosition(215, 210);

    // 创建用户名和密码的标签
    sf::Text usernameLabel("Username:", font, 18);
    usernameLabel.setFillColor(sf::Color::Black);
    usernameLabel.setPosition(10, 50);

    sf::Text passwordLabel("Password:", font, 18);
    passwordLabel.setFillColor(sf::Color::Black);
    passwordLabel.setPosition(10, 100);

    // 用户输入
    string usernameInput = "";
    string passwordInput = "";
    bool isUsernameFocused = true; // 当前输入框是否是用户名框

    // 显示文本
    sf::Text usernameText("", font, 18);
    usernameText.setFillColor(sf::Color::Black);
    usernameText.setPosition(100, 50);

    sf::Text passwordText("", font, 18);
    passwordText.setFillColor(sf::Color::Black);
    passwordText.setPosition(100, 100);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(100, 50);
    bool cursorVisible = true; // 光标是否可见
    sf::Clock cursorClock;     // 光标计时器

    while (loginWindow.isOpen()) {
        sf::Event event;
        while (loginWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                loginWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(loginWindow);

                if (usernameBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUsernameFocused = true;
                } else if (passwordBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUsernameFocused = false;
                }

                // 检查点击的是“登录”按钮
                if (loginButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (userLogin.verifyUser(usernameInput, passwordInput)) {
                        cout << "Login successful" << endl;
                        int result = loadMapFromFile("D:/vscode_wenjian/SFML/" + usernameInput + ".txt", graph);  // 读取地图数据
                        if (result == 1) {
                            cout << "Map read successfully!" << endl;
                            loginWindow.close();  // 关闭登录窗口
                            return; // 成功读取地图后退出
                        } else {
                            createErrorWindow("Error reading map");
                        }
                    } else {
                        // 如果密码错误，弹出错误提示窗口
                        createErrorWindow("Wrong Password");
                    }
                }

                // 检查点击的是“新建账号”按钮
                if (createAccountButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    cout << "Create Account button clicked" << endl;
                    if (userLogin.createUser(usernameInput, passwordInput)) {  // 调用 userLogin 的方法添加新用户
                        cout << "Account created successfully!" << endl;
                        createErrorWindow("Account Created");
                    } else {
                        createErrorWindow("Failed to Create Account");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (isUsernameFocused && !usernameInput.empty()) {
                            usernameInput.pop_back();
                        } else if (!isUsernameFocused && !passwordInput.empty()) {
                            passwordInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        if (isUsernameFocused) {
                            usernameInput += enteredChar;
                        } else {
                            passwordInput += enteredChar;
                        }
                    }
                }
            }
        }

        // 更新文本和光标位置
        usernameText.setString(usernameInput);
        passwordText.setString(string(passwordInput.size(), '*')); // 显示为星号

        if (isUsernameFocused) {
            cursor.setPosition(100 + usernameText.getGlobalBounds().width, 50);
        } else {
            cursor.setPosition(100 + passwordText.getGlobalBounds().width, 100);
        }

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        loginWindow.clear(sf::Color::White);

        // 绘制所有元素
        loginWindow.draw(usernameBox);
        loginWindow.draw(passwordBox);
        loginWindow.draw(loginButton);
        loginWindow.draw(createAccountButton);
        loginWindow.draw(usernameLabel);
        loginWindow.draw(passwordLabel);
        loginWindow.draw(loginText);
        loginWindow.draw(createAccountText);
        loginWindow.draw(usernameText);
        loginWindow.draw(passwordText);

        // 绘制光标
        if (cursorVisible) {
            loginWindow.draw(cursor);
        }
        
        loginWindow.display();
    }
}

void createAddSideWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow addSideWindow(sf::VideoMode(400, 300), "Add Side", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    sf::RectangleShape vBox(sf::Vector2f(250, 30));
    vBox.setFillColor(sf::Color::White);
    vBox.setPosition(75, 100);

    // 创建按钮
    sf::RectangleShape yesButton(sf::Vector2f(150, 40));
    yesButton.setFillColor(sf::Color::Green);
    yesButton.setPosition(125, 200);

    // 创建按钮文字
    sf::Text yesText("Yes", font, 18);
    yesText.setFillColor(sf::Color::Black);
    yesText.setPosition(175, 210);

    // 创建标签
    sf::Text uLabel("Node u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    sf::Text vLabel("Node v:", font, 18);
    vLabel.setFillColor(sf::Color::Black);
    vLabel.setPosition(10, 100);

    // 用户输入
    string uInput = "";
    string vInput = "";
    bool isUFocused = true; // 当前输入框是否是u框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(100, 50);

    sf::Text vText("", font, 18);
    vText.setFillColor(sf::Color::Black);
    vText.setPosition(100, 100);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(100, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (addSideWindow.isOpen()) {
        sf::Event event;
        while (addSideWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                addSideWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(addSideWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                } else if (vBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = false;
                }

                // 检查点击的是“Yes”按钮
                if (yesButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty() && !vInput.empty()) {
                        // 将u和v转换为整数并添加边
                        int u = stoi(uInput);
                        int v = stoi(vInput);
                        graph.addEdge(u, v);  // 假设addEdge是Graph类的方法
                        addSideWindow.close();  // 关闭窗口
                    } else {
                        // 提示用户输入有效的节点
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (isUFocused && !uInput.empty()) {
                            uInput.pop_back();
                        } else if (!isUFocused && !vInput.empty()) {
                            vInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        if (isUFocused) {
                            uInput += enteredChar;
                        } else {
                            vInput += enteredChar;
                        }
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);
        vText.setString(vInput);

        if (isUFocused) {
            cursor.setPosition(100 + uText.getGlobalBounds().width, 50);
        } else {
            cursor.setPosition(100 + vText.getGlobalBounds().width, 100);
        }

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        addSideWindow.clear(sf::Color::White);

        // 绘制所有元素
        addSideWindow.draw(uBox);
        addSideWindow.draw(vBox);
        addSideWindow.draw(yesButton);
        addSideWindow.draw(uLabel);
        addSideWindow.draw(vLabel);
        addSideWindow.draw(yesText);
        addSideWindow.draw(uText);
        addSideWindow.draw(vText);

        // 绘制光标
        if (cursorVisible) {
            addSideWindow.draw(cursor);
        }

        addSideWindow.display();
    }
}

void createDeleteSideWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow deleteSideWindow(sf::VideoMode(400, 300), "Delete Side", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    sf::RectangleShape vBox(sf::Vector2f(250, 30));
    vBox.setFillColor(sf::Color::White);
    vBox.setPosition(75, 100);

    // 创建按钮
    sf::RectangleShape yesButton(sf::Vector2f(150, 40));
    yesButton.setFillColor(sf::Color::Red);
    yesButton.setPosition(125, 200);

    // 创建按钮文字
    sf::Text yesText("Yes", font, 18);
    yesText.setFillColor(sf::Color::Black);
    yesText.setPosition(175, 210);

    // 创建标签
    sf::Text uLabel("Node u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    sf::Text vLabel("Node v:", font, 18);
    vLabel.setFillColor(sf::Color::Black);
    vLabel.setPosition(10, 100);

    // 用户输入
    string uInput = "";
    string vInput = "";
    bool isUFocused = true; // 当前输入框是否是u框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(100, 50);

    sf::Text vText("", font, 18);
    vText.setFillColor(sf::Color::Black);
    vText.setPosition(100, 100);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(100, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (deleteSideWindow.isOpen()) {
        sf::Event event;
        while (deleteSideWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                deleteSideWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(deleteSideWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                } else if (vBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = false;
                }

                // 检查点击的是“Yes”按钮
                if (yesButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty() && !vInput.empty()) {
                        // 将u和v转换为整数并删除边
                        int u = stoi(uInput);
                        int v = stoi(vInput);
                        graph.removeEdge(u, v);  // 假设deleteEdge是Graph类的方法
                        deleteSideWindow.close();  // 关闭窗口
                    } else {
                        // 提示用户输入有效的节点
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (isUFocused && !uInput.empty()) {
                            uInput.pop_back();
                        } else if (!isUFocused && !vInput.empty()) {
                            vInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        if (isUFocused) {
                            uInput += enteredChar;
                        } else {
                            vInput += enteredChar;
                        }
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);
        vText.setString(vInput);

        if (isUFocused) {
            cursor.setPosition(100 + uText.getGlobalBounds().width, 50);
        } else {
            cursor.setPosition(100 + vText.getGlobalBounds().width, 100);
        }

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        deleteSideWindow.clear(sf::Color::White);

        // 绘制所有元素
        deleteSideWindow.draw(uBox);
        deleteSideWindow.draw(vBox);
        deleteSideWindow.draw(yesButton);
        deleteSideWindow.draw(uLabel);
        deleteSideWindow.draw(vLabel);
        deleteSideWindow.draw(yesText);
        deleteSideWindow.draw(uText);
        deleteSideWindow.draw(vText);

        // 绘制光标
        if (cursorVisible) {
            deleteSideWindow.draw(cursor);
        }

        deleteSideWindow.display();
    }
}

void createSearchShortestPathWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow searchPathWindow(sf::VideoMode(400, 300), "Search Shortest Path", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    sf::RectangleShape vBox(sf::Vector2f(250, 30));
    vBox.setFillColor(sf::Color::White);
    vBox.setPosition(75, 100);

    // 创建按钮
    sf::RectangleShape yesButton(sf::Vector2f(150, 40));
    yesButton.setFillColor(sf::Color::Blue);
    yesButton.setPosition(125, 200);

    // 创建按钮文字
    sf::Text yesText("Yes", font, 18);
    yesText.setFillColor(sf::Color::White);
    yesText.setPosition(175, 210);

    // 创建标签
    sf::Text uLabel("Start Node u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    sf::Text vLabel("End Node v:", font, 18);
    vLabel.setFillColor(sf::Color::Black);
    vLabel.setPosition(10, 100);

    // 用户输入
    string uInput = "";
    string vInput = "";
    bool isUFocused = true; // 当前输入框是否是u框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(120, 50);

    sf::Text vText("", font, 18);
    vText.setFillColor(sf::Color::Black);
    vText.setPosition(120, 100);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(120, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (searchPathWindow.isOpen()) {
        sf::Event event;
        while (searchPathWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                searchPathWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(searchPathWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                } else if (vBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = false;
                }

                // 检查点击的是“Yes”按钮
                if (yesButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty() && !vInput.empty()) {
                        // 转换为整数并查找最短路径
                        int u = stoi(uInput);
                        int v = stoi(vInput);
                        string result = graph.shortestPath(u, v); // 假设返回路径或错误信息
                        createErrorWindow(result);
                        searchPathWindow.close();  // 关闭窗口
                    } else {
                        // 提示用户输入有效的节点
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (isUFocused && !uInput.empty()) {
                            uInput.pop_back();
                        } else if (!isUFocused && !vInput.empty()) {
                            vInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        if (isUFocused) {
                            uInput += enteredChar;
                        } else {
                            vInput += enteredChar;
                        }
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);
        vText.setString(vInput);

        if (isUFocused) {
            cursor.setPosition(120 + uText.getGlobalBounds().width, 50);
        } else {
            cursor.setPosition(120 + vText.getGlobalBounds().width, 100);
        }

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        searchPathWindow.clear(sf::Color::White);

        // 绘制所有元素
        searchPathWindow.draw(uBox);
        searchPathWindow.draw(vBox);
        searchPathWindow.draw(yesButton);
        searchPathWindow.draw(uLabel);
        searchPathWindow.draw(vLabel);
        searchPathWindow.draw(yesText);
        searchPathWindow.draw(uText);
        searchPathWindow.draw(vText);

        // 绘制光标
        if (cursorVisible) {
            searchPathWindow.draw(cursor);
        }

        searchPathWindow.display();
    }
}

void createSearchAllPathsWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow searchPathsWindow(sf::VideoMode(400, 300), "Search All Paths", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    sf::RectangleShape vBox(sf::Vector2f(250, 30));
    vBox.setFillColor(sf::Color::White);
    vBox.setPosition(75, 100);

    // 创建按钮
    sf::RectangleShape yesButton(sf::Vector2f(150, 40));
    yesButton.setFillColor(sf::Color::Magenta);
    yesButton.setPosition(125, 200);

    // 创建按钮文字
    sf::Text yesText("Yes", font, 18);
    yesText.setFillColor(sf::Color::White);
    yesText.setPosition(175, 210);

    // 创建标签
    sf::Text uLabel("Start Node u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    sf::Text vLabel("End Node v:", font, 18);
    vLabel.setFillColor(sf::Color::Black);
    vLabel.setPosition(10, 100);

    // 用户输入
    string uInput = "";
    string vInput = "";
    bool isUFocused = true; // 当前输入框是否是u框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(120, 50);

    sf::Text vText("", font, 18);
    vText.setFillColor(sf::Color::Black);
    vText.setPosition(120, 100);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(120, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (searchPathsWindow.isOpen()) {
        sf::Event event;
        while (searchPathsWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                searchPathsWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(searchPathsWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                } else if (vBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = false;
                }

                // 检查点击的是“Yes”按钮
                if (yesButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty() && !vInput.empty()) {
                        // 转换为整数并查找所有路径
                        int u = stoi(uInput);
                        int v = stoi(vInput);
                        string result =graph.searchAllPaths(u, v); // 假设返回所有路径信息或错误信息
                        createErrorWindow(result);
                        searchPathsWindow.close();  // 关闭窗口
                    } else {
                        // 提示用户输入有效的节点
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (isUFocused && !uInput.empty()) {
                            uInput.pop_back();
                        } else if (!isUFocused && !vInput.empty()) {
                            vInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        if (isUFocused) {
                            uInput += enteredChar;
                        } else {
                            vInput += enteredChar;
                        }
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);
        vText.setString(vInput);

        if (isUFocused) {
            cursor.setPosition(120+ uText.getGlobalBounds().width, 50);
        } else {
            cursor.setPosition(120 + vText.getGlobalBounds().width, 100);
        }

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        searchPathsWindow.clear(sf::Color::White);

        // 绘制所有元素
        searchPathsWindow.draw(uBox);
        searchPathsWindow.draw(vBox);
        searchPathsWindow.draw(yesButton);
        searchPathsWindow.draw(uLabel);
        searchPathsWindow.draw(vLabel);
        searchPathsWindow.draw(yesText);
        searchPathsWindow.draw(uText);
        searchPathsWindow.draw(vText);

        // 绘制光标
        if (cursorVisible) {
            searchPathsWindow.draw(cursor);
        }

        searchPathsWindow.display();
    }
}

void createDeleteDotWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow deleteDotWindow(sf::VideoMode(400, 200), "Delete Dot", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    // 创建按钮
    sf::RectangleShape yesButton(sf::Vector2f(150, 40));
    yesButton.setFillColor(sf::Color::Red);
    yesButton.setPosition(125, 120);

    // 创建按钮文字
    sf::Text yesText("Yes", font, 18);
    yesText.setFillColor(sf::Color::White);
    yesText.setPosition(175, 130);

    // 创建标签
    sf::Text uLabel("Node to delete u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    // 用户输入
    string uInput = "";
    bool isUFocused = true; // 当前输入框是否是u框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(145, 50);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(145, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (deleteDotWindow.isOpen()) {
        sf::Event event;
        while (deleteDotWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                deleteDotWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(deleteDotWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                }

                // 检查点击的是“Yes”按钮
                if (yesButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty()) {
                        // 转换为整数并删除节点
                        int u = stoi(uInput);
                        bool result = graph.deleteNode(u); // 假设返回删除成功或失败
                        if (result) {
                            createErrorWindow("Node deleted successfully!");
                        } else {
                            createErrorWindow("Failed to delete node.");
                        }
                        deleteDotWindow.close(); // 关闭窗口
                    } else {
                        // 提示用户输入有效的节点
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (isUFocused && !uInput.empty()) {
                            uInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        if (isUFocused) {
                            uInput += enteredChar;
                        }
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);

        cursor.setPosition(145 + uText.getGlobalBounds().width, 50);

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        deleteDotWindow.clear(sf::Color::White);

        // 绘制所有元素
        deleteDotWindow.draw(uBox);
        deleteDotWindow.draw(yesButton);
        deleteDotWindow.draw(uLabel);
        deleteDotWindow.draw(yesText);
        deleteDotWindow.draw(uText);

        // 绘制光标
        if (cursorVisible) {
            deleteDotWindow.draw(cursor);
        }

        deleteDotWindow.display();
    }
}

void createAddDotWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow addDotWindow(sf::VideoMode(400, 200), "Add Dot", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    // 创建按钮
    sf::RectangleShape yesButton(sf::Vector2f(150, 40));
    yesButton.setFillColor(sf::Color::Blue);
    yesButton.setPosition(125, 120);

    // 创建按钮文字
    sf::Text yesText("Yes", font, 18);
    yesText.setFillColor(sf::Color::White);
    yesText.setPosition(175, 130);

    // 创建标签
    sf::Text uLabel("Node Value u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    // 用户输入
    string uInput = "";
    bool isUFocused = true; // 当前焦点是否在 u 框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(120, 50);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(120, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (addDotWindow.isOpen()) {
        sf::Event event;
        while (addDotWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                addDotWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(addDotWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                }

                // 检查点击的是“Yes”按钮
                if (yesButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty()) {
                        int u = stoi(uInput);
                        if (graph.addDot(u)) { // 假设返回布尔值表示添加成功
                            createErrorWindow("Node added successfully!");
                        } else {
                            createErrorWindow("Node already exists or invalid input!");
                        }
                        addDotWindow.close();
                    } else {
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (!uInput.empty()) {
                            uInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        uInput += enteredChar;
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);
        cursor.setPosition(120 + uText.getGlobalBounds().width, 50);

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        addDotWindow.clear(sf::Color::White);

        // 绘制所有元素
        addDotWindow.draw(uBox);
        addDotWindow.draw(yesButton);
        addDotWindow.draw(uLabel);
        addDotWindow.draw(yesText);
        addDotWindow.draw(uText);

        // 绘制光标
        if (cursorVisible) {
            addDotWindow.draw(cursor);
        }

        addDotWindow.display();
    }
}

void createSearchAdjacentDotWindow(Graph& graph, sf::RenderWindow& window) {
    sf::RenderWindow searchAdjacentWindow(sf::VideoMode(400, 200), "Search Adjacent Dot", sf::Style::Close);

    // 创建字体
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return;
    }

    // 创建文本框
    sf::RectangleShape uBox(sf::Vector2f(250, 30));
    uBox.setFillColor(sf::Color::White);
    uBox.setPosition(75, 50);

    // 创建按钮
    sf::RectangleShape searchButton(sf::Vector2f(150, 40));
    searchButton.setFillColor(sf::Color::Green);
    searchButton.setPosition(125, 120);

    // 创建按钮文字
    sf::Text searchText("Search", font, 18);
    searchText.setFillColor(sf::Color::White);
    searchText.setPosition(175, 130);

    // 创建标签
    sf::Text uLabel("Node Value u:", font, 18);
    uLabel.setFillColor(sf::Color::Black);
    uLabel.setPosition(10, 50);

    // 用户输入
    string uInput = "";
    bool isUFocused = true; // 当前焦点是否在 u 框

    // 显示文本
    sf::Text uText("", font, 18);
    uText.setFillColor(sf::Color::Black);
    uText.setPosition(120, 50);

    // 光标
    sf::RectangleShape cursor(sf::Vector2f(2, 25));
    cursor.setFillColor(sf::Color::Black);
    cursor.setPosition(120, 50);
    bool cursorVisible = true;
    sf::Clock cursorClock;

    while (searchAdjacentWindow.isOpen()) {
        sf::Event event;
        while (searchAdjacentWindow.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                searchAdjacentWindow.close();
            }

            // 切换焦点
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(searchAdjacentWindow);

                if (uBox.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    isUFocused = true;
                }

                // 检查点击的是“Search”按钮
                if (searchButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    if (!uInput.empty()) {
                        int u = stoi(uInput);
                        string result =graph.adjacentVertices(u); // 调用Graph类的adjacentVertices方法
                        createErrorWindow(result);
                        searchAdjacentWindow.close();
                    } else {
                        createErrorWindow("Invalid input!");
                    }
                }
            }

            // 处理用户输入
            if (event.type == sf::Event::TextEntered) {
                if (event.text.unicode < 128) {
                    char enteredChar = static_cast<char>(event.text.unicode);

                    if (enteredChar == '\b') { // 退格键
                        if (!uInput.empty()) {
                            uInput.pop_back();
                        }
                    } else if (enteredChar >= 32 && enteredChar <= 126) { // 可打印字符
                        uInput += enteredChar;
                    }
                }
            }
        }

        // 更新文本和光标位置
        uText.setString(uInput);
        cursor.setPosition(120 + uText.getGlobalBounds().width, 50);

        // 光标闪烁
        if (cursorClock.getElapsedTime().asSeconds() > 0.5f) {
            cursorVisible = !cursorVisible;
            cursorClock.restart();
        }

        // 清空和绘制窗口
        searchAdjacentWindow.clear(sf::Color::White);

        // 绘制所有元素
        searchAdjacentWindow.draw(uBox);
        searchAdjacentWindow.draw(searchButton);
        searchAdjacentWindow.draw(uLabel);
        searchAdjacentWindow.draw(searchText);
        searchAdjacentWindow.draw(uText);

        // 绘制光标
        if (cursorVisible) {
            searchAdjacentWindow.draw(cursor);
        }

        searchAdjacentWindow.display();
    }
}

int main() {
    UserLogin userLogin(5);
    Graph graph;  // 创建一个图对象
    string username = "user";  // 假设这是从 UserLogin 获取的用户名
    string filename = "D:/vscode_wenjian/SFML/" + username + ".txt";
    graph.loadGraphFromFile(filename);

    sf::RenderWindow window(sf::VideoMode(800, 600), "SFML Window");

    window.setFramerateLimit(60);
    sf::Font font;
    if (!font.loadFromFile("D:/vscode_wenjian/SFML/arial.ttf")) {
        cerr << "Failed to load font!" << endl;
        return -1;
    }

    // 创建右上角的圆形按钮
    sf::CircleShape loginButton(25); // 半径为25，即直径为50
    loginButton.setFillColor(sf::Color::Blue);
    loginButton.setPosition(725, 25); // 设置位置（右上角）
    
    sf::RectangleShape deleteSideButton(sf::Vector2f(100, 50)); // 按钮尺寸100*50
    deleteSideButton.setFillColor(sf::Color::Green);
    deleteSideButton.setPosition(25, 25); // 设置位置

    sf::RectangleShape addSideButton(sf::Vector2f(100, 50));
    addSideButton.setFillColor(sf::Color::Cyan);
    addSideButton.setPosition(127, 25);

    sf::RectangleShape deleteDotButton(sf::Vector2f(100, 50));
    deleteDotButton.setFillColor(sf::Color::Green);
    deleteDotButton.setPosition(229, 25);

    sf::RectangleShape addDotButton(sf::Vector2f(100, 50));
    addDotButton.setFillColor(sf::Color::Cyan);
    addDotButton.setPosition(331, 25);

    sf::RectangleShape searchshortestpathButton(sf::Vector2f(140, 50));
    searchshortestpathButton.setFillColor(sf::Color::Green);
    searchshortestpathButton.setPosition(440, 25);

    sf::RectangleShape searchallpathButton(sf::Vector2f(120, 50));
    searchallpathButton.setFillColor(sf::Color::Cyan);
    searchallpathButton.setPosition(585, 25);

    sf::RectangleShape searchadjacentdotButton(sf::Vector2f(170, 50));
    searchadjacentdotButton.setFillColor(sf::Color::Cyan);
    searchadjacentdotButton.setPosition(585, 85);

    // 创建按钮文字
    sf::Text loginText("LOG", font, 18);
    loginText.setFillColor(sf::Color::White);
    loginText.setPosition(731, 40);

    sf::Text deleteSideText("Delete Side", font, 18);
    deleteSideText.setFillColor(sf::Color::Black);
    deleteSideText.setPosition(28, 40);

    sf::Text addSideText("Add Side", font, 18);
    addSideText.setFillColor(sf::Color::Black);
    addSideText.setPosition(140, 40);

    sf::Text deleteDotText("Delete Dot", font, 18);
    deleteDotText.setFillColor(sf::Color::Black);
    deleteDotText.setPosition(238, 40);

    sf::Text addDotText("Add Dot", font, 18);
    addDotText.setFillColor(sf::Color::Black);
    addDotText.setPosition(343, 40);

    sf::Text searchshortestpathText("SearchShortestP", font, 18);
    searchshortestpathText.setFillColor(sf::Color::Black);
    searchshortestpathText.setPosition(445, 40);

    sf::Text searchallpathText("Search All P", font, 18);
    searchallpathText.setFillColor(sf::Color::Black);
    searchallpathText.setPosition(595, 40);

    sf::Text searchadjacentdotText("Search Adjacent D", font, 18);
    searchadjacentdotText.setFillColor(sf::Color::Black);
    searchadjacentdotText.setPosition(595, 100);

    while (window.isOpen()) {
        sf::Event event;
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
            }
        }

        // 处理点击登录按钮
            if (event.type == sf::Event::MouseButtonPressed) {
                sf::Vector2i mousePos = sf::Mouse::getPosition(window);
                if (loginButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
                    createLoginWindow(userLogin,window,graph);  // 点击登录按钮时弹出登录窗口
                }
            }
        
        if (event.type == sf::Event::MouseButtonPressed) {
        sf::Vector2i mousePos = sf::Mouse::getPosition(window);

    // 检查点击的是“Add Side”按钮
    if (addSideButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createAddSideWindow(graph,window);
    }

    // 检查点击的是“Delete Side”按钮
    if (deleteSideButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createDeleteSideWindow(graph,window);
    }

    // 检查点击的是“Delete Dot”按钮
    if (deleteDotButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createDeleteDotWindow(graph,window);
    }

    // 检查点击的是“Add Dot”按钮
    if (addDotButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createAddDotWindow(graph, window);
    }

    // 检查点击的是“Search Shortest Path”按钮
    if (searchshortestpathButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createSearchShortestPathWindow(graph,window);
    }

    // 检查点击的是“Search All Paths”按钮
    if (searchallpathButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createSearchAllPathsWindow(graph,window);
    }

    // 检查点击的是“searchadjacentdotButton”按钮
    if (searchadjacentdotButton.getGlobalBounds().contains(mousePos.x, mousePos.y)) {
        createSearchAdjacentDotWindow(graph,window);
    }
}

        window.clear(sf::Color::White);

        // 在主窗口中绘制矩阵
        drawMatrix(graph,window,graph.n);
        
        // 绘制按钮
        window.draw(loginButton);      // 绘制右上角的按钮
        window.draw(deleteSideButton);
        window.draw(addSideButton);
        window.draw(deleteDotButton);
        window.draw(addDotButton);
        window.draw(searchshortestpathButton);
        window.draw(searchallpathButton);
        window.draw(searchadjacentdotButton);
        window.draw(deleteSideText);
        window.draw(addSideText);
        window.draw(deleteDotText);
        window.draw(addDotText);
        window.draw(loginText);
        window.draw(searchshortestpathText);
        window.draw(searchallpathText);
        window.draw(searchadjacentdotText);

        window.display();
    }
    
    return 0;
}