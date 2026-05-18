<template>
  <div>
    <div :style='{"padding":"12px","margin":"10px auto","borderColor":"#dbd9f4","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid"}' class="breadcrumb-preview">
      <el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>统计分析</el-breadcrumb-item>
        <el-breadcrumb-item>常用食材分析</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="statistics-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
      
      <!-- 统计卡片 -->
      <el-row :gutter="20" :style='{"marginBottom":"20px"}'>
        <el-col :span="6">
          <el-card shadow="hover" :style='{"background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"14px","marginBottom":"10px"}'>食材种类</div>
            <div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{stats.totalTypes}}</div>
            <div :style='{"fontSize":"12px","marginTop":"5px","opacity":"0.8"}'>种</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" :style='{"background":"linear-gradient(135deg, #f093fb 0%, #f5576c 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"14px","marginBottom":"10px"}'>总使用次数</div>
            <div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{stats.totalUsage}}</div>
            <div :style='{"fontSize":"12px","marginTop":"5px","opacity":"0.8"}'>次</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" :style='{"background":"linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"14px","marginBottom":"10px"}'>最常用食材</div>
            <div :style='{"fontSize":"24px","fontWeight":"bold"}'>{{stats.mostUsed}}</div>
            <div :style='{"fontSize":"12px","marginTop":"5px","opacity":"0.8"}'>{{stats.mostUsedCount}}次</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" :style='{"background":"linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"14px","marginBottom":"10px"}'>平均使用频率</div>
            <div :style='{"fontSize":"32px","fontWeight":"bold"}'>{{stats.avgFrequency}}</div>
            <div :style='{"fontSize":"12px","marginTop":"5px","opacity":"0.8"}'>次/周</div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 食材使用排行 -->
      <el-card :style='{"marginBottom":"20px"}'>
        <div slot="header">
          <span :style='{"fontSize":"18px","fontWeight":"bold"}'>📊 食材使用排行榜 TOP 10</span>
        </div>
        
        <el-table :data="topFoods" :style='{"width":"100%"}' :row-class-name="tableRowClassName">
          <el-table-column type="index" label="排名" width="80" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.$index === 0" type="danger" size="small">🥇</el-tag>
              <el-tag v-else-if="scope.$index === 1" type="warning" size="small">🥈</el-tag>
              <el-tag v-else-if="scope.$index === 2" type="success" size="small">🥉</el-tag>
              <span v-else>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="shicaiName" label="食材名称" width="200"></el-table-column>
          <el-table-column prop="usageCount" label="使用次数" width="120" align="center">
            <template slot-scope="scope">
              <el-tag type="primary">{{scope.row.usageCount}} 次</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="totalQuantity" label="总使用量" width="150" align="center">
            <template slot-scope="scope">
              {{scope.row.totalQuantity}} {{scope.row.unit}}
            </template>
          </el-table-column>
          <el-table-column prop="lastUsedDate" label="最后使用" width="180"></el-table-column>
          <el-table-column label="使用频率" align="center">
            <template slot-scope="scope">
              <el-progress :percentage="scope.row.percentage" :color="getProgressColor(scope.row.percentage)"></el-progress>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <!-- 食材分类统计 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span :style='{"fontSize":"18px","fontWeight":"bold"}'>🥗 食材使用频率分布</span>
            </div>
            <div :style='{"padding":"20px"}'>
              <div v-for="item in frequencyDistribution" :key="item.level" :style='{"marginBottom":"15px"}'>
                <div :style='{"display":"flex","justifyContent":"space-between","marginBottom":"5px"}'>
                  <span>{{item.label}}</span>
                  <span>{{item.count}} 种 ({{item.percentage}}%)</span>
                </div>
                <el-progress :percentage="item.percentage" :color="item.color" :show-text="false"></el-progress>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span :style='{"fontSize":"18px","fontWeight":"bold"}'>💡 使用建议</span>
            </div>
            <div :style='{"padding":"20px","lineHeight":"2"}'>
              <el-alert v-for="(tip, index) in suggestions" :key="index" :title="tip.title" :type="tip.type" :closable="false" :style='{"marginBottom":"10px"}'>
                <div>{{tip.content}}</div>
              </el-alert>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      stats: {
        totalTypes: 0,
        totalUsage: 0,
        mostUsed: '-',
        mostUsedCount: 0,
        avgFrequency: 0
      },
      topFoods: [],
      frequencyDistribution: [],
      suggestions: []
    }
  },
  created() {
    this.loadStatistics();
  },
  methods: {
    loadStatistics() {
      let userid = this.$storage.get('userid');
      
      // 获取用户的食材使用数据
      this.$http.get('usershicai/list', {
        params: {
          page: 1,
          limit: 1000,
          userid: userid
        }
      }).then(res => {
        if (res.data && res.data.code === 0) {
          this.processStatistics(res.data.data.list);
        }
      });
    },
    
    processStatistics(data) {
      if (!data || data.length === 0) {
        return;
      }
      
      // 统计每种食材的使用情况
      let foodMap = {};
      data.forEach(item => {
        let name = item.shicaiName;
        if (!foodMap[name]) {
          foodMap[name] = {
            shicaiName: name,
            usageCount: 0,
            totalQuantity: 0,
            unit: item.unit || '个',
            lastUsedDate: item.purchaseDate
          };
        }
        foodMap[name].usageCount++;
        foodMap[name].totalQuantity += parseFloat(item.quantity || 0);
        if (item.purchaseDate > foodMap[name].lastUsedDate) {
          foodMap[name].lastUsedDate = item.purchaseDate;
        }
      });
      
      // 转换为数组并排序
      let foodList = Object.values(foodMap);
      foodList.sort((a, b) => b.usageCount - a.usageCount);
      
      // 计算统计数据
      this.stats.totalTypes = foodList.length;
      this.stats.totalUsage = data.length;
      if (foodList.length > 0) {
        this.stats.mostUsed = foodList[0].shicaiName;
        this.stats.mostUsedCount = foodList[0].usageCount;
      }
      this.stats.avgFrequency = (data.length / 4).toFixed(1); // 假设统计4周数据
      
      // TOP 10
      let maxCount = foodList[0].usageCount;
      this.topFoods = foodList.slice(0, 10).map(item => ({
        ...item,
        percentage: Math.round((item.usageCount / maxCount) * 100)
      }));
      
      // 频率分布
      let highFreq = foodList.filter(f => f.usageCount >= 10).length;
      let normalFreq = foodList.filter(f => f.usageCount >= 5 && f.usageCount < 10).length;
      let lowFreq = foodList.filter(f => f.usageCount < 5).length;
      let total = foodList.length;
      
      this.frequencyDistribution = [
        {
          level: 'high',
          label: '高频食材 (≥10次)',
          count: highFreq,
          percentage: Math.round((highFreq / total) * 100),
          color: '#67C23A'
        },
        {
          level: 'normal',
          label: '中频食材 (5-9次)',
          count: normalFreq,
          percentage: Math.round((normalFreq / total) * 100),
          color: '#E6A23C'
        },
        {
          level: 'low',
          label: '低频食材 (<5次)',
          count: lowFreq,
          percentage: Math.round((lowFreq / total) * 100),
          color: '#909399'
        }
      ];
      
      // 生成建议
      this.generateSuggestions(foodList);
    },
    
    generateSuggestions(foodList) {
      this.suggestions = [];
      
      if (foodList.length > 0) {
        let top3 = foodList.slice(0, 3).map(f => f.shicaiName).join('、');
        this.suggestions.push({
          title: '常用食材',
          content: `您最常使用的食材是：${top3}，建议保持适量库存。`,
          type: 'success'
        });
      }
      
      let lowFreqFoods = foodList.filter(f => f.usageCount < 3);
      if (lowFreqFoods.length > 5) {
        this.suggestions.push({
          title: '食材多样性',
          content: `您有 ${lowFreqFoods.length} 种低频使用的食材，建议增加这些食材的使用频率，保持饮食多样性。`,
          type: 'warning'
        });
      }
      
      this.suggestions.push({
        title: '健康提示',
        content: '建议每周摄入至少20种不同的食材，保持营养均衡。',
        type: 'info'
      });
    },
    
    tableRowClassName({row, rowIndex}) {
      if (rowIndex === 0) return 'top-1-row';
      if (rowIndex === 1) return 'top-2-row';
      if (rowIndex === 2) return 'top-3-row';
      return '';
    },
    
    getProgressColor(percentage) {
      if (percentage >= 80) return '#67C23A';
      if (percentage >= 50) return '#E6A23C';
      return '#909399';
    }
  }
}
</script>

<style scoped>
.el-table >>> .top-1-row {
  background: #fef0f0;
}
.el-table >>> .top-2-row {
  background: #fdf6ec;
}
.el-table >>> .top-3-row {
  background: #f0f9ff;
}
</style>
