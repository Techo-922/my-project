// 工具函数库

// 日期工具
const dateUtils = {
    // 格式化日期
    format(date, format = 'YYYY-MM-DD') {
        if (!date) return '';
        
        const d = new Date(date);
        if (isNaN(d.getTime())) return '';
        
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hour = String(d.getHours()).padStart(2, '0');
        const minute = String(d.getMinutes()).padStart(2, '0');
        const second = String(d.getSeconds()).padStart(2, '0');
        
        return format
            .replace('YYYY', year)
            .replace('MM', month)
            .replace('DD', day)
            .replace('HH', hour)
            .replace('mm', minute)
            .replace('ss', second);
    },
    
    // 获取相对时间
    getRelativeTime(date) {
        if (!date) return '';
        
        const now = new Date();
        const target = new Date(date);
        const diff = now - target;
        
        const minute = 60 * 1000;
        const hour = 60 * minute;
        const day = 24 * hour;
        const month = 30 * day;
        const year = 365 * day;
        
        if (diff < minute) {
            return '刚刚';
        } else if (diff < hour) {
            return Math.floor(diff / minute) + '分钟前';
        } else if (diff < day) {
            return Math.floor(diff / hour) + '小时前';
        } else if (diff < month) {
            return Math.floor(diff / day) + '天前';
        } else if (diff < year) {
            return Math.floor(diff / month) + '个月前';
        } else {
            return Math.floor(diff / year) + '年前';
        }
    },
    
    // 获取年龄
    getAge(birthDate) {
        if (!birthDate) return 0;
        
        const birth = new Date(birthDate);
        const now = new Date();
        
        let age = now.getFullYear() - birth.getFullYear();
        const monthDiff = now.getMonth() - birth.getMonth();
        
        if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < birth.getDate())) {
            age--;
        }
        
        return age;
    },
    
    // 获取学期
    getSemester(date = new Date()) {
        const d = new Date(date);
        const month = d.getMonth() + 1;
        const year = d.getFullYear();
        
        if (month >= 9 || month <= 1) {
            return `${year}-${year + 1}-1`; // 第一学期
        } else {
            return `${year - 1}-${year}-2`; // 第二学期
        }
    },
    
    // 获取学年
    getAcademicYear(date = new Date()) {
        const d = new Date(date);
        const month = d.getMonth() + 1;
        const year = d.getFullYear();
        
        if (month >= 9) {
            return `${year}-${year + 1}`;
        } else {
            return `${year - 1}-${year}`;
        }
    }
};

// 字符串工具
const stringUtils = {
    // 截断字符串
    truncate(str, length = 50, suffix = '...') {
        if (!str || str.length <= length) return str;
        return str.substring(0, length) + suffix;
    },
    
    // 首字母大写
    capitalize(str) {
        if (!str) return '';
        return str.charAt(0).toUpperCase() + str.slice(1);
    },
    
    // 驼峰转下划线
    camelToSnake(str) {
        return str.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);
    },
    
    // 下划线转驼峰
    snakeToCamel(str) {
        return str.replace(/_([a-z])/g, (match, letter) => letter.toUpperCase());
    },
    
    // 生成随机字符串
    random(length = 8) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for (let i = 0; i < length; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    },
    
    // 脱敏处理
    mask(str, start = 3, end = 4, maskChar = '*') {
        if (!str || str.length <= start + end) return str;
        
        const prefix = str.substring(0, start);
        const suffix = str.substring(str.length - end);
        const middle = maskChar.repeat(str.length - start - end);
        
        return prefix + middle + suffix;
    },
    
    // 手机号脱敏
    maskPhone(phone) {
        return this.mask(phone, 3, 4);
    },
    
    // 身份证脱敏
    maskIdCard(idCard) {
        return this.mask(idCard, 6, 4);
    },
    
    // 邮箱脱敏
    maskEmail(email) {
        if (!email || !email.includes('@')) return email;
        
        const [username, domain] = email.split('@');
        const maskedUsername = this.mask(username, 1, 1);
        return `${maskedUsername}@${domain}`;
    }
};

// 数字工具
const numberUtils = {
    // 格式化数字
    format(num, decimals = 0) {
        if (isNaN(num)) return '0';
        return Number(num).toLocaleString('zh-CN', {
            minimumFractionDigits: decimals,
            maximumFractionDigits: decimals
        });
    },
    
    // 格式化文件大小
    formatFileSize(bytes) {
        if (bytes === 0) return '0 B';
        
        const k = 1024;
        const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
    
    // 格式化百分比
    formatPercent(num, decimals = 1) {
        if (isNaN(num)) return '0%';
        return (num * 100).toFixed(decimals) + '%';
    },
    
    // 生成随机数
    random(min = 0, max = 100) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    },
    
    // 计算GPA
    calculateGPA(grades) {
        if (!grades || grades.length === 0) return 0;
        
        let totalPoints = 0;
        let totalCredits = 0;
        
        grades.forEach(grade => {
            const score = parseFloat(grade.score);
            const credits = parseFloat(grade.credits);
            
            if (!isNaN(score) && !isNaN(credits)) {
                let point = 0;
                if (score >= 90) point = 4.0;
                else if (score >= 80) point = 3.0;
                else if (score >= 70) point = 2.0;
                else if (score >= 60) point = 1.0;
                
                totalPoints += point * credits;
                totalCredits += credits;
            }
        });
        
        return totalCredits > 0 ? (totalPoints / totalCredits).toFixed(2) : 0;
    },
    
    // 分数转等级
    scoreToGrade(score) {
        if (score >= 90) return 'A';
        if (score >= 80) return 'B';
        if (score >= 70) return 'C';
        if (score >= 60) return 'D';
        return 'F';
    }
};

// 验证工具
const validateUtils = {
    // 验证邮箱
    email(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    },
    
    // 验证手机号
    phone(phone) {
        const regex = /^1[3-9]\d{9}$/;
        return regex.test(phone);
    },
    
    // 验证身份证
    idCard(idCard) {
        const regex = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
        return regex.test(idCard);
    },
    
    // 验证学号
    studentId(studentId) {
        const regex = /^\d{10}$/; // 10位数字
        return regex.test(studentId);
    },
    
    // 验证教师编号
    teacherId(teacherId) {
        const regex = /^T\d{6}$/; // T开头6位数字
        return regex.test(teacherId);
    },
    
    // 验证课程编号
    courseId(courseId) {
        const regex = /^[A-Z]{2}\d{4}$/; // 2位字母4位数字
        return regex.test(courseId);
    },
    
    // 验证密码强度
    password(password) {
        // 至少8位，包含大小写字母、数字
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/;
        return regex.test(password);
    },
    
    // 验证分数
    score(score) {
        const num = parseFloat(score);
        return !isNaN(num) && num >= 0 && num <= 100;
    },
    
    // 验证学分
    credits(credits) {
        const num = parseFloat(credits);
        return !isNaN(num) && num >= 0.5 && num <= 10;
    }
};

// 存储工具
const storageUtils = {
    // 设置localStorage
    setLocal(key, value) {
        try {
            localStorage.setItem(key, JSON.stringify(value));
        } catch (error) {
            console.error('设置localStorage失败:', error);
        }
    },
    
    // 获取localStorage
    getLocal(key, defaultValue = null) {
        try {
            const value = localStorage.getItem(key);
            return value ? JSON.parse(value) : defaultValue;
        } catch (error) {
            console.error('获取localStorage失败:', error);
            return defaultValue;
        }
    },
    
    // 删除localStorage
    removeLocal(key) {
        try {
            localStorage.removeItem(key);
        } catch (error) {
            console.error('删除localStorage失败:', error);
        }
    },
    
    // 设置sessionStorage
    setSession(key, value) {
        try {
            sessionStorage.setItem(key, JSON.stringify(value));
        } catch (error) {
            console.error('设置sessionStorage失败:', error);
        }
    },
    
    // 获取sessionStorage
    getSession(key, defaultValue = null) {
        try {
            const value = sessionStorage.getItem(key);
            return value ? JSON.parse(value) : defaultValue;
        } catch (error) {
            console.error('获取sessionStorage失败:', error);
            return defaultValue;
        }
    },
    
    // 删除sessionStorage
    removeSession(key) {
        try {
            sessionStorage.removeItem(key);
        } catch (error) {
            console.error('删除sessionStorage失败:', error);
        }
    }
};

// DOM工具
const domUtils = {
    // 获取元素
    $(selector) {
        return document.querySelector(selector);
    },
    
    // 获取所有元素
    $$(selector) {
        return document.querySelectorAll(selector);
    },
    
    // 添加类名
    addClass(element, className) {
        if (element && className) {
            element.classList.add(className);
        }
    },
    
    // 移除类名
    removeClass(element, className) {
        if (element && className) {
            element.classList.remove(className);
        }
    },
    
    // 切换类名
    toggleClass(element, className) {
        if (element && className) {
            element.classList.toggle(className);
        }
    },
    
    // 检查是否有类名
    hasClass(element, className) {
        return element && className && element.classList.contains(className);
    },
    
    // 设置样式
    setStyle(element, styles) {
        if (element && styles) {
            Object.assign(element.style, styles);
        }
    },
    
    // 获取元素位置
    getPosition(element) {
        if (!element) return { top: 0, left: 0 };
        
        const rect = element.getBoundingClientRect();
        return {
            top: rect.top + window.pageYOffset,
            left: rect.left + window.pageXOffset
        };
    },
    
    // 滚动到元素
    scrollTo(element, offset = 0) {
        if (!element) return;
        
        const position = this.getPosition(element);
        window.scrollTo({
            top: position.top - offset,
            behavior: 'smooth'
        });
    }
};

// URL工具
const urlUtils = {
    // 获取URL参数
    getParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    },
    
    // 设置URL参数
    setParam(name, value) {
        const url = new URL(window.location);
        url.searchParams.set(name, value);
        window.history.replaceState({}, '', url);
    },
    
    // 删除URL参数
    removeParam(name) {
        const url = new URL(window.location);
        url.searchParams.delete(name);
        window.history.replaceState({}, '', url);
    },
    
    // 获取所有参数
    getAllParams() {
        const params = {};
        const urlParams = new URLSearchParams(window.location.search);
        for (const [key, value] of urlParams) {
            params[key] = value;
        }
        return params;
    }
};

// 文件工具
const fileUtils = {
    // 下载文件
    download(data, filename, type = 'text/plain') {
        const blob = new Blob([data], { type });
        const url = URL.createObjectURL(blob);
        
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        URL.revokeObjectURL(url);
    },
    
    // 读取文件
    read(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = e => resolve(e.target.result);
            reader.onerror = reject;
            reader.readAsText(file);
        });
    },
    
    // 获取文件扩展名
    getExtension(filename) {
        return filename.split('.').pop().toLowerCase();
    },
    
    // 检查文件类型
    isImage(filename) {
        const imageExts = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
        return imageExts.includes(this.getExtension(filename));
    },
    
    // 检查文件大小
    checkSize(file, maxSize) {
        return file.size <= maxSize;
    }
};

// 防抖和节流
const throttleUtils = {
    // 防抖
    debounce(func, wait, immediate = false) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                timeout = null;
                if (!immediate) func(...args);
            };
            const callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func(...args);
        };
    },
    
    // 节流
    throttle(func, limit) {
        let inThrottle;
        return function(...args) {
            if (!inThrottle) {
                func.apply(this, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }
};

// 通用工具函数
const utils = {
    // 深拷贝
    deepClone(obj) {
        if (obj === null || typeof obj !== 'object') return obj;
        if (obj instanceof Date) return new Date(obj.getTime());
        if (obj instanceof Array) return obj.map(item => this.deepClone(item));
        if (typeof obj === 'object') {
            const clonedObj = {};
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    clonedObj[key] = this.deepClone(obj[key]);
                }
            }
            return clonedObj;
        }
    },
    
    // 合并对象
    merge(target, ...sources) {
        if (!sources.length) return target;
        const source = sources.shift();
        
        if (this.isObject(target) && this.isObject(source)) {
            for (const key in source) {
                if (this.isObject(source[key])) {
                    if (!target[key]) Object.assign(target, { [key]: {} });
                    this.merge(target[key], source[key]);
                } else {
                    Object.assign(target, { [key]: source[key] });
                }
            }
        }
        
        return this.merge(target, ...sources);
    },
    
    // 检查是否为对象
    isObject(item) {
        return item && typeof item === 'object' && !Array.isArray(item);
    },
    
    // 检查是否为空
    isEmpty(value) {
        if (value == null) return true;
        if (Array.isArray(value) || typeof value === 'string') return value.length === 0;
        if (typeof value === 'object') return Object.keys(value).length === 0;
        return false;
    },
    
    // 延迟执行
    delay(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    },
    
    // 重试函数
    async retry(fn, times = 3, delay = 1000) {
        for (let i = 0; i < times; i++) {
            try {
                return await fn();
            } catch (error) {
                if (i === times - 1) throw error;
                await this.delay(delay);
            }
        }
    }
};

// 导出所有工具
window.dateUtils = dateUtils;
window.stringUtils = stringUtils;
window.numberUtils = numberUtils;
window.validateUtils = validateUtils;
window.storageUtils = storageUtils;
window.domUtils = domUtils;
window.urlUtils = urlUtils;
window.fileUtils = fileUtils;
window.throttleUtils = throttleUtils;
window.utils = utils;

console.log('工具函数库已加载');