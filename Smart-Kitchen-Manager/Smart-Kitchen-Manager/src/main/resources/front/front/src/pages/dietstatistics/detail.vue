<template>
<div>
	<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
			<el-breadcrumb-item>首页</el-breadcrumb-item>
			<el-breadcrumb-item>饮食统计</el-breadcrumb-item>
			<el-breadcrumb-item>详情</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="detail-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
		<el-card :style='{"border":"2px solid #dbd9f4","borderRadius":"8px","background":"#fff"}'>
			<div slot="header" class="clearfix">
				<span :style='{"fontSize":"20px","fontWeight":"bold","color":"#333"}'>饮食记录详情</span>
			</div>
			
			<div class="detail-content">
				<el-row :gutter="20">
					<el-col :span="12">
						<div class="detail-item">
							<div class="label">日期：</div>
							<div class="value">{{formatDate(detail.recordDate)}}</div>
						</div>
					</el-col>
					<el-col :span="12">
						<div class="detail-item">
							<div class="label">餐次：</div>
							<div class="value">
								<el-tag :type="getMealTypeColor(detail.mealType)">{{getMealTypeText(detail.mealType)}}</el-tag>
							</div>
						</div>
					</el-col>
				</el-row>
				
				<el-row :gutter="20">
					<el-col :span="24">
						<div class="detail-item">
							<div class="label">食物名称：</div>
							<div class="value" :style='{"fontSize":"18px","fontWeight":"bold","color":"#333"}'>{{detail.foodName}}</div>
						</div>
					</el-col>
				</el-row>
				
				<el-divider></el-divider>
				
				<div :style='{"fontSize":"16px","fontWeight":"bold","color":"#333","marginBottom":"20px"}'>营养成分</div>
				
				<el-row :gutter="20">
					<el-col :span="6">
						<div class="nutrition-card" :style='{"padding":"20px","borderRadius":"8px","background":"linear-gradient(135deg, #f093fb 0%, #f5576c 100%)","color":"#fff","textAlign":"center"}'>
							<div :style='{"fontSize":"28px","fontWeight":"bold"}'>{{detail.calories}}</div>
							<div :style='{"fontSize":"14px","marginTop":"8px"}'>卡路里 (kcal)</div>
						</div>
					</el-col>
					<el-col :span="6">
						<div class="nutrition-card" :style='{"padding":"20px","borderRadius":"8px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","color":"#fff","textAlign":"center"}'>
							<div :style='{"fontSize":"28px","fontWeight":"bold"}'>{{detail.protein}}</div>
							<div :style='{"fontSize":"14px","marginTop":"8px"}'>蛋白质 (g)</div>
						</div>
					</el-col>
					<el-col :span="6">
						<div class="nutrition-card" :style='{"padding":"20px","borderRadius":"8px","background":"linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)","color":"#fff","textAlign":"center"}'>
							<div :style='{"fontSize":"28px","fontWeight":"bold"}'>{{detail.carbs}}</div>
							<div :style='{"fontSize":"14px","marginTop":"8px"}'>碳水化合物 (g)</div>
						</div>
					</el-col>
					<el-col :span="6">
						<div class="nutrition-card" :style='{"padding":"20px","borderRadius":"8px","background":"linear-gradient(135deg, #fa709a 0%, #fee140 100%)","color":"#fff","textAlign":"center"}'>
							<div :style='{"fontSize":"28px","fontWeight":"bold"}'>{{detail.fat}}</div>
							<div :style='{"fontSize":"14px","marginTop":"8px"}'>脂肪 (g)</div>
						</div>
					</el-col>
				</el-row>
				
				<el-divider></el-divider>
				
				<el-row :gutter="20" v-if="detail.notes">
					<el-col :span="24">
						<div class="detail-item">
							<div class="label">备注：</div>
							<div class="value">{{detail.notes}}</div>
						</div>
					</el-col>
				</el-row>
				
				<div :style='{"marginTop":"30px","textAlign":"center"}'>
					<el-button @click="back()" :style='{"cursor":"pointer","border":"0","padding":"0px 30px","margin":"0 10px","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}'>返回</el-button>
					<el-button type="primary" @click="edit()" :style='{"cursor":"pointer","border":"0","padding":"0px 30px","margin":"0 10px","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}'>编辑</el-button>
				</div>
			</div>
		</el-card>
	</div>
</div>
</template>

<script>
export default {
	data() {
		return {
			id: '',
			detail: {}
		}
	},
	created() {
		this.id = this.$route.query.id;
		if (this.id) {
			this.getInfo();
		}
	},
	methods: {
		// 获取详情
		getInfo() {
			this.$http.get(`dietstatistics/info/${this.id}`).then(res => {
				if (res.data && res.data.code === 0) {
					this.detail = res.data.data;
				}
			});
		},
		// 格式化日期
		formatDate(date) {
			if (!date) return '';
			let d = new Date(date);
			return d.getFullYear() + '-' + 
				   String(d.getMonth() + 1).padStart(2, '0') + '-' + 
				   String(d.getDate()).padStart(2, '0') + ' ' +
				   String(d.getHours()).padStart(2, '0') + ':' +
				   String(d.getMinutes()).padStart(2, '0');
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
		// 编辑
		edit() {
			this.$router.push({path: '/index/dietStatisticsAdd', query: {id: this.id}});
		},
		// 返回
		back() {
			this.$router.go(-1);
		}
	}
}
</script>

<style scoped>
.detail-content {
	padding: 20px;
}
.detail-item {
	display: flex;
	margin-bottom: 20px;
	align-items: center;
}
.detail-item .label {
	font-weight: bold;
	color: #666;
	min-width: 120px;
}
.detail-item .value {
	color: #333;
	font-size: 16px;
}
.nutrition-card {
	margin-bottom: 20px;
}
</style>
