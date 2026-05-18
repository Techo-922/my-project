<template>
<div class="content" :style='{"padding":"20px"}'>
	<div class="welcome-header" :style='{"padding":"30px","margin":"20px 0","borderRadius":"12px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","textAlign":"center","color":"#fff"}'>
		<div :style='{"fontSize":"28px","fontWeight":"bold","marginBottom":"10px"}'>欢迎使用 {{this.$project.projectName}}</div>
		<div :style='{"fontSize":"16px","opacity":"0.9"}'>智能菜谱推荐系统管理平台</div>
	</div>
    
    <div class="cardView">
        <div class="stats-cards" :style='{"margin":"0 0 30px 0","alignItems":"center","justifyContent":"center","display":"flex","gap":"20px"}'>
			<div :style='{"padding":"25px","borderRadius":"12px","background":"linear-gradient(135deg, #f093fb 0%, #f5576c 100%)","color":"#fff","textAlign":"center","minWidth":"200px","boxShadow":"0 4px 15px rgba(0,0,0,0.1)"}' v-if="isAuth('caipuxinxi','首页总数')">
				<div :style='{"fontSize":"32px","fontWeight":"bold","marginBottom":"8px"}'>{{caipuxinxiCount}}</div>
				<div :style='{"fontSize":"16px","opacity":"0.9"}'>菜谱信息总数</div>
			</div>
			<div :style='{"padding":"25px","borderRadius":"12px","background":"linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)","color":"#fff","textAlign":"center","minWidth":"200px","boxShadow":"0 4px 15px rgba(0,0,0,0.1)"}' v-if="isAuth('pingfenxinxi','首页总数')">
				<div :style='{"fontSize":"32px","fontWeight":"bold","marginBottom":"8px"}'>{{pingfenxinxiCount}}</div>
				<div :style='{"fontSize":"16px","opacity":"0.9"}'>评分信息总数</div>
			</div>
        </div>
        
        <div class="charts-container" style="display: flex;align-items: flex-start;width: 100%;gap: 20px;flex-wrap: wrap;">
            <el-card :style='{"borderRadius":"12px","border":"2px solid #dbd9f4","boxShadow":"0 4px 15px rgba(0,0,0,0.08)","flex":"1","minWidth":"300px"}' v-if="isAuth('caipuxinxi','首页统计')">
                <div id="caipuxinxiChart1" style="width:100%;height:400px;"></div>
            </el-card>
            <el-card :style='{"borderRadius":"12px","border":"2px solid #dbd9f4","boxShadow":"0 4px 15px rgba(0,0,0,0.08)","flex":"1","minWidth":"300px"}' v-if="isAuth('caipuxinxi','首页统计')">
                <div id="caipuxinxiChart2" style="width:100%;height:400px;"></div>
            </el-card>
            <el-card :style='{"borderRadius":"12px","border":"2px solid #dbd9f4","boxShadow":"0 4px 15px rgba(0,0,0,0.08)","flex":"1","minWidth":"300px"}' v-if="isAuth('pingfenxinxi','首页统计')">
                <div id="pingfenxinxiChart1" style="width:100%;height:400px;"></div>
            </el-card>
        </div>
    </div>
</div>
</template>
<script>
//3
import router from '@/router/router-static'
import * as echarts from 'echarts'
export default {
	data() {
		return {
            caipuxinxiCount: 0,
            pingfenxinxiCount: 0,
		};
	},
  mounted(){
    this.init();
    this.getcaipuxinxiCount();
    this.caipuxinxiChat1();
    this.caipuxinxiChat2();
    this.getpingfenxinxiCount();
    this.pingfenxinxiChat1();
  },
  methods:{
    init(){
        if(this.$storage.get('Token')){
        this.$http({
            url: `${this.$storage.get('sessionTable')}/session`,
            method: "get"
        }).then(({ data }) => {
            if (data && data.code != 0) {
            router.push({ name: 'login' })
            }
        });
        }else{
            router.push({ name: 'login' })
        }
    },
    getcaipuxinxiCount() {
        this.$http({
            url: `caipuxinxi/count`,
            method: "get"
        }).then(({
            data
        }) => {
            if (data && data.code == 0) {
                this.caipuxinxiCount = data.data
            }
        })
    },

    caipuxinxiChat1() {
      this.$nextTick(()=>{

        var caipuxinxiChart1 = echarts.init(document.getElementById("caipuxinxiChart1"),'macarons');
        this.$http({
            url: "caipuxinxi/group/caishileixing",
            method: "get",
        }).then(({ data }) => {
            if (data && data.code === 0) {
                let res = data.data;
                let xAxis = [];
                let yAxis = [];
                let pArray = []
                for(let i=0;i<res.length;i++){
                    xAxis.push(res[i].caishileixing);
                    yAxis.push(parseFloat((res[i].total)));
                    pArray.push({
                        value: parseFloat((res[i].total)),
                        name: res[i].caishileixing
                    })
                }
                var option = {};
                option = {
                        title: {
                            text: '菜谱分类统计',
                            left: 'center'
                        },
                        tooltip: {
                          trigger: 'item',
                          formatter: '{b} : {c} ({d}%)'
                        },
                        series: [
                            {
                                type: 'pie',
                                radius: ['25%', '55%'],
                                center: ['50%', '60%'],
                                data: pArray,
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                };
                // 使用刚指定的配置项和数据显示图表。
                caipuxinxiChart1.setOption(option);
                  //根据窗口的大小变动图表
                window.onresize = function() {
                    caipuxinxiChart1.resize();
                };
            }
        });
      })
    },

    caipuxinxiChat2() {
      this.$nextTick(()=>{

        var caipuxinxiChart2 = echarts.init(document.getElementById("caipuxinxiChart2"),'macarons');
        this.$http({
            url: `caipuxinxi/value/caipumingcheng/fenshu`,
            method: "get",
        }).then(({ data }) => {
            if (data && data.code === 0) {
                let res = data.data;
                let xAxis = [];
                let yAxis = [];
                let pArray = []
                for(let i=0;i<res.length;i++){
                    xAxis.push(res[i].caipumingcheng);
                    yAxis.push(parseFloat((res[i].total)));
                    pArray.push({
                        value: parseFloat((res[i].total)),
                        name: res[i].caipumingcheng
                    })
                }
                var option = {};
                option = {
                    title: {
                        text: '菜谱评分统计',
                        left: 'center'
                    },
                    tooltip: {
                      trigger: 'item',
                      formatter: '{b} : {c}'
                    },
                    xAxis: {
                        type: 'category',
                        data: xAxis,
                        axisLabel : {
                            rotate:70
                        }
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: yAxis,
                        type: 'bar'
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                caipuxinxiChart2.setOption(option);
                  //根据窗口的大小变动图表
                window.onresize = function() {
                    caipuxinxiChart2.resize();
                };
            }
        });
      })
    },





    getpingfenxinxiCount() {
        this.$http({
            url: `pingfenxinxi/count`,
            method: "get"
        }).then(({
            data
        }) => {
            if (data && data.code == 0) {
                this.pingfenxinxiCount = data.data
            }
        })
    },

    pingfenxinxiChat1() {
      this.$nextTick(()=>{

        var pingfenxinxiChart1 = echarts.init(document.getElementById("pingfenxinxiChart1"),'macarons');
        this.$http({
            url: "pingfenxinxi/group/pingfenriqi",
            method: "get",
        }).then(({ data }) => {
            if (data && data.code === 0) {
                let res = data.data;
                let xAxis = [];
                let yAxis = [];
                let pArray = []
                for(let i=0;i<res.length;i++){
                    xAxis.push(res[i].pingfenriqi);
                    yAxis.push(parseFloat((res[i].total)));
                    pArray.push({
                        value: parseFloat((res[i].total)),
                        name: res[i].pingfenriqi
                    })
                }
                var option = {};
                option = {
                    title: {
                        text: '每日评分人数统计',
                        left: 'center'
                    },
                    tooltip: {
                      trigger: 'item',
                      formatter: '{b} : {c}'
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: xAxis
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: yAxis,
                        type: 'line',
                        smooth: true
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                pingfenxinxiChart1.setOption(option);
                  //根据窗口的大小变动图表
                window.onresize = function() {
                    pingfenxinxiChart1.resize();
                };
            }
        });
      })
    },






  }
};
</script>
<style lang="scss" scoped>
    .content {
        background: #f8f8fc;
        min-height: calc(100vh - 120px);
    }
    
    .cardView {
        display: flex;
        flex-direction: column;
        width: 100%;

        .stats-cards {
            display: flex;
            align-items: center;
            width: 100%;
            margin-bottom: 30px;
            justify-content: center;
            gap: 20px;
            flex-wrap: wrap;
        }
        
        .charts-container {
            ::v-deep .el-card__body {
                padding: 20px;
            }
        }
    }
    
    ::v-deep .el-card {
        border-radius: 12px;
        border: 2px solid #dbd9f4;
        box-shadow: 0 4px 15px rgba(0,0,0,0.08);
    }
</style>
