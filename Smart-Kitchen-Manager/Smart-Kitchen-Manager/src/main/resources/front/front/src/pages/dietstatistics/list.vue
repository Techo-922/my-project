<template>
<div>
	<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
			<el-breadcrumb-item>首页</el-breadcrumb-item>
			<el-breadcrumb-item>饮食统计</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="list-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
		<!-- 统计卡片 -->
		<div class="stats-cards" :style='{"display":"flex","justifyContent":"space-between","marginBottom":"20px","flexWrap":"wrap"}'>
			<div class="stat-card" :style='{"flex":"1","padding":"20px","margin":"10px","borderRadius":"8px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","color":"#fff","textAlign":"center","minWidth":"200px"}'>
				<div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{totalRecords}}</div>
				<div :style='{"fontSize":"14px","marginTop":"8px"}'>总记录数</div>
			</div>
			<div class="stat-card" :style='{"flex":"1","padding":"20px","margin":"10px","borderRadius":"8px","background":"linear-gradient(135deg, #f093fb 0%, #f5576c 100%)","color":"#fff","textAlign":"center","minWidth":"200px"}'>
				<div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{totalCalories}}</div>
				<div :style='{"fontSize":"14px","marginTop":"8px"}'>总卡路里 (kcal)</div>
			</div>
			<div class="stat-card" :style='{"flex":"1","padding":"20px","margin":"10px","borderRadius":"8px","background":"linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)","color":"#fff","textAlign":"center","minWidth":"200px"}'>
				<div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{avgCalories}}</div>
				<div :style='{"fontSize":"14px","marginTop":"8px"}'>平均卡路里</div>
			</div>
		</div>

		<!-- 搜索表单 -->
		<el-form :inline="true" :model="formSearch" class="list-form-pv" :style='{"border":"2px solid #dbd9f4","padding":"10px","margin":"10px 0 0 0","alignItems":"center","borderRadius":"8px","flexWrap":"wrap","background":"#f8f8fc","display":"flex","width":"100%","height":"auto"}'>
			<el-form-item :style='{"margin":"10px"}'>
				<div class="lable" v-if="true" :style='{"width":"auto","padding":"0 10px","lineHeight":"42px","display":"inline-block"}'>日期范围</div>
				<el-date-picker
					v-model="dateRange"
					type="daterange"
					range-separator="至"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					value-format="yyyy-MM-dd">
				</el-date-picker>
			</el-form-item>
			<el-button :style='{"cursor":"pointer","border":"0","padding":"0px 15px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}' type="primary" @click="getList(1)"><i :style='{"color":"#666","margin":"0 10px 0 0","fontSize":"14px"}' class="el-icon-search"></i>查询</el-button>
			<el-button :style='{"cursor":"pointer","border":"0","padding":"0px 15px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}' type="primary" @click="add()"><i :style='{"color":"#666","margin":"0 10px 0 0","fontSize":"14px"}' class="el-icon-circle-plus-outline"></i>添加记录</el-button>
		</el-form>

		<!-- 饮食记录列表 -->
		<div class="list" :style='{"border":"2px solid #dbd9f4","margin":"20px 0 0 0","borderRadius":"8px","background":"#fff"}'>
			<el-table :data="dataList" style="width: 100%" :header-cell-style="{'background':'#f8f8fc','color':'#333'}">
				<el-table-column prop="recordDate" label="日期" width="180">
					<template slot-scope="scope">
						{{formatDate(scope.row.recordDate)}}
					</template>
				</el-table-column>
				<el-table-column prop="mealType" label="餐次" width="120">
					<template slot-scope="scope">
						<el-tag :type="getMealTypeColor(scope.row.mealType)">{{getMealTypeText(scope.row.mealType)}}</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="foodName" label="食物名称" width="200"></el-table-column>
				<el-table-column prop="calories" label="卡路里 (kcal)" width="150">
					<template slot-scope="scope">
						<span :style='{"fontWeight":"bold","color":"#f56c6c"}'>{{scope.row.calories}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="protein" label="蛋白质 (g)" width="120"></el-table-column>
				<el-table-column prop="carbs" label="碳水 (g)" width="120"></el-table-column>
				<el-table-column prop="fat" label="脂肪 (g)" width="120"></el-table-column>
				<el-table-column label="操作" width="250" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" @click="toDetail(scope.row)">详情</el-button>
						<el-button size="mini" type="primary" @click="edit(scope.row)">编辑</el-button>
						<el-button size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<el-pagination
			background
			class="pagination"
			:pager-count="7"
			:page-size="pageSize"
			:page-sizes="[10, 20, 50, 100]"
			prev-text="<"
			next-text=">"
			:hide-on-single-page="false"
			layout="total, prev, pager, next, sizes, jumper"
			:total="total"
			:style='{"width":"1200px","padding":"0","margin":"20px auto","whiteSpace":"nowrap","color":"#333","fontWeight":"500"}'
			@current-change="curChange"
			@size-change="sizeChange"
		></el-pagination>
	</div>
</div>
</template>

<script>
export default {
	data() {
		return {
			baseUrl: '',
			dataList: [],
			formSearch: {},
			dateRange: [],
			pageSize: 10,
			total: 0,
			totalRecords: 0,
			totalCalories: 0,
			avgCalories: 0
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.getStats();
		this.getList(1);
	},
	methods: {
		// 获取统计数据
		getStats() {
			let userid = this.$storage.get('userid');
			this.$http.get(`dietstatistics/listByUser?userid=${userid}`).then(res => {
				if (res.data && res.data.code === 0) {
					let list = res.data.data;
					this.totalRecords = list.length;
					this.totalCalories = list.reduce((sum, item) => sum + (item.calories || 0), 0);
					this.avgCalories = this.totalRecords > 0 ? Math.round(this.totalCalories / this.totalRecords) : 0;
				}
			});
		},
		// 获取列表
		getList(page) {
			let userid = this.$storage.get('userid');
			let params = {
				page: page || 1,
				limit: this.pageSize,
				userid: userid
			};
			
			if (this.dateRange && this.dateRange.length === 2) {
				params.startDate = this.dateRange[0];
				params.endDate = this.dateRange[1];
			}
			
			this.$http.get('dietstatistics/page', {params}).then(res => {
				if (res.data && res.data.code === 0) {
					this.dataList = res.data.data.list;
					this.total = res.data.data.total;
				}
			});
		},
		// 格式化日期
		formatDate(date) {
			if (!date) return '';
			let d = new Date(date);
			return d.getFullYear() + '-' + 
				   String(d.getMonth() + 1).padStart(2, '0') + '-' + 
				   String(d.getDate()).padStart(2, '0');
		},
		// 获取餐次颜色
		getMealTypeColor(mealType) {
			const colors = {
				'breakfast': 'success',
				'lunch': 'warning',
				'dinner': 'danger',
				'snack': 'info'
			};
			return colors[mealType] || '';
		},
		// 获取餐次文本
		getMealTypeText(mealType) {
			const texts = {
				'breakfast': '早餐',
				'lunch': '午餐',
				'dinner': '晚餐',
				'snack': '加餐'
			};
			return texts[mealType] || mealType;
		},
		// 删除
		del(id) {
			this.$confirm('确定删除吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http.post('dietstatistics/delete', [id]).then(res => {
					if (res.data && res.data.code === 0) {
						this.$message.success('删除成功');
						this.getStats();
						this.getList();
					}
				});
			});
		},
		// 添加
		add() {
			this.$router.push('/index/dietStatisticsAdd');
		},
		// 编辑
		edit(row) {
			this.$router.push({path: '/index/dietStatisticsAdd', query: {id: row.id}});
		},
		// 详情
		toDetail(row) {
			this.$router.push({path: '/index/dietStatisticsDetail', query: {id: row.id}});
		},
		// 分页
		curChange(page) {
			this.getList(page);
		},
		sizeChange(size) {
			this.pageSize = size;
			this.getList(1);
		}
	}
}
</script>

<style scoped>
.stats-cards {
	display: flex;
	justify-content: space-between;
	margin-bottom: 20px;
}
</style>
