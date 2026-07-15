// 基础配置
const API_CONFIG = {
    baseURL: 'http://localhost:8000',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
};

// Axios实例配置
const apiClient = axios.create(API_CONFIG);

// 请求拦截器
apiClient.interceptors.request.use(
    config => {
        // 添加认证token
        const token = localStorage.getItem('userToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        
        // 添加请求ID用于追踪
        config.headers['X-Request-ID'] = generateRequestId();
        
        // 显示加载状态
        if (config.showLoading !== false) {
            showLoading();
        }
        
        console.log('发送请求:', config.method.toUpperCase(), config.url, config.data || config.params);
        return config;
    },
    error => {
        hideLoading();
        console.error('请求配置错误:', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
apiClient.interceptors.response.use(
    response => {
        hideLoading();
        console.log('收到响应:', response.config.url, response.data);
        
        // 统一处理响应数据格式
        if (response.data && typeof response.data === 'object') {
            return {
                success: response.data.code === 200 || response.data.success === true,
                data: response.data.data || response.data.result || response.data,
                message: response.data.message || response.data.msg || 'success',
                code: response.data.code || 200
            };
        }
        
        return {
            success: true,
            data: response.data,
            message: 'success',
            code: 200
        };
    },
    error => {
        hideLoading();
        console.error('响应错误:', error);
        
        // 处理不同类型的错误
        let errorMessage = '请求失败';
        let errorCode = 500;
        
        if (error.response) {
            // 服务器响应错误
            errorCode = error.response.status;
            errorMessage = error.response.data?.message || error.response.data?.msg || getErrorMessage(errorCode);
            
            // 处理特殊状态码
            switch (errorCode) {
                case 401:
                    // 未授权，跳转到登录页
                    localStorage.removeItem('userToken');
                    localStorage.removeItem('userInfo');
                    // window.location.href = 'login.html';
                    break;
                case 403:
                    // 禁止访问
                    errorMessage = '没有权限访问该资源';
                    break;
                case 404:
                    // 资源不存在
                    //errorMessage = '请求的资源不存在';
                    break;
                case 500:
                    // 服务器内部错误
                    errorMessage = '服务器内部错误，请稍后重试';
                    break;
            }
        } else if (error.request) {
            // 网络错误
            errorMessage = '网络连接失败，请检查网络设置';
        } else {
            // 其他错误
            errorMessage = error.message || '未知错误';
        }
        
        // 显示错误消息
        if (window.Vue && window.Vue.prototype.$message) {
            window.Vue.prototype.$message.error(errorMessage);
        } else {
            alert(errorMessage);
        }
        
        return Promise.resolve({
            success: false,
            data: null,
            message: errorMessage,
            code: errorCode
        });
    }
);

// API接口定义
const api = {
    // ==================== 认证相关 ====================
    
    // 用户登录
    login(credentials) {
        return apiClient.post('/auth/login', credentials);
    },
    
    // 用户注册
    register(userInfo) {
        return apiClient.post('/auth/register', userInfo);
    },
    
    // 退出登录
    logout() {
        return apiClient.post('/auth/logout');
    },
    
    // 刷新token
    refreshToken() {
        return apiClient.post('/auth/refresh');
    },
    
    // 获取当前用户信息
    getCurrentUser() {
        return apiClient.get('/auth/user');
    },
    
    // 修改密码
    changePassword(passwordData) {
        return apiClient.put('/auth/password', passwordData);
    },
    
    // ==================== 仪表盘相关 ====================
    
    // 获取仪表盘统计数据
    getDashboardStats() {
        return apiClient.get('/dashboard/stats');
    },
    
    // 获取图表数据
    getChartData(chartType) {
        return apiClient.get(`/dashboard/charts/${chartType}`);
    },
    
    // ==================== 学生管理 ====================
    
    // 获取学生列表
    getStudents(params = {}) {
        return apiClient.get('/students', { params });
    },
    
    // 获取学生详情
    getStudent(id) {
        return apiClient.get(`/students/${id}`);
    },
    
    // 添加学生
    addStudent(studentData) {
        return apiClient.post('/students_add', studentData);
    },
    
    // 更新学生信息
    updateStudent(id, studentData) {
        return apiClient.put(`/students/${id}`, studentData);
    },
    
    // 删除学生
    deleteStudent(id) {
        return apiClient.delete(`/students/${id}`);
    },
    
    // 批量删除学生
    batchDeleteStudents(ids) {
        return apiClient.delete('/students/batch', { data: { ids } });
    },
    
    // 导入学生数据
    importStudents(file) {
        const formData = new FormData();
        formData.append('file', file);
        return apiClient.post('/students/import', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    
    // 导出学生数据
    exportStudents(params = {}) {
        return apiClient.get('/students/export', {
            params,
            responseType: 'blob'
        });
    },
    
    // 获取学生成绩单
    getStudentTranscript(studentId) {
        return apiClient.get(`/students/${studentId}/transcript`);
    },
    
    // 获取学生选课记录
    getStudentEnrollments(studentId) {
        return apiClient.get(`/students/${studentId}/enrollments`);
    },
    
    // 学生选课
    enrollCourse(studentId, offeringId) {
        return apiClient.post(`/students/${studentId}/enroll`, { offeringId });
    },
    
    // 学生退课
    dropCourse(studentId, enrollmentId) {
        return apiClient.delete(`/students/${studentId}/enrollments/${enrollmentId}`);
    },
    
    // ==================== 教师管理 ====================
    
    // 获取教师列表
    getTeachers(params = {}) {
        return apiClient.get('/teachers', { params });
    },
    
    // 获取教师详情
    getTeacher(id) {
        return apiClient.get(`/teachers/${id}`);
    },
    
    // 添加教师
    addTeacher(teacherData) {
        return apiClient.post('/teachers', teacherData);
    },
    
    // 更新教师信息
    updateTeacher(id, teacherData) {
        return apiClient.put(`/teachers/${id}`, teacherData);
    },
    
    // 删除教师
    deleteTeacher(id) {
        return apiClient.delete(`/teachers/${id}`);
    },
    
    // 获取教师授课记录
    getTeacherCourses(teacherId) {
        return apiClient.get(`/teachers/${teacherId}/courses`);
    },
    
    // 获取教师课程成绩
    getTeacherCourseGrades(teacherId, offeringId) {
        return apiClient.get(`/teachers/${teacherId}/courses/${offeringId}/grades`);
    },
    
    // 录入成绩
    inputGrades(teacherId, offeringId, grades) {
        return apiClient.post(`/teachers/${teacherId}/courses/${offeringId}/grades`, { grades });
    },
    
    // ==================== 课程管理 ====================
    
    // 获取课程列表
    getCourses(params = {}) {
        return apiClient.get('/courses', { params });
    },
    
    // 获取课程详情
    getCourse(id) {
        return apiClient.get(`/courses/${id}`);
    },
    
    // 添加课程
    addCourse(courseData) {
        return apiClient.post('/courses', courseData);
    },
    
    // 更新课程信息
    updateCourse(id, courseData) {
        return apiClient.put(`/courses/${id}`, courseData);
    },
    
    // 删除课程
    deleteCourse(id) {
        return apiClient.delete(`/courses/${id}`);
    },
    
    // 获取开课信息列表
    getCourseOfferings(params = {}) {
        return apiClient.get('/course-offerings', { params });
    },
    
    // 添加开课信息
    addCourseOffering(offeringData) {
        return apiClient.post('/course-offerings', offeringData);
    },
    
    // 更新开课信息
    updateCourseOffering(id, offeringData) {
        return apiClient.put(`/course-offerings/${id}`, offeringData);
    },
    
    // 删除开课信息
    deleteCourseOffering(id) {
        return apiClient.delete(`/course-offerings/${id}`);
    },
    
    // ==================== 部门管理 ====================
    
    // 获取部门列表
    getDepartments(params = {}) {
        return apiClient.get('/departments', { params });
    },
    
    // 获取部门详情
    getDepartment(id) {
        return apiClient.get(`/departments/${id}`);
    },
    
    // 添加部门
    addDepartment(departmentData) {
        return apiClient.post('/departments', departmentData);
    },
    
    // 更新部门信息
    updateDepartment(id, departmentData) {
        return apiClient.put(`/departments/${id}`, departmentData);
    },
    
    // 删除部门
    deleteDepartment(id) {
        return apiClient.delete(`/departments/${id}`);
    },
    
    // 获取部门统计信息
    getDepartmentStats(id) {
        return apiClient.get(`/departments/${id}/stats`);
    },
    
    // ==================== 成绩管理 ====================
    
    // 获取成绩列表
    getGrades(params = {}) {
        return apiClient.get('/grades', { params });
    },
    
    // 获取成绩详情
    getGrade(id) {
        return apiClient.get(`/grades/${id}`);
    },
    
    // 添加成绩
    addGrade(gradeData) {
        return apiClient.post('/grades', gradeData);
    },
    
    // 更新成绩
    updateGrade(id, gradeData) {
        return apiClient.put(`/grades/${id}`, gradeData);
    },
    
    // 删除成绩
    deleteGrade(id) {
        return apiClient.delete(`/grades/${id}`);
    },
    
    // 批量录入成绩
    batchInputGrades(grades) {
        return apiClient.post('/grades/batch', { grades });
    },
    
    // ==================== 报表统计 ====================
    
    // 获取学生统计报表
    getStudentReport(params = {}) {
        return apiClient.get('/reports/students', { params });
    },
    
    // 获取教师统计报表
    getTeacherReport(params = {}) {
        return apiClient.get('/reports/teachers', { params });
    },
    
    // 获取课程统计报表
    getCourseReport(params = {}) {
        return apiClient.get('/reports/courses', { params });
    },
    
    // 获取成绩统计报表
    getGradeReport(params = {}) {
        return apiClient.get('/reports/grades', { params });
    },
    
    // 获取生源地统计
    getHometownStats() {
        return apiClient.get('/reports/hometown-stats');
    },
    
    // ==================== 文件上传 ====================
    
    // 上传文件
    uploadFile(file, type = 'general') {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('type', type);
        return apiClient.post('/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    
    // 上传头像
    uploadAvatar(file) {
        return this.uploadFile(file, 'avatar');
    }
};

// 工具函数

// 生成请求ID
function generateRequestId() {
    return 'req_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
}

// 获取错误消息
function getErrorMessage(code) {
    const errorMessages = {
        400: '请求参数错误',
        401: '未授权访问',
        403: '禁止访问',
        404: '资源不存在',
        405: '请求方法不允许',
        408: '请求超时',
        409: '资源冲突',
        422: '请求参数验证失败',
        429: '请求过于频繁',
        500: '服务器内部错误',
        502: '网关错误',
        503: '服务不可用',
        504: '网关超时'
    };
    return errorMessages[code] || '未知错误';
}

// 显示加载状态
function showLoading() {
    if (window.loadingCount === undefined) {
        window.loadingCount = 0;
    }
    window.loadingCount++;
    
    if (window.loadingCount === 1) {
        // 显示全局加载指示器
        const loadingEl = document.getElementById('global-loading');
        if (loadingEl) {
            loadingEl.style.display = 'flex';
        }
    }
}

// 隐藏加载状态
function hideLoading() {
    if (window.loadingCount === undefined) {
        window.loadingCount = 0;
    }
    
    if (window.loadingCount > 0) {
        window.loadingCount--;
    }
    
    if (window.loadingCount === 0) {
        // 隐藏全局加载指示器
        const loadingEl = document.getElementById('global-loading');
        if (loadingEl) {
            loadingEl.style.display = 'none';
        }
    }
}

// 请求重试机制
function retryRequest(requestFn, maxRetries = 3, delay = 1000) {
    return new Promise((resolve, reject) => {
        let retries = 0;
        
        function attempt() {
            requestFn()
                .then(resolve)
                .catch(error => {
                    retries++;
                    if (retries < maxRetries && isRetryableError(error)) {
                        setTimeout(attempt, delay * retries);
                    } else {
                        reject(error);
                    }
                });
        }
        
        attempt();
    });
}

// 判断是否为可重试的错误
function isRetryableError(error) {
    if (!error.response) {
        return true; // 网络错误可重试
    }
    
    const status = error.response.status;
    return status >= 500 || status === 408 || status === 429;
}

// 请求缓存机制
const requestCache = new Map();

function getCachedRequest(key, requestFn, ttl = 5 * 60 * 1000) { // 默认5分钟缓存
    const cached = requestCache.get(key);
    const now = Date.now();
    
    if (cached && (now - cached.timestamp) < ttl) {
        return Promise.resolve(cached.data);
    }
    
    return requestFn().then(data => {
        requestCache.set(key, {
            data,
            timestamp: now
        });
        return data;
    });
}

// 清除缓存
function clearCache(pattern) {
    if (pattern) {
        for (const key of requestCache.keys()) {
            if (key.includes(pattern)) {
                requestCache.delete(key);
            }
        }
    } else {
        requestCache.clear();
    }
}

// 导出API对象
window.api = api;
window.apiClient = apiClient;
window.retryRequest = retryRequest;
window.getCachedRequest = getCachedRequest;
window.clearCache = clearCache;

// 在控制台输出API信息
console.log('API模块已加载', {
    baseURL: API_CONFIG.baseURL,
    timeout: API_CONFIG.timeout,
    methods: Object.keys(api).length
});