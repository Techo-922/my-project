<template>
<div class="home-preview" :style='{"maxWidth":"1200px","margin":"0 auto","padding":"0 16px"}'>

	<!-- 功能卡片区域 -->
	<div v-if="Token" class="features-section" :style='{"display":"grid","gridTemplateColumns":"repeat(4, 1fr)","gap":"20px","margin":"32px 0"}'>
		<!-- 我的食材库 -->
		<div @click="goMenu('/index/usershicaiList')" class="feature-card" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden","transition":"all 0.3s ease","cursor":"pointer"}'>
			<div class="card-header" :style='{"padding":"24px","textAlign":"center","background":"linear-gradient(135deg, #14b8a6 0%, #0d9488 100%)","color":"white"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","marginBottom":"8px"}'>我的食材库</div>
				<div class="card-subtitle" :style='{"fontSize":"16px","opacity":"0.9","marginBottom":"16px"}'>管理您的食材库存</div>
				<div class="card-description" :style='{"fontSize":"14px","opacity":"0.8","marginBottom":"24px"}'>查看食材、添加新食材、过期提醒</div>
				<div class="card-action" :style='{"display":"inline-flex","alignItems":"center","padding":"12px 24px","background":"rgba(255,255,255,0.2)","borderRadius":"12px","fontSize":"14px","fontWeight":"500","transition":"all 0.2s ease"}'>立即查看</div>
			</div>
		</div>

		<!-- 饮食统计 -->
		<div @click="goMenu('/index/dietStatisticsList')" class="feature-card" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden","transition":"all 0.3s ease","cursor":"pointer"}'>
			<div class="card-header" :style='{"padding":"24px","textAlign":"center","background":"linear-gradient(135deg, #f97316 0%, #ea580c 100%)","color":"white"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","marginBottom":"8px"}'>饮食统计</div>
				<div class="card-subtitle" :style='{"fontSize":"16px","opacity":"0.9","marginBottom":"16px"}'>查看您的饮食数据</div>
				<div class="card-description" :style='{"fontSize":"14px","opacity":"0.8","marginBottom":"24px"}'>消耗统计、健康分析</div>
				<div class="card-action" :style='{"display":"inline-flex","alignItems":"center","padding":"12px 24px","background":"rgba(255,255,255,0.2)","borderRadius":"12px","fontSize":"14px","fontWeight":"500","transition":"all 0.2s ease"}'>查看统计</div>
			</div>
		</div>

		<!-- 常用食材分析 -->
		<div @click="goMenu('/index/foodUsageStatistics')" class="feature-card" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden","transition":"all 0.3s ease","cursor":"pointer"}'>
			<div class="card-header" :style='{"padding":"24px","textAlign":"center","background":"linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)","color":"white"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","marginBottom":"8px"}'>常用食材分析</div>
				<div class="card-subtitle" :style='{"fontSize":"16px","opacity":"0.9","marginBottom":"16px"}'>了解您的食材使用习惯</div>
				<div class="card-description" :style='{"fontSize":"14px","opacity":"0.8","marginBottom":"24px"}'>使用排行、频率分析</div>
				<div class="card-action" :style='{"display":"inline-flex","alignItems":"center","padding":"12px 24px","background":"rgba(255,255,255,0.2)","borderRadius":"12px","fontSize":"14px","fontWeight":"500","transition":"all 0.2s ease"}'>查看分析</div>
			</div>
		</div>

		<!-- 健康偏好管理 -->
		<div @click="goMenu('/index/healthPreference')" class="feature-card" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden","transition":"all 0.3s ease","cursor":"pointer"}'>
			<div class="card-header" :style='{"padding":"24px","textAlign":"center","background":"linear-gradient(135deg, #10b981 0%, #059669 100%)","color":"white"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","marginBottom":"8px"}'>健康偏好管理</div>
				<div class="card-subtitle" :style='{"fontSize":"16px","opacity":"0.9","marginBottom":"16px"}'>设置您的健康目标</div>
				<div class="card-description" :style='{"fontSize":"14px","opacity":"0.8","marginBottom":"24px"}'>营养目标、饮食偏好</div>
				<div class="card-action" :style='{"display":"inline-flex","alignItems":"center","padding":"12px 24px","background":"rgba(255,255,255,0.2)","borderRadius":"12px","fontSize":"14px","fontWeight":"500","transition":"all 0.2s ease"}'>设置偏好</div>
			</div>
		</div>
	</div>

	<!-- 主要内容区域 -->
	<div class="main-content" :style='{"display":"grid","gridTemplateColumns":"2fr 1fr","gap":"32px","margin":"32px 0"}'>
		
		<!-- 菜谱推荐 -->
		<div class="content-card recommend" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden"}'>
			<div class="card-header" :style='{"padding":"24px 24px 16px","borderBottom":"1px solid #e5e7eb","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","color":"white"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","textAlign":"center","marginBottom":"8px"}'>菜谱信息推荐</div>
				<div class="card-subtitle" :style='{"fontSize":"14px","opacity":"0.9","textAlign":"center"}'>发现美味，享受烹饪乐趣</div>
			</div>
			
			<div class="card-body" :style='{"padding":"24px"}'>
				<div class="recipe-grid-container" :style='{"position":"relative","overflow":"hidden","borderRadius":"12px"}'>
					<div class="swiper-container" id="recommendcaipuxinxi">
						<div class="swiper-wrapper">
							<!-- 每个 slide 包含 4 个菜谱，2x2 布局 -->
							<div class="swiper-slide grid-slide" v-for="(slideItems, slideIndex) in chunkedRecommendList" :key="slideIndex" :style='{"display":"grid","gridTemplateColumns":"1fr 1fr","gridTemplateRows":"1fr 1fr","gap":"20px","padding":"0","height":"auto"}'>
								<div class="recipe-card" v-for="(item, index) in slideItems" :key="item.id" @click="toDetail('caipuxinxiDetail', item)" :style='{"background":"#ffffff","borderRadius":"12px","boxShadow":"0 2px 12px rgba(0, 0, 0, 0.08)","border":"1px solid #e5e7eb","overflow":"hidden","transition":"all 0.3s ease","cursor":"pointer","position":"relative","height":"200px"}'>
									
									<!-- 图片区域 -->
									<div class="card-image-container" :style='{"position":"relative","height":"120px","overflow":"hidden"}'>
										<img :name="item.id" class="card-image" :style='{"width":"100%","height":"100%","objectFit":"cover","transition":"all 0.3s ease"}' v-if="preHttp(item.caipufengmian)" :src="item.caipufengmian.split(',')[0]" alt="" />
										<img :name="item.id" class="card-image" :style='{"width":"100%","height":"100%","objectFit":"cover","transition":"all 0.3s ease"}' v-else-if="item.caipufengmian" :src="baseUrl + item.caipufengmian.split(',')[0]" alt="" />
										<img :name="item.id" class="card-image" :style='{"width":"100%","height":"100%","objectFit":"cover","transition":"all 0.3s ease"}' v-else src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='100' height='100'%3E%3Crect fill='%23f0f0f0' width='100' height='100'/%3E%3Ctext fill='%23999' x='50%25' y='50%25' text-anchor='middle' dy='.3em'%3E暂无图片%3C/text%3E%3C/svg%3E" alt="" />
										
										<!-- 评分徽章 -->
										<div class="rating-badge" :style='{"position":"absolute","top":"8px","right":"8px","background":"rgba(0,0,0,0.75)","color":"white","padding":"3px 8px","borderRadius":"10px","fontSize":"11px","fontWeight":"600","display":"flex","alignItems":"center","gap":"2px","backdropFilter":"blur(10px)"}'>
											<span style="color: #ffd700;">★</span>
											<span>{{item.fenshu || '5.0'}}</span>
										</div>
										
										<!-- 收藏按钮 -->
										<div class="favorite-btn" :style='{"position":"absolute","top":"8px","left":"8px","width":"28px","height":"28px","background":"rgba(255,255,255,0.9)","borderRadius":"50%","display":"flex","alignItems":"center","justifyContent":"center","cursor":"pointer","transition":"all 0.3s ease","boxShadow":"0 2px 6px rgba(0,0,0,0.1)"}'>
											<i class="el-icon-heart" :style='{"fontSize":"12px","color":"#ff6b6b"}'></i>
										</div>
										
										<!-- 图片遮罩 -->
										<div class="image-overlay" :style='{"position":"absolute","bottom":"0","left":"0","right":"0","height":"30px","background":"linear-gradient(transparent, rgba(0,0,0,0.2))","pointerEvents":"none"}'></div>
									</div>
									
									<!-- 内容区域 -->
									<div class="card-content" :style='{"padding":"12px","height":"80px","display":"flex","flexDirection":"column","justifyContent":"space-between"}'>
										<!-- 标题 -->
										<div class="card-title" :style='{"fontSize":"14px","fontWeight":"700","color":"#1f2937","lineHeight":"1.3","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap","marginBottom":"6px"}'>{{item.caipumingcheng}}</div>
										
										<!-- 标签和信息 -->
										<div class="card-meta" :style='{"display":"flex","alignItems":"center","justifyContent":"space-between","marginBottom":"8px"}'>
											<div class="category-tag" :style='{"background":"#f3f4f6","color":"#6b7280","padding":"2px 6px","borderRadius":"6px","fontSize":"10px","fontWeight":"500"}'>
												{{item.caishileixing || '美食'}}
											</div>
											<div class="view-count" :style='{"display":"flex","alignItems":"center","gap":"3px","fontSize":"10px","color":"#9ca3af"}'>
												<i class="el-icon-view" :style='{"fontSize":"10px"}'></i>
												<span>{{item.clicknum || 0}}</span>
											</div>
										</div>
										
										<!-- 操作按钮 -->
										<div class="card-action" :style='{"display":"flex","alignItems":"center","justifyContent":"center","background":"linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)","color":"white","padding":"6px 12px","borderRadius":"8px","fontSize":"11px","fontWeight":"600","cursor":"pointer","transition":"all 0.3s ease"}'>
											<span>查看详情</span>
											<i class="el-icon-d-arrow-right" :style='{"fontSize":"9px","marginLeft":"4px"}'></i>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<!-- 导航按钮 -->
						<div class="swiper-button-prev" :style='{"color":"#6366f1","fontWeight":"bold"}' ></div>
						<div class="swiper-button-next" :style='{"color":"#6366f1","fontWeight":"bold"}'></div>
						
						<!-- 分页指示器 -->
						<div class="swiper-pagination" :style='{"bottom":"10px","textAlign":"center"}'></div>
					</div>
				</div>
				
				<!-- 查看更多按钮 -->
				<div @click="moreBtn('caipuxinxi')" class="more-recipes-btn" :style='{"display":"flex","alignItems":"center","justifyContent":"center","padding":"14px 28px","border":"2px solid #6366f1","borderRadius":"12px","background":"transparent","color":"#6366f1","fontSize":"14px","fontWeight":"600","cursor":"pointer","transition":"all 0.3s ease","margin":"24px auto 0","width":"fit-content","position":"relative","overflow":"hidden"}'>
					<span :style='{"position":"relative","zIndex":"2"}'>查看更多菜谱</span>
					<i class="el-icon-d-arrow-right" :style='{"marginLeft":"8px","fontSize":"14px","position":"relative","zIndex":"2"}'></i>
					<div class="btn-bg" :style='{"position":"absolute","top":"0","left":"-100%","width":"100%","height":"100%","background":"linear-gradient(135deg, #6366f1 0%, #4f46e5 100%)","transition":"left 0.3s ease","zIndex":"1"}'></div>
				</div>
			</div>
		</div>

		<!-- 公告信息 -->
		<div class="content-card news" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden"}'>
			<div class="card-header" :style='{"padding":"24px 24px 16px","borderBottom":"1px solid #e5e7eb"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","color":"#1f2937","textAlign":"center"}'>公告信息</div>
			</div>
			
			<div class="card-body" :style='{"padding":"0"}'>
				<div v-if="newsList.length" class="news-list">
					<div v-for="(item,index) in newsList" v-if="index<6" :key="index" @click="toDetail('newsDetail', item)" class="news-item" :style='{"padding":"16px","borderBottom":"1px solid #e5e7eb","transition":"all 0.2s ease","cursor":"pointer"}'>
						<div class="news-meta" :style='{"display":"flex","justifyContent":"space-between","alignItems":"center","marginBottom":"8px"}'>
							<div class="news-tag" :style='{"display":"inline-flex","alignItems":"center","padding":"4px 12px","background":"#6366f1","color":"white","borderRadius":"8px","fontSize":"12px","fontWeight":"500"}'>新闻动态</div>
							<div class="news-date" :style='{"fontSize":"12px","color":"#9ca3af"}'>{{ item.addtime.split(' ')[0] }}</div>
						</div>
						<div class="news-title" :style='{"fontSize":"16px","fontWeight":"600","color":"#1f2937","marginBottom":"8px","lineHeight":"1.4","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap"}'>{{ item.title }}</div>
						<div class="news-description" :style='{"fontSize":"14px","color":"#6b7280","lineHeight":"1.5","overflow":"hidden","display":"-webkit-box","-webkitLineClamp":"2","-webkitBoxOrient":"vertical"}'>{{ item.introduction }}</div>
					</div>
				</div>
				
				<div class="card-footer" :style='{"padding":"16px","textAlign":"center","borderTop":"1px solid #e5e7eb"}'>
					<div @click="moreBtn('news')" class="modern-btn btn-secondary" :style='{"display":"inline-flex","alignItems":"center","justifyContent":"center","padding":"8px 16px","border":"none","borderRadius":"8px","background":"#f3f4f6","color":"#374151","fontSize":"14px","fontWeight":"500","cursor":"pointer","transition":"all 0.2s ease"}'>
						<span>查看更多</span>
						<i class="el-icon-d-arrow-right" :style='{"marginLeft":"8px","fontSize":"14px"}'></i>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 关于我们和系统简介 -->
	<div class="info-section" :style='{"display":"grid","gridTemplateColumns":"1fr 1fr","gap":"32px","margin":"32px 0"}'>
		<!-- 关于我们 -->
		<div class="content-card" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden"}'>
			<div class="card-header" :style='{"padding":"24px 24px 16px","borderBottom":"1px solid #e5e7eb"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","color":"#1f2937","textAlign":"center"}'>{{aboutUsDetail.title}}</div>
			</div>
			<div class="card-body" :style='{"padding":"24px"}'>
				<div class="image-container" :style='{"marginBottom":"16px"}'>
					<img :style='{"width":"100%","height":"200px","objectFit":"cover","borderRadius":"12px"}' :src="baseUrl + aboutUsDetail.picture1">
				</div>
				<div class="content-text" :style='{"fontSize":"14px","color":"#6b7280","lineHeight":"1.6","overflow":"hidden","display":"-webkit-box","-webkitLineClamp":"4","-webkitBoxOrient":"vertical"}' v-html="aboutUsDetail.content"></div>
			</div>
		</div>

		<!-- 系统简介 -->
		<div class="content-card" :style='{"background":"#ffffff","borderRadius":"16px","boxShadow":"0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)","border":"1px solid #e5e7eb","overflow":"hidden"}'>
			<div class="card-header" :style='{"padding":"24px 24px 16px","borderBottom":"1px solid #e5e7eb"}'>
				<div class="card-title" :style='{"fontSize":"20px","fontWeight":"700","color":"#1f2937","textAlign":"center"}'>{{systemIntroductionDetail.title}}</div>
			</div>
			<div class="card-body" :style='{"padding":"24px"}'>
				<div class="image-container" :style='{"marginBottom":"16px"}'>
					<img :style='{"width":"100%","height":"200px","objectFit":"cover","borderRadius":"12px"}' :src="baseUrl + systemIntroductionDetail.picture1">
				</div>
				<div class="content-text" :style='{"fontSize":"14px","color":"#6b7280","lineHeight":"1.6","overflow":"hidden","display":"-webkit-box","-webkitLineClamp":"4","-webkitBoxOrient":"vertical"}' v-html="systemIntroductionDetail.content"></div>
			</div>
		</div>
	</div>

</div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
        baseUrl: '',
        aboutUsDetail: {},
        systemIntroductionDetail: {},
        newsList: [],
        caipuxinxiRecommend: [],
        Token: localStorage.getItem('Token')
      }
    },
    computed: {
      // 将菜谱数据分组，每4个一组用于2x2网格显示
      chunkedRecommendList() {
        const chunks = [];
        const items = this.caipuxinxiRecommend || [];
        for (let i = 0; i < items.length; i += 4) {
          chunks.push(items.slice(i, i + 4));
        }
        return chunks;
      }
    },
    created() {
      this.baseUrl = this.$config.baseUrl;
      this.getNewsList();
      this.getAboutUs();
      this.getSystemIntroduction();
      this.getList();
    },
    //方法集合
    methods: {
      preHttp(str) {
          return str && str.substr(0,4)=='http';
      },
      getAboutUs() {
          this.$http.get('aboutus/detail/1', {}).then(res => {
            if(res.data.code == 0) {
              this.aboutUsDetail = res.data.data;
            }
          })
      },
      getSystemIntroduction() {
          this.$http.get('systemintro/detail/1', {}).then(res => {
            if(res.data.code == 0) {
              this.systemIntroductionDetail = res.data.data;
            }
          })
      },
		getNewsList() {
			this.$http.get('news/list', {params: {
				page: 1,
				limit: 4,
                sort: 'addtime',
			order: 'desc'}}).then(res => {
				if (res.data.code == 0) {
					this.newsList = res.data.data.list;
					
					
				}
			});
		},
		getList() {
          let autoSortUrl = "";
          autoSortUrl = "caipuxinxi/autoSort";
          if(localStorage.getItem('Token')) {
              autoSortUrl = "caipuxinxi/autoSort2";
          }
			this.$http.get(autoSortUrl, {params: {
				page: 1,
				limit: 10,
			}}).then(res => {
				if (res.data.code == 0) {
					this.caipuxinxiRecommend = res.data.data.list;
					
					let options = {"observer":true,"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"observeParents":true,"loop":true,"slidesPerView":"1","speed":500,"spaceBetween":0,"autoplay":{"delay":4000,"disableOnInteraction":false},"pagination":{"el":".swiper-pagination","clickable":true}}
					options.pagination = {el:'null'}
					if(options.slidesPerView) {
						options.slidesPerView = Number(options.slidesPerView);
					}
					if(options.spaceBetween) {
						options.spaceBetween = Number(options.spaceBetween);
					}
					this.$nextTick(() => {
						new Swiper('#recommendcaipuxinxi', options)
					})
					
					// 商品列表样式五
					
				}
			});
			
		},
		toDetail(path, item) {
			this.$router.push({path: '/index/' + path, query: {detailObj: JSON.stringify(item)}});
		},
		moreBtn(path) {
			this.$router.push({path: '/index/' + path});
		},
		goMenu(path) {
			if (!localStorage.getItem('Token')) {
				this.$router.push('/login');
			} else {
				this.$router.push(path);
			}
		}
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	@import '@/assets/css/front-theme.scss';
	
	.home-preview {
		
		// 功能卡片悬停效果增强
		.feature-card:hover {
			transform: translateY(-8px) scale(1.02);
			box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
			
			.card-action {
				background: rgba(255,255,255,0.3) !important;
				transform: translateY(-2px) scale(1.05);
			}
		}
		
		// 内容卡片悬停效果增强
		.content-card:hover {
			transform: translateY(-4px);
			box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
		}
		
		// 推荐项目悬停效果增强
		.recipe-card {
			&:hover {
				transform: translateY(-8px) scale(1.02);
				box-shadow: 0 20px 40px -12px rgba(0, 0, 0, 0.15);
				
				.card-image {
					transform: scale(1.1);
				}
				
				.rating-badge {
					transform: scale(1.1);
					background: rgba(0,0,0,0.8);
				}
				
				.favorite-btn {
					transform: scale(1.15);
					background: rgba(255, 255, 255, 1);
					box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
					
					i {
						color: #ff4757 !important;
					}
				}
				
				.card-action {
					background: linear-gradient(135deg, #5a67d8 0%, #7c3aed 100%);
					transform: translateY(-2px);
					box-shadow: 0 4px 15px rgba(99, 102, 241, 0.3);
				}
			}
		}
		
		// 网格布局样式
		.grid-slide {
			min-height: 420px;
			
			.recipe-card {
				transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
			}
		}
		
		// 查看更多按钮样式
		.more-recipes-btn {
			&:hover {
				color: white !important;
				transform: translateY(-2px);
				box-shadow: 0 10px 25px rgba(99, 102, 241, 0.3);
				border-color: transparent !important;
				
				.btn-bg {
					left: 0 !important;
				}
			}
		}
		
		// 新闻项目悬停效果增强
		.news-item:hover {
			background: linear-gradient(135deg, rgba(99, 102, 241, 0.02) 0%, rgba(139, 92, 246, 0.02) 100%) !important;
		}
		
		// 按钮悬停效果增强
		.modern-btn:hover {
			&.btn-outline {
				color: white !important;
				transform: translateY(-2px);
				box-shadow: 0 10px 25px rgba(99, 102, 241, 0.3);
				border-color: transparent !important;
				
				.btn-bg {
					left: 0 !important;
				}
			}
			
			&.btn-secondary {
				background: #e5e7eb !important;
				transform: translateY(-1px);
			}
		}
		
		// Swiper 按钮样式增强
		.swiper-button-prev, .swiper-button-next {
			width: 48px !important;
			height: 48px !important;
			margin-top: -24px !important;
			background: rgba(255, 255, 255, 0.95) !important;
			border-radius: 50% !important;
			box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15) !important;
			backdrop-filter: blur(10px) !important;
			transition: all 0.3s ease !important;
			border: 2px solid rgba(99, 102, 241, 0.1) !important;
			
			&::after {
				font-size: 18px !important;
				color: #6366f1 !important;
				font-weight: 700;
			}
			
			&:hover {
				background: white !important;
				box-shadow: 0 15px 35px rgba(99, 102, 241, 0.2) !important;
				transform: scale(1.15) !important;
				border-color: rgba(99, 102, 241, 0.3) !important;
				
				&::after {
					color: #4338ca !important;
				}
			}
		}
		
		.swiper-button-prev {
			left: 15px !important;
		}
		
		.swiper-button-next {
			right: 15px !important;
		}
		
		// 菜谱推荐区域特殊样式
		.content-card.recommend {
			position: relative;
			overflow: visible;
			background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
			
			&::before {
				content: '';
				position: absolute;
				top: -2px;
				left: -2px;
				right: -2px;
				bottom: -2px;
				background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
				border-radius: 18px;
				z-index: -1;
				opacity: 0;
				transition: opacity 0.3s ease;
			}
			
			&::after {
				content: '';
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				background: 
					radial-gradient(circle at 20% 80%, rgba(99, 102, 241, 0.03) 0%, transparent 50%),
					radial-gradient(circle at 80% 20%, rgba(139, 92, 246, 0.03) 0%, transparent 50%),
					radial-gradient(circle at 40% 40%, rgba(168, 85, 247, 0.02) 0%, transparent 50%);
				pointer-events: none;
				z-index: 0;
			}
			
			&:hover::before {
				opacity: 0.1;
			}
			
			.card-header {
				position: relative;
				z-index: 1;
				
				&::after {
					content: '';
					position: absolute;
					bottom: 0;
					left: 50%;
					transform: translateX(-50%);
					width: 60px;
					height: 3px;
					background: rgba(255, 255, 255, 0.3);
					border-radius: 2px;
				}
			}
			
			.card-body {
				position: relative;
				z-index: 1;
			}
		}
		
		// 加载动画效果
		.recommend-item {
			animation: fadeInUp 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
			opacity: 0;
			transform: translateY(40px);
			
			@for $i from 1 through 10 {
				&:nth-child(#{$i}) {
					animation-delay: #{$i * 0.15}s;
				}
			}
		}
		
		@keyframes fadeInUp {
			to {
				opacity: 1;
				transform: translateY(0);
			}
		}
		
		// 彩虹动画
		@keyframes rainbow {
			0% { background-position: 0% 50%; }
			50% { background-position: 100% 50%; }
			100% { background-position: 0% 50%; }
		}
		
		// 脉冲动画
		@keyframes pulse {
			0%, 100% { transform: scale(1); }
			50% { transform: scale(1.05); }
		}
		
		// 闪烁动画
		@keyframes shimmer {
			0% { transform: translateX(-100%); }
			100% { transform: translateX(100%); }
		}
		
		// 图片加载效果
		.image-container {
			position: relative;
			
			&::before {
				content: '';
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
				background-size: 200% 100%;
				animation: shimmer 1.5s infinite;
				z-index: 1;
			}
			
			img {
				position: relative;
				z-index: 2;
			}
		}
		
		@keyframes shimmer {
			0% {
				background-position: -200% 0;
			}
			100% {
				background-position: 200% 0;
			}
		}
		
		// 响应式设计
		@media (max-width: 1024px) {
			.main-content {
				grid-template-columns: 1fr !important;
				gap: 24px !important;
			}
			
			.info-section {
				grid-template-columns: 1fr !important;
				gap: 24px !important;
			}
			
			// 平板端网格调整
			.grid-slide {
				gap: 16px !important;
				
				.recipe-card {
					height: 180px !important;
					
					.card-image-container {
						height: 100px !important;
					}
					
					.card-content {
						height: 80px !important;
						padding: 10px !important;
					}
				}
			}
		}
		
		@media (max-width: 1024px) {
			.features-section {
				grid-template-columns: repeat(2, 1fr) !important;
				gap: 16px !important;
			}
		}
		
		@media (max-width: 768px) {
			padding: 0 16px !important;
			
			.features-section {
				grid-template-columns: 1fr !important;
				gap: 16px !important;
				margin: 24px 0 !important;
			}
			
			.main-content {
				gap: 16px !important;
				margin: 24px 0 !important;
			}
			
			.info-section {
				gap: 16px !important;
				margin: 24px 0 !important;
			}
			
			.feature-card {
				.card-header {
					padding: 20px !important;
					
					.card-title {
						font-size: 18px !important;
					}
				}
				
				&:hover {
					transform: translateY(-4px) scale(1.01) !important;
				}
			}
			
			.content-card .card-header .card-title {
				font-size: 18px !important;
			}
			
			// 移动端网格布局
			.grid-slide {
				grid-template-columns: 1fr !important;
				grid-template-rows: repeat(4, 1fr) !important;
				gap: 12px !important;
				min-height: auto !important;
				
				.recipe-card {
					height: 140px !important;
					display: flex !important;
					flex-direction: row !important;
					
					.card-image-container {
						width: 120px !important;
						height: 100% !important;
						flex-shrink: 0 !important;
					}
					
					.card-content {
						flex: 1 !important;
						height: 100% !important;
						padding: 8px !important;
						
						.card-title {
							font-size: 13px !important;
							margin-bottom: 4px !important;
						}
						
						.card-meta {
							margin-bottom: 6px !important;
							
							.category-tag {
								font-size: 9px !important;
								padding: 1px 4px !important;
							}
							
							.view-count {
								font-size: 9px !important;
							}
						}
						
						.card-action {
							padding: 4px 8px !important;
							font-size: 10px !important;
							border-radius: 6px !important;
						}
					}
					
					&:hover {
						transform: translateY(-4px) scale(1.01) !important;
						box-shadow: 0 12px 25px -8px rgba(0, 0, 0, 0.12) !important;
					}
				}
			}
			
			// 移动端 Swiper 按钮
			.swiper-button-prev, .swiper-button-next {
				width: 40px !important;
				height: 40px !important;
				margin-top: -20px !important;
				
				&::after {
					font-size: 14px !important;
				}
			}
			
			.swiper-button-prev {
				left: 8px !important;
			}
			
			.swiper-button-next {
				right: 8px !important;
			}
			
			// 移动端查看更多按钮
			.more-recipes-btn {
				padding: 12px 20px !important;
				font-size: 13px !important;
				margin: 20px auto 0 !important;
			}
		}
		
		@media (max-width: 480px) {
			.grid-slide {
				.recipe-card {
					height: 120px !important;
					
					.card-image-container {
						width: 100px !important;
					}
					
					.card-content {
						.card-title {
							font-size: 12px !important;
						}
						
						.card-meta {
							.category-tag {
								font-size: 8px !important;
							}
							
							.view-count {
								font-size: 8px !important;
							}
						}
						
						.card-action {
							font-size: 9px !important;
							padding: 3px 6px !important;
						}
					}
				}
			}
		}
	}
</style>
