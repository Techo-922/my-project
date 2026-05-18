<template>
<div>
	<div :style='{"padding":"16px 24px","margin":"10px auto","borderColor":"transparent","borderRadius":"12px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","borderWidth":"0","width":"1200px","borderStyle":"solid","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.08)"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'›'" :style='{"width":"100%","margin":"0 auto","fontSize":"15px","lineHeight":"1","display":"flex","alignItems":"center"}'>
			<el-breadcrumb-item>🏠 首页</el-breadcrumb-item>
			<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index">{{item.name}}</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="list-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
		<!-- 智能推荐选择 -->
		<div v-if="isLoggedIn" class="recommend-selector" :style='{"padding":"20px 24px","borderColor":"transparent","borderRadius":"12px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","borderWidth":"0","marginBottom":"10px","display":"flex","alignItems":"center","justifyContent":"space-between","width":"100%","borderStyle":"solid","boxShadow":"0 8px 20px rgba(102, 126, 234, 0.25)"}'>
			<div style="display: flex; align-items: center; gap: 20px;">
				<span style="color: #fff; font-size: 18px; font-weight: 700; letter-spacing: 0.5px; text-shadow: 0 2px 4px rgba(0,0,0,0.1);">✨ 智能推荐</span>
				<el-radio-group v-model="recommendType" @change="changeRecommendType" size="medium">
					<el-radio-button label="stock_based">📦 基于库存</el-radio-button>
					<el-radio-button label="hot">🔥 热门推荐</el-radio-button>
					<el-radio-button label="personalized">💝 个性化</el-radio-button>
					<el-radio-button label="normal">📋 普通浏览</el-radio-button>
				</el-radio-group>
			</div>
			<el-button 
				v-if="recommendType !== 'normal'" 
				size="medium" 
				icon="el-icon-refresh" 
				@click="refreshRecommend"
				:loading="refreshing"
				style="background: rgba(255,255,255,0.25); border: 2px solid rgba(255,255,255,0.4); color: #fff; padding: 10px 20px; border-radius: 24px; font-weight: 600; box-shadow: 0 2px 8px rgba(0,0,0,0.1); transition: all 0.3s ease;">
				刷新推荐
			</el-button>
		</div>
		
		<div class="category-1" :style='{"padding":"16px 20px","borderColor":"transparent","borderRadius":"12px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","borderWidth":"0","display":"flex","width":"100%","borderStyle":"solid","height":"auto","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.08)","gap":"12px","flexWrap":"wrap"}'>
			<div class="item" :class="swiperIndex == '-1' ? 'active' : ''" @click="getList(1, '全部')" :plain="isPlain">全部</div>
			<div class="item" :class="swiperIndex == index ? 'active' : ''" v-for="(item, index) in fenlei" :key="index" @click="getList(1, item, 'btn' + index)" :ref="'btn' + index" plain>{{item}}</div>
		</div>
		
	
    <el-form :inline="true" :model="formSearch" class="list-form-pv" :style='{"border":"0","padding":"20px 24px","margin":"10px 0 0 0","alignItems":"center","borderRadius":"12px","flexWrap":"wrap","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","display":"flex","width":"100%","height":"auto","boxShadow":"0 2px 8px rgba(102, 126, 234, 0.08)","gap":"12px"}'>
      <el-form-item :style='{"margin":"0"}'>
	    <div class="lable" v-if="true" :style='{"width":"auto","padding":"0 12px 0 0","lineHeight":"42px","display":"inline-block","color":"#667eea","fontWeight":"600","fontSize":"14px"}'>🔍 菜谱名称</div>
        <el-input v-model="formSearch.caipumingcheng" placeholder="请输入菜谱名称" clearable></el-input>
      </el-form-item>
      <el-form-item :style='{"margin":"0"}'>
		<div class="lable" v-if="true" :style='{"width":"auto","padding":"0 12px 0 0","lineHeight":"42px","display":"inline-block","color":"#667eea","fontWeight":"600","fontSize":"14px"}'>🍽️ 菜式类型</div>
        <el-select v-model="formSearch.caishileixing" placeholder="请选择菜式类型" :clearable="true">
          <el-option v-for="(item, index) in caishileixingOptions" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :style='{"margin":"0"}'>
	    <div class="lable" v-if="true" :style='{"width":"auto","padding":"0 12px 0 0","lineHeight":"42px","display":"inline-block","color":"#667eea","fontWeight":"600","fontSize":"14px"}'>👨‍🍳 烹饪方式</div>
        <el-input v-model="formSearch.pengrenfangshi" placeholder="请输入烹饪方式" clearable></el-input>
      </el-form-item>
	  <el-button v-if=" true " :style='{"cursor":"pointer","border":"0","padding":"0px 24px","margin":"0","outline":"none","color":"#fff","borderRadius":"24px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px","fontWeight":"500","boxShadow":"0 4px 12px rgba(102, 126, 234, 0.3)","transition":"all 0.3s ease"}' type="primary" @click="getList(1, curFenlei)"><i v-if="true" :style='{"color":"#fff","margin":"0 8px 0 0","fontSize":"14px"}' class="el-icon-search"></i>查询</el-button>
	  <el-button v-if="isAuth('caipuxinxi','新增')" :style='{"cursor":"pointer","border":"2px solid #667eea","padding":"0px 24px","margin":"0","outline":"none","color":"#667eea","borderRadius":"24px","background":"#fff","width":"auto","fontSize":"14px","lineHeight":"38px","height":"42px","fontWeight":"500","transition":"all 0.3s ease"}' type="primary" @click="add('/index/caipuxinxiAdd')"><i v-if="true" :style='{"color":"#667eea","margin":"0 8px 0 0","fontSize":"14px"}' class="el-icon-circle-plus-outline"></i>添加</el-button>
    </el-form>

	<div class="list" :style='{"border":"0","margin":"20px 0 0 0","borderRadius":"16px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","boxShadow":"0 8px 24px rgba(102, 126, 234, 0.12)"}'>
		<!-- 样式一 -->
		
		<!-- 样式�?-->
		<div class="list2 index-pv1" :style='{"width":"100%","padding":"24px","flexWrap":"wrap","justifyContent":"space-between","display":"block","height":"auto"}'>
			<div :style='{"cursor":"pointer","padding":"0","margin":"0 0 24px 2%","borderRadius":"16px","background":"#fff","display":"inline-block","width":"23%","fontSize":"0","position":"relative","height":"auto","overflow":"hidden","boxShadow":"0 4px 12px rgba(0,0,0,0.06)"}' v-for="(item, index) in dataList" :key="index" @click="toDetail(item)" class="list-item animation-box">
				<img :style='{"width":"100%","margin":"0","objectFit":"cover","borderRadius":"0","display":"inline-block","height":"240px"}' v-if="item.caipufengmian && item.caipufengmian.substr(0,4)=='http'" :src="item.caipufengmian" class="image" />
				<img :style='{"width":"100%","margin":"0","objectFit":"cover","borderRadius":"0","display":"inline-block","height":"240px"}' v-else-if="item.caipufengmian" :src="baseUrl + item.caipufengmian.split(',')[0]" class="image" />
				<img :style='{"width":"100%","margin":"0","objectFit":"cover","borderRadius":"0","display":"inline-block","height":"240px"}' v-else src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='100' height='100'%3E%3Crect fill='%23f0f0f0' width='100' height='100'/%3E%3Ctext fill='%23999' x='50%25' y='50%25' text-anchor='middle' dy='.3em'%3E暂无图片%3C/text%3E%3C/svg%3E" class="image" />
				<div class="item-info" :style='{"padding":"16px","overflow":"hidden","borderRadius":"0","background":"#fff","display":"inline-block","width":"100%","height":"auto"}'>
					<div :style='{"padding":"0","margin":"0 0 8px 0","whiteSpace":"nowrap","overflow":"hidden","color":"#333","borderRadius":"0","background":"transparent","lineHeight":"24px","fontSize":"16px","textOverflow":"ellipsis","fontWeight":"600"}' class="name">🍽️ {{item.caipumingcheng}}</div>
					<div :style='{"padding":"6px 12px","margin":"0 0 8px 0","whiteSpace":"nowrap","overflow":"hidden","color":"#667eea","borderRadius":"20px","background":"linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%)","lineHeight":"20px","fontSize":"13px","textOverflow":"ellipsis","display":"inline-block","border":"1px solid rgba(102, 126, 234, 0.2)"}' class="tag">{{item.caishileixing}}</div>
					<div :style='{"padding":"0","margin":"0","whiteSpace":"nowrap","overflow":"hidden","color":"#ff9800","borderRadius":"0","background":"transparent","lineHeight":"24px","fontSize":"14px","textOverflow":"ellipsis","fontWeight":"600"}' class="score">⭐ 分数: {{item.fenshu}}</div>
					<div v-if="item.price" :style='{"padding":"0px","margin":"8px 0 0 0","lineHeight":"24px","fontSize":"14px","color":"#d15858","textAlign":"right","fontWeight":"600"}' class="price"><span :style='{"fontSize":"12px"}'>¥</span>{{item.price}}</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 热门信息 -->
	<div class="hot" :style='{"border":"0","margin":"20px 0 0","borderRadius":"16px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","width":"100%","position":"relative","height":"auto","boxShadow":"0 8px 24px rgba(102, 126, 234, 0.12)"}'>
	  <div :style='{"padding":"0","margin":"0 auto","color":"#fff","textAlign":"center","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"100%","lineHeight":"56px","fontSize":"20px","height":"56px","borderRadius":"16px 16px 0 0","fontWeight":"600","letterSpacing":"1px"}'>🔥 热门推荐</div>
	  <div :style='{"padding":"24px","margin":"0","background":"transparent","display":"flex","width":"100%","justifyContent":"space-between","height":"auto","gap":"16px"}'>
	    <div v-for="(item, index) in hotList" :key="item.id || index" :style='{"cursor":"pointer","width":"23%","padding":"0","borderRadius":"16px","background":"#fff","height":"auto","overflow":"hidden","boxShadow":"0 4px 12px rgba(0,0,0,0.06)"}' @click="toDetail(item)" class="hot-item">
	      <img :style='{"width":"100%","margin":"0","objectFit":"cover","borderRadius":"0","display":"block","height":"240px"}' v-if="item.caipufengmian && item.caipufengmian.substr(0,4)=='http'" :src="item.caipufengmian" alt="">
	      <img :style='{"width":"100%","margin":"0","objectFit":"cover","borderRadius":"0","display":"block","height":"240px"}' v-else-if="item.caipufengmian" :src="baseUrl + item.caipufengmian.split(',')[0]" alt="">
	      <img :style='{"width":"100%","margin":"0","objectFit":"cover","borderRadius":"0","display":"block","height":"240px"}' v-else src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='100' height='100'%3E%3Crect fill='%23f0f0f0' width='100' height='100'/%3E%3Ctext fill='%23999' x='50%25' y='50%25' text-anchor='middle' dy='.3em'%3E暂无图片%3C/text%3E%3C/svg%3E" alt="">
	      <div :style='{"padding":"12px 16px 8px","margin":"0","color":"#333","borderRadius":"0","background":"#fff","lineHeight":"24px","fontSize":"16px","fontWeight":"600","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap"}'>🍽️ {{item.caipumingcheng}}</div>
	      <div :style='{"padding":"4px 16px","margin":"0","color":"#667eea","borderRadius":"0","background":"#fff","lineHeight":"20px","fontSize":"13px"}'>{{item.caishileixing}}</div>
	      <div :style='{"padding":"4px 16px 12px","margin":"0","color":"#ff9800","borderRadius":"0","background":"#fff","lineHeight":"20px","fontSize":"14px","fontWeight":"600"}'>⭐ {{item.fenshu}}</div>
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
      @size-change="sizeChange"
	  @prev-click="prevClick"
	  @next-click="nextClick"
	></el-pagination>

  </div>
</div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
	    layouts: '',
		swiperIndex: -1,
        baseUrl: '',
        breadcrumbItem: [
          {
            name: '菜谱信息'
          }
        ],
        formSearch: {
          caipumingcheng: '',
          caishileixing: '',
          pengrenfangshi: '',
        },
        fenlei: [],
        hotList: [],
        dataList: [],
        total: 1,
        pageSize: 12,
		pageSizes: [10,20,30,50],
        totalPage: 1,
        curFenlei: '全部',
        isPlain: false,
        indexQueryCondition: '',
	      caishileixingOptions: [],
        timeRange: [],
        // 新增推荐相关
        isLoggedIn: false,
        userId: null,
        recommendType: 'normal',
        refreshing: false
      }
    },
    created() {
      this.indexQueryCondition = this.$route.query.indexQueryCondition ? this.$route.query.indexQueryCondition : '';
      this.baseUrl = this.$config.baseUrl;
      
      // 检查登录状态
      this.isLoggedIn = !!localStorage.getItem('Token');
      this.userId = localStorage.getItem('userid');
      
      // 如果已登录，默认使用基于库存的推荐
      if (this.isLoggedIn) {
        this.recommendType = 'stock_based';
      }
      
      this.$http.get('option/caishileixing/caishileixing').then(res => {
        if (res.data.code == 0) {
          this.caishileixingOptions = res.data.data;
        }
      });
      this.getFenlei();
      this.getList(1, '全部');
      this.getHotList();
    },
    //方法集合
    methods: {
      add(path) {
        this.$router.push({path: path});
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
      getFenlei() {
        this.$http.get('option/caishileixing/caishileixing').then(res => {
          if (res.data.code == 0) {
            this.fenlei = res.data.data;
          }
        });
      },
      getList(page, fenlei, ref = '') {
		if(fenlei == '全部') this.swiperIndex = -1;
		for(let i=0;i<this.fenlei.length;i++) {
			if(fenlei == this.fenlei[i]) {
				this.swiperIndex = i;
				break;
			}
		}
        this.curFenlei = fenlei;
        if (this.curFenlei == '全部') {
          this.isPlain = false;
        } else {
          this.isPlain = true;
        }
        
        // 如果是智能推荐模式且已登录，使用推荐接口
        if (this.isLoggedIn && this.recommendType !== 'normal') {
          this.getRecommendList(page);
          return;
        }
        
        // 普通列表模式
        let params = {page, limit: this.pageSize};
        let searchWhere = {};
        if (this.formSearch.caipumingcheng != '') searchWhere.caipumingcheng = '%' + this.formSearch.caipumingcheng + '%';
        if (this.formSearch.caishileixing != '') searchWhere.caishileixing = this.formSearch.caishileixing;
        if (this.formSearch.pengrenfangshi != '') searchWhere.pengrenfangshi = '%' + this.formSearch.pengrenfangshi + '%';
        params['sort'] = 'fenshu';
        params['order'] = 'desc';
        if (this.curFenlei != '全部') searchWhere.caishileixing = this.curFenlei;
        this.$http.get('caipuxinxi/list', {params: Object.assign(params, searchWhere)}).then(res => {
          if (res.data.code == 0) {
            this.dataList = res.data.data.list;
            this.total = res.data.data.total;
            this.pageSize = res.data.data.pageSize;
            this.totalPage = res.data.data.totalPage;
			
			this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
          }
        });
      },
      
      // 获取智能推荐列表
      getRecommendList(page) {
        let params = {
          userId: this.userId,
          pageNum: page,
          pageSize: this.pageSize,
          recommendType: this.recommendType,
          sortType: 'score'
        };
        
        // 添加分类过滤
        if (this.curFenlei != '全部') {
          params.caishileixing = this.curFenlei;
        }
        
        // 添加搜索条件
        if (this.formSearch.caipumingcheng != '') {
          params.caipumingcheng = this.formSearch.caipumingcheng;
        }
        if (this.formSearch.caishileixing != '') {
          params.caishileixing = this.formSearch.caishileixing;
        }
        if (this.formSearch.pengrenfangshi != '') {
          params.pengrenfangshi = this.formSearch.pengrenfangshi;
        }
        
        this.$http.get('caipuxinxi/recommend', {params}).then(res => {
          if (res.data.code == 0) {
            this.dataList = res.data.data.list;
            this.total = res.data.data.total;
            this.pageSize = res.data.data.pageSize;
            this.totalPage = res.data.data.totalPage;
            
            this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
          } else {
            this.$message.warning(res.data.msg || '获取推荐失败，切换到普通模式');
            this.recommendType = 'normal';
            this.getList(page, this.curFenlei);
          }
        }).catch(err => {
          this.$message.error('推荐服务异常，切换到普通模式');
          this.recommendType = 'normal';
          this.getList(page, this.curFenlei);
        });
      },
      
      // 切换推荐类型
      changeRecommendType(type) {
        this.getList(1, this.curFenlei);
      },
      
      // 刷新推荐
      refreshRecommend() {
        this.refreshing = true;
        let params = {
          userId: this.userId,
          pageNum: 1,
          pageSize: this.pageSize,
          recommendType: this.recommendType,
          sortType: 'score',
          refresh: true  // 强制刷新缓存
        };
        
        if (this.curFenlei != '全部') {
          params.caishileixing = this.curFenlei;
        }
        
        // 添加搜索条件
        if (this.formSearch.caipumingcheng != '') {
          params.caipumingcheng = this.formSearch.caipumingcheng;
        }
        if (this.formSearch.caishileixing != '') {
          params.caishileixing = this.formSearch.caishileixing;
        }
        if (this.formSearch.pengrenfangshi != '') {
          params.pengrenfangshi = this.formSearch.pengrenfangshi;
        }
        
        this.$http.get('caipuxinxi/recommend', {params}).then(res => {
          this.refreshing = false;
          if (res.data.code == 0) {
            this.dataList = res.data.data.list;
            this.total = res.data.data.total;
            this.$message.success('推荐已刷新');
          } else {
            this.$message.error(res.data.msg || '刷新失败');
          }
        }).catch(err => {
          this.refreshing = false;
          this.$message.error('刷新失败：' + err.message);
        });
      },
      curChange(page) {
        this.getList(page,this.curFenlei);
      },
      prevClick(page) {
        this.getList(page,this.curFenlei);
      },
      sizeChange(size){
        this.pageSize = size
        this.getList(1,this.curFenlei);
      },
      nextClick(page) {
        this.getList(page,this.curFenlei);
      },
      toDetail(item) {
        this.$router.push({path: '/index/caipuxinxiDetail', query: {detailObj: JSON.stringify(item)}});
      },
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.list-preview .list-form-pv .el-input {
		width: auto;
	}
	
	.recommend-selector ::v-deep .el-radio-button__inner {
		background: rgba(255,255,255,0.15);
		border: 2px solid rgba(255,255,255,0.3);
		color: #fff;
		padding: 10px 20px;
		border-radius: 24px;
		font-weight: 600;
		font-size: 14px;
		transition: all 0.3s ease;
		box-shadow: 0 2px 6px rgba(0,0,0,0.1);
	}
	
	.recommend-selector ::v-deep .el-radio-button__inner:hover {
		background: rgba(255,255,255,0.25);
		border-color: rgba(255,255,255,0.5);
		transform: translateY(-2px);
		box-shadow: 0 4px 12px rgba(0,0,0,0.15);
	}
	
	.recommend-selector ::v-deep .el-radio-button__orig-radio:checked+.el-radio-button__inner {
		background: rgba(255,255,255,0.95);
		border-color: rgba(255,255,255,0.95);
		color: #667eea;
		box-shadow: 0 4px 12px rgba(0,0,0,0.2);
		font-weight: 700;
	}
	
	.recommend-selector ::v-deep .el-radio-button:first-child .el-radio-button__inner {
		border-left: 2px solid rgba(255,255,255,0.3);
		border-radius: 24px;
	}
	
	.recommend-selector ::v-deep .el-radio-button:last-child .el-radio-button__inner {
		border-radius: 24px;
	}
	
	.recommend-selector .el-button:hover {
		background: rgba(255,255,255,0.35) !important;
		border-color: rgba(255,255,255,0.6) !important;
		transform: translateY(-2px);
		box-shadow: 0 4px 12px rgba(0,0,0,0.2) !important;
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
	
	.category-1 .item {
		cursor: pointer;
		border: 2px solid transparent;
		border-radius: 24px;
		padding: 0 20px;
		margin: 0;
		color: #666;
		background: #fff;
		display: flex;
		min-width: 80px;
		font-size: 14px;
		line-height: 40px;
		justify-content: center;
		text-align: center;
		transition: all 0.3s ease;
		font-weight: 500;
		box-shadow: 0 2px 6px rgba(0,0,0,0.05);
	}
	
	.category-1 .item:hover {
		cursor: pointer;
		border: 2px solid rgba(102, 126, 234, 0.3);
		border-radius: 24px;
		padding: 0 20px;
		margin: 0;
		color: #667eea;
		background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
		min-width: 80px;
		font-size: 14px;
		line-height: 40px;
		text-align: center;
		transform: translateY(-2px);
		box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
	}
	
	.category-1 .item.active {
		cursor: pointer;
		border: 2px solid #667eea;
		border-radius: 24px;
		padding: 0 20px;
		margin: 0;
		color: #fff;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		min-width: 80px;
		font-size: 14px;
		line-height: 40px;
		text-align: center;
		font-weight: 600;
		box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
	}
	
	.category-2 .item {
		cursor: pointer;
		border-radius: 4px;
		margin: 0 0 10px 0;
		color: #999;
		background: #efefef;
		width: 100%;
		font-size: 14px;
		line-height: 36px;
		text-align: center;
	}
	
	.category-2 .item:hover {
		cursor: pointer;
		border-radius: 4px;
		margin: 0 0 10px 0;
		color: #999;
		background: #efefef;
		width: 100%;
		font-size: 14px;
		line-height: 36px;
		text-align: center;
	}
	
	.category-2 .item.active {
		cursor: pointer;
		border-radius: 4px;
		margin: 0 0 10px 0;
		color: #999;
		background: #efefef;
		width: 100%;
		font-size: 14px;
		line-height: 36px;
		text-align: center;
	}
	
	.list-form-pv .el-input ::v-deep .el-input__inner {
		border: 2px solid #e8e6f5;
		border-radius: 24px;
		padding: 0 20px;
		margin: 0;
		outline: none;
		color: #333;
		width: 180px;
		font-size: 14px;
		line-height: 42px;
		height: 42px;
		transition: all 0.3s ease;
		background: #fff;
	}
	
	.list-form-pv .el-input ::v-deep .el-input__inner:focus {
		border-color: #667eea;
		box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
	}
	
	.list-form-pv .el-select ::v-deep .el-input__inner {
		border: 2px solid #e8e6f5;
		border-radius: 24px;
		padding: 0 20px;
		margin: 0;
		outline: none;
		color: #333;
		width: 180px;
		font-size: 14px;
		line-height: 42px;
		height: 42px;
		transition: all 0.3s ease;
		background: #fff;
	}
	
	.list-form-pv .el-select ::v-deep .el-input__inner:focus {
		border-color: #667eea;
		box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
	}
	
	.list-form-pv .el-button:hover {
		transform: translateY(-2px);
		box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4) !important;
	}
	
	.list-form-pv .el-button:nth-child(2):hover {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
		color: #fff !important;
		border-color: #667eea !important;
	}
	
	.list-form-pv .el-date-editor ::v-deep .el-input__inner {
		border: 1px solid #dbd9f4;
		border-radius: 8px;
		padding: 0 30px;
		margin: 0;
		outline: none;
		color: #333;
		width: 140px;
		font-size: 14px;
		line-height: 42px;
		height: 42px;
	}
	
	.list .index-pv1 .animation-box {
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		z-index: initial;
	}
	
	.list .index-pv1 .animation-box:hover {
		transform: rotate(0) scale(1.02) skew(0deg, 0deg) translate3d(0px, -8px, 0px);
		-webkit-perspective: 1000px;
		perspective: 1000px;
		transition: 0.3s;
		z-index: 1;
		box-shadow: 0 12px 24px rgba(102, 126, 234, 0.15) !important;
	}
	
	.list .index-pv1 .animation-box img {
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		transition: 0.3s;
	}
	
	.list .index-pv1 .animation-box:hover img {
		transform: scale(1.05);
		-webkit-perspective: 1000px;
		perspective: 1000px;
		transition: 0.3s;
	}
	
	// 热门项目悬停效果
	.hot-item {
		transition: all 0.3s ease;
	}
	
	.hot-item:hover {
		transform: translateY(-6px);
		box-shadow: 0 12px 24px rgba(102, 126, 234, 0.2) !important;
	}
	
	.hot-item:hover img {
		transform: scale(1.05);
		transition: 0.3s;
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

