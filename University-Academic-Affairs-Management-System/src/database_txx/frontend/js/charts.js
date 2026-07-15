// 图表管理文件

// 图表实例存储
const chartInstances = {};

// 图表配置
const chartConfig = {
    // 默认主题色彩
    colors: [
        '#667eea', '#764ba2', '#f093fb', '#f5576c',
        '#4facfe', '#00f2fe', '#43e97b', '#38f9d7',
        '#fa709a', '#fee140', '#a8edea', '#fed6e3'
    ],
    
    // 默认网格配置
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    
    // 默认动画配置
    animation: {
        duration: 1000,
        easing: 'cubicOut'
    }
};

// 初始化仪表盘图表
function initDashboardCharts(studentsByDept) {
    try {
        // 初始化专业分布图表
        try {
            initMajorChart(studentsByDept);
            // 其他图表初始化函数，按需要传参
        } catch (error) {
            console.error('图表初始化失败:', error);
        }
        
        // 初始化成绩分布图表
        initGradeChart();
        
        // 初始化生源地统计图表
        initHometownChart();
        
        // 初始化选课情况图表
        initEnrollmentChart();
        
        console.log('仪表盘图表初始化完成');
    } catch (error) {
        console.error('图表初始化失败:', error);
    }
}

async function initMajorChart() {
    const chartDom = document.getElementById('majorChart');
    if (!chartDom) return;

    const chart = echarts.init(chartDom);
    chartInstances.majorChart = chart;

    try {
        const response = await axios.get('http://localhost:8000/dashboard/charts/students_by_dept');
        const rawData = response.data;  // 是 [{dept_name, student_count}, ...]

        // 转换成 ECharts 饼图需要的格式 [{ name, value }]
        const data = rawData.map(item => ({
            name: item.dept_name,
            value: item.student_count
        }));

        const option = {
            title: {
                text: '学生专业分布',
                left: 'center',
                textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#666'
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                top: 'middle',
                textStyle: {
                    fontSize: 12
                }
            },
            series: [
                {
                    name: '专业分布',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    center: ['60%', '50%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 5,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: '16',
                            fontWeight: 'bold'
                        },
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: data,
                    animationType: 'scale',
                    animationEasing: 'elasticOut',
                    animationDelay: function (idx) {
                        return Math.random() * 200;
                    }
                }
            ],
            color: chartConfig.colors
        };

        chart.setOption(option);

        window.addEventListener('resize', () => {
            chart.resize();
        });
    } catch (error) {
        console.error('获取专业分布数据失败:', error);
    }
}

async function initGradeChart() {
    const chartDom = document.getElementById('gradeChart');
    if (!chartDom) return;

    const chart = echarts.init(chartDom);
    chartInstances.gradeChart = chart;

    try {
        const response = await axios.get('http://localhost:8000/dashboard/charts/grade_distribution');
        const rawData = response.data; // [{range: '90-100', count: 156}, ...]

        // 提取类别和数据
        const categories = rawData.map(item => item.range);
        const data = rawData.map(item => item.count);

        const option = {
            title: {
                text: '成绩分布统计',
                left: 'center',
                textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#666'
                }
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function(params) {
                    const param = params[0];
                    return `${param.name}<br/>${param.seriesName}: ${param.value}人`;
                }
            },
            grid: chartConfig.grid,
            xAxis: {
                type: 'category',
                data: categories,
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel: {
                    fontSize: 12,
                    color: '#666'
                }
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    fontSize: 12,
                    color: '#666',
                    formatter: '{value}人'
                },
                splitLine: {
                    lineStyle: {
                        color: '#f0f0f0'
                    }
                }
            },
            series: [
                {
                    name: '学生人数',
                    type: 'bar',
                    barWidth: '60%',
                    data: data,
                    itemStyle: {
                        borderRadius: [4, 4, 0, 0],
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#667eea' },
                            { offset: 1, color: '#764ba2' }
                        ])
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                { offset: 0, color: '#764ba2' },
                                { offset: 1, color: '#667eea' }
                            ])
                        }
                    },
                    animationDelay: function (idx) {
                        return idx * 100;
                    }
                }
            ]
        };

        chart.setOption(option);

        window.addEventListener('resize', () => {
            chart.resize();
        });

    } catch (error) {
        console.error('获取成绩分布数据失败:', error);
    }
}


// 生源地统计地图
async function initHometownChart() {
    const chartDom = document.getElementById('hometownChart');
    if (!chartDom) return;

    const chart = echarts.init(chartDom);
    chartInstances.hometownChart = chart;

    try {
        const response = await axios.get('http://localhost:8000/dashboard/charts/hometown_distribution');
        const data = response.data;  // 格式 [{name, value}, ...]

        const option = {
            title: {
                text: '生源地分布',
                left: 'center',
                textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#666'
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: '{b}: {c}人 ({d}%)'
            },
            series: [
                {
                    name: '生源地',
                    type: 'pie',
                    radius: '70%',
                    center: ['50%', '50%'],
                    data: data,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        borderRadius: 5,
                        borderColor: '#fff',
                        borderWidth: 1
                    },
                    label: {
                        fontSize: 11,
                        formatter: '{b}\n{d}%'
                    },
                    animationType: 'scale',
                    animationEasing: 'elasticOut'
                }
            ],
            color: chartConfig.colors
        };

        chart.setOption(option);

        window.addEventListener('resize', () => {
            chart.resize();
        });

    } catch (error) {
        console.error('获取生源地分布数据失败:', error);
    }
}


// 选课情况折线图
function initEnrollmentChart() {
    const chartDom = document.getElementById('enrollmentChart');
    if (!chartDom) return;
    
    const chart = echarts.init(chartDom);
    chartInstances.enrollmentChart = chart;
    
    // 模拟数据
    const months = ['9月', '10月', '11月', '12月', '1月', '2月'];
    const enrollmentData = [120, 132, 101, 134, 90, 230];
    const dropData = [20, 18, 15, 23, 12, 35];
    
    const option = {
        title: {
            text: '选课趋势',
            left: 'center',
            textStyle: {
                fontSize: 14,
                fontWeight: 'normal',
                color: '#666'
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data: ['选课人数', '退课人数'],
            bottom: 10,
            textStyle: {
                fontSize: 12
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: months,
            axisLabel: {
                fontSize: 12,
                color: '#666'
            }
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                fontSize: 12,
                color: '#666',
                formatter: '{value}人'
            },
            splitLine: {
                lineStyle: {
                    color: '#f0f0f0'
                }
            }
        },
        series: [
            {
                name: '选课人数',
                type: 'line',
                stack: 'Total',
                smooth: true,
                lineStyle: {
                    width: 3
                },
                areaStyle: {
                    opacity: 0.3,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: '#667eea' },
                        { offset: 1, color: 'rgba(102, 126, 234, 0.1)' }
                    ])
                },
                emphasis: {
                    focus: 'series'
                },
                data: enrollmentData,
                itemStyle: {
                    color: '#667eea'
                }
            },
            {
                name: '退课人数',
                type: 'line',
                stack: 'Total',
                smooth: true,
                lineStyle: {
                    width: 3
                },
                areaStyle: {
                    opacity: 0.3,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: '#f5576c' },
                        { offset: 1, color: 'rgba(245, 87, 108, 0.1)' }
                    ])
                },
                emphasis: {
                    focus: 'series'
                },
                data: dropData,
                itemStyle: {
                    color: '#f5576c'
                }
            }
        ]
    };
    
    chart.setOption(option);
    
    // 响应式处理
    window.addEventListener('resize', () => {
        chart.resize();
    });
}

// 更新图表数据
function updateChartData(chartName, newData) {
    const chart = chartInstances[chartName];
    if (!chart) {
        console.warn(`图表 ${chartName} 不存在`);
        return;
    }

    const option = chart.getOption();

    switch (chartName) {
        case 'majorChart':
            option.series[0].data = newData.map(item => ({
                value: item.student_count,
                name: item.dept_name
            }));
            break;

        case 'hometownChart':
            option.series[0].data = newData;
            break;

        case 'gradeChart':
            option.series[0].data = newData.data;
            option.xAxis[0].data = newData.categories;
            break;

        case 'enrollmentChart':
            option.series[0].data = newData.enrollmentData;
            option.series[1].data = newData.dropData;
            option.xAxis[0].data = newData.months;
            break;
    }

    chart.setOption(option, true);
}


// 销毁图表
function disposeChart(chartName) {
    const chart = chartInstances[chartName];
    if (chart) {
        chart.dispose();
        delete chartInstances[chartName];
    }
}

// 销毁所有图表
function disposeAllCharts() {
    Object.keys(chartInstances).forEach(chartName => {
        disposeChart(chartName);
    });
}

// 重置图表大小
function resizeCharts() {
    Object.values(chartInstances).forEach(chart => {
        chart.resize();
    });
}

// 获取图表实例
function getChartInstance(chartName) {
    return chartInstances[chartName];
}

// 设置图表主题
function setChartTheme(theme) {
    // 重新初始化所有图表
    disposeAllCharts();
    
    // 注册主题
    if (theme === 'dark') {
        echarts.registerTheme('dark', {
            backgroundColor: '#2c3e50',
            textStyle: {
                color: '#ecf0f1'
            },
            title: {
                textStyle: {
                    color: '#ecf0f1'
                }
            },
            legend: {
                textStyle: {
                    color: '#ecf0f1'
                }
            },
            grid: {
                borderColor: '#34495e'
            }
        });
    }
    
    // 重新初始化图表
    initDashboardCharts();
}

// 导出图表为图片
function exportChart(chartName, type = 'png') {
    const chart = chartInstances[chartName];
    if (!chart) {
        console.warn(`图表 ${chartName} 不存在`);
        return null;
    }
    
    return chart.getDataURL({
        type: type,
        pixelRatio: 2,
        backgroundColor: '#fff'
    });
}

// 图表数据加载
async function loadChartData(chartType) {
    try {
        const response = await api.getChartData(chartType);
        if (response.success) {
            updateChartData(chartType + 'Chart', response.data);
        }
    } catch (error) {
        console.error(`加载图表数据失败 (${chartType}):`, error);
    }
}

// 批量加载图表数据
async function loadAllChartData() {
    const chartTypes = ['major', 'grade', 'hometown', 'enrollment'];
    
    try {
        await Promise.all(
            chartTypes.map(type => loadChartData(type))
        );
        console.log('所有图表数据加载完成');
    } catch (error) {
        console.error('批量加载图表数据失败:', error);
    }
}

// 图表工具函数
const chartUtils = {
    // 格式化数值
    formatValue(value, type = 'number') {
        switch (type) {
            case 'percent':
                return (value * 100).toFixed(1) + '%';
            case 'currency':
                return '¥' + value.toLocaleString();
            case 'number':
            default:
                return value.toLocaleString();
        }
    },
    
    // 生成颜色
    generateColors(count) {
        const colors = [];
        for (let i = 0; i < count; i++) {
            const hue = (i * 360 / count) % 360;
            colors.push(`hsl(${hue}, 70%, 60%)`);
        }
        return colors;
    },
    
    // 数据排序
    sortData(data, key = 'value', order = 'desc') {
        return data.sort((a, b) => {
            if (order === 'desc') {
                return b[key] - a[key];
            } else {
                return a[key] - b[key];
            }
        });
    },
    
    // 数据分组
    groupData(data, groupBy) {
        return data.reduce((groups, item) => {
            const group = item[groupBy];
            if (!groups[group]) {
                groups[group] = [];
            }
            groups[group].push(item);
            return groups;
        }, {});
    }
};

// 响应式处理
window.addEventListener('resize', () => {
    resizeCharts();
});

// 页面可见性变化处理
document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
        // 页面变为可见时重新调整图表大小
        setTimeout(() => {
            resizeCharts();
        }, 100);
    }
});

// 导出函数
window.initDashboardCharts = initDashboardCharts;
window.updateChartData = updateChartData;
window.disposeChart = disposeChart;
window.disposeAllCharts = disposeAllCharts;
window.resizeCharts = resizeCharts;
window.getChartInstance = getChartInstance;
window.setChartTheme = setChartTheme;
window.exportChart = exportChart;
window.loadChartData = loadChartData;
window.loadAllChartData = loadAllChartData;
window.chartUtils = chartUtils;

console.log('图表模块已加载');