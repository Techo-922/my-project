<template>
<div>
	<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
			<el-breadcrumb-item>首页</el-breadcrumb-item>
			<el-breadcrumb-item>我的食材库</el-breadcrumb-item>
			<el-breadcrumb-item>食材详情</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="detail-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
		<el-card :style='{"border":"2px solid #dbd9f4","borderRadius":"8px","background":"#fff"}'>
			<div slot="header" class="clearfix">
				<span :style='{"fontSize":"20px","fontWeight":"bold"}'>{{detail.shicaiName}}</span>
				<el-tag :type="getStatusType(detail.status)" :style='{"marginLeft":"20px"}'>{{getStatusText(detail.status)}}</el-tag>
			</div>
			
			<el-descriptions :column="2" border>
				<el-descriptions-item label="食材名称">{{detail.shicaiName}}</el-descriptions-item>
				<el-descriptions-item label="数量">{{detail.quantity}} {{detail.unit}}</el-descriptions-item>
				<el-descriptions-item label="购买日期">{{formatDate(detail.purchaseDate)}}</el-descriptions-item>
				<el-descriptions-item label="过期日期">
					<span :style="getExpiryStyle(detail.expiryDate)">
						{{formatDate(detail.expiryDate)}}
						<span v-if="isExpiringSoon(detail.expiryDate)"> ⚠️ 即将过期</span>
					</span>
				</el-descriptions-item>
				<el-descriptions-item label="状态">
					<el-tag :type="getStatusType(detail.status)">{{getStatusText(detail.status)}}</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="添加时间">{{formatDateTime(detail.addtime)}}</el-descriptions-item>
			</el-descriptions>
			
			<div :style='{"marginTop":"30px","textAlign":"center"}'>
				<el-button type="primary" @click="edit()" :style='{"margin":"0 10px"}'>编辑</el-button>
				<el-button type="warning" @click="markAsUsed()" v-if="detail.status=='new'" :style='{"margin":"0 10px"}'>标记已使用</el-button>
				<el-button type="danger" @click="del()" :style='{"margin":"0 10px"}'>删除</el-button>
				<el-button @click="back()" :style='{"margin":"0 10px"}'>返回</el-button>
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
		this.getInfo();
	},
	methods: {
		getInfo() {
			this.$http.get(`usershicai/info/${this.id}`).then(res => {
				if (res.data && res.data.code === 0) {
					this.detail = res.data.data;
				}
			});
		},
		formatDate(date) {
			if (!date) return '';
			let d = new Date(date);
			return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
		},
		formatDateTime(date) {
			if (!date) return '';
			let d = new Date(date);
			return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0') + ' ' +
				   String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0') + ':' + String(d.getSeconds()).padStart(2, '0');
		},
		isExpiringSoon(expiryDate) {
			if (!expiryDate) return false;
			let now = new Date();
			let expiry = new Date(expiryDate);
			let diff = expiry - now;
			let days = diff / (1000 * 60 * 60 * 24);
			return days > 0 && days <= 7;
		},
		getExpiryStyle(expiryDate) {
			if (this.isExpiringSoon(expiryDate)) {
				return {'color': '#f56c6c', 'fontWeight': 'bold'};
			}
			return {};
		},
		getStatusType(status) {
			const types = {'new': 'success', 'used': 'info', 'expired': 'danger', 'discarded': 'warning'};
			return types[status] || '';
		},
		getStatusText(status) {
			const texts = {'new': '新鲜', 'used': '已使用', 'expired': '已过期', 'discarded': '已丢弃'};
			return texts[status] || status;
		},
		markAsUsed() {
			this.$confirm('确定标记为已使用吗?', '提示', {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'}).then(() => {
				this.$http.post('usershicai/update', {id: this.detail.id, status: 'used'}).then(res => {
					if (res.data && res.data.code === 0) {
						this.$message.success('操作成功');
						this.getInfo();
					}
				});
			});
		},
		del() {
			this.$confirm('确定删除吗?', '提示', {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'}).then(() => {
				this.$http.post('usershicai/delete', [this.id]).then(res => {
					if (res.data && res.data.code === 0) {
						this.$message.success('删除成功');
						this.$router.push('/index/usershicaiList');
					}
				});
			});
		},
		edit() {
			this.$router.push({path: '/index/usershicaiAdd', query: {id: this.id}});
		},
		back() {
			this.$router.go(-1);
		}
	}
}
</script>
