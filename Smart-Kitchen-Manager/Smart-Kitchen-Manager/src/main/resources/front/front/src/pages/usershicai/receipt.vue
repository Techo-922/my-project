<template>
  <div>
    <div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
      <el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>我的食材库</el-breadcrumb-item>
        <el-breadcrumb-item>小票识别录入</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="receipt-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
      
      <el-card :style='{"marginBottom":"20px"}'>
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>📸 上传购物小票</span>
        </div>
        
        <div class="upload-area">
          <el-upload
            class="upload-demo"
            drag
            :action="uploadUrl"
            :headers="headers"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="false"
            accept="image/*">
            <i class="el-icon-upload" style="font-size: 67px; color: #C0C4CC;"></i>
            <div class="el-upload__text">将小票图片拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip" style="font-size: 12px; color: #606266; margin-top: 7px;">支持 JPG、PNG 格式，建议图片清晰、光线充足</div>
          </el-upload>
          
          <div v-if="uploading" :style='{"textAlign":"center","marginTop":"20px"}'>
            <i class="el-icon-loading" :style='{"fontSize":"32px","color":"#409EFF"}'></i>
            <div :style='{"marginTop":"10px","color":"#666"}'>正在识别中，请稍候...</div>
          </div>
        </div>
      </el-card>
      
      <el-card v-if="recognizedItems.length > 0">
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>✅ 识别结果</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="clearResults">清空</el-button>
        </div>
        
        <div :style='{"marginBottom":"20px"}'>
          <el-alert title="请检查并修改识别结果，确认无误后点击批量添加" type="info" :closable="false"></el-alert>
        </div>
        
        <el-form :inline="true" :style='{"marginBottom":"20px","padding":"15px","background":"#f5f7fa","borderRadius":"4px"}'>
          <el-form-item label="购买日期">
            <el-date-picker v-model="commonPurchaseDate" type="datetime" placeholder="选择购买日期" value-format="yyyy-MM-dd HH:mm:ss" :style='{"width":"200px"}'></el-date-picker>
          </el-form-item>
          <el-form-item label="默认状态">
            <el-select v-model="commonStatus" placeholder="选择状态" :style='{"width":"120px"}'>
              <el-option label="新鲜" value="new"></el-option>
              <el-option label="已使用" value="used"></el-option>
            </el-select>
          </el-form-item>
          <el-button type="primary" size="small" @click="applyCommonSettings">应用到全部</el-button>
        </el-form>
        
        <el-table :data="recognizedItems" border :style='{"width":"100%"}'>
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column label="食材名称" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.shicaiName" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="1" size="small" :style='{"width":"100%"}'></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="单位" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.unit" size="small" :style='{"width":"100%"}'>
                <el-option label="克" value="克"></el-option>
                <el-option label="千克" value="千克"></el-option>
                <el-option label="斤" value="斤"></el-option>
                <el-option label="个" value="个"></el-option>
                <el-option label="袋" value="袋"></el-option>
                <el-option label="盒" value="盒"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="购买日期" width="180">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.purchaseDate" type="datetime" size="small" value-format="yyyy-MM-dd HH:mm:ss" :style='{"width":"100%"}'></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="过期日期" width="180">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.expiryDate" type="datetime" size="small" value-format="yyyy-MM-dd HH:mm:ss" :style='{"width":"100%"}'></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" size="small" :style='{"width":"100%"}'>
                <el-option label="新鲜" value="new"></el-option>
                <el-option label="已使用" value="used"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" @click="removeItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div :style='{"marginTop":"20px","textAlign":"center"}'>
          <el-button type="primary" size="large" @click="batchAdd" :loading="saving">
            <i class="el-icon-check"></i> 批量添加 ({{recognizedItems.length}}项)
          </el-button>
          <el-button size="large" @click="back">返回</el-button>
        </div>
      </el-card>
      
      <el-card v-if="recognizedItems.length === 0" :style='{"marginTop":"20px"}'>
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>💡 使用说明</span>
        </div>
        <div :style='{"lineHeight":"2"}'>
          <p><strong>1. 拍摄小票：</strong>确保小票平整、光线充足、文字清晰</p>
          <p><strong>2. 上传图片：</strong>支持拖拽或点击上传，系统会自动识别</p>
          <p><strong>3. 检查结果：</strong>识别后请仔细核对商品名称、数量等信息</p>
          <p><strong>4. 修改信息：</strong>可以直接在表格中修改识别错误的内容</p>
          <p><strong>5. 批量添加：</strong>确认无误后点击"批量添加"按钮</p>
          <p :style='{"color":"#E6A23C","marginTop":"10px"}'>
            <i class="el-icon-warning"></i> 提示：OCR识别可能存在误差，请务必检查后再添加
          </p>
        </div>
      </el-card>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      baseUrl: '',
      uploadUrl: '',
      headers: {},
      uploading: false,
      saving: false,
      recognizedItems: [],
      commonPurchaseDate: '',
      commonStatus: 'new'
    }
  },
  created() {
    this.baseUrl = this.$config.baseUrl;
    this.uploadUrl = this.baseUrl + '/ocr/recognizeReceipt';
    this.commonPurchaseDate = this.formatDateTime(new Date());
    
    // 设置请求头，携带 token
    let token = this.$storage.get('Token');
    if (token) {
      this.headers = {
        'Token': token
      };
    }
  },
  methods: {
    beforeUpload(file) {
      const isImage = file.type.indexOf('image') !== -1;
      const isLt5M = file.size / 1024 / 1024 < 5;
      
      if (!isImage) {
        this.$message.error('只能上传图片文件！');
        return false;
      }
      if (!isLt5M) {
        this.$message.error('图片大小不能超过 5MB！');
        return false;
      }
      
      this.uploading = true;
      return true;
    },
    
    handleUploadSuccess(response, file, fileList) {
      this.uploading = false;
      
      if (response.code === 0) {
        const items = response.items || [];
        
        if (items.length === 0) {
          this.$message.warning('未识别到食材信息，请检查图片是否清晰');
          return;
        }
        
        this.recognizedItems = items.map(item => ({
          shicaiName: item.name || '',
          quantity: item.quantity || 1,
          unit: item.unit || '个',
          purchaseDate: this.commonPurchaseDate,
          expiryDate: this.calculateExpiryDate(this.commonPurchaseDate, 7),
          status: 'new'
        }));
        
        this.$message.success(`识别成功！共识别到 ${items.length} 项食材`);
      } else {
        this.$message.error(response.msg || '识别失败');
      }
    },
    
    handleUploadError(err, file, fileList) {
      this.uploading = false;
      this.$message.error('上传失败：' + err.message);
    },
    
    applyCommonSettings() {
      this.recognizedItems.forEach(item => {
        item.purchaseDate = this.commonPurchaseDate;
        item.status = this.commonStatus;
        item.expiryDate = this.calculateExpiryDate(this.commonPurchaseDate, 7);
      });
      this.$message.success('已应用统一设置');
    },
    
    calculateExpiryDate(purchaseDate, days) {
      if (!purchaseDate) return '';
      let date = new Date(purchaseDate);
      date.setDate(date.getDate() + days);
      return this.formatDateTime(date);
    },
    
    removeItem(index) {
      this.recognizedItems.splice(index, 1);
    },
    
    clearResults() {
      this.$confirm('确定清空所有识别结果吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.recognizedItems = [];
      });
    },
    
    batchAdd() {
      for (let item of this.recognizedItems) {
        if (!item.shicaiName) {
          this.$message.error('请填写所有食材名称');
          return;
        }
        if (!item.purchaseDate) {
          this.$message.error('请选择购买日期');
          return;
        }
      }
      
      this.saving = true;
      let userid = this.$storage.get('userid');
      
      let items = this.recognizedItems.map(item => ({
        ...item,
        userid: userid
      }));
      
      this.$http.post('usershicai/batchSave', items).then(res => {
        this.saving = false;
        if (res.data && res.data.code === 0) {
          this.$message.success('批量添加成功！');
          this.$router.push('/index/usershicaiList');
        } else {
          this.$message.error(res.data.msg || '添加失败');
        }
      }).catch(() => {
        this.saving = false;
        this.$message.error('添加失败');
      });
    },
    
    back() {
      this.$router.go(-1);
    },
    
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
.upload-area {
  padding: 20px;
}
.upload-demo {
  text-align: center;
}
.upload-demo >>> .el-upload {
  width: 100%;
}
.upload-demo >>> .el-upload-dragger {
  width: 100%;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.el-upload__text {
  font-size: 14px;
  color: #606266;
  margin-top: 10px;
}
.el-upload__text em {
  color: #409EFF;
  font-style: normal;
}
</style>
