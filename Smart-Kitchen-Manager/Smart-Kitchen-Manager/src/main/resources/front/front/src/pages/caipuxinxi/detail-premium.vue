<template>
<div class="detail-premium">
	<!-- 面包屑导航 -->
	<div class="breadcrumb-container" :style='{"maxWidth":"1400px","margin":"0 auto 24px","padding":"0 40px"}'>
		<el-breadcrumb separator="/" :style='{"fontSize":"13px","color":"#999"}'>
			<el-breadcrumb-item :to="{ path: '/index/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item :to="{ path: '/index/caipuxinxi' }">菜谱信息</el-breadcrumb-item>
			<el-breadcrumb-item>详情</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="main-container" :style='{"maxWidth":"1400px","margin":"0 auto","padding":"0 40px"}'>
		
		<!-- 主要内容区域 -->
		<div class="content-grid" :style='{"display":"grid","gridTemplateColumns":"1fr 1fr","gap":"40px","marginBottom":"40px"}'>
			
			<!-- 左侧：图片轮播 -->
			<div class="image-section" :style='{"background":"#ffffff","border":"1px solid #e8e8e8","overflow":"hidden"}'>
				<el-carousel :style='{"width":"100%","height":"600px"}' trigger="click" indicator-position="inside" arrow="always" type="default" direction="horizontal" height="600px" autoplay="false" interval="3000" loop="true">
					<el-carousel-item :style='{"width":"100%","height":"100%"}' v-for="item in detailBanner" :key="item">
						<el-image :style='{"width":"100%","height":"100%","objectFit":"cover"}' v-if="item.substr(0,4)=='http'" :src="item" fit="cover"></el-image>
						<el-image :style='{"width":"100%","height":"100%","objectFit":"cover"}' v-else :src="baseUrl + item" fit="cover"></el-image>
					</el-carousel-item>
				</el-carousel>
			</div>
			
			<!-- 右侧：详细信息 -->
			<div class="info-section" :style='{"background":"#ffffff","border":"1px solid #e8e8e8","padding":"40px","display":"flex","flexDirection":"column"}'>
				
				<!-- 标题和收藏 -->
				<div class="header-row" :style='{"display":"flex","alignItems":"center","justifyContent":"space-between","marginBottom":"32px","paddingBottom":"24px","borderBottom":"1px solid #f5f5f5"}'>
					<h1 :style='{"fontSize":"32px","fontWeight":"400","color":"#1a1a1a","letterSpacing":"0.3px","flex":"1"}'>{{detail.caipumingcheng}}</h1>
					<div @click="storeup(1)" v-show="!isStoreup" class="action-btn" :style='{"cursor":"pointer","padding":"10px 20px","border":"1px solid #e8e8e8","background":"#ffffff","display":"flex","alignItems":"center","gap":"8px","transition":"all 0.2s ease"}'>
						<i class="el-icon-star-off" :style='{"fontSize":"18px","color":"#666"}'></i>
						<span :style='{"fontSize":"14px","color":"#666"}'>收藏</span>
					</div>
					<div @click="storeup(-1)" v-show="isStoreup" class="action-btn active" :style='{"cursor":"pointer","padding":"10px 20px","border":"1px solid #1a1a1a","background":"#1a1a1a","display":"flex","alignItems":"center","gap":"8px","transition":"all 0.2s ease"}'>
						<i class="el-icon-star-on" :style='{"fontSize":"18px","color":"#ffffff"}'></i>
						<span :style='{"fontSize":"14px","color":"#ffffff"}'>已收藏</span>
					</div>
				</div>

				<!-- 详细信息列表 -->
				<div class="info-list" :style='{"flex":"1"}'>
					<div class="info-item" :style='{"display":"flex","alignItems":"center","padding":"16px 0","borderBottom":"1px solid #f5f5f5"}'>
						<div class="label" :style='{"width":"120px","fontSize":"14px","color":"#999","fontWeight":"400"}'>菜式类型</div>
						<div class="value" :style='{"flex":"1","fontSize":"15px","color":"#1a1a1a","fontWeight":"400"}'>{{detail.caishileixing}}</div>
					</div>
					<div class="info-item" :style='{"display":"flex","alignItems":"center","padding":"16px 0","borderBottom":"1px solid #f5f5f5"}'>
						<div class="label" :style='{"width":"120px","fontSize":"14px","color":"#999","fontWeight":"400"}'>烹饪方式</div>
						<div class="value" :style='{"flex":"1","fontSize":"15px","color":"#1a1a1a","fontWeight":"400"}'>{{detail.pengrenfangshi}}</div>
					</div>
					<div class="info-item" :style='{"display":"flex","alignItems":"center","padding":"16px 0","borderBottom":"1px solid #f5f5f5"}'>
						<div class="label" :style='{"width":"120px","fontSize":"14px","color":"#999","fontWeight":"400"}'>评分</div>
						<div class="value" :style='{"flex":"1","fontSize":"15px","color":"#1a1a1a","fontWeight":"500","display":"flex","alignItems":"center","gap":"8px"}'>
							<i class="el-icon-star-on" :style='{"fontSize":"18px","color":"#1a1a1a"}'></i>
							<span>{{detail.fenshu}}</span>
						</div>
					</div>
					<div class="info-item" :style='{"display":"flex","alignItems":"flex-start","padding":"16px 0","borderBottom":"1px solid #f5f5f5"}'>
						<div class="label" :style='{"width":"120px","fontSize":"14px","color":"#999","fontWeight":"400","paddingTop":"2px"}'>材料</div>
						<div class="value" :style='{"flex":"1","fontSize":"15px","color":"#1a1a1a","fontWeight":"400","lineHeight":"1.6"}'>{{detail.cailiao}}</div>
					</div>
					<div class="info-item" :style='{"display":"flex","alignItems":"center","padding":"16px 0","borderBottom":"1px solid #f5f5f5"}'>
						<div class="label" :style='{"width":"120px","fontSize":"14px","color":"#999","fontWeight":"400"}'>发布日期</div>
						<div class="value" :style='{"flex":"1","fontSize":"15px","color":"#1a1a1a","fontWeight":"400"}'>{{detail.faburiqi}}</div>
					</div>
					<div class="info-item" :style='{"display":"flex","alignItems":"center","padding":"16px 0"}'>
						<div class="label" :style='{"width":"120px","fontSize":"14px","color":"#999","fontWeight":"400"}'>浏览次数</div>
						<div class="value" :style='{"flex":"1","fontSize":"15px","color":"#1a1a1a","fontWeight":"400","display":"flex","alignItems":"center","gap":"6px"}'>
							<i class="el-icon-view" :style='{"fontSize":"16px","color":"#999"}'></i>
							<span>{{detail.clicknum}}</span>
						</div>
					</div>
				</div>

				<!-- 操作按钮 -->
				<div class="action-buttons" :style='{"display":"flex","gap":"12px","marginTop":"32px","paddingTop":"24px","borderTop":"1px solid #f5f5f5"}'>
					<el-button v-if="isAuth('caipuxinxi','评分')" @click="onAcross('pingfenxinxi','','[1]','该菜系已评分')" :style='{"flex":"1","background":"#1a1a1a","border":"none","color":"#ffffff","padding":"14px 0","fontSize":"15px","fontWeight":"400"}'>评分</el-button>
					<el-button @click="back()" :style='{"flex":"1","background":"transparent","border":"1px solid #1a1a1a","color":"#1a1a1a","padding":"14px 0","fontSize":"15px","fontWeight":"400"}'>返回</el-button>
				</div>
			</div>
		</div>

		<!-- 热门推荐 -->
		<div class="hot-section" :style='{"background":"#ffffff","border":"1px solid #e8e8e8","padding":"40px","marginBottom":"40px"}'>
			<h2 :style='{"fontSize":"20px","fontWeight":"400","color":"#1a1a1a","marginBottom":"32px","letterSpacing":"0.2px"}'>热门推荐</h2>
			<div :style='{"display":"grid","gridTemplateColumns":"repeat(4, 1fr)","gap":"24px"}'>
				<div v-for="(item, index) in hotList" :key="item.id || index" @click="toDetail(item)" class="hot-card" :style='{"cursor":"pointer","background":"#fafafa","overflow":"hidden","transition":"all 0.3s ease"}'>
					<img v-if="item.caipufengmian && item.caipufengmian.substr(0,4)=='http'" :src="item.caipufengmian" :style='{"width":"100%","height":"200px","objectFit":"cover","display":"block"}' />
					<img v-else-if="item.caipufengmian" :src="baseUrl + item.caipufengmian.split(',')[0]" :style='{"width":"100%","height":"200px","objectFit":"cover","display":"block"}' />
					<img v-else src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='100' height='100'%3E%3Crect fill='%23f5f5f5' width='100' height='100'/%3E%3Ctext fill='%23ccc' x='50%25' y='50%25' text-anchor='middle' dy='.3em' font-family='Arial' font-size='14'%3E暂无图片%3C/text%3E%3C/svg%3E" :style='{"width":"100%","height":"200px","objectFit":"cover","display":"block"}' />
					<div :style='{"padding":"20px"}'>
						<div :style='{"fontSize":"15px","fontWeight":"500","color":"#1a1a1a","marginBottom":"8px","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap"}'>{{item.caipumingcheng}}</div>
						<div :style='{"fontSize":"13px","color":"#999","display":"flex","alignItems":"center","justifyContent":"space-between"}'>
							<span>{{item.caishileixing}}</span>
							<span :style='{"display":"flex","alignItems":"center","gap":"4px"}'>
								<i class="el-icon-star-on" :style='{"fontSize":"14px"}'></i>
								<span>{{item.fenshu}}</span>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 详细内容标签页 -->
		<el-tabs class="detail-tabs" :style='{"background":"#ffffff","border":"1px solid #e8e8e8","padding":"40px","marginBottom":"40px"}' v-model="activeName" type="border-card">
			<el-tab-pane label="制作流程" name="first">
				<div class="content-text" :style='{"fontSize":"15px","color":"#333","lineHeight":"1.8","padding":"20px 0"}' v-html="detail.zhizuoliucheng"></div>
			</el-tab-pane>
			<el-tab-pane label="评论" name="second">
				<el-form class="comment-form" :style='{"padding":"20px 0","marginBottom":"32px"}' :model="form" :rules="rules" ref="form">
					<el-form-item label="发表评论" prop="content" :style='{"marginBottom":"20px"}'>
						<el-textarea v-model="form.content" :rows="5" placeholder="请输入您的评论..." :style='{"width":"100%"}'></el-textarea>
					</el-form-item>
					<el-form-item :style='{"marginBottom":"0"}'>
						<el-button type="primary" @click="submitForm('form')" :style='{"background":"#1a1a1a","border":"none","color":"#ffffff","padding":"12px 32px","fontSize":"14px"}'>提交评论</el-button>
						<el-button @click="resetForm('form')" :style='{"background":"transparent","border":"1px solid #e8e8e8","color":"#666","padding":"12px 32px","fontSize":"14px","marginLeft":"12px"}'>重置</el-button>
					</el-form-item>
				</el-form>
				
				<div v-if="infoList.length" class="comment-list" :style='{"display":"grid","gridTemplateColumns":"1fr","gap":"20px"}'>
					<div v-for="item in infoList" :key="item.id" class="comment-item" :style='{"padding":"24px","background":"#fafafa","border":"1px solid #f5f5f5"}'>
						<div class="comment-header" :style='{"display":"flex","alignItems":"center","marginBottom":"16px"}'>
							<el-image v-if="item.avatarurl" :style='{"width":"40px","height":"40px","borderRadius":"50%","marginRight":"12px","objectFit":"cover"}' :src="baseUrl + item.avatarurl"></el-image>
							<el-image v-else :style='{"width":"40px","height":"40px","borderRadius":"50%","marginRight":"12px","objectFit":"cover"}' :src="require('@/assets/touxiang.png')"></el-image>
							<div :style='{"fontSize":"15px","fontWeight":"500","color":"#1a1a1a"}'>{{item.nickname}}</div>
						</div>
						<div class="comment-content" :style='{"fontSize":"14px","color":"#333","lineHeight":"1.6","marginBottom":"12px"}'>{{item.content}}</div>
						<div v-if="item.reply" class="comment-reply" :style='{"padding":"16px","background":"#ffffff","border":"1px solid #e8e8e8","fontSize":"14px","color":"#666","lineHeight":"1.6"}'>
							<span :style='{"fontWeight":"500","color":"#1a1a1a"}'>回复：</span>{{item.reply}}
						</div>
					</div>
				</div>
				
				<el-pagination background class="pagination" :pager-count="7" :page-size="pageSize" :page-sizes="pageSizes" prev-text="上一页" next-text="下一页" :hide-on-single-page="true" layout="total, prev, pager, next, sizes, jumper" :total="total" :style='{"textAlign":"center","marginTop":"32px"}' @current-change="curChange" @size-change="sizeChange" @prev-click="prevClick" @next-click="nextClick"></el-pagination>
			</el-tab-pane>
		</el-tabs>
	</div>
</div>
</template>

<script>
export default {
	data() {
		return {
			tablename: 'caipuxinxi',
			baseUrl: '',
			title: '',
			detailBanner: [],
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
			pageSizes: [5, 10, 20, 30],
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
		}
	},
	created() {
		this.init();
	},
	methods: {
		init() {
			this.baseUrl = this.$config.baseUrl;
			if(this.$route.query.detailObj) {
				this.detail = JSON.parse(this.$route.query.detailObj);
			}
			if(this.$route.query.storeupObj) {
				this.detail.id = JSON.parse(this.$route.query.storeupObj).refid;
			}
			this.$http.get(this.tablename + '/detail/' + this.detail.id, {}).then(res => {
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
			let autoSortUrl = "caipuxinxi/autoSort";
			if(localStorage.getItem('Token')) {
				autoSortUrl = "caipuxinxi/autoSort2";
			}
			this.$http.get(autoSortUrl, {params: {page: 1, limit: 4}}).then(res => {
				if (res.data.code == 0) {
					this.hotList = res.data.data.list;
				}
			})
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
		sizeChange(size){
			this.pageSize = size
			this.getDiscussList(1);
		},
		getDiscussList(page) {
			this.$http.get('discusscaipuxinxi/list', {params: {page, limit: this.pageSize, refid: this.detail.id}}).then(res => {
				if (res.data.code == 0) {
					this.infoList = res.data.data.list;
					this.total = res.data.data.total;
					this.pageSize = res.data.data.pageSize;
					this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
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
				var reg = new RegExp(sensitiveWordsArr[i],"g");
				if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
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
		back() {
			this.$router.go(-1);
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-premium {
	min-height: 100vh;
	background: #fafafa;
	padding: 40px 0 80px;
	
	// 面包屑样式
	.breadcrumb-container ::v-deep .el-breadcrumb__item {
		.el-breadcrumb__inner {
			color: #999;
			font-weight: 300;
			
			&:hover {
				color: #1a1a1a;
			}
		}
		
		&:last-child .el-breadcrumb__inner {
			color: #1a1a1a;
		}
	}
	
	// 轮播图样式
	.image-section ::v-deep .el-carousel__arrow {
		width: 40px;
		height: 40px;
		background: rgba(255, 255, 255, 0.95);
		border: 1px solid #e8e8e8;
		color: #1a1a1a;
		font-size: 14px;
		
		&:hover {
			background: #1a1a1a;
			border-color: #1a1a1a;
			color: #ffffff;
		}
	}
	
	.image-section ::v-deep .el-carousel__indicators {
		.el-carousel__indicator {
			.el-carousel__button {
				width: 8px;
				height: 8px;
				border-radius: 50%;
				background: rgba(255, 255, 255, 0.5);
			}
			
			&.is-active .el-carousel__button {
				width: 24px;
				border-radius: 4px;
				background: #ffffff;
			}
		}
	}
	
	// 操作按钮悬停效果
	.action-btn:hover {
		border-color: #1a1a1a;
		background: #1a1a1a;
		
		i, span {
			color: #ffffff !important;
		}
	}
	
	.action-btn.active:hover {
		border-color: #666;
		background: #666;
	}
	
	// 热门卡片悬停效果
	.hot-card:hover {
		transform: translateY(-4px);
		box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
	}
	
	// 标签页样式
	.detail-tabs ::v-deep .el-tabs__header {
		border: 0;
		margin: 0 0 32px;
		background: none;
		
		.el-tabs__item {
			border: 1px solid #e8e8e8;
			padding: 0 24px;
			margin: 0 8px 0 0;
			color: #666;
			background: #fafafa;
			font-weight: 400;
			font-size: 15px;
			line-height: 40px;
			height: 40px;
			transition: all 0.2s ease;
			
			&:hover {
				color: #1a1a1a;
				border-color: #1a1a1a;
			}
			
			&.is-active {
				color: #ffffff;
				background: #1a1a1a;
				border-color: #1a1a1a;
			}
		}
	}
	
	.detail-tabs ::v-deep .el-tabs__content {
		padding: 0;
	}
	
	// 表单样式
	.comment-form ::v-deep .el-form-item__label {
		color: #666;
		font-weight: 400;
		font-size: 14px;
	}
	
	.comment-form ::v-deep .el-textarea__inner {
		border: 1px solid #e8e8e8;
		color: #333;
		font-size: 14px;
		line-height: 1.6;
		padding: 12px;
		
		&:focus {
			border-color: #1a1a1a;
		}
	}
	
	// 分页样式
	.pagination {
		::v-deep .el-pagination__total {
			color: #666;
			font-weight: 300;
		}
		
		::v-deep .btn-prev,
		::v-deep .btn-next {
			background: #ffffff;
			border: 1px solid #e8e8e8;
			color: #666;
			
			&:hover {
				color: #1a1a1a;
				border-color: #1a1a1a;
			}
			
			&:disabled {
				color: #ccc;
				border-color: #e8e8e8;
			}
		}
		
		::v-deep .el-pager .number {
			background: #ffffff;
			border: 1px solid #e8e8e8;
			color: #666;
			margin: 0 4px;
			
			&:hover {
				color: #1a1a1a;
				border-color: #1a1a1a;
			}
			
			&.active {
				background: #1a1a1a;
				border-color: #1a1a1a;
				color: #ffffff;
			}
		}
	}
	
	// 响应式设计
	@media (max-width: 1200px) {
		.main-container {
			padding: 0 32px !important;
		}
		
		.content-grid {
			grid-template-columns: 1fr !important;
		}
		
		.hot-section > div:last-child {
			grid-template-columns: repeat(3, 1fr) !important;
		}
	}
	
	@media (max-width: 768px) {
		padding: 20px 0 40px !important;
		
		.main-container {
			padding: 0 20px !important;
		}
		
		.breadcrumb-container {
			padding: 0 20px !important;
		}
		
		.content-grid {
			gap: 24px !important;
		}
		
		.image-section ::v-deep .el-carousel {
			height: 400px !important;
		}
		
		.info-section {
			padding: 24px !important;
		}
		
		.header-row {
			flex-direction: column;
			align-items: flex-start !important;
			gap: 16px;
			
			h1 {
				font-size: 24px !important;
			}
		}
		
		.hot-section > div:last-child {
			grid-template-columns: repeat(2, 1fr) !important;
			gap: 16px !important;
		}
	}
	
	@media (max-width: 480px) {
		.hot-section > div:last-child {
			grid-template-columns: 1fr !important;
		}
	}
}
</style>
