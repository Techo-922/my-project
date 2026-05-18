<template>
	<div class="main-containers">
		<!-- 装饰性背景元素 -->
		<div class="geometric-bg"></div>
		
		<div class="top-container" :style='{"padding":"0 20px","alignItems":"center","display":"flex","justifyContent":"space-between","overflow":"hidden","top":"0","left":"0","background":"rgba(255, 255, 255, 0.95)","width":"100%","position":"fixed","height":"70px","zIndex":"1002","borderBottom":"1px solid rgba(229, 231, 235, 0.8)","backdropFilter":"blur(20px) saturate(180%)"}'>
			<div v-if="true" class="system-title" :style='{"color":"#1f2937","fontSize":"24px","fontWeight":"700","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","-webkitBackgroundClip":"text","-webkitTextFillColor":"transparent","backgroundClip":"text"}'>智能菜谱推荐系统</div>
			<div class="nav-actions">
				<div v-if="Token" class="user-info" :style='{"color":"#6b7280","fontSize":"16px","marginRight":"16px","display":"inline-block","fontWeight":"500"}'>{{username}}</div>
				<el-button v-if="!Token" @click="toLogin()" class="login-btn" :style='{"background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","border":"none","color":"white","borderRadius":"16px","padding":"8px 20px","fontSize":"14px","fontWeight":"500","transition":"all 0.3s ease","position":"relative","overflow":"hidden"}'>登录/注册</el-button>
                <el-button v-if="Token" @click="logout" class="logout-btn" :style='{"background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","border":"none","color":"white","borderRadius":"16px","padding":"8px 20px","fontSize":"14px","fontWeight":"500","transition":"all 0.3s ease","position":"relative","overflow":"hidden"}'>退出</el-button>
			</div>
		</div>
		
		<div class="body-containers" :style='{"minHeight":"100vh","paddingTop":"70px","margin":"0","position":"relative","background":"#f9fafb"}'>
			<div class="menu-preview" :style='{"background":"rgba(255, 255, 255, 0.95)","borderBottom":"1px solid rgba(229, 231, 235, 0.8)","boxShadow":"0 1px 2px 0 rgba(0, 0, 0, 0.05)","backdropFilter":"blur(20px) saturate(180%)"}'>
				<el-menu class="el-menu-horizontal-demo" :style='{"border":"none","padding":"0","margin":"0 auto","alignItems":"center","background":"transparent","display":"flex","maxWidth":"1200px","position":"relative","justifyContent":"center","height":"60px"}' :default-active="activeIndex" :unique-opened="true" mode="horizontal" :router="true" @select="handleSelect">
					<el-menu-item v-for="(menu, index) in menuList" :index="index + ''" :key="index" :route="menu.url">
						<i v-if="true" :style='{"marginRight":"8px","color":"inherit","fontSize":"16px","transition":"transform 0.3s ease"}' :class="iconArr[index]"></i>
						<span :style='{"color":"inherit","fontSize":"16px","fontWeight":"500"}'>{{menu.name}}</span>
					</el-menu-item>
					<el-menu-item @click="goBackend">
						<i v-if="true" :style='{"marginRight":"8px","color":"inherit","fontSize":"16px","transition":"transform 0.3s ease"}' class="el-icon-box"></i>
						<span :style='{"color":"inherit","fontSize":"16px","fontWeight":"500"}'>后台管理</span>
					</el-menu-item>
					<el-menu-item :index="menuList.length + 2 + ''" v-if="Token && notAdmin" @click="goMenu('/index/center')">
						<i v-if="true" :style='{"marginRight":"8px","color":"inherit","fontSize":"16px","transition":"transform 0.3s ease"}' class="el-icon-user"></i>
						<span :style='{"color":"inherit","fontSize":"16px","fontWeight":"500"}'>个人中心</span>
					</el-menu-item>
				</el-menu>
			</div>
			
			<div class="banner-preview" :style='{"width":"100%","padding":"32px 16px"}'>
				<el-carousel :style='{"maxWidth":"1200px","margin":"0 auto","borderRadius":"24px","overflow":"hidden","boxShadow":"0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)"}' trigger="click" indicator-position="inside" arrow="always" type="default" direction="horizontal" height="400px" :autoplay="true" :interval="3000" :loop="true">
					<el-carousel-item :style='{"width":"100%","height":"100%"}' v-for="(item, index) in carouselList" :key="'carousel-' + item.id + '-' + index">
						<el-image :style='{"objectFit":"cover","width":"100%","height":"100%","display":"block"}' :src="baseUrl + item.value" fit="cover"></el-image>
					</el-carousel-item>
				</el-carousel>
			</div>
			
			<router-view></router-view>
			
			<div class="bottom-preview" :style='{"background":"linear-gradient(135deg, #1f2937 0%, #111827 100%)","color":"#d1d5db","padding":"48px 0","marginTop":"80px","position":"relative","overflow":"hidden"}'>
				<div class="footer-content" :style='{"maxWidth":"1200px","margin":"0 auto","padding":"0 16px","textAlign":"center","position":"relative","zIndex":"2"}'>
					<div class="footer-title" :style='{"fontSize":"18px","fontWeight":"600","color":"white","marginBottom":"16px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","-webkitBackgroundClip":"text","-webkitTextFillColor":"transparent","backgroundClip":"text"}'>智能菜谱推荐系统</div>
					<div class="footer-description" :style='{"fontSize":"14px","color":"#9ca3af","lineHeight":"1.6"}'>为您提供个性化的菜谱推荐和智能饮食管理服务</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import Vue from 'vue'
export default {
    data() {
		return {
            activeIndex: '0',
			roleMenus: [{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-circle","buttons":["新增","查看","修改","删除"],"menu":"用户","menuJump":"列表","tableName":"yonghu"}],"menu":"用户管理"},{"child":[{"appFrontIcon":"cuIcon-pic","buttons":["新增","查看","修改","删除"],"menu":"菜式类型","menuJump":"列表","tableName":"caishileixing"}],"menu":"菜式类型管理"},{"child":[{"appFrontIcon":"cuIcon-explore","buttons":["新增","查看","修改","删除","菜谱分类统计","菜谱评分统计","查看评论","首页总数","首页统计"],"menu":"菜谱信息","menuJump":"列表","tableName":"caipuxinxi"}],"menu":"菜谱信息管理"},{"child":[{"appFrontIcon":"cuIcon-pic","buttons":["查看","删除","每日评分人数统计","首页总数","首页统计"],"menu":"评分信息","menuJump":"列表","tableName":"pingfenxinxi"}],"menu":"评分信息管理"},{"child":[{"appFrontIcon":"cuIcon-message","buttons":["查看","回复","删除"],"menu":"留言信息","tableName":"messages"}],"menu":"留言信息"},{"child":[{"appFrontIcon":"cuIcon-news","buttons":["查看","修改"],"menu":"关于我们","tableName":"aboutus"},{"appFrontIcon":"cuIcon-time","buttons":["查看","修改"],"menu":"系统简介","tableName":"systemintro"},{"appFrontIcon":"cuIcon-newshot","buttons":["查看","修改"],"menu":"轮播图管理","tableName":"config"},{"appFrontIcon":"cuIcon-news","buttons":["新增","查看","修改","删除"],"menu":"公告信息","tableName":"news"}],"menu":"系统管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-rank","buttons":["查看","评分"],"menu":"菜谱信息列表","menuJump":"列表","tableName":"caipuxinxi"}],"menu":"菜谱信息模块"}],"hasBackLogin":"是","hasBackRegister":"否","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"管理员","tableName":"users"},{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-pic","buttons":["查看","删除"],"menu":"评分信息","menuJump":"列表","tableName":"pingfenxinxi"}],"menu":"评分信息管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-rank","buttons":["查看","评分"],"menu":"菜谱信息列表","menuJump":"列表","tableName":"caipuxinxi"}],"menu":"菜谱信息模块"}],"hasBackLogin":"是","hasBackRegister":"否","hasFrontLogin":"是","hasFrontRegister":"是","roleName":"用户","tableName":"yonghu"}],
			baseUrl: '',
			carouselList: [],
			menuList: [],
			form: {
				ask: '',
				userid: localStorage.getItem('userid')
			},
			Token: localStorage.getItem('Token'),
            username: localStorage.getItem('username'),
            notAdmin: localStorage.getItem('sessionTable')!='"users"',
			timer: '',
			iconArr: [
				'el-icon-star-off',
				'el-icon-goods',
				'el-icon-warning',
				'el-icon-question',
				'el-icon-info',
				'el-icon-help',
				'el-icon-picture-outline-round',
				'el-icon-camera-solid',
				'el-icon-video-camera-solid',
				'el-icon-video-camera',
				'el-icon-bell',
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check'
			],	
		}
    },
    created() {
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
    },
    mounted() {
        this.activeIndex = localStorage.getItem('keyPath') || '0';
    },
    watch: {
        $route(newValue) {
            let that = this
            let url = window.location.href
            let arr = url.split('#')
            for (let x in this.menuList) {
                if (newValue.path == this.menuList[x].url) {
                    this.activeIndex = x
                }
            }
            this.Token = localStorage.getItem('Token')
        },
    },
    methods: {
        handleSelect(keyPath) {
            if (keyPath) {
                localStorage.setItem('keyPath', keyPath)
            }
        },
		toLogin() {
		  this.$router.push('/login');
		},
        logout() {
            localStorage.clear();
            Vue.http.headers.common['Token'] = "";
            this.$router.push('/index/home');
            this.activeIndex = '0'
            localStorage.setItem('keyPath', this.activeIndex)
            this.Token = ''
            this.$forceUpdate()
            this.$message({
                message: '登出成功',
                type: 'success',
                duration: 1000,
            });
        },
		getCarousel() {
			this.$http.get('config/list', {params: { page: 1, limit: 3 }}).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
				}
			});
		},
		goBackend() {
			window.location.href = `${this.$config.baseUrl}admin/admin/dist/index.html`;
		},
		goMenu(path) {
            if (!localStorage.getItem('Token')) {
                this.toLogin();
            } else {
                this.$router.push(path);
            }
		},
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	@import '@/assets/css/front-theme.scss';
	
	.menu-preview {
	  .el-scrollbar {
	    height: 100%;
	
	    & ::v-deep .scrollbar-wrapper {
	      overflow-x: hidden;
	    }
	  }
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item {
		cursor: pointer;
		border: none !important;
		padding: 0 24px !important;
		margin: 0 4px;
		background: none !important;
		color: #6b7280 !important;
		white-space: nowrap;
		display: flex;
		font-size: 16px !important;
		font-weight: 500;
		line-height: 60px !important;
		align-items: center;
		position: relative;
		list-style: none;
		height: 60px !important;
		border-radius: 12px;
		transition: all 0.2s ease;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item:hover {
		background: #f3f4f6 !important;
		color: #6366f1 !important;
		transform: none;
		box-shadow: none;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item.is-active {
		background: #6366f1 !important;
		color: white !important;
		font-weight: 600;
		box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
	}
	
	.banner-preview {
	  .el-carousel ::v-deep .el-carousel__indicator button {
	    width: 0;
	    height: 0;
	    display: none;
	  }
	  
	  // 确保轮播图容器正常显示
	  .el-carousel {
	    .el-carousel-item {
	      .el-image {
	        display: block !important;
	        width: 100% !important;
	        height: 100% !important;
	      }
	    }
	  }
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left {
		width: 44px;
		height: 44px;
		font-size: 16px;
		background: rgba(255, 255, 255, 0.9);
		color: #6366f1;
		border-radius: 50%;
		box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left:hover {
		background: white;
		color: #4338ca;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right {
		width: 44px;
		height: 44px;
		font-size: 16px;
		background: rgba(255, 255, 255, 0.9);
		color: #6366f1;
		border-radius: 50%;
		box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right:hover {
		background: white;
		color: #4338ca;
	}

	.banner-preview .el-carousel ::v-deep .el-carousel__indicators {
		padding: 0;
		margin: 0 0 16px 0;
		z-index: 2;
		position: absolute;
		list-style: none;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li {
		border-radius: 50%;
		padding: 0;
		margin: 0 6px;
		background: rgba(255, 255, 255, 0.5);
		display: inline-block;
		width: 12px;
		opacity: 0.6;
		transition: all 0.3s ease;
		height: 12px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li:hover {
		background: rgba(255, 255, 255, 0.8);
		opacity: 0.9;
		transform: scale(1.1);
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li.is-active {
		background: white;
		opacity: 1;
		transform: scale(1.2);
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}

    .chat-content {
      .left-content {
          width: 100%;
          text-align: left;
      }
      .right-content {
          width: 100%;
          text-align: right;
      }
    }
    
    // 响应式设计
    @media (max-width: 768px) {
      .menu-preview .el-menu-horizontal-demo {
        flex-wrap: wrap;
        height: auto !important;
        padding: 16px 0;
        
        .el-menu-item {
          flex: 1;
          min-width: 120px;
          text-align: center;
          margin: 4px 2px;
          padding: 0 16px !important;
        }
      }
      
      .top-container {
        flex-direction: column;
        height: auto !important;
        padding: 16px 20px !important;
        
        .system-title {
          margin-bottom: 16px !important;
          font-size: 20px !important;
        }
        
        .nav-actions {
          display: flex;
          align-items: center;
          gap: 12px;
        }
      }
      
      .body-containers {
        padding-top: 120px !important;
      }
      
      .banner-preview {
        padding: 16px !important;
        
        .el-carousel {
          height: 250px !important;
        }
      }
    }
</style>
