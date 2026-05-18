<template>
  <div>
    <div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
      <el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>统计分析</el-breadcrumb-item>
        <el-breadcrumb-item>健康偏好管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="preference-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
      
      <!-- 营养目标设置 -->
      <el-card :style='{"marginBottom":"20px"}'>
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>🎯 每日营养目标</span>
          <el-button style="float: right" type="primary" size="small" @click="savePreference">保存设置</el-button>
        </div>
        
        <el-form :model="preference" label-width="150px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="饮食偏好类型">
                <el-select v-model="preference.preferenceType" placeholder="请选择" :style='{"width":"100%"}'>
                  <el-option label="均衡饮食" value="balanced"></el-option>
                  <el-option label="素食主义" value="vegetarian"></el-option>
                  <el-option label="低碳水化合物" value="low_carb"></el-option>
                  <el-option label="高蛋白" value="high_protein"></el-option>
                  <el-option label="低脂肪" value="low_fat"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="每日卡路里目标">
                <el-input-number v-model="preference.dailyCalorieTarget" :min="1000" :max="5000" :step="100" :style='{"width":"100%"}'></el-input-number>
                <span style="margin-left: 10px">千卡</span>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="每日蛋白质目标">
                <el-input-number v-model="preference.dailyProteinTarget" :min="0" :max="500" :precision="1" :style='{"width":"100%"}'></el-input-number>
                <span style="margin-left: 10px">克</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="每日碳水化合物">
                <el-input-number v-model="preference.dailyCarbsTarget" :min="0" :max="1000" :precision="1" :style='{"width":"100%"}'></el-input-number>
                <span style="margin-left: 10px">克</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="每日脂肪目标">
                <el-input-number v-model="preference.dailyFatTarget" :min="0" :max="300" :precision="1" :style='{"width":"100%"}'></el-input-number>
                <span style="margin-left: 10px">克</span>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
      
      <!-- 食材偏好设置 -->
      <el-row :gutter="20" :style='{"marginBottom":"20px"}'>
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span :style='{"fontSize":"16px","fontWeight":"bold"}'>❤️ 喜欢的食材</span>
            </div>
            <el-tag v-for="(food, index) in favoriteFoods" :key="index" closable @close="removeFavorite(index)" :style='{"margin":"5px"}'>
              {{food}}
            </el-tag>
            <el-input v-model="newFavorite" placeholder="添加喜欢的食材" size="small" :style='{"marginTop":"10px"}' @keyup.enter.native="addFavorite">
              <el-button slot="append" icon="el-icon-plus" @click="addFavorite"></el-button>
            </el-input>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span :style='{"fontSize":"16px","fontWeight":"bold"}'>😐 不喜欢的食材</span>
            </div>
            <el-tag v-for="(food, index) in dislikeFoods" :key="index" type="info" closable @close="removeDislike(index)" :style='{"margin":"5px"}'>
              {{food}}
            </el-tag>
            <el-input v-model="newDislike" placeholder="添加不喜欢的食材" size="small" :style='{"marginTop":"10px"}' @keyup.enter.native="addDislike">
              <el-button slot="append" icon="el-icon-plus" @click="addDislike"></el-button>
            </el-input>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span :style='{"fontSize":"16px","fontWeight":"bold"}'>⚠️ 过敏食材</span>
            </div>
            <el-tag v-for="(food, index) in allergicFoods" :key="index" type="danger" closable @close="removeAllergic(index)" :style='{"margin":"5px"}'>
              {{food}}
            </el-tag>
            <el-input v-model="newAllergic" placeholder="添加过敏食材" size="small" :style='{"marginTop":"10px"}' @keyup.enter.native="addAllergic">
              <el-button slot="append" icon="el-icon-plus" @click="addAllergic"></el-button>
            </el-input>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 健康目标 -->
      <el-card :style='{"marginBottom":"20px"}'>
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>🏃 健康目标</span>
        </div>
        <el-input type="textarea" v-model="preference.healthGoals" :rows="4" placeholder="例如：减重5公斤、增肌、改善睡眠质量等"></el-input>
      </el-card>
      
      <!-- 营养摄入分析 -->
      <el-card>
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>📈 近期营养摄入分析</span>
          <el-button style="float: right" size="small" @click="refreshAnalysis">刷新</el-button>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="nutrition-card">
              <div class="nutrition-label">卡路里</div>
              <div class="nutrition-value">{{analysis.avgCalories}}</div>
              <div class="nutrition-unit">千卡/天</div>
              <el-progress :percentage="getPercentage(analysis.avgCalories, preference.dailyCalorieTarget)" :color="getProgressColor(analysis.avgCalories, preference.dailyCalorieTarget)"></el-progress>
              <div class="nutrition-target">目标: {{preference.dailyCalorieTarget}} 千卡</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="nutrition-card">
              <div class="nutrition-label">蛋白质</div>
              <div class="nutrition-value">{{analysis.avgProtein}}</div>
              <div class="nutrition-unit">克/天</div>
              <el-progress :percentage="getPercentage(analysis.avgProtein, preference.dailyProteinTarget)" :color="getProgressColor(analysis.avgProtein, preference.dailyProteinTarget)"></el-progress>
              <div class="nutrition-target">目标: {{preference.dailyProteinTarget}} 克</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="nutrition-card">
              <div class="nutrition-label">碳水化合物</div>
              <div class="nutrition-value">{{analysis.avgCarbs}}</div>
              <div class="nutrition-unit">克/天</div>
              <el-progress :percentage="getPercentage(analysis.avgCarbs, preference.dailyCarbsTarget)" :color="getProgressColor(analysis.avgCarbs, preference.dailyCarbsTarget)"></el-progress>
              <div class="nutrition-target">目标: {{preference.dailyCarbsTarget}} 克</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="nutrition-card">
              <div class="nutrition-label">脂肪</div>
              <div class="nutrition-value">{{analysis.avgFat}}</div>
              <div class="nutrition-unit">克/天</div>
              <el-progress :percentage="getPercentage(analysis.avgFat, preference.dailyFatTarget)" :color="getProgressColor(analysis.avgFat, preference.dailyFatTarget)"></el-progress>
              <div class="nutrition-target">目标: {{preference.dailyFatTarget}} 克</div>
            </div>
          </el-col>
        </el-row>
        
        <el-divider></el-divider>
        
        <div :style='{"padding":"20px","background":"#f5f7fa","borderRadius":"4px"}'>
          <h4>💡 个性化建议</h4>
          <el-alert v-for="(tip, index) in healthTips" :key="index" :title="tip.title" :type="tip.type" :closable="false" :style='{"marginBottom":"10px"}'>
            <div>{{tip.content}}</div>
          </el-alert>
        </div>
      </el-card>
      
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      preference: {
        preferenceType: 'balanced',
        dailyCalorieTarget: 2000,
        dailyProteinTarget: 80,
        dailyCarbsTarget: 250,
        dailyFatTarget: 60,
        healthGoals: ''
      },
      favoriteFoods: [],
      dislikeFoods: [],
      allergicFoods: [],
      newFavorite: '',
      newDislike: '',
      newAllergic: '',
      analysis: {
        avgCalories: 0,
        avgProtein: 0,
        avgCarbs: 0,
        avgFat: 0
      },
      healthTips: []
    }
  },
  created() {
    this.loadPreference();
    this.loadNutritionAnalysis();
  },
  methods: {
    loadPreference() {
      // 从 localStorage 加载偏好设置
      let saved = localStorage.getItem('healthPreference');
      if (saved) {
        let data = JSON.parse(saved);
        this.preference = data.preference || this.preference;
        this.favoriteFoods = data.favoriteFoods || [];
        this.dislikeFoods = data.dislikeFoods || [];
        this.allergicFoods = data.allergicFoods || [];
      }
    },
    
    savePreference() {
      let data = {
        preference: this.preference,
        favoriteFoods: this.favoriteFoods,
        dislikeFoods: this.dislikeFoods,
        allergicFoods: this.allergicFoods
      };
      localStorage.setItem('healthPreference', JSON.stringify(data));
      this.$message.success('保存成功！');
    },
    
    addFavorite() {
      if (this.newFavorite.trim()) {
        this.favoriteFoods.push(this.newFavorite.trim());
        this.newFavorite = '';
      }
    },
    
    removeFavorite(index) {
      this.favoriteFoods.splice(index, 1);
    },
    
    addDislike() {
      if (this.newDislike.trim()) {
        this.dislikeFoods.push(this.newDislike.trim());
        this.newDislike = '';
      }
    },
    
    removeDislike(index) {
      this.dislikeFoods.splice(index, 1);
    },
    
    addAllergic() {
      if (this.newAllergic.trim()) {
        this.allergicFoods.push(this.newAllergic.trim());
        this.newAllergic = '';
      }
    },
    
    removeAllergic(index) {
      this.allergicFoods.splice(index, 1);
    },
    
    loadNutritionAnalysis() {
      let userid = this.$storage.get('userid');
      
      // 获取最近7天的饮食统计
      this.$http.get('dietstatistics/list', {
        params: {
          page: 1,
          limit: 100,
          userid: userid
        }
      }).then(res => {
        if (res.data && res.data.code === 0) {
          this.processNutritionData(res.data.data.list);
        }
      });
    },
    
    processNutritionData(data) {
      if (!data || data.length === 0) {
        return;
      }
      
      // 计算平均值
      let totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFat = 0;
      let count = data.length;
      
      data.forEach(item => {
        totalCalories += parseFloat(item.calories || 0);
        totalProtein += parseFloat(item.protein || 0);
        totalCarbs += parseFloat(item.carbs || 0);
        totalFat += parseFloat(item.fat || 0);
      });
      
      this.analysis.avgCalories = Math.round(totalCalories / count);
      this.analysis.avgProtein = (totalProtein / count).toFixed(1);
      this.analysis.avgCarbs = (totalCarbs / count).toFixed(1);
      this.analysis.avgFat = (totalFat / count).toFixed(1);
      
      // 生成健康建议
      this.generateHealthTips();
    },
    
    generateHealthTips() {
      this.healthTips = [];
      
      // 卡路里建议
      let calorieRatio = this.analysis.avgCalories / this.preference.dailyCalorieTarget;
      if (calorieRatio < 0.8) {
        this.healthTips.push({
          title: '卡路里摄入不足',
          content: `您的平均卡路里摄入为 ${this.analysis.avgCalories} 千卡，低于目标值。建议适当增加主食和优质蛋白的摄入。`,
          type: 'warning'
        });
      } else if (calorieRatio > 1.2) {
        this.healthTips.push({
          title: '卡路里摄入过高',
          content: `您的平均卡路里摄入为 ${this.analysis.avgCalories} 千卡，高于目标值。建议控制高热量食物的摄入，增加运动量。`,
          type: 'warning'
        });
      } else {
        this.healthTips.push({
          title: '卡路里摄入合理',
          content: '您的卡路里摄入在目标范围内，请继续保持！',
          type: 'success'
        });
      }
      
      // 蛋白质建议
      let proteinRatio = this.analysis.avgProtein / this.preference.dailyProteinTarget;
      if (proteinRatio < 0.8) {
        this.healthTips.push({
          title: '蛋白质摄入不足',
          content: '建议增加鸡蛋、鱼肉、豆制品等优质蛋白的摄入。',
          type: 'info'
        });
      }
      
      // 营养均衡建议
      this.healthTips.push({
        title: '营养均衡提示',
        content: '建议每天摄入多种颜色的蔬菜水果，保持饮食多样性。',
        type: 'info'
      });
    },
    
    refreshAnalysis() {
      this.loadNutritionAnalysis();
      this.$message.success('数据已刷新');
    },
    
    getPercentage(actual, target) {
      if (!target) return 0;
      let percentage = Math.round((actual / target) * 100);
      return Math.min(percentage, 100);
    },
    
    getProgressColor(actual, target) {
      if (!target) return '#909399';
      let ratio = actual / target;
      if (ratio >= 0.8 && ratio <= 1.2) return '#67C23A';
      if (ratio >= 0.6 && ratio < 0.8) return '#E6A23C';
      if (ratio > 1.2 && ratio <= 1.4) return '#E6A23C';
      return '#F56C6C';
    }
  }
}
</script>

<style scoped>
.nutrition-card {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}
.nutrition-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.nutrition-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
}
.nutrition-unit {
  font-size: 12px;
  color: #909399;
  margin-bottom: 15px;
}
.nutrition-target {
  font-size: 12px;
  color: #606266;
  margin-top: 10px;
}
</style>
