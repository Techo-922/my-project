<template>
<div :style='{"border":"0","padding":"32px","margin":"10px auto","borderRadius":"16px","background":"linear-gradient(135deg, #ffffff 0%, #f8f7fc 100%)","width":"1200px","position":"relative","boxShadow":"0 8px 24px rgba(102, 126, 234, 0.12)"}'>
    <div class="section-title" :style='{"padding":"24px 0","margin":"0px 0 24px","borderColor":"transparent","color":"#fff","borderRadius":"16px","textAlign":"center","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","borderWidth":"0","fontSize":"24px","lineHeight":"32px","borderStyle":"solid","fontWeight":"700","letterSpacing":"1px","boxShadow":"0 8px 20px rgba(102, 126, 234, 0.25)"}'>💬 留言信息</div>
	<el-form :model="form" :rules="rules" ref="form" label-width="80px" label-position="left" :style='{"padding":"32px","borderRadius":"16px","background":"#fff","boxShadow":"0 2px 12px rgba(0,0,0,0.04)","marginBottom":"24px"}'>
	  <el-form-item label="📝 留言" prop="content">
		<el-input type="textarea" :rows="6" v-model="form.content" placeholder="请输入您的留言内容..." :style='{"borderRadius":"12px"}'></el-input>
	  </el-form-item>
      <el-form-item label="🖼️ 图片" prop="cpicture">
        <file-upload
        tip="点击上传图片"
        action="file/upload"
        :limit="1"
        :multiple="true"
        :fileUrls="form.cpicture?form.cpicture:''"
        @change="cpictureUploadChange"
        ></file-upload>
      </el-form-item>
	  <el-form-item :style='{"textAlign":"center","marginTop":"24px"}'>
		<el-button type="primary" @click="submitForm('form')" :style='{"border":"0","cursor":"pointer","padding":"0 32px","margin":"0 8px","outline":"none","color":"#fff","borderRadius":"24px","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","width":"auto","lineHeight":"44px","fontSize":"15px","height":"44px","fontWeight":"600","boxShadow":"0 4px 12px rgba(102, 126, 234, 0.3)","transition":"all 0.3s ease"}'>✉️ 立即提交</el-button>
		<el-button @click="resetForm('form')" :style='{"border":"2px solid #e0e0e0","cursor":"pointer","padding":"0 32px","margin":"0 8px","outline":"none","color":"#666","borderRadius":"24px","background":"#fff","width":"auto","lineHeight":"40px","fontSize":"15px","height":"44px","fontWeight":"600","transition":"all 0.3s ease"}'>🔄 重置</el-button>
	  </el-form-item>
	</el-form>
	<div class="section-content" :style='{"padding":"24px","borderRadius":"16px","background":"#fff","boxShadow":"0 2px 12px rgba(0,0,0,0.04)"}'>
	  <span v-for="item in infoList" :key="item.id" class="message-item">
		<div class="header-block" :style='{"padding":"16px 0","display":"flex","alignItems":"center"}'>
		  <el-avatar v-if="item.avatarurl" :size="56" :src="$config.baseUrl + item.avatarurl" :style='{"border":"3px solid #e8e6f5","boxShadow":"0 2px 8px rgba(0,0,0,0.08)"}'></el-avatar>
		  <el-avatar v-if="!item.avatarurl" :size="56" :src="require('@/assets/touxiang.png')" :style='{"border":"3px solid #e8e6f5","boxShadow":"0 2px 8px rgba(0,0,0,0.08)"}'></el-avatar>
		  <span class="userinfo" :style='{"marginLeft":"16px","fontSize":"16px","fontWeight":"600","color":"#667eea"}'>👤 {{item.username}}</span>
		</div>
		<div class="content-block-ask" :style='{"marginLeft":"72px","padding":"16px 20px","borderRadius":"12px","background":"linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%)","fontSize":"15px","lineHeight":"24px","color":"#333","border":"2px solid rgba(102, 126, 234, 0.1)"}'>
		  {{item.content}}
		</div>
        <div v-if="item.cpicture" class="content" style="margin: 16px 0 0 72px;flex: 1;">
            <img style="max-width: 400px;max-height: 400px;border: 3px solid #e8e6f5;border-radius: 12px;box-shadow: 0 4px 12px rgba(0,0,0,0.08);" :src="$config.baseUrl+item.cpicture">
        </div>
		<div class="content-block-reply" v-if="item.reply" :style='{"marginLeft":"72px","marginTop":"16px","padding":"16px 20px","borderRadius":"12px","background":"linear-gradient(135deg, rgba(255, 193, 7, 0.08) 0%, rgba(255, 152, 0, 0.08) 100%)","fontSize":"15px","lineHeight":"24px","color":"#333","border":"2px solid rgba(255, 193, 7, 0.15)"}'>
		  <span :style='{"color":"#ff9800","fontWeight":"600","marginRight":"8px"}'>💬 回复：</span>{{item.reply}}
		</div>
        <div v-if="item.rpicture" class="content" style="margin: 16px 0 0 72px;flex: 1;">
            <img style="max-width: 400px;max-height: 400px;border: 3px solid #e8e6f5;border-radius: 12px;box-shadow: 0 4px 12px rgba(0,0,0,0.08);" :src="$config.baseUrl+item.rpicture">
        </div>
		<el-divider :style='{"margin":"24px 0","borderColor":"rgba(102, 126, 234, 0.1)"}' ></el-divider>
	  </span>
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
      @prev-click="prevClick"
      @next-click="nextClick"
    ></el-pagination>
	  
</div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
		  layouts: '',
        form: {
          content: '',
          userid: localStorage.getItem('userid'),
          username: localStorage.getItem('username')
        },
        total: 1,
        pageSize: 20,
		pageSizes: [10,20,30,50],
        totalPage: 1,
        rules: {
					content: [
						{ required: true, message: '请输入内容', trigger: 'blur' }
					]
        },
        infoList: []
      }
    },
    created() {
      this.getInfo(1);
    },
    //方法集合
    methods: {
      getInfo(page) {
        this.$http.get('messages/list', {params: {page, limit: this.pageSize,sort:"addtime", order:"desc"}}).then(res => {
          if (res.data.code == 0) {
            this.infoList = res.data.data.list;
            this.total = res.data.data.total;
            this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
            this.totalPage = res.data.data.totalPage;
          }
        });
      },
      curChange(page) {
        this.getInfo(page);
      },
      prevClick(page) {
        this.getInfo(page);
      },
      nextClick(page) {
        this.getInfo(page);
      },
      cpictureUploadChange(fileUrls) {
          this.form.cpicture = fileUrls;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.form.cpicture!=null) {
               this.form.cpicture = this.form.cpicture.replace(new RegExp(this.$config.baseUrl,"g"),"");
            }
            this.form.avatarurl = localStorage.getItem('headportrait')?localStorage.getItem('headportrait'):'';
            let sensitiveWords = "";
            let sensitiveWordsArr = [];
            if(sensitiveWords) {
                sensitiveWordsArr = sensitiveWords.split(",");
            }
            for(var i=0; i<sensitiveWordsArr.length; i++){
                //全局替换
                var reg = new RegExp(sensitiveWordsArr[i],"g");
                //判断内容中是否包括敏感词
                if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
					// 将敏感词替换**
                    this.form.content = this.form.content.replace(reg,"**");
                }
            }
            this.$http.post('messages/add', this.form).then(res => {
              if (res.data.code == 0) {
                this.$message({
                  type: 'success',
                  message: '留言成功!',
                  duration: 1500,
                  onClose: () => {
                    this.form.content = '';
                    this.getInfo(1);
                  }
                });
              }
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .section {
    width: 900px;
    margin: 0 auto;
  }

  .section-content {
    margin-top: 0;
  }
  
  .message-item {
  	transition: all 0.3s ease;
  }
  
  .section-pagination {
    margin-top: 30px;
    text-align: center;
  }
  .header-block {
    height: auto;
    line-height: normal;
    display: flex;
  }
  .userinfo {
    align-self: center;
    margin-left: 15px;
  }
  .content-block-ask, .content-block-reply {
    margin-left: 72px;
    margin-top: 16px;
  }
  .content-block-reply {
    margin-top: 16px;
  }
  .z-box {
  	  width: 100% !important;
  }
  
  ::v-deep .el-form-item__label {
  	color: #667eea;
  	font-weight: 600;
  	font-size: 15px;
  }
  
  ::v-deep .el-textarea__inner {
  	border: 2px solid #e8e6f5;
  	border-radius: 12px;
  	padding: 16px;
  	font-size: 14px;
  	line-height: 24px;
  	transition: all 0.3s ease;
  }
  
  ::v-deep .el-textarea__inner:focus {
  	border-color: #667eea;
  	box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }
  
  ::v-deep .el-button:hover {
  	transform: translateY(-2px);
  	box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4) !important;
  }
  
  ::v-deep .el-button:nth-child(2):hover {
  	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  	color: #fff !important;
  	border-color: #667eea !important;
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

