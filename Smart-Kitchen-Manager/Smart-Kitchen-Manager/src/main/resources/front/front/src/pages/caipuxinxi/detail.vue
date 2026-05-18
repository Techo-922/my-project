<template>
<div>
	<div :style='{"padding":"16px 24px","margin":"10px auto","borderColor":"transparent","borderRadius":"12px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","borderWidth":"0","width":"1200px","borderStyle":"solid","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.08)"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'›'" :style='{"width":"100%","margin":"0 auto","fontSize":"15px","lineHeight":"1","display":"flex","alignItems":"center"}'>
			<el-breadcrumb-item>🏠 首页</el-breadcrumb-item>
			<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index">{{item.name}}</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="detail-preview" :style='{"width":"1200px","padding":"0 0 20px","margin":"20px auto 0"}'>
		<div class="attr" :style='{"minHeight":"780px","padding":"0 0 20px","margin":"20px  0 20px","overflow":"hidden","borderRadius":"16px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","display":"block","position":"relative","justifyContent":"space-between","height":"auto","boxShadow":"0 8px 24px rgba(102, 126, 234, 0.12)"}'>
			<el-carousel :style='{"width":"45%","margin":"20px 10px 0px 20px","position":"","float":"left","height":"450px","order":"1"}' trigger="click" indicator-position="inside" arrow="always" type="default" direction="horizontal" height="450px" :autoplay="false" :interval="3000" :loop="true">
				<el-carousel-item :style='{"border":"0","width":"100%","padding":"0","borderRadius":"16px","background":"transparent","height":"100%","overflow":"hidden"}' v-for="item in detailBanner" :key="item.id">
					<el-image :style='{"width":"100%","objectFit":"cover","borderRadius":"16px","height":"100%","boxShadow":"0 4px 12px rgba(0,0,0,0.08)"}' v-if="item.substr(0,4)=='http'" :src="item" fit="cover" class="image"></el-image>
					<el-image :style='{"width":"100%","objectFit":"cover","borderRadius":"16px","height":"100%","boxShadow":"0 4px 12px rgba(0,0,0,0.08)"}' v-else :src="baseUrl + item" fit="cover" class="image"></el-image>
				</el-carousel-item>
			</el-carousel>
	
			
			<div class="info" :style='{"border":"0","minHeight":"750px","padding":"24px","margin":"20px 20px 0 0","borderRadius":"16px","background":"linear-gradient(135deg, rgba(255,255,255,0.95) 0%, rgba(248,247,254,0.95) 100%)","width":"50%","float":"right","height":"auto","order":"2","boxShadow":"0 4px 16px rgba(102, 126, 234, 0.08)","backdropFilter":"blur(10px)"}'>
				<div class="item" :style='{"padding":"0","margin":"0 0 20px 0","alignItems":"center","background":"none","display":"flex","width":"100%","justifyContent":"space-between"}'>
					<div :style='{"padding":"0 20px","fontSize":"24px","color":"#333","fontWeight":"700","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","-webkitBackgroundClip":"text","-webkitTextFillColor":"transparent"}'>
                    {{detail.caipumingcheng}}
                    </div>
					<div @click="storeup(1)" v-show="!isStoreup" :style='{"cursor":"pointer","width":"auto","padding":"10px 20px","borderRadius":"24px","textAlign":"center","background":"linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%)","border":"2px solid rgba(102, 126, 234, 0.2)","transition":"all 0.3s ease"}'><i v-if="true" :style='{"color":"#667eea","fontSize":"18px"}' class="el-icon-star-off"></i><span :style='{"color":"#667eea","fontSize":"15px","marginLeft":"6px","fontWeight":"500"}'>点我收藏</span></div>
					<div @click="storeup(-1)" v-show="isStoreup" :style='{"cursor":"pointer","width":"auto","padding":"10px 20px","borderRadius":"24px","textAlign":"center","background":"linear-gradient(135deg, rgba(255, 193, 7, 0.15) 0%, rgba(255, 152, 0, 0.15) 100%)","border":"2px solid rgba(255, 193, 7, 0.3)","transition":"all 0.3s ease"}'><i v-if="true" :style='{"color":"#ffc107","fontSize":"18px"}' class="el-icon-star-on"></i><span :style='{"color":"#ff9800","fontSize":"15px","marginLeft":"6px","fontWeight":"500"}'>取消收藏</span></div>
				</div>

				<div class="item" :style='{"padding":"16px 20px","margin":"0 0 12px 0","borderColor":"transparent","background":"linear-gradient(90deg, rgba(255,255,255,0.6) 0%, rgba(248,247,254,0.6) 100%)","borderWidth":"0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"12px","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.06)"}'>
					<div class="lable" :style='{"padding":"0 10px","color":"#667eea","textAlign":"left","display":"inline-block","width":"auto","fontSize":"15px","lineHeight":"32px","height":"32px","fontWeight":"600"}'>🍽️ 菜式类型</div>
					<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"32px","color":"#666","fontWeight":"500"}'>{{detail.caishileixing}}</div>
				</div>
				<div class="item" :style='{"padding":"16px 20px","margin":"0 0 12px 0","borderColor":"transparent","background":"linear-gradient(90deg, rgba(255,255,255,0.6) 0%, rgba(248,247,254,0.6) 100%)","borderWidth":"0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"12px","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.06)"}'>
					<div class="lable" :style='{"padding":"0 10px","color":"#667eea","textAlign":"left","display":"inline-block","width":"auto","fontSize":"15px","lineHeight":"32px","height":"32px","fontWeight":"600"}'>👨‍🍳 烹饪方式</div>
					<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"32px","color":"#666","fontWeight":"500"}'>{{detail.pengrenfangshi}}</div>
				</div>
				<div class="item" :style='{"padding":"16px 20px","margin":"0 0 12px 0","borderColor":"transparent","background":"linear-gradient(90deg, rgba(255,255,255,0.6) 0%, rgba(248,247,254,0.6) 100%)","borderWidth":"0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"12px","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.06)"}'>
					<div class="lable" :style='{"padding":"0 10px","color":"#667eea","textAlign":"left","display":"inline-block","width":"auto","fontSize":"15px","lineHeight":"32px","height":"32px","fontWeight":"600"}'>⭐ 分数</div>
					<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"32px","color":"#ff9800","fontWeight":"600"}'>{{detail.fenshu}}</div>
				</div>
				<div class="item" :style='{"padding":"16px 20px","margin":"0 0 12px 0","borderColor":"transparent","background":"linear-gradient(90deg, rgba(255,255,255,0.6) 0%, rgba(248,247,254,0.6) 100%)","borderWidth":"0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"12px","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.06)"}'>
					<div class="lable" :style='{"padding":"0 10px","color":"#667eea","textAlign":"left","display":"inline-block","width":"auto","fontSize":"15px","lineHeight":"32px","height":"32px","fontWeight":"600"}'>🥘 材料</div>
					<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"32px","color":"#666","fontWeight":"500"}'>{{detail.cailiao}}</div>
				</div>
				<div class="item" :style='{"padding":"16px 20px","margin":"0 0 12px 0","borderColor":"transparent","background":"linear-gradient(90deg, rgba(255,255,255,0.6) 0%, rgba(248,247,254,0.6) 100%)","borderWidth":"0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"12px","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.06)"}'>
					<div class="lable" :style='{"padding":"0 10px","color":"#667eea","textAlign":"left","display":"inline-block","width":"auto","fontSize":"15px","lineHeight":"32px","height":"32px","fontWeight":"600"}'>📅 发布日期</div>
					<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"32px","color":"#666","fontWeight":"500"}'>{{detail.faburiqi}}</div>
				</div>
				<div class="item" :style='{"padding":"16px 20px","margin":"0 0 12px 0","borderColor":"transparent","background":"linear-gradient(90deg, rgba(255,255,255,0.6) 0%, rgba(248,247,254,0.6) 100%)","borderWidth":"0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"12px","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.06)"}'>
					<div class="lable" :style='{"padding":"0 10px","color":"#667eea","textAlign":"left","display":"inline-block","width":"auto","fontSize":"15px","lineHeight":"32px","height":"32px","fontWeight":"600"}'>👁️ 点击次数</div>
					<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"32px","color":"#666","fontWeight":"500"}'>{{detail.clicknum}}</div>
				</div>
				<div class="btn" :style='{"width":"auto","padding":"20px 0 10px","margin":"0","flexWrap":"wrap","display":"flex","gap":"12px"}'>
					<el-button :style='{"border":"0","cursor":"pointer","padding":"0 24px","margin":"0","outline":"none","color":"#fff","borderRadius":"12px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"auto","lineHeight":"44px","fontSize":"15px","height":"44px","fontWeight":"500","boxShadow":"0 4px 12px rgba(102, 126, 234, 0.3)","transition":"all 0.3s ease"}' v-if="isAuth('caipuxinxi','评分')" @click="onAcross('pingfenxinxi','','[1]','该菜系已评分')" type="warning">⭐ 评分</el-button>
					<el-button :style='{"border":"2px solid #e0e0e0","cursor":"pointer","padding":"0 24px","margin":"0","outline":"none","color":"#666","borderRadius":"12px","background":"#fff","width":"auto","lineHeight":"40px","fontSize":"15px","height":"44px","fontWeight":"500","transition":"all 0.3s ease"}' @click="back()">← 返回</el-button>
				</div>
			</div>
			
			<!-- 热门信息 -->
			<div class="hot" :style='{"border":"0","padding":"0 0 16px","margin":"0px 10px 0px 20px","top":"544px","borderRadius":"16px","flexWrap":"wrap","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","display":"flex","width":"45%","position":"absolute","height":"auto","boxShadow":"0 4px 16px rgba(102, 126, 234, 0.1)"}'>
			  <div :style='{"padding":"0","margin":"0 auto","color":"#fff","textAlign":"center","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"100%","lineHeight":"48px","fontSize":"18px","order":"0","borderRadius":"16px 16px 0 0","fontWeight":"600","letterSpacing":"1px"}'>🔥 热门推荐</div>
			  <div :style='{"padding":"16px","flexWrap":"wrap","background":"transparent","display":"flex","width":"100%","justifyContent":"space-between","height":"auto","gap":"12px"}'>
			    <div v-for="item in hotList" :key="item.id" :style='{"cursor":"pointer","width":"23%","padding":"0","background":"#fff","height":"auto","borderRadius":"12px","overflow":"hidden","boxShadow":"0 2px 8px rgba(0,0,0,0.06)","transition":"all 0.3s ease"}' @click="toDetail(item)" class="hot-item">
			      <img :style='{"width":"100%","objectFit":"cover","display":"block","height":"120px"}' v-if="item.caipufengmian && item.caipufengmian.substr(0,4)=='http'" :src="item.caipufengmian" alt="">
			      <img :style='{"width":"100%","objectFit":"cover","display":"block","height":"120px"}' v-else :src="baseUrl + (item.caipufengmian?item.caipufengmian.split(',')[0]:'')" alt="">
			      <div :style='{"padding":"8px 10px 4px","fontSize":"14px","lineHeight":"20px","color":"#333","background":"#fff","fontWeight":"500","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap"}'>{{item.caipumingcheng}}</div>
			      <div :style='{"padding":"4px 10px","fontSize":"13px","lineHeight":"20px","color":"#667eea","background":"#fff"}'>{{item.caishileixing}}</div>
			      <div :style='{"padding":"4px 10px 8px","fontSize":"13px","lineHeight":"20px","color":"#ff9800","background":"#fff","fontWeight":"600"}'>⭐ {{item.fenshu}}</div>
			    </div>
			  </div>
			</div>
		</div>
		
		
		<el-tabs class="detail" :style='{"border":"2px solid #dbd9f4","boxShadow":"none","padding":"10px","margin":"20px 0","borderRadius":"8px","background":"#fff"}' v-model="activeName" type="border-card">
			<el-tab-pane label="制作流程" name="first">
				<div v-html="detail.zhizuoliucheng"></div>
			</el-tab-pane>
			<el-tab-pane label="评论" name="second">
				<el-form class="add comment" :style='{"padding":"15px","margin":"0 0 20px","display":"flex"}' :model="form" :rules="rules" ref="form">
					<el-form-item class="item" :style='{"width":"60%","display":"flex","height":"auto"}' label="评论" prop="content">
						<el-input type="textarea" :rows="5" v-model="form.content" placeholder="请输入内容"></el-input>
					</el-form-item>
					<el-form-item class="btn" :style='{"width":"100%","padding":"0 0 0 50px","margin":"0px 0 0","height":"100px"}'>
						<el-button :style='{"border":"0","cursor":"pointer","padding":"0","margin":"40px 20px 40px 50px","outline":"none","color":"#666","borderRadius":"10px","background":"rgba(223, 218, 235, 1)","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px"}' type="primary" @click="submitForm('form')">立即提交</el-button>
						<el-button :style='{"border":"2px solid #dfdaeb","cursor":"pointer","padding":"0","margin":"0 20px 0 0","outline":"none","color":"#666","borderRadius":"10px","background":"none","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px"}' @click="resetForm('form')">重置</el-button>
					</el-form-item>
				</el-form>
				
				<div v-if="infoList.length" :style='{"padding":"15px","flexWrap":"wrap","display":"flex"}' class="comment">
					<div :style='{"padding":"0","margin":"0 20px 20px","borderColor":"#ddd","overflow":"hidden","alignItems":"center","borderWidth":"0 0 1px 0","background":"#fcfcfc","width":"45%","borderStyle":"solid","height":"auto"}' v-for="item in infoList" :key="item.id">
						<div class="user" :style='{"width":"100%","alignItems":"center","background":"#f5f5fc","display":"flex","height":"auto"}'>
							<el-image v-if="item.avatarurl" :style='{"width":"50px","margin":"0 10px 0 0","borderRadius":"0","objectFit":"cover","height":"50px"}' :size="50" :src="baseUrl + item.avatarurl"></el-image>
							<el-image v-if="!item.avatarurl" :style='{"width":"50px","margin":"0 10px 0 0","borderRadius":"0","objectFit":"cover","height":"50px"}' :size="50" :src="require('@/assets/touxiang.png')"></el-image>
							<div :style='{"color":"#ab85d3","fontSize":"16px"}' class="name">{{item.nickname}}</div>
						</div>
						<div :style='{"padding":"8px","margin":"10px 0px 0px","color":"#333","borderRadius":"4px","lineHeight":"30px","fontSize":"14px","textIndent":"2em"}' class="content-block-ask">
							{{item.content}}
						</div>
						<div :style='{"padding":"8px","margin":"10px 0px 0px","color":"#333","borderRadius":"4px","lineHeight":"30px","fontSize":"14px","textIndent":"2em"}' class="content-block-reply" v-if="item.reply">
							回复：{{item.reply}}
						</div>
					</div>
				</div>
				
				<el-pagination
				  background
				  class="pagination"
				  :pager-count="7"
				  :page-size="pageSize"
				  :page-sizes="pageSizes"
				  prev-text="<"
				  next-text=">"
				  :hide-on-single-page="true"
				  :layout='["total","prev","pager","next","sizes","jumper"].join()'
				  :total="total"
				  :style='{"width":"1200px","padding":"0","margin":"20px auto","whiteSpace":"nowrap","color":"#333","fontWeight":"500"}'
				  @current-change="curChange"
				  @prev-click="prevClick"
				  @next-click="nextClick"
				></el-pagination>
			</el-tab-pane>
			
		</el-tabs>
	</div>
</div>
</template>

<script>
  import CountDown from '@/components/CountDown';
  export default {
    //数据集合
    data() {
      return {
        tablename: 'caipuxinxi',
        baseUrl: '',
        breadcrumbItem: [
          {
            name: '详情信息'
          }
        ],
        title: '',
        detailBanner: [],
        endTime: 0,
        hotList: [],
        detail: {},
        activeName: 'first',
        form: {
          content: '',
          userid: localStorage.getItem('userid'),
          nickname: localStorage.getItem('username'),
          avatarurl: '',
        },
        infoList: [],
        total: 1,
        pageSize: 5,
		pageSizes: [10,20,30,50],
        totalPage: 1,
        rules: {
					content: [
						{ required: true, message: '请输入内容', trigger: 'blur' }
					]
        },
        storeupParams: {
          name: '',
          picture: '',
          refid: 0,
          tablename: 'caipuxinxi',
          userid: localStorage.getItem('userid')
        },
        isStoreup: false,
        storeupInfo: {},
        buynumber: 1,
      }
    },
    created() {
        this.init();
    },
    //方法集合
    methods: {
        init() {
          this.baseUrl = this.$config.baseUrl;
          if(this.$route.query.detailObj) {
            this.detail = JSON.parse(this.$route.query.detailObj);
          }
          if(this.$route.query.storeupObj) {
            this.detail.id = JSON.parse(this.$route.query.storeupObj).refid;
          }
          this.$http.get(this.tablename + '/detail/'  + this.detail.id, {}).then(res => {
            if (res.data.code == 0) {
              this.detail = res.data.data;
              this.title = this.detail.caipumingcheng;
              this.detailBanner = this.detail.caipufengmian ? this.detail.caipufengmian.split(",") : [];
              this.$forceUpdate();
            }
          });

          this.getDiscussList(1);
          this.getStoreupStatus();
          this.getHotList();

        },
        toDetail(item) {
          this.$router.push({path: '/index/caipuxinxiDetail', query: {detailObj: JSON.stringify(item)}});
          this.init();
          document.body.scrollTop = 0
          document.documentElement.scrollTop = 0
        },
        getHotList() {
          // 如果用户已登录，使用智能推荐的热门模式
          if(localStorage.getItem('Token') && localStorage.getItem('userid')) {
            this.$http.get('caipuxinxi/recommend', {
              params: {
                userId: localStorage.getItem('userid'),
                pageNum: 1,
                pageSize: 4,
                recommendType: 'hot',
                sortType: 'popularity'
              }
            }).then(res => {
              if (res.data.code == 0) {
                this.hotList = res.data.data.list;
              }
            }).catch(err => {
              // 如果推荐接口失败，降级使用旧接口
              console.warn('热门推荐接口失败，使用降级方案', err);
              this.getHotListFallback();
            });
          } else {
            // 未登录用户使用旧接口
            this.getHotListFallback();
          }
        },
        
        // 降级方法：使用旧的热门接口
        getHotListFallback() {
          let autoSortUrl = "caipuxinxi/autoSort";
          if(localStorage.getItem('Token')) {
            autoSortUrl = "caipuxinxi/autoSort2";
          }
          this.$http.get(autoSortUrl, {
            params: {
              page: 1,
              limit: 4,
            }
          }).then(res => {
            if (res.data.code == 0) {
              this.hotList = res.data.data.list;
            }
          });
        },
      onAcross(acrossTable,crossOptAudit,statusColumnName,tips,statusColumnValue){
        localStorage.setItem('crossTable',`caipuxinxi`);
        localStorage.setItem('crossObj', JSON.stringify(this.detail));
        localStorage.setItem('statusColumnName',statusColumnName);
        localStorage.setItem('statusColumnValue',statusColumnValue);
        localStorage.setItem('tips',tips);
        if(statusColumnName!=''&&!statusColumnName.startsWith("[")) {
            var obj = JSON.parse(localStorage.getItem('crossObj'));
            for (var o in obj){
                if(o==statusColumnName && obj[o]==statusColumnValue){
                    this.$message({
                        type: 'success',
                        message: tips,
                        duration: 1500
                    });
                    return
                }
            }
        }
        this.$router.push({path: '/index/' + acrossTable + 'Add', query: {type: 'cross'}});
      },
      storeup(type) {
        if (type == 1 && !this.isStoreup) {
          this.storeupParams.name = this.title;
          this.storeupParams.picture = this.detailBanner[0];
          this.storeupParams.inteltype = this.detail.caishileixing;
          this.storeupParams.refid = this.detail.id;
          this.storeupParams.type = type;
          this.$http.post('storeup/add', this.storeupParams).then(res => {
            if (res.data.code == 0) {
              this.isStoreup = true;
              this.$message({
                type: 'success',
                message: '收藏成功!',
                duration: 1500,
              });
            }
          });
        }
        if (type == -1 && this.isStoreup) {
          this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'caipuxinxi', userid: localStorage.getItem('userid')}}).then(res => {
            if (res.data.code == 0 && res.data.data.list.length > 0) {
              this.isStoreup = true;
              this.storeupInfo = res.data.data.list[0];
              let delIds = new Array();
              delIds.push(this.storeupInfo.id);
              this.$http.post('storeup/delete', delIds).then(res => {
                if (res.data.code == 0) {
                  this.isStoreup = false;
                  this.$message({
                    type: 'success',
                    message: '取消成功!',
                    duration: 1500,
                  });
                }
              });
            }
          });
        }
      },
      getStoreupStatus(){
        if(localStorage.getItem("Token")) {
            this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'caipuxinxi', userid: localStorage.getItem('userid')}}).then(res => {
              if (res.data.code == 0 && res.data.data.list.length > 0) {
                this.isStoreup = true;
                this.storeupInfo = res.data.data.list[0];
              }
            });
        }
      },
      curChange(page) {
        this.getDiscussList(page);
      },
      prevClick(page) {
        this.getDiscussList(page);
      },
      nextClick(page) {
        this.getDiscussList(page);
      },
      // 下载
      download(file){
        if(!file) {
						this.$message({
							type: 'error',
							message: '文件不存在',
							duration: 1500,
						});
            return;
        }
        window.open(this.baseUrl+file)
      },
      getDiscussList(page) {
        this.$http.get('discusscaipuxinxi/list', {params: {page, limit: this.pageSize, refid: this.detail.id}}).then(res => {
          if (res.data.code == 0) {
            this.infoList = res.data.data.list;
            this.total = res.data.data.total;
            this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
            this.totalPage = res.data.data.totalPage;
          }
        });
      },
      submitForm(formName) {
        let sensitiveWords = "";
        let sensitiveWordsArr = [];
        if(sensitiveWords) {
            sensitiveWordsArr = sensitiveWords.split(",");
        }
        for(var i=0; i<sensitiveWordsArr.length; i++){
            //全局替换
            var reg = new RegExp(sensitiveWordsArr[i],"g");
            //判断内容中是否包括敏感词
            if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
				// 将敏感词替换**
                this.form.content = this.form.content.replace(reg,"**");
            }
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.form.refid = this.detail.id;
            this.form.avatarurl = localStorage.getItem('headportrait')?localStorage.getItem('headportrait'):'';
            this.$http.post('discusscaipuxinxi/add', this.form).then(res => {
              if (res.data.code == 0) {
                this.form.content = '';
                this.getDiscussList(1);
                this.$message({
                  type: 'success',
                  message: '评论成功!',
                  duration: 1500,
                });
              }
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      // 返回
      back() {
        this.$router.go(-1);
      }


    },
    components: {
      CountDown
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.detail-preview {
	
	  .attr {
	    .el-carousel ::v-deep .el-carousel__indicator button {
	      width: 0;
	      height: 0;
	      display: none;
	    }
	
	    .el-input-number__decrease:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled), .el-input-number__increase:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled) {
	      border-color: none;
	    }
	  }
	
	  .detail {
	    & ::v-deep .el-tabs__header .el-tabs__nav-wrap {
	      margin-bottom: 0;
	    }
	
	    & .add .el-textarea {
	      width: auto;
	    }
	  }
	}
	
	// 热门项目悬停效果
	.hot-item:hover {
		transform: translateY(-4px);
		box-shadow: 0 8px 16px rgba(102, 126, 234, 0.15) !important;
	}
	
	// 收藏按钮悬停效果
	.info .item div[style*="cursor:pointer"]:hover {
		transform: scale(1.05);
		box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2) !important;
	}
	
	// 按钮悬停效果
	.btn .el-button:hover {
		transform: translateY(-2px);
		box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4) !important;
	}
	
	.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left {
		width: 44px;
		font-size: 14px;
		height: 44px;
		background: rgba(255, 255, 255, 0.9);
		border-radius: 50%;
		box-shadow: 0 4px 12px rgba(0,0,0,0.1);
	}
	
	.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left:hover {
		background: rgba(102, 126, 234, 0.9);
		color: #fff;
	}
	
	.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right {
		width: 44px;
		font-size: 14px;
		height: 44px;
		background: rgba(255, 255, 255, 0.9);
		border-radius: 50%;
		box-shadow: 0 4px 12px rgba(0,0,0,0.1);
	}
	
	.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right:hover {
		background: rgba(102, 126, 234, 0.9);
		color: #fff;
	}

	.attr .el-carousel ::v-deep .el-carousel__indicators {
		padding: 0;
		margin: 0 0 16px;
		z-index: 2;
		position: absolute;
		list-style: none;
	}
	
	.attr .el-carousel ::v-deep .el-carousel__indicators li {
		border-radius: 50%;
		padding: 0;
		margin: 0 4px;
		background: #fff;
		display: inline-block;
		width: 12px;
		opacity: 0.4;
		transition: 0.3s;
		height: 12px;
	}
	
	.attr .el-carousel ::v-deep .el-carousel__indicators li:hover {
		border-radius: 50%;
		padding: 0;
		margin: 0 4px;
		background: #fff;
		display: inline-block;
		width: 12px;
		opacity: 0.7;
		height: 12px;
	}
	
	.attr .el-carousel ::v-deep .el-carousel__indicators li.is-active {
		border-radius: 50%;
		padding: 0;
		margin: 0 4px;
		background: #fff;
		display: inline-block;
		width: 12px;
		opacity: 1;
		height: 12px;
	}
	
	.attr .el-input-number ::v-deep .el-input-number__decrease {
		cursor: pointer;
		z-index: 1;
		display: flex;
		border-color: #DCDFE6;
		border-radius: 4px 0 0 4px;
		top: 1px;
		left: 1px;
		background: #f5f5f5;
		width: 40px;
		justify-content: center;
		border-width: 0 1px 0 0;
		align-items: center;
		position: absolute;
		border-style: solid;
		text-align: center;
		height: 38px;
	}
	
	.attr .el-input-number ::v-deep .el-input-number__decrease i {
		color: #666;
		font-size: 14px;
	}

	.attr .el-input-number ::v-deep .el-input-number__increase {
		cursor: pointer;
		z-index: 1;
		display: flex;
		border-color: #DCDFE6;
		right: 1px;
		border-radius: 0 4px 4px 0;
		top: 1px;
		background: #f5f5f5;
		width: 40px;
		justify-content: center;
		border-width: 0 0 0 1px;
		align-items: center;
		position: absolute;
		border-style: solid;
		text-align: center;
		height: 38px;
	}
	
	.attr .el-input-number ::v-deep .el-input-number__increase i {
		color: #666;
		font-size: 14px;
	}
	
	.attr .el-input-number ::v-deep .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		border-radius: 4px;
		padding: 0 40px;
		outline: none;
		color: #666;
		background: #FFF;
		display: inline-block;
		width: 100%;
		font-size: 14px;
		line-height: 40px;
		text-align: center;
		height: 40px;
	}
	
	.detail-preview .detail.el-tabs ::v-deep .el-tabs__header {
		border: 0;
		margin: 0;
		background: none;
	}
	
	.detail-preview .detail.el-tabs ::v-deep .el-tabs__header .el-tabs__item {
		border: 2px solid #dbd9f4;
		padding: 0 20px;
		margin: 0 4px 0 0;
		color: #999;
		background: none;
		font-weight: 500;
		display: inline-block;
		font-size: 14px;
		line-height: 40px;
		position: relative;
		list-style: none;
		height: 40px;
	}
	
	.detail-preview .detail.el-tabs ::v-deep .el-tabs__header .el-tabs__item:hover {
		border: 2px solid #dbd9f4;
		margin: 0 4px 0 0;
		color: #ab85d3;
		background: #FFF;
	}
	
	.detail-preview .detail.el-tabs ::v-deep .el-tabs__header .el-tabs__item.is-active {
		border: 2px solid #dbd9f4;
		margin: 0 4px 0 0;
		color: #ab85d3;
		background: radial-gradient(circle, rgba(248,247,254,1) 0%, rgba(204,202,240,1) 100%);
	}
	
	.detail-preview .detail.el-tabs ::v-deep .el-tabs__content {
		padding: 15px;
	}
	
	.detail-preview .detail.el-tabs .add ::v-deep .el-form-item__label {
		padding: 0 10px 0 0;
		color: #666;
		width: 20%;
		font-size: 14px;
		line-height: 40px;
		text-align: right;
	}
	
	.detail-preview .detail.el-tabs .add ::v-deep .el-textarea__inner {
		border: 1px solid #dfdaeb;
		border-radius: 4px;
		padding: 0 12px;
		outline: none;
		color: #333;
		width: 450px;
		font-size: 14px;
		line-height: 32px;
		height: 120px;
	}
	
	.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__separator {
		margin: 0 12px;
		color: #667eea;
		font-weight: 600;
		font-size: 16px;
	}
	
	.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner a {
		color: #667eea;
		display: inline-block;
		font-weight: 500;
		transition: all 0.3s ease;
	}
	
	.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner a:hover {
		color: #764ba2;
	}

	.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner {
		color: #666;
		display: inline-block;
		font-weight: 500;
	}
	
	.el-pagination ::v-deep .el-pagination__total {
		margin: 0 10px 0 0;
		color: #666;
		font-weight: 400;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .btn-prev {
		border: none;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px 0 40%;
		color: #999;
		background: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .btn-next {
		border: none;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #999;
		background: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .btn-prev:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #C0C4CC;
		background: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .btn-next:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #C0C4CC;
		background: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pager {
		padding: 0;
		margin: 0;
		display: inline-block;
		vertical-align: top;
	}
	
	.el-pagination ::v-deep .el-pager .number {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #999;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		border-radius: 2px;
		background: #fff;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pager .number:hover {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #666;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		border-radius: 2px;
		background: rgba(171, 133, 211,.2);
		text-align: center;
		min-width: 30px;
		height: 28px;
}

.el-pagination ::v-deep .el-pager .number.active {
		cursor: default;
		padding: 0 4px;
		margin: 0 5px;
		color: #FFF;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		border-radius: 2px;
		background: rgba(171, 133, 211,.5);
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pagination__sizes {
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pagination__sizes .el-input {
		margin: 0 5px;
		width: 100px;
		position: relative;
	}
	
	.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 25px 0 8px;
		color: #606266;
		display: inline-block;
		font-size: 13px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pagination__sizes .el-input span.el-input__suffix {
		top: 0;
		position: absolute;
		right: 0;
		height: 100%;
	}
	
	.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
		cursor: pointer;
		color: #C0C4CC;
		width: 25px;
		font-size: 14px;
		line-height: 28px;
		text-align: center;
	}

	.el-pagination ::v-deep .el-pagination__jump {
		margin: 0 0 0 24px;
		color: #606266;
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pagination__jump .el-input {
		border-radius: 3px;
		padding: 0 2px;
		margin: 0 2px;
		display: inline-block;
		width: 50px;
		font-size: 14px;
		line-height: 18px;
		position: relative;
		text-align: center;
		height: 28px;
	}
	
	.el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 3px;
		color: #606266;
		display: inline-block;
		font-size: 14px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
</style>

