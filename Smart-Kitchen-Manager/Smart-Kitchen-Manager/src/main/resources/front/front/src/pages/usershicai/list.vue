<template>
<div>
	<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
		<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
			<el-breadcrumb-item>首页</el-breadcrumb-item>
			<el-breadcrumb-item>我的食材库</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="list-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
		<!-- 统计卡片 -->
		<div class="stats-cards" :style='{"display":"flex","justifyContent":"space-between","marginBottom":"20px"}'>
			<div class="stat-card" :style='{"flex":"1","padding":"20px","margin":"0 10px","borderRadius":"8px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","color":"#fff","textAlign":"center"}'>
				<div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{totalCount}}</div>
				<div :style='{"fontSize":"14px","marginTop":"8px"}'>食材总数</div>
			</div>
			<div class="stat-card" :style='{"flex":"1","padding":"20px","margin":"0 10px","borderRadius":"8px","background":"linear-gradient(135deg, #f093fb 0%, #f5576c 100%)","color":"#fff","textAlign":"center"}'>
				<div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{expiringSoonCount}}</div>
				<div :style='{"fontSize":"14px","marginTop":"8px"}'>即将过期</div>
			</div>
			<div class="stat-card" :style='{"flex":"1","padding":"20px","margin":"0 10px","borderRadius":"8px","background":"linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)","color":"#fff","textAlign":"center"}'>
				<div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{newCount}}</div>
				<div :style='{"fontSize":"14px","marginTop":"8px"}'>新鲜食材</div>
			</div>
		</div>

		<!-- 过期提醒横幅 -->
		<el-alert
			v-if="unreadReminders.length > 0"
			:title="`您有 ${unreadReminders.length} 条未读提醒`"
			type="warning"
			:closable="false"
			:style='{"marginBottom":"20px","borderRadius":"8px"}'
			show-icon>
			<div slot="default">
				<div v-for="(reminder, index) in unreadReminders.slice(0, 3)" :key="reminder.id" :style='{"padding":"5px 0","borderBottom":index < Math.min(2, unreadReminders.length - 1) ? "1px dashed #e6a23c" : "none"}'>
					<span :style='{"fontWeight":"bold"}'>{{reminder.shicaiName}}</span>
					<span :style='{"margin":"0 10px","color":"#909399"}'>|</span>
					<span>{{formatRemindDate(reminder.expiryDate)}}</span>
					<el-button type="text" size="mini" @click="markReminderAsRead(reminder.id)" :style='{"marginLeft":"10px","color":"#409eff"}'>标记已读</el-button>
				</div>
				<div v-if="unreadReminders.length > 3" :style='{"marginTop":"10px","textAlign":"center"}'>
					<el-button type="text" size="small" @click="viewAllReminders">查看全部 {{unreadReminders.length}} 条提醒 >></el-button>
				</div>
			</div>
		</el-alert>

		<!-- 状态筛选 -->
		<div class="category-1" :style='{"padding":"10px","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"2px 1px 1px 1px","display":"flex","width":"100%","borderStyle":"solid","height":"auto"}'>
			<div class="item" :class="statusFilter == '' ? 'active' : ''" @click="filterByStatus('')">全部</div>
			<div class="item" :class="statusFilter == 'new' ? 'active' : ''" @click="filterByStatus('new')">新鲜</div>
			<div class="item" :class="statusFilter == 'used' ? 'active' : ''" @click="filterByStatus('used')">已使用</div>
			<div class="item" :class="statusFilter == 'expired' ? 'active' : ''" @click="filterByStatus('expired')">已过期</div>
			<div class="item" :class="statusFilter == 'expiring' ? 'active' : ''" @click="filterByStatus('expiring')">⚠️ 即将过期</div>
		</div>
		
		<!-- 搜索表单 -->
		<el-form :inline="true" :model="formSearch" class="list-form-pv" :style='{"border":"2px solid #dbd9f4","padding":"10px","margin":"10px 0 0 0","alignItems":"center","borderRadius":"8px","flexWrap":"wrap","background":"#f8f8fc","display":"flex","width":"100%","height":"auto"}'>
			<el-form-item :style='{"margin":"10px"}'>
				<div class="lable" v-if="true" :style='{"width":"auto","padding":"0 10px","lineHeight":"42px","display":"inline-block"}'>食材名称</div>
				<el-input v-model="formSearch.shicaiName" placeholder="食材名称" clearable></el-input>
			</el-form-item>
			<el-button :style='{"cursor":"pointer","border":"0","padding":"0px 15px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}' type="primary" @click="getList(1)"><i :style='{"color":"#666","margin":"0 10px 0 0","fontSize":"14px"}' class="el-icon-search"></i>查询</el-button>
			<el-button :style='{"cursor":"pointer","border":"0","padding":"0px 15px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px"}' type="primary" @click="add()"><i :style='{"color":"#666","margin":"0 10px 0 0","fontSize":"14px"}' class="el-icon-circle-plus-outline"></i>添加食材</el-button>
			<el-button :style='{"cursor":"pointer","border":"0","padding":"0px 15px","margin":"0 10px 0 0","outline":"none","color":"#666","borderRadius":"6px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"auto","fontSize":"14px","lineHeight":"42px","height":"42px","color":"#fff"}' type="primary" @click="receiptScan()"><i :style='{"color":"#fff","margin":"0 10px 0 0","fontSize":"14px"}' class="el-icon-camera"></i>小票识别</el-button>
		</el-form>

		<!-- 食材列表 -->
		<div class="list" :style='{"border":"2px solid #dbd9f4","margin":"20px 0 0 0","borderRadius":"8px","background":"#fff"}'>
			<el-table :data="dataList" style="width: 100%" :header-cell-style="{'background':'#f8f8fc','color':'#333'}">
				<el-table-column prop="shicaiName" label="食材名称" width="180">
					<template slot-scope="scope">
						<span :style='{"fontSize":"16px","fontWeight":"bold"}'>{{scope.row.shicaiName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="quantity" label="数量" width="120">
					<template slot-scope="scope">
						{{scope.row.quantity}} {{scope.row.unit}}
					</template>
				</el-table-column>
				<el-table-column prop="purchaseDate" label="购买日期" width="180">
					<template slot-scope="scope">
						{{formatDate(scope.row.purchaseDate)}}
					</template>
				</el-table-column>
				<el-table-column prop="expiryDate" label="过期日期" width="200">
					<template slot-scope="scope">
						<span :style="getExpiryStyle(scope.row.expiryDate)">
							{{formatDate(scope.row.expiryDate)}}
							<span v-if="isExpiringSoon(scope.row.expiryDate)"> ⚠️</span>
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="100">
					<template slot-scope="scope">
						<el-tag :type="getStatusType(scope.row.status)">{{getStatusText(scope.row.status)}}</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="380">
					<template slot-scope="scope">
						<div :style='{"display":"flex","flexWrap":"wrap","gap":"5px"}'>
							<el-button size="mini" @click="toDetail(scope.row)">详情</el-button>
							<el-button size="mini" type="primary" @click="edit(scope.row)">编辑</el-button>
							<el-button size="mini" type="warning" @click="markAsUsed(scope.row)" v-if="scope.row.status=='new'">标记已使用</el-button>
							<el-button size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
						</div>
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
			formSearch: {
				shicaiName: ''
			},
			statusFilter: '',
			pageSize: 10,
			total: 0,
			totalCount: 0,
			expiringSoonCount: 0,
			newCount: 0,
			unreadReminders: []
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.getStats();
		this.getUnreadReminders();
		this.getList(1);
	},
	methods: {
		// 获取统计数据
		getStats() {
			let userid = this.$storage.get('userid');
			this.$http.get(`usershicai/listByUser?userid=${userid}`).then(res => {
				if (res.data && res.data.code === 0) {
					let list = res.data.data;
					this.totalCount = list.length;
					this.newCount = list.filter(item => item.status === 'new').length;
					this.expiringSoonCount = list.filter(item => this.isExpiringSoon(item.expiryDate)).length;
				}
			});
		},
		// 获取未读提醒
		getUnreadReminders() {
			let userid = this.$storage.get('userid');
			this.$http.get('expiryreminder/page', {
				params: {
					page: 1,
					limit: 10,
					userid: userid,
					status: 'pending'
				}
			}).then(res => {
				if (res.data && res.data.code === 0) {
					this.unreadReminders = res.data.data.list || [];
				}
			});
		},
		// 标记提醒为已读
		markReminderAsRead(reminderId) {
			let userid = this.$storage.get('userid');
			this.$http.post('expiryreminder/markAsRead', null, {
				params: {
					id: reminderId,
					userid: userid
				}
			}).then(res => {
				if (res.data && res.data.code === 0) {
					this.$message.success('已标记为已读');
					this.getUnreadReminders();
				} else {
					this.$message.error(res.data.msg || '操作失败');
				}
			});
		},
		// 查看所有提醒
		viewAllReminders() {
			this.$alert('提醒列表功能开发中...', '提示', {
				confirmButtonText: '确定'
			});
		},
		// 格式化提醒日期
		formatRemindDate(date) {
			if (!date) return '';
			let d = new Date(date);
			let now = new Date();
			let diff = d - now;
			let days = Math.ceil(diff / (1000 * 60 * 60 * 24));
			
			if (days < 0) {
				return '已过期';
			} else if (days === 0) {
				return '今天过期';
			} else if (days === 1) {
				return '明天过期';
			} else if (days <= 3) {
				return `${days}天后过期`;
			} else {
				return this.formatDate(date);
			}
		},
		// 获取列表
		getList(page) {
			let userid = this.$storage.get('userid');
			let params = {
				page: page || 1,
				limit: this.pageSize,
				userid: userid
			};
			if (this.formSearch.shicaiName) {
				params.shicaiName = this.formSearch.shicaiName;
			}
			if (this.statusFilter && this.statusFilter !== 'expiring') {
				params.status = this.statusFilter;
			}
			
			if (this.statusFilter === 'expiring') {
				// 查询即将过期的食材
				this.$http.get(`usershicai/listExpiringSoon?userid=${userid}`).then(res => {
					if (res.data && res.data.code === 0) {
						this.dataList = res.data.data;
						this.total = this.dataList.length;
					}
				});
			} else {
				this.$http.get('usershicai/page', {params}).then(res => {
					if (res.data && res.data.code === 0) {
						this.dataList = res.data.data.list;
						this.total = res.data.data.total;
					}
				});
			}
		},
		// 按状态筛选
		filterByStatus(status) {
			this.statusFilter = status;
			this.getList(1);
		},
		// 格式化日期
		formatDate(date) {
			if (!date) return '';
			let d = new Date(date);
			return d.getFullYear() + '-' + 
				   String(d.getMonth() + 1).padStart(2, '0') + '-' + 
				   String(d.getDate()).padStart(2, '0');
		},
		// 判断是否即将过期（3天内）
		isExpiringSoon(expiryDate) {
			if (!expiryDate) return false;
			let now = new Date();
			let expiry = new Date(expiryDate);
			let diff = expiry - now;
			let days = diff / (1000 * 60 * 60 * 24);
			return days > 0 && days <= 3;
		},
		// 获取过期日期样式
		getExpiryStyle(expiryDate) {
			if (this.isExpiringSoon(expiryDate)) {
				return {'color': '#f56c6c', 'fontWeight': 'bold'};
			}
			return {};
		},
		// 获取状态类型
		getStatusType(status) {
			const types = {
				'new': 'success',
				'used': 'info',
				'expired': 'danger',
				'discarded': 'warning'
			};
			return types[status] || '';
		},
		// 获取状态文本
		getStatusText(status) {
			const texts = {
				'new': '新鲜',
				'used': '已使用',
				'expired': '已过期',
				'discarded': '已丢弃'
			};
			return texts[status] || status;
		},
		// 标记为已使用
		markAsUsed(row) {
			this.$confirm('确定标记为已使用吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http.post('usershicai/update', {
					id: row.id,
					status: 'used'
				}).then(res => {
					if (res.data && res.data.code === 0) {
						this.$message.success('操作成功');
						this.getStats();
						this.getList();
					}
				});
			});
		},
		// 删除
		del(id) {
			this.$confirm('确定删除吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http.post('usershicai/delete', [id]).then(res => {
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
			this.$router.push('/index/usershicaiAdd');
		},
		// 小票识别
		receiptScan() {
			this.$router.push('/index/usershicaiReceipt');
		},
		// 编辑
		edit(row) {
			this.$router.push({path: '/index/usershicaiAdd', query: {id: row.id}});
		},
		// 详情
		toDetail(row) {
			this.$router.push({path: '/index/usershicaiDetail', query: {id: row.id}});
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
.category-1 .item {
	padding: 10px 20px;
	margin: 0 10px;
	cursor: pointer;
	border-radius: 6px;
	transition: all 0.3s;
}
.category-1 .item:hover {
	background: #f0f0f0;
}
.category-1 .item.active {
	background: linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%);
	color: #666;
	font-weight: bold;
}
</style>
