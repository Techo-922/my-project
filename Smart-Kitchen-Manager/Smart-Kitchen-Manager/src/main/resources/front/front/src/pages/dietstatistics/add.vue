<template>
<div>
	<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
			<el-breadcrumb-item>首页</el-breadcrumb-item>
			<el-breadcrumb-item>饮食统计</el-breadcrumb-item>
			<el-breadcrumb-item>{{id ? '编辑' : '添加'}}记录</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="add-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
		<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm" :style='{"border":"2px solid #dbd9f4","padding":"40px 20px","borderRadius":"8px","background":"#fff"}'>
			
			<el-form-item label="日期" prop="recordDate">
				<el-date-picker
					v-model="ruleForm.recordDate"
					type="datetime"
					placeholder="选择日期"
					value-format="yyyy-MM-dd HH:mm:ss"
					:style='{"width":"100%"}'>
				</el-date-picker>
			</el-form-item>
			
			<el-form-item label="餐次" prop="mealType">
				<el-radio-group v-model="ruleForm.mealType">
					<el-radio label="breakfast">早餐</el-radio>
					<el-radio label="lunch">午餐</el-radio>
					<el-radio label="dinner">晚餐</el-radio>
					<el-radio label="snack">加餐</el-radio>
				</el-radio-group>
			</el-form-item>
			
			<el-form-item label="食物名称" prop="foodName">
				<el-input v-model="ruleForm.foodName" placeholder="请输入食物名称" clearable></el-input>
			</el-form-item>
			
			<el-form-item label="卡路里 (kcal)" prop="calories">
				<el-input-number v-model="ruleForm.calories" :min="0" :max="99999" label="卡路里"></el-input-number>
			</el-form-item>
			
			<el-form-item label="蛋白质 (g)" prop="protein">
				<el-input-number v-model="ruleForm.protein" :min="0" :max="9999" :precision="1" label="蛋白质"></el-input-number>
			</el-form-item>
			
			<el-form-item label="碳水化合物 (g)" prop="carbs">
				<el-input-number v-model="ruleForm.carbs" :min="0" :max="9999" :precision="1" label="碳水化合物"></el-input-number>
			</el-form-item>
			
			<el-form-item label="脂肪 (g)" prop="fat">
				<el-input-number v-model="ruleForm.fat" :min="0" :max="9999" :precision="1" label="脂肪"></el-input-number>
			</el-form-item>
			
			<el-form-item label="备注" prop="notes">
				<el-input type="textarea" v-model="ruleForm.notes" placeholder="请输入备注" :rows="4"></el-input>
			</el-form-item>
			
			<el-form-item>
				<el-button type="primary" @click="submitForm('ruleForm')" :style='{"cursor":"pointer","border":"0","padding":"0px 30px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}'>提交</el-button>
				<el-button @click="resetForm('ruleForm')" :style='{"cursor":"pointer","border":"0","padding":"0px 30px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"#f0f0f0","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}'>重置</el-button>
				<el-button @click="back()" :style='{"cursor":"pointer","border":"0","padding":"0px 30px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"#f0f0f0","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}'>返回</el-button>
			</el-form-item>
		</el-form>
	</div>
</div>
</template>

<script>
export default {
	data() {
		return {
			id: '',
			ruleForm: {
				recordDate: '',
				mealType: 'breakfast',
				foodName: '',
				calories: 0,
				protein: 0,
				carbs: 0,
				fat: 0,
				notes: ''
			},
			rules: {
				recordDate: [
					{ required: true, message: '请选择日期', trigger: 'change' }
				],
				mealType: [
					{ required: true, message: '请选择餐次', trigger: 'change' }
				],
				foodName: [
					{ required: true, message: '请输入食物名称', trigger: 'blur' }
				],
				calories: [
					{ required: true, message: '请输入卡路里', trigger: 'blur' }
				]
			}
		}
	},
	created() {
		this.id = this.$route.query.id;
		if (this.id) {
			this.getInfo();
		} else {
			// 默认设置日期为当前时间
			this.ruleForm.recordDate = this.formatDateTime(new Date());
		}
	},
	methods: {
		// 获取详情
		getInfo() {
			this.$http.get(`dietstatistics/info/${this.id}`).then(res => {
				if (res.data && res.data.code === 0) {
					this.ruleForm = res.data.data;
				}
			});
		},
		// 提交表单
		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					let userid = this.$storage.get('userid');
					this.ruleForm.userid = userid;
					
					let url = this.id ? 'dietstatistics/update' : 'dietstatistics/save';
					this.$http.post(url, this.ruleForm).then(res => {
						if (res.data && res.data.code === 0) {
							this.$message.success(this.id ? '修改成功' : '添加成功');
							this.$router.push('/index/dietStatisticsList');
						} else {
							this.$message.error(res.data.msg);
						}
					});
				} else {
					return false;
				}
			});
		},
		// 重置表单
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		// 返回
		back() {
			this.$router.go(-1);
		},
		// 格式化日期时间
		formatDateTime(date) {
			let d = new Date(date);
			return d.getFullYear() + '-' + 
				   String(d.getMonth() + 1).padStart(2, '0') + '-' + 
				   String(d.getDate()).padStart(2, '0') + ' ' +
				   String(d.getHours()).padStart(2, '0') + ':' +
				   String(d.getMinutes()).padStart(2, '0') + ':' +
				   String(d.getSeconds()).padStart(2, '0');
		}
	}
}
</script>

<style scoped>
.el-form {
	max-width: 800px;
	margin: 0 auto;
}
</style>
