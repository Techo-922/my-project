const base = {
    get() {
        return {
            url : "http://localhost:8080/",
            name: "springbootct3p7",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/front/front/dist/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "智能菜谱推荐系统"
        } 
    }
}
export default base
