<template>
	<div class="navbar">
		<div class="title" :style='{"display":"block"}'>
			<el-image v-if="false" class="title-img" :style='{"width":"44px","objectFit":"cover","borderRadius":"100%","float":"left","height":"44px"}' src="http://codegen.caihongy.cn/20201114/7856ba26477849ea828f481fa2773a95.jpg" fit="cover" />
			<span class="title-name" :style='{"padding":"0 0 0 12px","lineHeight":"44px","fontSize":"20px","color":"#333","float":"left","fontWeight":"600"}'>{{this.$project.projectName}}</span>
		</div>
		<div class="right" :style='{"position":"absolute","right":"20px","top":"12px","display":"flex","alignItems":"center"}'>
			<div :style='{"cursor":"pointer","margin":"0 15px","padding":"8px 16px","borderRadius":"20px","background":"rgba(171, 133, 211, 0.1)","lineHeight":"1","color":"#ab85d3","fontSize":"14px","fontWeight":"500"}' class="nickname">{{this.$storage.get('role')}} {{this.$storage.get('adminName')}}</div>
			<div :style='{"cursor":"pointer","margin":"0 10px","padding":"8px 16px","borderRadius":"20px","background":"rgba(76, 72, 147, 0.1)","lineHeight":"1","color":"#4c4893","fontSize":"14px","transition":"all 0.3s ease"}' v-if="this.$storage.get('role')!='管理员'" class="logout" @click="onIndexTap">退出到前台</div>
			<div :style='{"cursor":"pointer","margin":"0 5px","padding":"8px 16px","borderRadius":"20px","background":"rgba(245, 87, 108, 0.1)","lineHeight":"1","color":"#f5576c","fontSize":"14px","transition":"all 0.3s ease"}' class="logout" @click="onLogout">退出登录</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dialogVisible: false,
				ruleForm: {},
				user: {},
			};
		},
		created() {
			
		},
		mounted() {
			let sessionTable = this.$storage.get("sessionTable")
			this.$http({
				url: sessionTable + '/session',
				method: "get"
			}).then(({
				data
			}) => {
				if (data && data.code === 0) {
					this.user = data.data;
					this.$storage.set('userid',data.data.id);
				} else {
					let message = this.$message
					message.error(data.msg);
				}
			});
		},
		methods: {
			onLogout() {
				let storage = this.$storage
				let router = this.$router
				storage.clear()
				router.replace({
					name: "login"
				});
			},
			onIndexTap(){
				window.location.href = `${this.$base.indexUrl}`
			},
		}
	};
</script>


<style lang="scss" scoped>
	.navbar {
		.logout:hover {
			transform: translateY(-1px);
			box-shadow: 0 4px 8px rgba(0,0,0,0.1);
		}
	}
</style>
