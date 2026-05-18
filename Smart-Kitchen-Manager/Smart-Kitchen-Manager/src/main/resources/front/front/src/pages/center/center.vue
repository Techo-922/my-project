<template>
<div class="center-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
	<div class="title" :style='{"padding":"24px 0","margin":"0px 0 16px","borderColor":"transparent","color":"#fff","borderRadius":"16px","textAlign":"center","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","borderWidth":"0","fontSize":"24px","lineHeight":"32px","borderStyle":"solid","fontWeight":"700","letterSpacing":"1px","boxShadow":"0 8px 20px rgba(102, 126, 234, 0.25)"}'>✨ {{ title }}</div>

    <el-tabs tab-position="left" :style='{"border":"0","padding":"24px","margin":"0","borderRadius":"16px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","boxShadow":"0 8px 24px rgba(102, 126, 234, 0.12)"}' @tab-click="handleClick">
      <el-tab-pane label="个人中心">
        <el-form class="center-preview-pv" ref="sessionForm" :model="sessionForm" :rules="rules" label-width="80px">
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="账号" prop="zhanghao">
            <el-input v-model="sessionForm.zhanghao" placeholder="账号" readonly></el-input>
          </el-form-item>
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="密码" prop="mima">
            <el-input type="password" v-model="sessionForm.mima" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="姓名" prop="xingming">
            <el-input v-model="sessionForm.xingming" placeholder="姓名" ></el-input>
          </el-form-item>
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="性别">
            <el-select v-model="sessionForm.xingbie" placeholder="请选择性别" >
              <el-option v-for="(item, index) in dynamicProp.xingbie" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="邮箱" prop="youxiang">
            <el-input v-model="sessionForm.youxiang" placeholder="邮箱" ></el-input>
          </el-form-item>
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="手机号码" prop="shoujihaoma">
            <el-input v-model="sessionForm.shoujihaoma" placeholder="手机号码" ></el-input>
          </el-form-item>
          <el-form-item :style='{"width":"80%","padding":"10px","margin":"4px auto","background":"none"}' v-if="userTableName=='yonghu'" label="头像">
            <el-upload
              class="avatar-uploader"
              :action="uploadUrl"
              :headers="headers"
              :show-file-list="false"
              :on-success="yonghutouxiangHandleAvatarSuccess">
              <img :style='{"border":"1px solid #dbd9f4","cursor":"pointer","color":"#b3aeea","borderRadius":"30px","textAlign":"center","width":"200px","fontSize":"32px","lineHeight":"120px"}' v-if="sessionForm.touxiang" :src="baseUrl + sessionForm.touxiang" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item :style='{"padding":"0","margin":"24px 0 0 0","justifyContent":"center","display":"flex","gap":"16px"}'>
            <el-button :style='{"border":"0","cursor":"pointer","padding":"0 32px","margin":"0","outline":"none","color":"#fff","borderRadius":"24px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"auto","lineHeight":"44px","fontSize":"15px","height":"44px","fontWeight":"600","boxShadow":"0 4px 12px rgba(102, 126, 234, 0.3)","transition":"all 0.3s ease"}' type="primary" @click="onSubmit('sessionForm')">💾 更新信息</el-button>
            <el-button :style='{"border":"2px solid #e0e0e0","cursor":"pointer","padding":"0 32px","margin":"0","outline":"none","color":"#666","borderRadius":"24px","background":"#fff","width":"auto","lineHeight":"40px","fontSize":"15px","height":"44px","fontWeight":"600","transition":"all 0.3s ease"}' type="danger" @click="logout">🚪 退出登录</el-button>
          </el-form-item>
        </el-form>
		
        <el-dialog title="用户充值" :visible.sync="dialogFormVisibleMoney" width="726px" center>
          <el-form :model="chongzhiForm">
            <el-form-item label="充值金额" label-width="120px">
              <el-input type="number" v-model="chongzhiForm.money" autocomplete="off" placeholder="充值金额"></el-input>
            </el-form-item>
            <el-form-item label-width="120px">
              <el-radio-group v-model="chongzhiForm.radio">
                <el-radio style="margin-bottom: 30px" label="微信支付">
                  <el-image
                    style="width: 60px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/weixin.png')"
                    fit="fill"></el-image>
                    <span style="display: inline-block;margin-left: 10px">微信支付</span>
                </el-radio>
                <el-radio label="支付宝支付">
                  <el-image
                    style="width: 60px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/zhifubao.png')"
                    fit="fill"></el-image>
                    <span style="display: inline-block;margin-left: 10px">支付宝支付</span>
                </el-radio>
                <el-radio label="中国建设银行支付">
                  <el-image
                    style="width: 120px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/jianshe.png')"
                    fit="fill"></el-image>
                </el-radio>
                <el-radio label="中国农业银行支付">
                  <el-image
                    style="width: 126px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/nongye.png')"
                    fit="fill"></el-image>
                </el-radio>
                <el-radio label="中国银行支付">
                  <el-image
                    style="width: 140px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/zhongguo.png')"
                    fit="fill"></el-image>
                </el-radio>
                <el-radio label="交通银行支付">
                  <el-image
                    style="width: 120px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/jiaotong.png')"
                    fit="fill"></el-image>
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisibleMoney = false">取消</el-button>
            <el-button type="primary" @click="chongzhi">确认充值</el-button>
          </div>
        </el-dialog>
        <el-dialog title="会员购买" :visible.sync="dialogFormVisibleVip" width="726px" center>
          <el-form :model="chongzhiForm">
            <el-form-item label="会员价" label-width="120px">
              <el-input readonly autocomplete="off" value="¥99/年"></el-input>
            </el-form-item>
            <el-form-item label-width="120px">
              <el-radio-group v-model="chongzhiForm.radio">
                <el-radio style="margin-bottom: 30px" label="微信支付">
                  <el-image
                    style="width: 60px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/weixin.png')"
                    fit="fill"></el-image>
                    <span style="display: inline-block;margin-left: 10px">微信支付</span>
                </el-radio>
                <el-radio label="支付宝支付">
                  <el-image
                    style="width: 60px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/zhifubao.png')"
                    fit="fill"></el-image>
                    <span style="display: inline-block;margin-left: 10px">支付宝支付</span>
                </el-radio>
                <el-radio label="中国建设银行支付">
                  <el-image
                    style="width: 120px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/jianshe.png')"
                    fit="fill"></el-image>
                </el-radio>
                <el-radio label="中国农业银行支付">
                  <el-image
                    style="width: 126px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/nongye.png')"
                    fit="fill"></el-image>
                </el-radio>
                <el-radio label="中国银行支付">
                  <el-image
                    style="width: 140px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/zhongguo.png')"
                    fit="fill"></el-image>
                </el-radio>
                <el-radio label="交通银行支付">
                  <el-image
                    style="width: 120px; height: 60px;vertical-align: middle;"
                    :src="require('@/assets/jiaotong.png')"
                    fit="fill"></el-image>
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisibleVip = false">取消</el-button>
            <el-button type="primary" @click="chongzhivip">确认支付</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="偏好设置"></el-tab-pane>
      <el-tab-pane label="我的收藏"></el-tab-pane>
    </el-tabs>
</div>
</template>

<script>
  import config from '@/config/config'
  import Vue from 'vue'
  export default {
    //数据集合
    data() {
      return {
        title: '个人中心',
        baseUrl: config.baseUrl,
        sessionForm: {},
        rules: {},
        chongzhiForm: {
          money: '',
          radio: ''
        },
        disabled: false,
        dialogFormVisibleMoney: false,
        dialogFormVisibleVip: false,
        uploadUrl: config.baseUrl + 'file/upload',
        imageUrl: '',
        headers: {Token: localStorage.getItem('Token')},
        userTableName: localStorage.getItem('UserTableName'),
        dynamicProp: {}
      }
    },
    created() {
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'zhanghao', null);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'mima', null);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'xingming', null);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'xingbie', null);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'youxiang', null);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'shoujihaoma', null);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.sessionForm, 'touxiang', null);
      }

      if ('yonghu' == this.userTableName) {
        this.$set(this.rules, 'zhanghao', [{ required: true, message: '请输入账号', trigger: 'blur' }]);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.rules, 'mima', [{ required: true, message: '请输入密码', trigger: 'blur' }]);
      }
      if ('yonghu' == this.userTableName) {
        this.$set(this.rules, 'xingming', [{ required: true, message: '请输入姓名', trigger: 'blur' }]);
      }
			if ('yonghu' == this.userTableName) {
        this.$set(this.rules, 'youxiang', [{ required: false, validator: this.$validate.isEmail, trigger: 'blur' }]);
      }
			if ('yonghu' == this.userTableName) {
        this.$set(this.rules, 'shoujihaoma', [{ required: false, validator: this.$validate.isMobile, trigger: 'blur' }]);
      }

      this.init();
      this.getSession();
    },
    //方法集合
    methods: {
      init() {
        if ('yonghu' == this.userTableName) {
          this.dynamicProp.xingbie = ['男','女'];
        }
      },
      getSession() {
        this.$http.get(this.userTableName + '/session', {emulateJSON: true}).then(res => {
          if (res.data.code == 0) {
            this.sessionForm = res.data.data;
            localStorage.setItem('userid', res.data.data.id);
            if(res.data.data.vip) {
                localStorage.setItem('vip', res.data.data.vip);
            }
            if(res.data.data.touxiang) {
                localStorage.setItem('headportrait', res.data.data.touxiang);
            } else if(res.data.data.headportrait) {
                localStorage.setItem('headportrait', res.data.data.headportrait);
            }
          }
        });
      },
      onSubmit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
              if (res.data.code == 0) {
                this.$message({
                  message: '更新成功',
                  type: 'success',
                  duration: 1500
                });
              }
            });
          } else {
            return false;
          }
        });
      },
      yonghutouxiangHandleAvatarSuccess(res, file) {
        if (res.code == 0) {
          if ('yonghu' == this.userTableName) {
            this.sessionForm.touxiang = 'upload/' + res.file;
          }
        }
      },
      chongzhi() {
        if (this.chongzhiForm.money === '' || this.chongzhiForm.money === null) {
          this.$message({
            message: '请输入充值金额',
            type: 'error',
            duration: 1500
          });
          return;
        }
        if (this.chongzhiForm.money <= 0) {
          this.$message({
            message: '请输入正确的充值金额',
            type: 'error',
            duration: 1500
          });
          return;
        }
        if (this.chongzhiForm.radio === '') {
          this.$message({
            message: '请选择充值方式',
            type: 'error',
            duration: 1500
          });
          return;
        }
        this.sessionForm.money = (parseInt(this.sessionForm.money) || 0) + parseInt(this.chongzhiForm.money);
        this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
          if (res.data.code == 0) {
            this.$message({
              message: '充值成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dialogFormVisibleMoney = false;
              }
            });
          }
        });
      },
      chongzhivip() {
        this.chongzhiForm.money = 199;
        if (this.chongzhiForm.radio === '') {
          this.$message({
            message: '请选择支付方式',
            type: 'error',
            duration: 1500
          });
          return;
        }
        if (this.sessionForm.vip) {
          this.$message({
            message: '您已是我们的尊贵会员',
            type: 'success',
            duration: 1500
          });
          return;
        }

        this.sessionForm.vip = '1';
        this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
          if (res.data.code == 0) {
            this.$message({
              message: '会员购买成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                localStorage.setItem('vip', this.sessionForm.vip);
                this.dialogFormVisibleVip = false;
              }
            });
          }
        });
      },
      handleClick(tab, event) {
        switch(event.target.outerText) {
          case '个人中心':
            tab.$router.push('/index/center');
            break;
          case '偏好设置':
            tab.$router.push('/index/preferenceSettings');
            break;
          case '我的收藏':
            localStorage.setItem('storeupType', 1);
            tab.$router.push('/index/storeup');
            break;
        }

        this.title = event.target.outerText;
      },
      logout() {
        localStorage.clear();
        Vue.http.headers.common['Token'] = "";
        this.$router.push('/index/home');
        this.activeIndex = '0'
        localStorage.setItem('keyPath', this.activeIndex)
        this.$forceUpdate()
        this.$message({
            message: '登出成功',
            type: 'success',
            duration: 1500,
        });
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .center-preview {
  
    .el-tabs {
      & ::v-deep .el-tabs__header {
        .el-tabs__nav-wrap {
          margin: 0;
  
          &::after {
            content: none;
          }
        }
  
        .el-tabs__active-bar {
          display: none !important;
        }
      }
  
      .center-preview-pv {
        .el-date-editor.el-input {
          width: auto;
        }
  
        .balance {
          .el-input {
            width: auto;
          }
        }
      }
    }
  }
  
  .center-preview .el-tabs ::v-deep .el-tabs__header {
	padding: 0;
	margin: 0 20px 0 0;
	background: none;
	width: 200px;
	border-width: 0;
	position: relative;
	float: left;
  }
  
  .center-preview .el-tabs ::v-deep .el-tabs__header .el-tabs__item {
  	border: 2px solid transparent;
  	border-radius: 24px;
  	padding: 0 24px;
  	margin: 0 0 12px 0;
  	color: #666;
  	background: #fff;
  	font-weight: 600;
  	font-size: 15px;
  	line-height: 48px;
  	position: relative;
  	text-align: center;
  	height: 48px;
  	transition: all 0.3s ease;
  	box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  }
  
  .center-preview .el-tabs ::v-deep .el-tabs__header .el-tabs__item:hover {
  	border: 2px solid rgba(102, 126, 234, 0.3);
  	border-radius: 24px;
  	padding: 0 24px;
  	color: #667eea;
  	background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  	font-weight: 600;
  	font-size: 15px;
  	line-height: 48px;
  	position: relative;
  	text-align: center;
  	height: 48px;
  	transform: translateX(4px);
  	box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
  }
  
  .center-preview .el-tabs ::v-deep .el-tabs__header .el-tabs__item.is-active {
  	border: 2px solid #667eea;
  	border-radius: 24px;
  	padding: 0 24px;
  	color: #fff;
  	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  	font-weight: 700;
  	font-size: 15px;
  	line-height: 48px;
  	position: relative;
  	text-align: center;
  	height: 48px;
  	box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  }
  
  .center-preview .el-tabs ::v-deep .el-tabs__content .el-tab-pane {
  	border-radius: 16px;
  	padding: 32px;
  	background: #fff;
  	width: calc(100% - 240px);
  	float: right;
  	box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  }
  
  .center-preview-pv .el-form-item ::v-deep .el-form-item__label {
  	padding: 0 16px 0 0;
  	color: #667eea;
  	font-weight: 600;
  	width: 100px;
  	font-size: 15px;
  	line-height: 44px;
  	text-align: right;
  }
  
  .center-preview-pv .el-form-item .el-form-item__content {
    margin-left: 100px;
  }
  
  .center-preview-pv .el-input ::v-deep .el-input__inner {
  	border: 2px solid #e8e6f5;
  	border-radius: 24px;
  	padding: 0 20px;
  	outline: none;
  	color: #333;
  	width: 450px;
  	font-size: 14px;
  	height: 44px;
  	transition: all 0.3s ease;
  	background: #fff;
  }
  
  .center-preview-pv .el-input ::v-deep .el-input__inner:focus {
  	border-color: #667eea;
  	box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }
  
  .center-preview-pv .el-select ::v-deep .el-input__inner {
  	border: 2px solid #e8e6f5;
  	border-radius: 24px;
  	padding: 0 20px;
  	outline: none;
  	color: #333;
  	width: 450px;
  	font-size: 14px;
  	height: 44px;
  	transition: all 0.3s ease;
  	background: #fff;
  }
  
  .center-preview-pv .el-select ::v-deep .el-input__inner:focus {
  	border-color: #667eea;
  	box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }
  
  .center-preview-pv .el-date-editor ::v-deep .el-input__inner {
  	border: 2px solid #e8e6f5;
  	border-radius: 24px;
  	padding: 0 20px 0 40px;
  	outline: none;
  	color: #333;
  	width: 220px;
  	font-size: 14px;
  	height: 44px;
  	transition: all 0.3s ease;
  	background: #fff;
  }
  
  .center-preview-pv .el-date-editor ::v-deep .el-input__inner:focus {
  	border-color: #667eea;
  	box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }
  
  .center-preview-pv ::v-deep .avatar-uploader-icon {
  	border: 3px dashed #e8e6f5;
  	cursor: pointer;
  	border-radius: 16px;
  	color: #667eea;
  	width: 160px;
  	font-size: 48px;
  	line-height: 160px;
  	text-align: center;
  	transition: all 0.3s ease;
  	background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  }
  
  .center-preview-pv ::v-deep .avatar-uploader-icon:hover {
  	border-color: #667eea;
  	color: #764ba2;
  	background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  }
  
  .center-preview-pv ::v-deep .avatar {
  	border: 3px solid #e8e6f5;
  	cursor: pointer;
  	border-radius: 16px;
  	width: 160px;
  	height: 160px;
  	object-fit: cover;
  	transition: all 0.3s ease;
  	box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  }
  
  .center-preview-pv ::v-deep .avatar:hover {
  	border-color: #667eea;
  	transform: scale(1.05);
  	box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
  }
  
  .center-preview-pv .el-button:hover {
  	transform: translateY(-2px);
  	box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4) !important;
  }
  
  .center-preview-pv .el-button:nth-child(2):hover {
  	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  	color: #fff !important;
  	border-color: #667eea !important;
  }
  
  .center-preview-pv .el-form-item.balance ::v-deep .el-input__inner {
  	border: 1px solid #dbd9f4;
  	border-radius: 20px 0 0 20px;
  	padding: 0 12px;
  	outline: none;
  	color: #999;
  	display: inline-block;
  	width: 200px;
  	font-size: 14px;
  	height: 40px;
  }
</style>

