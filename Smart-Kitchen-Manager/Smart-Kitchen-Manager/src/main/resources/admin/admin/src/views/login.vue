<template>
  <div>
    <div class="container" :style='{"minHeight":"100vh","alignItems":"center","backgroundImage":"url(http://codegen.caihongy.cn/20221026/dfa16b1e27da4aacaf5408c8a8adddab.png)","display":"flex","width":"100%","backgroundSize":"100% 100%","backgroundPosition":"center center","backgroundRepeat":"no-repeat","justifyContent":"center"}'>

      <el-form :style='{"padding":"40px 20px 20px","margin":"0","borderRadius":"10px","top":"0","background":"#fff","width":"570px","position":"absolute","right":"0","height":"100%"}'>
        <div v-if="true" :style='{"width":"100%","margin":"150px  0 0 0","lineHeight":"1.5","fontSize":"32px","color":"rgba(51,51,51,1)","textAlign":"center"}'>ADMIN / LOGIN</div>
        <div v-if="true" :style='{"width":"100%","margin":"20px 0","lineHeight":"1.5","fontSize":"24px","color":"#ab85d3","textAlign":"center"}'>智能菜谱推荐系统管理</div>
        <div v-if="loginType==1" class="list-item" :style='{"width":"80%","margin":"50px auto","borderColor":"#ab85d3","borderStyle":"solid","borderWidth":"0 0  1px 0"}'>
          <div v-if="true" :style='{"width":"20%","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>账号</div>
          <input :style='{"border":"0","padding":"0 10px","color":"#999","display":"inline-block","width":"70%","fontSize":"14px","height":"44px"}' placeholder="请输入管理员账号" name="username" type="text" v-model="rulesForm.username">
        </div>
        <div v-if="loginType==1" class="list-item" :style='{"width":"80%","margin":"50px auto","borderColor":"#ab85d3","borderStyle":"solid","borderWidth":"0 0  1px 0"}'>
          <div v-if="true" :style='{"width":"20%","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>密码</div>
          <input :style='{"border":"0","padding":"0 10px","color":"#999","display":"inline-block","width":"70%","fontSize":"14px","height":"44px"}' placeholder="请输入密码" name="password" type="password" v-model="rulesForm.password">
        </div>
        <div :style='{"width":"80%","margin":"20px auto"}' v-if="roles.length>1" prop="loginInRole" class="list-type">
          <el-radio v-for="item in roles" v-bind:key="item.roleName" v-model="rulesForm.role" :label="item.roleName">{{item.roleName}}</el-radio>
        </div>
        <div :style='{"width":"80%","margin":"20px auto"}'>
          <el-button v-if="loginType==1" :style='{"border":"0","cursor":"pointer","padding":"0 24px","margin":"10px 20px","outline":"none","color":"#666","borderRadius":"10px","background":"linear-gradient(90deg, rgba(255,233,100,1) 0%, rgba(194,248,126,1) 29%, rgba(181,233,252,1) 61%, rgba(246,172,218,1) 100%)","width":"40%","fontSize":"18px","height":"44px"}' @click="login()" class="loginInBt">登录</el-button>
          <el-button v-if="loginType==1" :style='{"border":"2px solid #494592","cursor":"pointer","padding":"0 24px","margin":"10px 20px","outline":"none","color":"#000","borderRadius":"10px","background":"none","width":"40%","fontSize":"14px","height":"44px"}' @click="resetForm()">重置</el-button>
        </div>
      </el-form>

    </div>
  </div>
</template>
<script>

import menu from "@/utils/menu";
export default {
  data() {
    return {
      baseUrl:this.$base.url,
      loginType: 1,
      rulesForm: {
        username: "",
        password: "",
        role: "",
        code: '',
      },
      menus: [],
      roles: [],
      tableName: "",
      codes: [{
        num: 1,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 2,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 3,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 4,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      }],
    };
  },
  mounted() {
    let menus = menu.list();
    this.menus = menus;

    for (let i = 0; i < this.menus.length; i++) {
      if (this.menus[i].hasBackLogin=='是') {
        this.roles.push(this.menus[i])
      }
    }

  },
  created() {
    this.getRandCode()
  },
  destroyed() {
	    },
  components: {
  },
  methods: {

    //注册
    register(tableName){
		this.$storage.set("loginTable", tableName);
        this.$storage.set("pageFlag", "register");
		this.$router.push({path:'/register'})
    },
    // 重置表单
    resetForm() {
      this.rulesForm = {
        username: "",
        password: "",
        role: "",
        code: '',
      };
    },
    // 登陆
    login() {

		if (!this.rulesForm.username) {
			this.$message.error("请输入用户名");
			return;
		}
		if (!this.rulesForm.password) {
			this.$message.error("请输入密码");
			return;
		}
		
		// 强制设置为管理员登录
		this.tableName = "users";
		this.rulesForm.role = "管理员";

		this.$http({
			url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
			method: "post"
		}).then(({ data }) => {
			if (data && data.code === 0) {
				this.$storage.set("Token", data.token);
				this.$storage.set("role", this.rulesForm.role);
				this.$storage.set("sessionTable", this.tableName);
				this.$storage.set("adminName", this.rulesForm.username);
				this.$router.replace({ path: "/index/" });
			} else {
				this.$message.error(data.msg);
			}
		});
    },
    getRandCode(len = 4){
		this.randomString(len)
    },
    randomString(len = 4) {
      let chars = [
          "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
          "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
          "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
          "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
          "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2",
          "3", "4", "5", "6", "7", "8", "9"
      ]
      let colors = ["0", "1", "2","3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"]
      let sizes = ['14', '15', '16', '17', '18']

      let output = [];
      for (let i = 0; i < len; i++) {
        // 随机验证码
        let key = Math.floor(Math.random()*chars.length)
        this.codes[i].num = chars[key]
        // 随机验证码颜色
        let code = '#'
        for (let j = 0; j < 6; j++) {
          let key = Math.floor(Math.random()*colors.length)
          code += colors[key]
        }
        this.codes[i].color = code
        // 随机验证码方向
        let rotate = Math.floor(Math.random()*60)
        let plus = Math.floor(Math.random()*2)
        if(plus == 1) rotate = '-'+rotate
        this.codes[i].rotate = 'rotate('+rotate+'deg)'
        // 随机验证码字体大小
        let size = Math.floor(Math.random()*sizes.length)
        this.codes[i].size = sizes[size]+'px'
      }
    },
  }
};
</script>

<style lang="scss" scoped>
.container {
  position: relative;
  
  .el-form-item {
    & ::v-deep .el-form-item__content {
      width: 100%;
    }
  }
  
  .list-item ::v-deep .el-input .el-input__inner {
    border: 0;
    padding: 0 10px;
    color: #999;
    display: inline-block;
    width: 70%;
    font-size: 14px;
    height: 44px;
  }
  
  .list-code ::v-deep .el-input .el-input__inner {
    border: 0;
    padding: 0 10px;
    outline: none;
    color: #999;
    background: rgba(232, 240, 255,1);
    display: inline-block;
    vertical-align: middle;
    width: calc(100% - 154px);
    font-size: 14px;
    height: 44px;
  }
  
  .list-type ::v-deep .el-radio__input .el-radio__inner {
    background: rgba(53, 53, 53, 0);
    border-color: #666666;
  }
  .list-type ::v-deep .el-radio__input.is-checked .el-radio__inner {
    background: rgba(76, 72, 147, 1);
    border-color: rgba(76, 72, 147, 1);
  }
  .list-type ::v-deep .el-radio__label {
    color: #999;
    font-size: 14px;
  }
  .list-type ::v-deep .el-radio__input.is-checked+.el-radio__label {
    color: rgba(76, 72, 147, 1);
    font-size: 14px;
  }
}
</style>
