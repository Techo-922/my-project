export default {
    baseUrl: process.env.NODE_ENV === 'development' ? '/' : 'http://localhost:8080/',
    indexNav: [
        {
            name: '首页',
            url: '/index/home'
        },
        {
            name: '菜谱信息',
            url: '/index/caipuxinxi'
        },
        {
            name: '智能采购',
            url: '/index/shoppingList'
        },
        {
            name: '公告信息',
            url: '/index/news'
        },
        {
            name: '留言信息',
            url: '/index/messages'
        },
    ]
}
