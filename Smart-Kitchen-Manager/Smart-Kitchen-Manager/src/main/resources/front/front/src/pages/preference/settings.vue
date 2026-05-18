<template>
  <div class="preference-container">
    <div class="page-title">偏好设置</div>
    
    <el-card class="preference-card">
      <div slot="header">
        <span>健康偏好设置</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="loadPreference">刷新</el-button>
      </div>

      <el-form :model="preferenceForm" :rules="rules" ref="preferenceForm" label-width="120px">
        <!-- 饮食类型 -->
        <el-form-item label="饮食类型" prop="dietType">
          <el-select v-model="preferenceForm.dietType" placeholder="请选择饮食类型" style="width: 300px">
            <el-option label="普通饮食" value="normal"></el-option>
            <el-option label="素食" value="vegetarian"></el-option>
            <el-option label="纯素" value="vegan"></el-option>
            <el-option label="低碳水" value="low-carb"></el-option>
            <el-option label="低脂" value="low-fat"></el-option>
            <el-option label="高蛋白" value="high-protein"></el-option>
          </el-select>
          <span class="tip">选择您的饮食偏好类型</span>
        </el-form-item>

        <!-- 营养目标 -->
        <el-form-item label="营养目标">
          <div class="nutrient-goals">
            <div class="nutrient-item">
              <span class="nutrient-label">蛋白质（克/天）：</span>
              <el-input-number v-model="preferenceForm.proteinGoal" :min="0" :max="300" style="width: 150px"></el-input-number>
            </div>
            <div class="nutrient-item">
              <span class="nutrient-label">碳水化合物（克/天）：</span>
              <el-input-number v-model="preferenceForm.carbGoal" :min="0" :max="500" style="width: 150px"></el-input-number>
            </div>
            <div class="nutrient-item">
              <span class="nutrient-label">脂肪（克/天）：</span>
              <el-input-number v-model="preferenceForm.fatGoal" :min="0" :max="200" style="width: 150px"></el-input-number>
            </div>
          </div>
          <span class="tip">设置您的每日营养摄入目标</span>
        </el-form-item>

        <!-- 口味偏好 -->
        <el-form-item label="口味偏好" prop="flavor">
          <el-radio-group v-model="preferenceForm.flavor">
            <el-radio label="mild">清淡</el-radio>
            <el-radio label="spicy">辛辣</el-radio>
            <el-radio label="sweet">甜味</el-radio>
            <el-radio label="salty">咸味</el-radio>
          </el-radio-group>
          <span class="tip">选择您喜欢的口味</span>
        </el-form-item>

        <!-- 过敏信息 -->
        <el-form-item label="过敏信息" prop="allergyInfo">
          <el-input
            type="textarea"
            v-model="preferenceForm.allergyInfo"
            placeholder="请输入您的过敏原，多个过敏原用逗号分隔，例如：花生,牛奶,海鲜"
            :rows="3"
            style="width: 500px">
          </el-input>
          <span class="tip">填写您的过敏原信息，系统会在推荐食谱时自动过滤</span>
        </el-form-item>

        <!-- 按钮 -->
        <el-form-item>
          <el-button type="primary" @click="savePreference" :loading="saving">保存设置</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 当前设置预览 -->
    <el-card class="preview-card" v-if="currentPreference">
      <div slot="header">
        <span>当前设置预览</span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="饮食类型">
          <el-tag>{{ getDietTypeText(currentPreference.dietType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="口味偏好">
          <el-tag type="success">{{ getFlavorText(currentPreference.flavor) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="蛋白质目标">
          {{ currentPreference.proteinGoal || 0 }} 克/天
        </el-descriptions-item>
        <el-descriptions-item label="碳水化合物目标">
          {{ currentPreference.carbGoal || 0 }} 克/天
        </el-descriptions-item>
        <el-descriptions-item label="脂肪目标">
          {{ currentPreference.fatGoal || 0 }} 克/天
        </el-descriptions-item>
        <el-descriptions-item label="过敏信息">
          <el-tag v-if="!currentPreference.allergyInfo || currentPreference.allergyInfo === '无'" type="info">无</el-tag>
          <el-tag v-else type="warning">{{ currentPreference.allergyInfo }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: localStorage.getItem('userid'),
      preferenceForm: {
        dietType: 'normal',
        proteinGoal: 50,
        carbGoal: 150,
        fatGoal: 40,
        flavor: 'mild',
        allergyInfo: ''
      },
      currentPreference: null,
      rules: {
        dietType: [
          { required: true, message: '请选择饮食类型', trigger: 'change' }
        ],
        flavor: [
          { required: true, message: '请选择口味偏好', trigger: 'change' }
        ]
      },
      saving: false
    }
  },
  created() {
    this.loadPreference();
  },
  methods: {
    // 加载用户偏好
    loadPreference() {
      this.$http.get(`yonghu/preference/${this.userId}`).then(res => {
        if (res.data.code === 0 && res.data.data) {
          const data = res.data.data;
          
          // 解析健康偏好JSON
          if (data.healthPreference) {
            try {
              const healthPref = JSON.parse(data.healthPreference);
              this.preferenceForm.dietType = healthPref.dietType || 'normal';
              this.preferenceForm.flavor = healthPref.flavor || 'mild';
              
              if (healthPref.nutrientGoal) {
                this.preferenceForm.proteinGoal = healthPref.nutrientGoal.protein || 50;
                this.preferenceForm.carbGoal = healthPref.nutrientGoal.carb || 150;
                this.preferenceForm.fatGoal = healthPref.nutrientGoal.fat || 40;
              }
            } catch (e) {
              console.error('解析健康偏好失败:', e);
            }
          }
          
          // 设置过敏信息
          this.preferenceForm.allergyInfo = data.allergyInfo || '';
          
          // 保存当前设置用于预览
          this.currentPreference = { ...this.preferenceForm };
        }
      }).catch(err => {
        console.error('加载偏好设置失败:', err);
      });
    },
    
    // 保存用户偏好
    savePreference() {
      this.$refs.preferenceForm.validate((valid) => {
        if (valid) {
          this.saving = true;
          
          const requestData = {
            userId: this.userId,
            dietType: this.preferenceForm.dietType,
            proteinGoal: this.preferenceForm.proteinGoal,
            carbGoal: this.preferenceForm.carbGoal,
            fatGoal: this.preferenceForm.fatGoal,
            flavor: this.preferenceForm.flavor,
            allergyInfo: this.preferenceForm.allergyInfo || '无'
          };
          
          this.$http.post('yonghu/preference/init', requestData).then(res => {
            this.saving = false;
            if (res.data.code === 0) {
              this.$message.success('偏好设置保存成功');
              this.currentPreference = { ...this.preferenceForm };
              
              // 清除推荐缓存
              this.$http.get(`caipuxinxi/clearCache?userId=${this.userId}`).catch(() => {});
            } else {
              this.$message.error(res.data.msg || '保存失败');
            }
          }).catch(err => {
            this.saving = false;
            this.$message.error('保存失败：' + err.message);
          });
        } else {
          this.$message.warning('请填写必填项');
          return false;
        }
      });
    },
    
    // 重置表单
    resetForm() {
      this.loadPreference();
    },
    
    // 获取饮食类型文本
    getDietTypeText(type) {
      const map = {
        'normal': '普通饮食',
        'vegetarian': '素食',
        'vegan': '纯素',
        'low-carb': '低碳水',
        'low-fat': '低脂',
        'high-protein': '高蛋白'
      };
      return map[type] || type;
    },
    
    // 获取口味文本
    getFlavorText(flavor) {
      const map = {
        'mild': '清淡',
        'spicy': '辛辣',
        'sweet': '甜味',
        'salty': '咸味'
      };
      return map[flavor] || flavor;
    }
  }
}
</script>

<style scoped>
.preference-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.preference-card {
  margin-bottom: 20px;
}

.nutrient-goals {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.nutrient-item {
  display: flex;
  align-items: center;
}

.nutrient-label {
  width: 180px;
  color: #666;
  font-size: 14px;
}

.tip {
  display: block;
  margin-top: 8px;
  color: #999;
  font-size: 12px;
}

.preview-card {
  margin-top: 20px;
}
</style>
