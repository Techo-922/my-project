<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="600px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      
      <el-form-item label="选择食材" prop="shicaiId" v-if="!dataForm.id">
        <el-select 
          v-model="dataForm.shicaiId" 
          placeholder="请选择食材" 
          filterable 
          @change="handleShicaiChange"
          style="width: 100%;">
          <el-option 
            v-for="item in shicaiList" 
            :key="item.id" 
            :label="`${item.shicaiName} (${item.category})`" 
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="食材名称" prop="shicaiName">
        <el-input v-model="dataForm.shicaiName" :disabled="true" placeholder="选择食材后自动填充"></el-input>
      </el-form-item>
      
      <el-form-item label="分类" prop="category">
        <el-input v-model="dataForm.category" :disabled="true" placeholder="选择食材后自动填充"></el-input>
      </el-form-item>
      
      <el-form-item label="保质期(天)" prop="shelfLifeDays">
        <el-input-number v-model="dataForm.shelfLifeDays" :min="1" :max="3650" label="保质期天数" style="width: 200px;"></el-input-number>
        <span style="margin-left: 10px; color: #909399; font-size: 12px;">天</span>
      </el-form-item>
      
      <el-form-item label="存储方法" prop="storageMethod">
        <el-input v-model="dataForm.storageMethod" type="textarea" :rows="3" placeholder="例如: 冷藏保存，温度2-8℃"></el-input>
      </el-form-item>
      
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
// 修改时间: 2025-12-31 13:17 - 实现从食材表选择功能
export default {
  data() {
    return {
      visible: false,
      shicaiList: [], // 食材列表
      dataForm: {
        id: 0,
        shicaiId: '',
        shicaiName: '',
        category: '',
        shelfLifeDays: 7,
        storageMethod: ''
      },
      dataRule: {
        shicaiId: [
          { required: true, message: '请选择食材', trigger: 'change' }
        ],
        shelfLifeDays: [
          { required: true, message: '保质期天数不能为空', trigger: 'blur' }
        ],
        storageMethod: [
          { required: true, message: '存储方法不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        
        // 如果是新增,加载食材列表
        if (!this.dataForm.id) {
          this.loadShicaiList()
        }
        
        // 如果是修改,加载数据
        if (this.dataForm.id) {
          this.$http({
            url: `/shicaishelflife/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.shicaiId = data.data.shicaiId
              this.dataForm.shicaiName = data.data.shicaiName
              this.dataForm.category = data.data.category
              this.dataForm.shelfLifeDays = data.data.shelfLifeDays
              this.dataForm.storageMethod = data.data.storageMethod
            }
          })
        }
      })
    },
    // 加载食材列表
    loadShicaiList() {
      this.$http({
        url: '/shicai/list',
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.shicaiList = data.data || []
        }
      }).catch(() => {
        // 如果没有shicai接口,使用备用方案
        this.$message.warning('无法加载食材列表,请联系管理员')
      })
    },
    // 选择食材时自动填充信息
    handleShicaiChange(shicaiId) {
      const selectedShicai = this.shicaiList.find(item => item.id === shicaiId)
      if (selectedShicai) {
        this.dataForm.shicaiName = selectedShicai.shicaiName
        this.dataForm.category = selectedShicai.category
      }
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: `/shicaishelflife/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data: {
              'id': this.dataForm.id || undefined,
              'shicaiId': this.dataForm.shicaiId,
              'shicaiName': this.dataForm.shicaiName,
              'category': this.dataForm.category,
              'shelfLifeDays': this.dataForm.shelfLifeDays,
              'storageMethod': this.dataForm.storageMethod
            }
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
