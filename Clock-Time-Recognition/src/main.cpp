#include <opencv2/opencv.hpp>
#include <iostream>
#include <cmath>
#include <vector>
#include <algorithm>
#include <numeric>

using namespace cv;
using namespace std;

// 计算指针与12点方向的夹角（弧度）
// 四区域分支结构 + 精准计算：指针与12点方向的夹角（弧度），完全适配真实时钟
double getAngle(Point center, Point p) {
    double angle = 0.0;
    // 提取圆心和目标点的坐标
    int cx = center.x;
    int cy = center.y;
    int px = p.x;
    int py = p.y;

    // 计算指针到圆心的横向、纵向绝对偏移
    double dx_abs = abs(static_cast<double>(px - cx));
    double dy_abs = abs(static_cast<double>(cy - py)); // 翻转y轴，匹配真实时钟
    // 相对偏移（用于区域判定）
    int dx = px - cx;
    int dy = cy - py; // 翻转y轴，上正下负

    // 四区域分支逻辑
    if (dy >= 0 && dx >= 0) {
        // 区域1：12点 → 3点（0 ~ π/2）
        if (dx_abs < 1e-6) {
            angle = 0.0;
        } else if (dy_abs < 1e-6) {
            angle = CV_PI / 2;
        } else {
            angle = atan2(dx_abs, dy_abs);
        }
    } else if (dy <= 0 && dx >= 0) {
        // 区域2：3点 → 6点（π/2 ~ π）
        if (dx_abs < 1e-6) {
            angle = CV_PI;
        } else if (dy_abs < 1e-6) {
            angle = CV_PI / 2;
        } else {
            angle = CV_PI / 2 + atan2(dy_abs, dx_abs);
        }
    } else if (dy <= 0 && dx <= 0) {
        // 区域3：6点 → 9点（π ~ 3π/2）
        if (dx_abs < 1e-6) {
            angle = CV_PI;
        } else if (dy_abs < 1e-6) {
            angle = 3 * CV_PI / 2;
        } else {
            angle = CV_PI + atan2(dx_abs, dy_abs);
        }
    } else if (dy >= 0 && dx <= 0) {
        // 区域4：9点 → 12点（3π/2 ~ 2π）
        if (dx_abs < 1e-6) {
            angle = 0.0;
        } else if (dy_abs < 1e-6) {
            angle = 3 * CV_PI / 2;
        } else {
            angle = 3 * CV_PI / 2 + atan2(dy_abs, dx_abs);
        }
    }

    // ===================== 校准 =====================
    // 核心：根据实际偏差调整
    double angle_calibrate = angle + CV_PI / 40; 

    // 校准角度范围，确保在0 ~ 2π之间（避免负角度或超出范围）
    angle_calibrate = fmod(angle_calibrate, 2 * CV_PI);
    if (angle_calibrate < 0) {
        angle_calibrate += 2 * CV_PI;
    }

    return angle_calibrate; 
}

// 定义指针结构体：存储单根指针的综合信息
struct ClockHand {
    double angle;      // 指针平均角度
    double length;     // 指针平均长度
    int thickness;     // 指针粗细（核心区分依据：时针>>秒针）
    Point endPoint;    // 指针代表端点
    int type;          // 0=时针（粗、长度近秒针），1=分针（最长），2=秒针（最细）
    vector<Vec4i> relatedLines; // 该指针对应的所有角度+长度相近的直线
};

// 计算两点间距离
double getDistance(Point a, Point b) {
    return sqrt(pow((a.x - b.x), 2) + pow((a.y - b.y), 2));
}

// 计算两条直线之间的平均距离（多线时为指针粗细）
double getLineAvgDistance(Vec4i& line1, Vec4i& line2) {
    Point p1_1(line1[0], line1[1]);
    Point p1_2(line1[2], line1[3]);
    Point p2_1(line2[0], line2[1]);
    Point p2_2(line2[2], line2[3]);

    vector<double> distances;
    distances.push_back(getDistance(p1_1, p2_1));
    distances.push_back(getDistance(p1_1, p2_2));
    distances.push_back(getDistance(p1_2, p2_1));
    distances.push_back(getDistance(p1_2, p2_2));

    double sum = accumulate(distances.begin(), distances.end(), 0.0);
    return sum / distances.size();
}

// 筛选有效指针直线：过滤短直线、偏离圆心的直线
bool isValidHandLine(Point center, int radius, Vec4i line) {
    Point p1(line[0], line[1]);
    Point p2(line[2], line[3]);
    double lineLen = getDistance(p1, p2);
    if (lineLen < radius * 0.2) {
        return false;
    }
    Point midPoint((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    double midDist = getDistance(midPoint, center);
    if (midDist > radius || midDist < radius * 0.1) {
        return false;
    }
    double dist1 = getDistance(p1, center);
    double dist2 = getDistance(p2, center);
    if ((dist1 < radius * 0.3 && dist2 < radius * 0.3) || (dist1 > radius && dist2 > radius)) {
        return false;
    }
    return true;
}

// 角度+长度双重聚类：确保时针不被拆分，同时区分三根指针
vector<ClockHand> clusterHandsByAngleAndLength(vector<Vec4i>& validLines, Point center, int radius) {
    vector<ClockHand> handClusters;
    double angleThreshold = CV_PI / 18; // 10度宽松阈值，适配时针微小角度偏差
    double lengthThreshold = radius * 0.15; // 放宽长度容忍度（适配时针与秒针长度接近）
    int secondDefaultThickness = 2; // 秒针（最细）默认值，进一步减小
    int hourDefaultThickness = 10; // 时针（最粗）默认值，进一步增大，拉开与秒针的差距
    int minuteDefaultThickness = 6; // 分针（中等粗细）默认值

    for (Vec4i& line : validLines) {
        Point p1(line[0], line[1]);
        Point p2(line[2], line[3]);
        Point lineMid((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        double lineAngle = getAngle(center, lineMid);
        double lineLen1 = getDistance(p1, center);
        double lineLen2 = getDistance(p2, center);
        double lineLen = max(lineLen1, lineLen2);

        bool foundCluster = false;
        for (ClockHand& hand : handClusters) {
            bool angleNear = abs(lineAngle - hand.angle) < angleThreshold;
            bool lengthNear = abs(lineLen - hand.length) < lengthThreshold;
            if (angleNear && lengthNear) {
                hand.relatedLines.push_back(line);
                int lineCount = hand.relatedLines.size();
                // 更新平均长度和角度
                double newTotalLen = hand.length * (lineCount - 1) + lineLen;
                hand.length = newTotalLen / lineCount;
                double newTotalAngle = hand.angle * (lineCount - 1) + lineAngle;
                hand.angle = newTotalAngle / lineCount;
                foundCluster = true;
                break;
            }
        }

        if (!foundCluster) {
            ClockHand newHand;
            newHand.relatedLines.push_back(line);
            newHand.angle = lineAngle;
            newHand.length = lineLen;
            newHand.endPoint = (lineLen1 > lineLen2) ? p1 : p2;
            handClusters.push_back(newHand);
        }
    }

    // 计算粗细：极大拉开时针与秒针的粗细差距，避免误判
    for (ClockHand& hand : handClusters) {
        int lineCount = hand.relatedLines.size();
        if (lineCount >= 2) {
            // 多线：计算线间平均距离为粗细
            vector<double> lineDistances;
            for (int i = 0; i < lineCount; i++) {
                for (int j = i + 1; j < lineCount; j++) {
                    double dist = getLineAvgDistance(hand.relatedLines[i], hand.relatedLines[j]);
                    lineDistances.push_back(dist);
                }
            }
            double totalDist = accumulate(lineDistances.begin(), lineDistances.end(), 0.0);
            hand.thickness = (int)(totalDist / lineDistances.size());
            // 额外补偿：进一步放大粗细差异（针对时针/分针）
            if (hand.length < radius * 0.6) { // 大概率是时针
                hand.thickness += 5;
            } else if (hand.length > radius * 0.8) { // 大概率是分针
                hand.thickness += 2;
            }
            // 秒针（中间长度）不补偿，保持最细
        } else {
            // 单线：根据长度预判指针类型，分配差异极大的默认粗细
            if (hand.length < radius * 0.6) { // 长度较短：时针（最粗）
                hand.thickness = hourDefaultThickness;
            } else if (hand.length > radius * 0.8) { // 长度很长：分针（中等）
                hand.thickness = minuteDefaultThickness;
            } else { // 中间长度：秒针（最细）
                hand.thickness = secondDefaultThickness;
            }
        }
    }

    // 过滤无效聚类
    vector<ClockHand> validClusters;
    for (ClockHand& hand : handClusters) {
        if (hand.length > radius * 0.15) {
            validClusters.push_back(hand);
        }
    }

    return validClusters;
}

// 核心：先长度（优先区分分针），长度接近时强制按粗细排序（核心区分时针和秒针）
void classifyClockHands(vector<ClockHand>& hands, int radius) {
    if (hands.size() < 3) {
        cout << "numbers below 3,failed load" << endl;
        return;
    }

    // 第一步：先按长度降序排序（优先区分出分针：最长）
    sort(hands.begin(), hands.end(), [](const ClockHand& a, const ClockHand& b) {
        return a.length > b.length;
    });

    // 定义长度差阈值：当两根指针长度差异小于此值，判定为"长度接近"，用粗细兜底
    double lenDiffThreshold = radius * 0.1; // 阈值设为10%，适配时针与秒针长度接近的场景
    // 提取初始三根指针
    ClockHand& minHand = hands[0];  // 初始最长：分针（固定，不会与时针/秒针长度接近）
    ClockHand& midHand = hands[1];   // 初始中间：可能是时针或秒针
    ClockHand& hourHand_candidate = hands[2]; // 初始最短：可能是时针或秒针

    // 第二步：关键修复：判断中间和最短指针是否长度接近，若是则按粗细排序（区分时针和秒针）
    if (abs(midHand.length - hourHand_candidate.length) < lenDiffThreshold) {
        // 长度接近时，粗的是时针，细的是秒针：交换位置（确保粗的排前面）
        if (midHand.thickness < hourHand_candidate.thickness) {
            swap(midHand, hourHand_candidate);
        }
    }

    // 第三步：极端情况：三根指针长度都接近（极少出现），直接按粗细降序排序
    if (abs(minHand.length - hourHand_candidate.length) < lenDiffThreshold) {
        sort(hands.begin(), hands.end(), [](const ClockHand& a, const ClockHand& b) {
            return a.thickness > b.thickness; // 粗细降序：时针>分针>秒针
        });
        // 重新赋值
        minHand = hands[0];
        midHand = hands[1];
        hourHand_candidate = hands[2];
    }

    // 第四步：固定标记指针类型（彻底避免识别反）
    minHand.type = 1; // 最长 → 分针（不变）
    // 粗的是时针，细的是秒针（通过粗细判定，不依赖长度）
    if (midHand.thickness > hourHand_candidate.thickness) {
        midHand.type = 0; // 中间（粗）→ 时针
        hourHand_candidate.type = 2; // 最短（细）→ 秒针
    } else {
        midHand.type = 2; // 中间（细）→ 秒针
        hourHand_candidate.type = 0; // 最短（粗）→ 时针
    }

    // 更新排序后的指针数组
    hands[0] = minHand;
    hands[1] = midHand;
    hands[2] = hourHand_candidate;

}

int main() {
    // 1. 读取图像
    Mat img = imread("clock.jpg");
    if (img.empty()) { 
        cout << "failed to read!" << endl; 
        system("pause");
        return -1; 
    }
    Mat gray;
    cvtColor(img, gray, COLOR_BGR2GRAY);
    GaussianBlur(gray, gray, Size(7, 7), 0);

    // 2. Hough圆检测
    vector<Vec3f> circles;
    HoughCircles(gray, circles, HOUGH_GRADIENT, 1.2, gray.rows / 2, 150, 40, 0, 0);
    if (circles.empty()) { 
        cout << "failed to detect clock circle" << endl; 
        system("pause");
        return -1; 
    }
    Point center(cvRound(circles[0][0]), cvRound(circles[0][1]));
    int radius = cvRound(circles[0][2]);
    Mat circle_img = img.clone();
    circle(circle_img, center, radius, Scalar(0, 255, 0), 2);
    circle(circle_img, center, 3, Scalar(0, 255, 0), -1);
    imwrite("1_hough_circle_detection.jpg", circle_img);
    cout << "step1:hough circle" << endl;

    // 3. 边缘检测
    Mat circle_mask = Mat::zeros(gray.size(), CV_8U);
    circle(circle_mask, center, radius, Scalar(255), -1);
    Mat gray_roi;
    gray.copyTo(gray_roi, circle_mask);
    Mat edges;
    Canny(gray_roi, edges, 80, 200, 3);
    imwrite("2_roi_edge_detection.jpg", edges);
    cout << "step2:edge" << endl;

    // 4. 直线检测 + 聚类 + 分类
    vector<Vec4i> lines;
    HoughLinesP(
        edges, 
        lines, 
        1, 
        CV_PI / 180, 
        40, 
        radius * 0.3, 
        5 
    );

    Mat line_img = img.clone();
    vector<Vec4i> validLines;
    for (Vec4i l : lines) {
        if (isValidHandLine(center, radius, l)) {
            validLines.push_back(l);
            Point p1(l[0], l[1]);
            Point p2(l[2], l[3]);
            line(line_img, p1, p2, Scalar(0, 0, 255), 1);
        }
    }

    // 角度+长度聚类
    vector<ClockHand> uniqueHands = clusterHandsByAngleAndLength(validLines, center, radius);

    // 分类指针
    if (uniqueHands.size() >= 3) {
        classifyClockHands(uniqueHands, radius);
    }

    // 可视化标签
    if (uniqueHands.size() >= 3) {
        ClockHand hourHand, minHand, secHand;
        for (ClockHand hand : uniqueHands) {
            if (hand.type == 0) hourHand = hand;
            if (hand.type == 1) minHand = hand;
            if (hand.type == 2) secHand = hand;
        }
        putText(line_img, "Min (Longest)", minHand.endPoint, FONT_HERSHEY_SIMPLEX, 0.4, Scalar(0, 255, 255), 1);
        putText(line_img, "Sec (Thinnest)", secHand.endPoint, FONT_HERSHEY_SIMPLEX, 0.4, Scalar(255, 255, 0), 1);
        putText(line_img, "Hour (Thickest)", hourHand.endPoint, FONT_HERSHEY_SIMPLEX, 0.4, Scalar(255, 0, 0), 1);
    }
    imwrite("3_hand_detection_len+thick.jpg", line_img);
    cout << "step3:length+thickness" << endl;

    // 5. 计算时间
    int hour = 0, minute = 0, second = 0;
    if (uniqueHands.size() >= 3) {
        ClockHand hourHand, minHand, secHand;
        for (ClockHand hand : uniqueHands) {
            if (hand.type == 0) hourHand = hand;
            if (hand.type == 1) minHand = hand;
            if (hand.type == 2) secHand = hand;
        }
        // 时针转小时
        hour = (int)(hourHand.angle * 12 / (2 * CV_PI)) % 12;
        if (hour == 0) hour = 12;
        // 分针转分钟
        minute = (int)(minHand.angle * 60 / (2 * CV_PI)) % 60;
        // 秒针转秒
        second = (int)(secHand.angle * 60 / (2 * CV_PI)) % 60;
    } else {
        cout << "failed to detect,number below " << uniqueHands.size() << endl;
    }

    // 格式化输出时间
    cout << "=====================================" << endl;
    cout << "final time: " << hour << ":";
    if (minute < 10) cout << "0";
    cout << minute << ":";
    if (second < 10) cout << "0";
    cout << second << endl;
    cout << "=====================================" << endl;

    system("pause");
    return 0;
}