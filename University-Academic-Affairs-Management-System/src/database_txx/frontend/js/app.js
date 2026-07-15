// Vue应用实例
const app = new Vue({
    el: '#app',
    data() {
        return {
            // 当前激活的标签页
            activeTab: 'dashboard',
            
            // 加载状态
            loading: false,
            
            // 用户信息
            userInfo: {
                id: 1,
                name: '管理员',
                avatar: '',
                type: 'admin',
                permissions: ['all']
            },

            // 分页信息
            pagination: {
                currentPage: 1,
                pageSize: 20,
                total: 0
            },

            studentsByDept: [],
            avgGradeByCourse: [],

            // 仪表盘数据
            dashboardData: {
                studentCount: 0,
                teacherCount: 0,
                courseCount: 0,
                departmentCount: 0
            },
            
            // 学生管理相关数据
            students: [],
            studentSearch: {
                keyword: '',
                department: '',
                grade: '',
                status: '',
                region:''
            },
            regions:[],
            selectedStudents: [],
            
            // 学生详情对话框
            studentDetailDialog: {
                visible: false,
                student: null,
                activeTab: 'basic'
            },
            
            // 教师管理相关数据
            teachers: [],
            teacherSearch: {
                keyword: '',
                department: '',
                title: ''
            },
            teacherCourses: [],
            //教师编辑
            editTeacherDialog: {
                visible: false
            },
            editTeacherForm: {
                tno: '',
                tname: '',
                sex: '',
                age: null,
                title: '',
                phone: ''
            },
            //教师详情
            teacherDetailDialog: {
                visible: false,
                teacher: null,
                activeTab: 'basic'
            },

            addStudentDialog: {
                visible: false
            },
            addStudentForm: {
                sno: '',
                sname: '',
                sex: '',
                age: null,
                dept_no: '',
                class_id: '',
                start_date: '',
                credit_hours: 0
            },

            importDialogVisible: false,
            fileList: [],
            importFile: null,
            courseRecords: [],
            gradeRecords: [],
            editStudentDialog: { visible: false },
            editStudentForm: {
                sno: '',
                sname: '',
                sex: '',
                age: null,
                dept_no: '',
                class_id: '',
                start_date: '',
                credit_hours: 0
            },
            addTeacherDialog: {
                visible: false
            },
            addTeacherForm: {
                tno: '',
                tname: '',
                sex: '',
                age: null,
                title: '',
                phone: ''
            },
            addTeacherRules: {
                tno: [{ required: true, message: '请输入工号', trigger: 'blur' }],
                tname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
                age: [{ type: 'number', required: true, message: '请输入年龄', trigger: 'blur' }],
                title: [{ required: true, message: '请输入职称', trigger: 'blur' }],
                phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
            },
            importTeacherDialogVisible: false,
            teacherFileList: [],
            teacherImportFile: null,

            //课程管理
            courseSearch: {
                 keyword: ''
            },
            courses: [],
            loadingCourses: false,
            addCourseDialog:{
                visible : false
            },
            selectedCourses: [],
            courseDetailDialog: {
                visible: false,
                activeTab: "basic",
                course: null,  // 课程基本信息
            },
            courseTeachingClasses: [], // 教学班级及授课老师信息列表
            addCourseForm: {
                cno: '',
                cname: '',
                credit_hour: 0,
                hour: 0,
                assess_type: ''
            },

            importCourseDialogVisible: false,
            courseImportFile: null,
            //编辑课程
            editCourseDialog: {
                visible: false
            },
            editCourseForm: {
                cno: '',            // 课程编号
                cname: '',          // 课程名称
                credit_hour: 0,     // 学分
                total_hours: 0,     // 总课时
                assess_type: ''     // 考核方式
            },

            //专业列表
           departments: [],
           deptSearch: {
               keyword: ''
           },
            //添加专业
            addDeptDialog: {
                visible: false
            },
            addDeptForm: {
                dept_no: '',
                dept_name: '',
                description: ''
            },
            addDeptRules: {
                dept_no: [{ required: true, message: '请输入专业编号', trigger: 'blur' }],
                dept_name: [{ required: true, message: '请输入专业名称', trigger: 'blur' }]
            },
            //修改专业
            editDeptDialog: {
                visible: false
            },
            editDeptForm: {
                dept_no: '',
                dept_name: ''
            },
            //专业详情
            deptDetailDialog: {
                visible: false,
                activeTab: 'basic',
                department: null
            },
            deptClasses: [],  // 当前专业下的班级列表
            deptFileList: [],
            deptImportFile: null,
            importDeptDialogVisible: false,

            grades: [],
            // 成绩管理相关数据
            gradeSearch: {
                keyword: "",     // 学号或学生名
                grade: "",       // 学年
            },

            termOptions: ['2023-2024-1', '2023-2024-2', '2024-2025-1'],  // 你可以根据实际情况填充学期选项
            gradeQuery: {
                sno: '',
                sname:'',
                term: '',
                page: 1,
                size: 20
            },
            gradeList: [],
            gradeTotal: 0,
            loadingGrades: false,

            gradeDialog: {
                visible: false,
                form: {
                    sno: '',
                    teaching_id: '',
                    grade: null,
                },
                isEdit: false, // 标识新增还是编辑
            },
            addGradeDialog: {
                visible: false
            },
            addGradeForm: {
                cname: '',
                sname: '',
                grade: ''
            },
            addGradeRules: {
                cname: [{ required: true, message: '请输入课程名', trigger: 'blur' }],
                sname: [{ required: true, message: '请输入学生名', trigger: 'blur' }],
                grade: [{ required: true, message: '请输入成绩', trigger: 'blur' }]
            },
            earnedCredits: '',
            sort: {
                prop: '',       // 排序字段
                order: '',      // asc / desc
            },
            //查看成绩详情
            gradeDetailDialog: {
                visible: false,
                activeTab: 'basic',
                student: null,
                grades: []  // 当前学生的成绩列表
            },
            //编辑成绩
            editGradeDialog: {
                visible: false,
            },
            editGradeForm: {
                sno: '',
                sname: '',
                teaching_id: null, // 替代 cname
                grade: null
            },
            selectedCourses: [],
            question: "",
            answer: "",
        }
    },
    
    mounted() {
        this.initApp();
    },
    
    methods: {
        // 初始化应用
        async initApp() {
            try {
                this.loading = true;
                
                // 加载基础数据
                await this.loadDashboardData();
                await this.loadDepartments();
                await this.loadStudents();
                await this.loadTeachers();
                await this.loadDepartments();
                await this.loadGrades();
                await this.loadRegions();
                // 初始化图表
                this.$nextTick(() => {
                    this.initCharts();
                });
                
            } catch (error) {
                console.error('应用初始化失败:', error);
                this.$message.error('应用初始化失败，请刷新页面重试');
            } finally {
                this.loading = false;
            }
        },
        
        // 设置激活的标签页
        setActiveTab(tab) {
            this.activeTab = tab;
            
            // 根据标签页加载对应数据
            switch (tab) {
                case 'students':
                    this.loadStudents();
                    break;
                case 'teachers':
                    this.loadTeachers();
                    break;
                case 'courses':
                    this.loadCourses();
                    break;
                case 'grades':
                    this.loadGrades();
                    break;
                case 'departments':
                    this.loadDepartments();
                    break;
                case 'dashboard':
                    this.loadDashboardData();
                    this.loadStudentsByDept();
                    this.$nextTick(() => {
                        this.initCharts();
                    });
                    break;
            }
        },

        handleSizeChange(newSize) {
            this.pagination.pageSize = newSize;
            this.pagination.currentPage = 1;

            if (this.activeTab === 'students') {
                this.loadStudents();
            } else if (this.activeTab === 'teachers') {
                this.loadTeachers();
            } else if (this.activeTab === 'courses') {
                this.loadCourses();
            } else if (this.activeTab === 'departments') {
                this.loadDepartments();
            } else if (this.activeTab === 'grades') {
                this.loadGrades();
            }

        },

        handleCurrentChange(newPage) {
            this.pagination.currentPage = newPage;

            if (this.activeTab === 'students') {
                this.loadStudents();
            } else if (this.activeTab === 'teachers') {
                this.loadTeachers();
            } else if (this.activeTab === 'courses') {
                this.loadCourses();
            } else if (this.activeTab === 'departments') {
                this.loadDepartments();
            } else if (this.activeTab === 'grades') {
                this.loadGrades();
            }
        },


        // 加载仪表盘数据
        async loadDashboardData() {
            try {
                this.loading = true;
                const response = await axios.get('http://localhost:8000/dashboard/stats');
                // 后端接口返回格式是 { student_count, teacher_count, course_count }
                this.dashboardData.studentCount = response.data.student_count;
                this.dashboardData.teacherCount = response.data.teacher_count;
                this.dashboardData.courseCount = response.data.course_count;
                this.dashboardData.departmentCount = response.data.department_count;
                // departmentCount如果后端没有提供，你可以单独请求或者保持0
            } catch (error) {
                console.error('加载仪表盘数据失败:', error);
                // 使用模拟数据
                this.dashboardData = {
                    studentCount: 1250,
                    teacherCount: 85,
                    courseCount: 156,
                    departmentCount: 12
                };
            } finally {
                this.loading = false;
            }
        },

        renderStudentsByDeptChart() {
            console.log('正在绘制专业分布图：', this.studentsByDept);
            initMajorChart(this.studentsByDept);
        },

        async loadStudentsByDept() {
            console.log('调用 loadStudentsByDept 方法');
            try {
                const response = await axios.get('http://localhost:8000/dashboard/charts/major');
                console.log('接口返回数据:', response.data);
                this.studentsByDept = response.data;
                this.renderStudentsByDeptChart();
            } catch (error) {
                console.error('加载学生按院系分布失败:', error);
                this.studentsByDept = [];
            }
        },

        // 加载学生列表
        async loadStudents() {
            try {
                this.loading = true;
                const params = {
                    page: this.pagination.currentPage,
                    size: this.pagination.pageSize,
                    keyword: this.studentSearch.keyword,
                    department: this.studentSearch.department,
                    grade: this.studentSearch.grade,
                    status: this.studentSearch.status,
                    region: this.studentSearch.region,
                };
                const response = await api.getStudents(params);
                // 这里判断后端返回的code
                if (response.code === 200) {
                    this.students = response.data;  // 直接用 data
                    // 你后端接口没返回总数，分页total暂时用假数据或者后端扩展返回总数
                    this.pagination.total = 100;
                } else {
                    this.$message.error(response.message || '加载学生列表失败');
                    this.students = [];
                    this.pagination.total = 0;
                }
            } catch (error) {
                console.error('加载学生列表失败:', error);
                this.students = this.generateMockStudents();
                this.pagination.total = 100;
            } finally {
                this.loading = false;
            }
        },
        async loadRegions() {
            // const res = await api.getRegions();
            const res = await axios.get('http://127.0.0.1:8000/regions');
            this.regions = res.data.data;
        },

        // 生成模拟学生数据
        generateMockStudents() {
            const mockStudents = [];
            const majors = ['计算机科学与技术', '软件工程', '电子信息工程', '机械设计制造及其自动化', '工商管理'];
            const classes = ['1班', '2班', '3班', '4班'];
            const grades = ['2021', '2022', '2023', '2024'];
            
            for (let i = 1; i <= 20; i++) {
                const grade = grades[Math.floor(Math.random() * grades.length)];
                const major = majors[Math.floor(Math.random() * majors.length)];
                const className = classes[Math.floor(Math.random() * classes.length)];
                
                mockStudents.push({
                    id: i,
                    studentId: `${grade}${String(i).padStart(4, '0')}`,
                    name: `学生${i}`,
                    gender: Math.random() > 0.5 ? '男' : '女',
                    major: major,
                    className: className,
                    grade: grade,
                    phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
                    email: `student${i}@example.com`,
                    status: Math.random() > 0.1 ? '在校' : '休学',
                    birthDate: '2000-01-01',
                    idCard: '110101200001010001',
                    address: '北京市朝阳区',
                    hometown: '北京市',
                    departmentName: '计算机科学与技术学院',
                    enrollmentDate: `${grade}-09-01`,
                    totalCredits: Math.floor(Math.random() * 100)
                });
            }
            
            return mockStudents;
        },
        
        // 搜索学生
        searchStudents() {
            this.pagination.currentPage = 1;
            this.loadStudents();
        },
        
        // 查看学生详情
        async viewStudent(student) {
            this.studentDetailDialog.student = { ...student }; // 复制当前学生数据，避免直接修改原数据
            this.studentDetailDialog.visible = true;
            this.studentDetailDialog.activeTab = 'basic'; // 切换到默认的 tab，例如 "基本信息"

            try {
                const res = await axios.get(`http://127.0.0.1:8000/students_courses/${student.sno}`);
                if (res.data.code === 200) {
                    this.courseRecords = res.data.data;
                } else {
                    this.courseRecords = [];
                }
            } catch (error) {
                console.error('获取选课记录失败：', error);
                this.courseRecords = [];
            }

            const gradeRes = await axios.get(`http://127.0.0.1:8000/students_grades/${student.sno}`);
            if (gradeRes.data.code === 200) {
                this.gradeRecords = gradeRes.data.data;
            } else {
                this.gradeRecords = [];
            }
        },

        handleStudentSelectionChange(selection) {
            this.selectedStudents = selection;
        },

        // 关闭学生详情对话框
        closeStudentDetailDialog() {
            this.studentDetailDialog.visible = false;
            this.studentDetailDialog.student = null;
        },

        // 编辑学生
        editStudent(student) {
            this.editStudentForm = { ...student };  // 拷贝学生数据到表单
            this.editStudentDialog.visible = true;  // 显示对话框
        },
        handleEditStudentCancel() {
            this.editStudentDialog.visible = false;
        },
        async submitEditStudent() {
            try {
                // 调接口更新学生信息，例如：
                const res = await axios.post('http://127.0.0.1:8000/students_edit', this.editStudentForm);
                if(res.data.code === 200) {
                    this.$message.success('修改成功');
                    this.editStudentDialog.visible = false;
                    this.loadStudents();  // 重新加载学生列表
                } else {
                    this.$message.error(res.data.message || '修改失败');
                }
            } catch(e) {
                console.error(e);
                this.$message.error('请求出错');
            }
        },
        
        // 删除学生
        deleteStudent(student) {
            this.$confirm(`确定要删除学生 ${student.sname} 吗？`, '删除确认', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    const response = await axios.delete(`http://127.0.0.1:8000/students_delete/${student.sno}`);
                    if (response.data.code === 200) {
                        this.$message.success('删除成功');
                        this.loadStudents(); // 重新加载列表
                    } else {
                        this.$message.error(response.data.message || '删除失败');
                    }
                } catch (error) {
                    console.error('删除出错:', error);
                    this.$message.error('请求出错，请稍后再试');
                }
            }).catch(() => {
                // 用户取消删除
            });
        },
        
        // 显示添加学生对话框
        showAddStudentDialog() {
            this.addStudentDialog.visible = true;
            this.addStudentForm = {
                sno: '',
                sname: '',
                sex: '',
                age: null,
                dept_no: '',
                class_id: '',
                start_date: '',
                credit_hours: 0
            };
        },
        handleAddStudentCancel() {
            this.addStudentDialog.visible = false;
        },
        async submitAddStudent() {
            try {
                const response = await axios.post('http://127.0.0.1:8000/students_add', this.addStudentForm);
                if (response.data && response.data.code === 200) {
                    this.$message.success('添加成功');
                    this.addStudentDialog.visible = false;
                    this.loadStudents();  // 刷新学生列表
                } else {
                    this.$message.error(response.data.message || '添加失败');
                }
            } catch (err) {
                console.error('添加学生出错:', err);
                this.$message.error('请求出错');
            }
        },

        
        // 导入学生
        importStudents() {
            this.importDialogVisible = true;
        },
        handleFileChange(file) {
            this.importFile = file.raw;
        },
        async submitImport() {
            if (!this.importFile) {
                this.$message.warning("请先选择文件！");
                return;
            }
            const formData = new FormData();
            formData.append("file", this.importFile);

            try {
                const response = await axios.post("http://127.0.0.1:8000/students_import", formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });

                if (response.data.success) {
                    this.$message.success("导入成功！");
                    this.importDialogVisible = false;
                    this.loadStudents(); // 重新加载
                } else {
                    this.$message.error(response.data.message || "导入失败");
                }
            } catch (error) {
                console.error("导入失败：", error);
                this.$message.error("上传失败，请检查格式或重试");
            }
        },
        
        // 导出学生
        exportStudents() {
            this.$message.info("正在导出学生数据...");
            const url = 'http://127.0.0.1:8000/students/export';
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'students.xlsx'); // 文件名
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },
        //教师
        handleTeacherSelectionChange(selection) {
            this.selectedTeachers = selection;  // 或 this.selectedTeachers = selection;
        },

        // 加载教师列表
        async loadTeachers() {
            try {
                this.loading = true;
                const params = {
                    page: this.pagination.currentPage,
                    size: this.pagination.pageSize,
                    keyword: this.teacherSearch.keyword // 如果你有搜索字段
                };
                const response = await api.getTeachers(params);
                if (response.code === 200) {
                    this.teachers = response.data;
                    this.pagination.total = response.total || 0; // 如果后端返回了总数
                } else {
                    this.teachers = [];
                    this.pagination.total = 0;
                    this.$message.error(response.message || '加载教师列表失败');
                }
            } catch (error) {
                console.error('加载教师列表失败:', error);
                this.teachers = [];
                this.pagination.total = 0;
            } finally {
                this.loading = false;
            }
        },

        //搜索教师
        searchTeachers() {
            this.pagination.currentPage = 1;
            this.loadTeachers();
        },

        showAddTeacherDialog() {
            this.addTeacherDialog.visible = true;
            this.addTeacherForm = {
                tno: '',
                tname: '',
                sex: '',
                age: null,
                title: '',
                phone: ''
            };
        },


        // 关闭添加教师对话框
        handleAddTeacherCancel() {
            this.addTeacherDialog.visible = false;
        },

        //添加教师 主要逻辑
        async submitAddTeacher() {
            try {
                const res = await axios.post('http://127.0.0.1:8000/teachers_add', this.addTeacherForm);
                if (res.data.code === 200) {
                    this.$message.success('添加教师成功');
                    this.addTeacherDialog.visible = false;
                    this.loadTeachers();
                } else {
                    this.$message.error(res.data.message || '添加教师失败');
                }
            } catch (err) {
                console.error('添加教师失败:', err);
                this.$message.error('请求出错');
            }
        },

        //编辑教师
        editTeacher(teacher) {
            this.editTeacherForm = { ...teacher };
            this.editTeacherDialog.visible = true;
        },

        handleEditTeacherCancel() {
            this.editTeacherDialog.visible = false;
        },
        async submitEditTeacher() {
            try {
                const res = await axios.post('http://127.0.0.1:8000/teachers_edit', this.editTeacherForm);
                if (res.data.code === 200) {
                    this.$message.success('修改成功');
                    this.editTeacherDialog.visible = false;
                    this.loadTeachers();
                } else {
                    this.$message.error(res.data.message || '修改失败');
                }
            } catch (error) {
                console.error('修改失败：', error);
                this.$message.error('请求出错');
            }
        },
        //删除教师
        deleteTeacher(teacher) {
            this.$confirm(`确定删除教师 ${teacher.tname} 吗？`, '提示', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    const res = await axios.delete(`http://127.0.0.1:8000/teachers_delete/${teacher.tno}`);
                    if (res.data.code === 200) {
                        this.$message.success('删除成功');
                        this.loadTeachers();
                    } else {
                        this.$message.error(res.data.message || '删除失败');
                    }
                } catch (err) {
                    console.error('删除出错：', err);
                    this.$message.error('请求失败，存在关联数据，无法删除');
                }
            }).catch(() => {});
        },

        //批量导入教师
        importTeachers() {
            this.importTeacherDialogVisible = true;
        },
        handleTeacherFileChange(file) {
            this.teacherImportFile = file.raw;
        },

        async submitImportTeachers() {
            if (!this.teacherImportFile) {
                this.$message.warning('请先选择文件');
                return;
            }
            const formData = new FormData();
            formData.append("file", this.teacherImportFile);

            try {
                const res = await axios.post('http://127.0.0.1:8000/teachers_import', formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                if (res.data.success) {
                    this.$message.success("导入成功");
                    this.importTeacherDialogVisible = false;
                    this.loadTeachers();
                } else {
                    this.$message.error(res.data.message || "导入失败");
                }
            } catch (error) {
                console.error("导入失败：", error);
                this.$message.error("上传失败，请检查格式");
            }
        },

        //查看教师主要信息
        viewTeacher(teacher) {
            this.teacherDetailDialog.teacher = teacher;
            this.teacherDetailDialog.visible = true;
            this.teacherDetailDialog.activeTab = 'basic';
            // 调用后端获取授课信息
            axios.get(`http://127.0.0.1:8000/teacher_courses?tno=${teacher.tno}`).then(res => {
                this.teacherCourses = res.data.courses || [];
            }).catch(() => {
                this.teacherCourses = [];
            });
        },
        closeTeacherDetailDialog() {
            this.teacherDetailDialog.visible = false;
        },

        //教师导出
        exportTeachers() {
            this.$message.info("正在导出教师数据...");
            const url = 'http://127.0.0.1:8000/teachers/export';
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'teachers.xlsx');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },


        //课程管理
        handleCourseSelectionChange(selection) {
            this.selectedCourses = selection;  // 或 this.selectedTeachers = selection;
        },

        searchCourses() {
            this.pagination.currentPage = 1;
            this.loadCourses();
        },

        //添加课程表格
        showAddCourseDialog() {
            this.addCourseDialog.visible = true;
            this.addCourseForm = {
                cno: '',
                cname: '',
                credit_hour: 0,
                hour: 0,
                assess_type: ''
            };
        },

        //取消添加课程
        handleAddCourseCancel() {
            this.addCourseDialog.visible = false;
        },

        //批量导入课程
        importCourses() {
            this.importCourseDialogVisible = true;
        },
        handleCourseFileChange(file) {
            this.courseImportFile = file.raw;
        },

        async submitImportCourses() {
            if (!this.courseImportFile) {
                this.$message.warning("请先选择文件！");
                return;
            }
            const formData = new FormData();
            formData.append("file", this.courseImportFile);

            try {
                const response = await axios.post("http://127.0.0.1:8000/courses_import", formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                if (response.data.success) {
                    this.$message.success("导入成功！");
                    this.importCourseDialogVisible = false;
                    this.loadCourses();
                } else {
                    this.$message.error(response.data.message || "导入失败");
                }
            } catch (error) {
                console.error("导入失败：", error);
                this.$message.error("上传失败，请检查格式");
            }
        },

        // 加载课程数据
        async loadCourses() {
            try {
                const res = await axios.get("http://127.0.0.1:8000/courses", {
                    params: {
                        page: this.pagination.currentPage,
                        size: this.pagination.pageSize,
                        keyword: this.courseSearch.keyword
                    }
                });

                if (res.data.code === 200) {
                    this.courses = res.data.data;
                    this.pagination.total = res.data.total;
                } else {
                    this.$message.error(res.data.message || "课程加载失败");
                }
            } catch (error) {
                console.error("课程加载失败：", error);
                this.$message.error("请求失败");
            }
        },
        async submitAddCourse() {
            try {
                const res = await axios.post('http://127.0.0.1:8000/courses_add', this.addCourseForm);
                if (res.data.code === 200) {
                    this.$message.success('添加课程成功');
                    this.addCourseDialog.visible = false;
                    this.loadCourses();
                } else {
                    this.$message.error(res.data.message || '添加课程失败');
                }
            } catch (err) {
                console.error('添加课程失败:', err);
                this.$message.error('请求出错');
            }
        },

        //课程详情
        viewCourse(course) {
            this.courseDetailDialog.course = course;
            this.courseDetailDialog.visible = true;
            this.courseDetailDialog.activeTab = "basic";
            this.loadCourseAvgGrades(course.cno);
            // 这里调用接口加载教学班级及授课老师数据
            this.loadCourseTeachingClasses(course.cno);
        },
        async loadCourseTeachingClasses(cno) {
            try {
                const res = await axios.get('http://127.0.0.1:8000/course_teaching_classes', { params: { cno } });
                if (res.data.code === 200) {
                    this.courseTeachingClasses = res.data.data;
                } else {
                    this.courseTeachingClasses = [];
                    this.$message.error('加载教学班级失败');
                }
            } catch (error) {
                console.error(error);
                this.courseTeachingClasses = [];
                this.$message.error('请求失败');
            }
        },
        closeCourseDetailDialog() {
            this.courseDetailDialog.visible = false;
            this.courseDetailDialog.course = null;
            this.courseTeachingClasses = [];
        },
        async loadCourseAvgGrades(cno) {
            try {
                const res = await axios.get("http://127.0.0.1:8000/courses/avg_grades", {
                    params: { cno }
                });
                if (res.data.code === 200) {
                    this.courseAvgGrades = res.data.data;
                } else {
                    this.$message.error("获取课程平均成绩失败：" + res.data.message);
                }
            } catch (error) {
                console.error(error);
                this.$message.error("请求失败");
            }
        },


        //删除课程
        deleteCourse(course) {
            this.$confirm(`确定删除课程 ${course.cname} 吗？`, '提示', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    const res = await axios.delete(`http://127.0.0.1:8000/courses_delete/${course.cno}`);
                    if (res.data.code === 200) {
                        this.$message.success('删除成功');
                        this.loadCourses();
                    } else {
                        this.$message.error(res.data.message || '删除失败');
                    }
                } catch (err) {
                    console.error('删除出错：', err);
                    this.$message.error('请求失败');
                }
            }).catch(() => {});
        },

        //编辑课程
        editCourse(teacher) {
            this.editCourseForm = { ...teacher };
            this.editCourseDialog.visible = true;
        },
        handleEditCourseCancel() {
            this.editCourseDialog.visible = false;
        },
        async submitEditCourse() {
            try {
                const res = await axios.post('http://127.0.0.1:8000/courses_edit', this.editCourseForm);
                if (res.data.code === 200) {
                    this.$message.success('修改成功');
                    this.editCourseDialog.visible = false;
                    this.loadCourses(); // 重新加载课程
                } else {
                    this.$message.error(res.data.message || '修改失败');
                }
            } catch (error) {
                console.error('修改失败：', error);
                this.$message.error('请求出错');
            }
        },
        exportCourses() {
            this.$message.info("正在导出课程数据...");
            const url = 'http://127.0.0.1:8000/courses/export';
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'courses.xlsx');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },

        //专业管理
        async loadDepartments() {
            this.loading = true;
            try {
                const res = await axios.get("http://127.0.0.1:8000/departments_get", {
                    params: {
                        page: this.pagination.currentPage,
                        size: this.pagination.pageSize,
                        keyword: this.deptSearch.keyword
                    }
                });
                console.log('部门数据', res.data.data, 'total:', res.data.total);
                if (res.data.code === 200) {
                    this.departments = res.data.data;
                    this.pagination.total = res.data.total;
                    console.log("专业加载成功", this.departments);
                } else {
                    this.$message.error(res.data.message || "专业加载失败");
                }
            } catch (error) {
                console.error("专业加载失败：", error);
                this.$message.error("请求失败");
            } finally {
                this.loading = false;
            }
        },

        searchDept() {
            this.pagination.currentPage = 1;
            this.loadDepartments();
        },

        //添加专业
        showAddDeptDialog() {
            this.addDeptDialog.visible = true;
        },
        handleAddDeptCancel() {
            this.addDeptDialog.visible = false;
        },
        async submitAddDept() {
            this.$refs.addDeptFormRef.validate(async (valid) => {
                if (!valid) return;
                try {
                    const res = await axios.post('http://127.0.0.1:8000/departments_add', this.addDeptForm);
                    if (res.data.code === 200) {
                        this.$message.success('添加成功');
                        this.addDeptDialog.visible = false;
                        this.loadDepartments();  // 刷新专业列表
                    } else {
                        this.$message.error(res.data.message || '添加失败');
                    }
                } catch (error) {
                    console.error('添加失败：', error);
                    this.$message.error('请求出错');
                }
            });
        },
        // 📤 导出部门
        async exportDept() {
            try {
                const res = await axios.get('http://127.0.0.1:8000/departments/export', {
                    responseType: 'blob'  // 导出文件流
                });
                const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
                const link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = '部门信息.xlsx';
                link.click();
                URL.revokeObjectURL(link.href);
            } catch (err) {
                console.error('导出失败:', err);
                this.$message.error('导出失败');
            }
        },

        // 📥 处理文件变更
        handleDeptFileChange(file) {
            this.deptImportFile = file.raw;
        },

        // 📥 上传导入
        async submitImportDept() {
            if (!this.deptImportFile) {
                this.$message.warning('请先选择文件');
                return;
            }
            const formData = new FormData();
            formData.append('file', this.deptImportFile);
            try {
                const res = await axios.post('http://127.0.0.1:8000/departments/import', formData);
                if (res.data.code === 200) {
                    this.$message.success('导入成功');
                    this.importDeptDialogVisible = false;
                    this.searchDepts();
                } else {
                    this.$message.error(res.data.message || '导入失败');
                }
            } catch (err) {
                console.error('导入失败:', err);
                this.$message.error('导入出错');
            }
        },

        // 📤 点击“批量导入”
        importDept() {
            this.importDeptDialogVisible = true;
            this.deptImportFile = null;
            this.deptFileList = [];
        },

        //修改专业
        showEditDeptDialog(dept) {
            this.editDeptForm = { ...dept };  // 设置要编辑的值
            this.editDeptDialog.visible = true;
        },
        handleEditDeptCancel() {
            this.editDeptDialog.visible = false;
        },
        editDept(row) {
            this.editDeptForm = {
                dept_no: row.dept_no,
                dept_name: row.dept_name
            };
            this.editDeptDialog.visible = true;
        },
        async submitEditDept() {
            try {
                const res = await axios.put("http://127.0.0.1:8000/departments_update", this.editDeptForm);
                if (res.data.code === 200) {
                    this.$message.success("修改成功");
                    this.editDeptDialog.visible = false;
                    this.loadDepartments();  // 刷新专业列表
                } else {
                    this.$message.error(res.data.message || "修改失败");
                }
            } catch (err) {
                console.error("修改失败：", err);
                this.$message.error("请求出错");
            }
        },

        //专业详情
        // 关闭对话框
        closeDeptDetailDialog() {
            this.deptDetailDialog.visible = false;
            this.deptDetailDialog.department = null;
            this.deptClasses = [];
        },
        // 查询班级信息
        async loadDeptClasses(deptNo) {
            try {
                const res = await axios.get(`http://127.0.0.1:8000/departments/${deptNo}/classes`);
                this.deptClasses = res.data.classes || [];
            } catch (err) {
                console.error("加载班级信息失败", err);
            }
        },
        viewDept(dept) {
            this.deptDetailDialog.department = dept;
            this.deptDetailDialog.visible = true;
            this.deptDetailDialog.activeTab = 'basic';
            this.loadDeptClasses(dept.dept_no);
        },

        async deleteDept(dept) {
            this.$confirm(`确定删除专业【${dept.dept_name}】吗？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    // 调用后端删除接口，假设路径是 /departments_delete/{dept_no}
                    await axios.delete(`http://127.0.0.1:8000/departments_delete/${dept.dept_no}`);

                    this.$message({
                        type: 'success',
                        message: '删除成功'
                    });

                    // 删除成功后刷新列表
                    this.loadDepartments();

                } catch (error) {
                    this.$message.error('删除失败：' + (error.response?.data?.message || error.message));
                }
            }).catch(() => {
                // 取消删除
            });
        },

        // 加载成绩列表
        searchGrade() {
            this.pagination.currentPage = 1;
            this.loadGrades();
        },

        // async loadGrades() {
        //     try {
        //         const params = {
        //             keyword: this.gradeQuery.sno || '',
        //             grade_year: this.gradeQuery.term || '',
        //             page: this.pagination.currentPage,
        //             size: this.pagination.pageSize
        //         };
        //         const res = await axios.get('http://127.0.0.1:8000/grades/year_summary', { params });
        //         if (res.data.code === 200) {
        //             this.grades = res.data.data;
        //             this.pagination.total = res.data.total;
        //         } else {
        //             this.$message.error('查询失败: ' + res.data.message);
        //         }
        //     } catch (error) {
        //         console.error("加载成绩列表失败:", error);
        //         this.$message.error("加载成绩失败");
        //     } finally {
        //         this.loading = false;
        //     }
        // },

        async searchGrades() {
            this.loadingGrades = true;
            try {
                console.log("searchGrades 触发");

                const params = {
                    keyword: this.gradeSearch.keyword || "",       // 关键词支持模糊查
                    grade_year: this.studentSearch.grade || "",    // 学年
                    page: this.pagination.currentPage || 1,
                    size: this.pagination.pageSize || 20,
                };

                const res = await axios.get('http://127.0.0.1:8000/grades/year_summary', { params });

                if (res.data.code === 200) {
                    this.grades = res.data.data;
                    this.pagination.total = res.data.total;
                } else {
                    this.$message.error('查询失败: ' + res.data.message);
                }
            } catch (error) {
                console.error("接口请求失败", error);
                this.$message.error('接口请求失败');
            } finally {
                this.loadingGrades = false;
            }
        },


        //添加成绩
        showAddGradeDialog() {
            this.addGradeDialog.visible = true;
        },
        handleAddGradeCancel() {
            this.addGradeDialog.visible = false;
            this.$refs.addGradeFormRef.resetFields();
        },
        async submitAddGrade() {
            this.$refs.addGradeFormRef.validate(async (valid) => {
                if (!valid) return;

                try {
                    const res = await axios.post("http://127.0.0.1:8000/grades/add_by_name", {
                        cname: this.addGradeForm.cname,
                        sname: this.addGradeForm.sname,
                        grade: this.addGradeForm.grade
                    });

                    if (res.data.code === 200) {
                        this.$message.success('成绩添加成功');
                        this.addGradeDialog.visible = false;
                        this.loadGrades(); // 刷新成绩表格
                    } else {
                        this.$message.error(res.data.message || '添加失败');
                    }
                } catch (error) {
                    console.error("成绩添加失败", error);
                    this.$message.error("接口异常");
                }
            });
            this.earnedCredits = '';
        },

        async fetchCredits() {
            const { sname } = this.addGradeForm;
            if (!sname) {
                this.earnedCredits = '';
                return;
            }
            try {
                const res = await axios.get('http://127.0.0.1:8000/grades/earned_credits', {
                    params: { sname }
                });
                if (res.data.code === 200) {
                    this.earnedCredits = res.data.credits ?? '0';
                } else {
                    this.earnedCredits = '查询失败';
                }
            } catch (error) {
                console.error('查询已修学分失败:', error);
                this.earnedCredits = '查询失败';
            }
        },
         handleSortChange({ prop, order }) {
             // order 是 'ascending' 或 'descending' 或 null
             this.sort.prop = prop;
             this.sort.order = order === 'ascending' ? 'asc' : order === 'descending' ? 'desc' : '';
             this.loadGrades();
         },

        async loadGrades() {
            this.loading = true;
            try {
                const params = {
                    page: this.pagination.currentPage,
                    size: this.pagination.pageSize,
                    sort_prop: this.sort.prop,
                    sort_order: this.sort.order,
                    // 其他筛选参数
                };
                const res = await axios.get('http://127.0.0.1:8000/grades/year_summary', { params });
                if (res.data.code === 200) {
                    this.grades = res.data.data;
                    this.pagination.total = res.data.total;
                } else {
                    this.grades = [];
                    this.$message.error('加载失败: ' + res.data.message);
                }
            } catch (e) {
                this.grades = [];
                this.$message.error('接口请求失败');
            } finally {
                this.loading = false;
            }
        },
        //查看学生详情
        async viewGrade(row) {
            try {
                // 假设 row.sno 存的是学号
                const res = await axios.get(`http://127.0.0.1:8000/students/detail_with_grades?sno=${row.sno}`);
                if (res.data.code === 200) {
                    this.gradeDetailDialog.student = res.data.student;
                    this.gradeDetailDialog.grades = res.data.grades;
                    this.gradeDetailDialog.visible = true;
                    this.gradeDetailDialog.activeTab = "basic";
                } else {
                    this.$message.error("加载学生详情失败");
                }
            } catch (error) {
                console.error("加载详情错误：", error);
                this.$message.error("接口异常");
            }
        },

        closeGradeDetailDialog() {
            this.gradeDetailDialog.visible = false;
            this.gradeDetailDialog.student = null;
            this.gradeDetailDialog.grades = [];
        },

        // editGrade(row) {
        //     // row 为当前成绩行，含 sno/sname/cname/grade
        //     this.editGradeForm = {
        //         sno: row.sno,
        //         sname: row.sname,
        //         cname: row.course_name,
        //         grade: row.grade
        //     };
        //     this.editGradeDialog.visible = true;
        // },
        // async editGrade(row) {
        //     // 加载学生的所有选课信息
        //     await this.loadStudentCourses(row.sno);
        //
        //     // 查找对应 teaching_id（通过 course_name + term 匹配）
        //     const matchedCourse = this.selectedCourses.find(
        //         course => course.course_name === row.course_name && course.term === row.term
        //     );
        //
        //     this.editGradeForm = {
        //         sno: row.sno,
        //         sname: row.sname,
        //         teaching_id: matchedCourse ? matchedCourse.teaching_id : null,
        //         grade: row.grade
        //     };
        //     this.editGradeDialog.visible = true;
        // },
        async loadStudentCourses(sno) {
            // 模拟接口请求
            const res = await axios.get(`http://127.0.0.1:8000/students/${sno}/selected_courses`);
            if (res.data.code === 200) {
                this.selectedCourses = res.data.data;
            } else {
                this.selectedCourses = [];
            }
        },

        async editGrade(row) {
            console.log("开始加载学生课程", row.sno);
            await this.loadStudentCourses(row.sno);
            console.log("加载完成课程", this.selectedCourses);

            const matchedCourse = this.selectedCourses.find(course =>
                course.course_name === row.course_name && course.term === row.term
            );

            console.log("匹配课程", matchedCourse);

            this.editGradeForm = {
                sno: row.sno,
                sname: row.sname,
                teaching_id: matchedCourse ? matchedCourse.teaching_id : null,
                grade: row.grade
            };

            this.editGradeDialog.visible = true;
        },


        async submitEditGrade() {
            try {
                const payload = {
                    sno: this.editGradeForm.sno,
                    teaching_id: this.editGradeForm.teaching_id,  // 改用 teaching_id
                    grade: this.editGradeForm.grade
                };
                const res = await axios.post('http://127.0.0.1:8000/grades/update', payload);
                if (res.data.code === 200) {
                    this.$message.success('成绩修改成功');
                    this.editGradeDialog.visible = false;
                    this.searchGrades(); // 刷新列表
                } else {
                    this.$message.error('修改失败：' + res.data.message);
                }
            } catch (error) {
                console.error(error);
                this.$message.error('请求失败');
            }
        },

        exportGrades() {
            const keyword = this.gradeSearch.keyword || "";
            const grade_year = this.gradeSearch.grade || "";
            const url = `http://127.0.0.1:8000/grades/export?keyword=${encodeURIComponent(keyword)}&grade_year=${encodeURIComponent(grade_year)}`;

            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'grades.xlsx');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },
        async handleSubmit() {
            if (!this.question.trim()) {
                this.$message.warning("请输入问题");
                return;
            }
            this.loading = true;
            this.answer = "";
            try {
                // 调用后端接口，比如 POST /api/langchain_qa
                const res = await axios.post("http://127.0.0.1:8000/langchain_qa", {
                    question: this.question.trim(),
                });
                // 假设后端返回 { answer: "xxx" }
                this.answer = res.data.answer || "未返回答案";
            } catch (error) {
                console.error(error);
                this.$message.error("请求失败，请稍后重试");
            } finally {
                this.loading = false;
            }
        },
        clear() {
            this.question = "";
            this.answer = "";
        },


        // 处理用户命令
        handleUserCommand(command) {
            switch (command) {
                case 'profile':
                    this.$message.info('个人资料功能正在开发中...');
                    break;
                case 'settings':
                    this.$message.info('系统设置功能正在开发中...');
                    break;
                case 'logout':
                    this.logout();
                    window.location.href ="login.html";
                    break;
            }
        },
        
        // 退出登录
        logout() {
            this.$confirm('确定要退出登录吗？', '确认退出', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 清除本地存储的用户信息
                localStorage.removeItem('userToken');
                localStorage.removeItem('userInfo');
                
                // 跳转到登录页面
                this.$message.success('退出登录成功');
                // window.location.href = 'login.html';
            }).catch(() => {
                this.$message.info('已取消退出');
            });
        },
        
        // 快速操作
        quickAction(action) {
            switch (action) {
                case 'addStudent':
                    this.setActiveTab('students');
                    this.showAddStudentDialog();
                    break;
                case 'addTeacher':
                    this.setActiveTab('teachers');
                    this.showAddTeacherDialog();
                    break;
                case 'addCourse':
                    this.setActiveTab('courses');
                    this.$message.info('添加课程功能正在开发中...');
                    break;
                case 'importData':
                    this.$message.info('导入数据功能正在开发中...');
                    break;
                case 'exportData':
                    this.$message.info('导出数据功能正在开发中...');
                    break;
            }
        },
        
        // 初始化图表
        initCharts() {
            if (typeof initDashboardCharts === 'function') {
                initDashboardCharts(this.studentsByDept);
            }
        },

        
        // 格式化日期
        formatDate(date, format = 'YYYY-MM-DD') {
            if (!date) return '';
            const d = new Date(date);
            const year = d.getFullYear();
            const month = String(d.getMonth() + 1).padStart(2, '0');
            const day = String(d.getDate()).padStart(2, '0');
            
            return format
                .replace('YYYY', year)
                .replace('MM', month)
                .replace('DD', day);
        },
        
        // 格式化数字
        formatNumber(num) {
            if (num === null || num === undefined) return '0';
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        
        // 获取性别图标
        getGenderIcon(gender) {
            return gender === '男' ? 'fas fa-mars' : 'fas fa-venus';
        },
        
        // 获取状态颜色
        getStatusColor(status) {
            const colorMap = {
                '在校': 'success',
                '休学': 'warning',
                '毕业': 'info',
                '退学': 'danger'
            };
            return colorMap[status] || 'default';
        },
        
        // 防抖函数
        debounce(func, wait) {
            let timeout;
            return function executedFunction(...args) {
                const later = () => {
                    clearTimeout(timeout);
                    func(...args);
                };
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
            };
        },
        
        // 节流函数
        throttle(func, limit) {
            let inThrottle;
            return function() {
                const args = arguments;
                const context = this;
                if (!inThrottle) {
                    func.apply(context, args);
                    inThrottle = true;
                    setTimeout(() => inThrottle = false, limit);
                }
            }
        }
    },
    
    // 计算属性
    computed: {
        // 是否有选中的学生
        hasSelectedStudents() {
            return this.selectedStudents.length > 0;
        },
        
        // 当前用户是否为管理员
        isAdmin() {
            return this.userInfo.type === 'admin';
        },
        
        // 格式化的统计数据
        formattedDashboardData() {
            return {
                studentCount: this.formatNumber(this.dashboardData.studentCount),
                teacherCount: this.formatNumber(this.dashboardData.teacherCount),
                courseCount: this.formatNumber(this.dashboardData.courseCount),
                departmentCount: this.formatNumber(this.dashboardData.departmentCount)
            };
        }
    },
    
    // 监听器
    watch: {
        // 监听搜索关键词变化
        'studentSearch.keyword': {
            handler: function(newVal, oldVal) {
                if (newVal !== oldVal) {
                    this.debounce(this.searchStudents, 500)();
                }
            }
        }
    },
    
    // 过滤器
    filters: {
        // 日期格式化过滤器
        dateFormat(value, format = 'YYYY-MM-DD') {
            if (!value) return '';
            const date = new Date(value);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            
            return format
                .replace('YYYY', year)
                .replace('MM', month)
                .replace('DD', day);
        },
        
        // 数字格式化过滤器
        numberFormat(value) {
            if (value === null || value === undefined) return '0';
            return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        
        // 性别格式化过滤器
        genderFormat(value) {
            const genderMap = {
                'M': '男',
                'F': '女',
                'male': '男',
                'female': '女'
            };
            return genderMap[value] || value;
        }
    }
});

// 全局错误处理
window.addEventListener('error', function(event) {
    console.error('全局错误:', event.error);
});

// 全局未处理的Promise拒绝
window.addEventListener('unhandledrejection', function(event) {
    console.error('未处理的Promise拒绝:', event.reason);
    event.preventDefault();
});

// 页面加载完成后的初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log('学生信息管理系统已加载');
});