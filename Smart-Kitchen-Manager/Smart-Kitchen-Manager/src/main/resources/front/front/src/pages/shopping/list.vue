<template>
  <div class="shopping-container">
    <div class="page-title">智能采购清单</div>
    
    <el-card class="form-card">
      <el-form :inline="true">
        <el-form-item label="选择食谱">
          <el-select v-model="selectedRecipes" multiple placeholder="请选择食谱" style="width: 300px">
            <el-option
              v-for="recipe in recipeList"
              :key="recipe.id"
              :label="recipe.caipumingcheng"
              :value="recipe.id">
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="补充阈值">
          <el-input-number v-model="threshold" :min="0" :max="1" :step="0.1" style="width: 120px"></el-input-number>
          <span style="margin-left: 10px; color: #999;">（低于此比例时补充）</span>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="generateList">生成采购清单</el-button>
          <el-dropdown @command="handleExport" :disabled="!shoppingList">
            <el-button :disabled="!shoppingList">
              导出清单<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="json">导出为JSON</el-dropdown-item>
              <el-dropdown-item command="txt">导出为文本</el-dropdown-item>
              <el-dropdown-item command="csv">导出为CSV</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="shoppingList" class="result-card">
      <div slot="header" class="clearfix">
        <span>采购清单</span>
        <span style="float: right; color: #999;">
          共 {{shoppingList.totalItems}} 项 | 必买 {{shoppingList.mustBuyItems}} 项 | 可选 {{shoppingList.optionalItems}} 项
        </span>
      </div>

      <div v-if="shoppingList.targetRecipes && shoppingList.targetRecipes.length > 0" class="target-recipes">
        <strong>目标食谱：</strong>
        <el-tag v-for="(recipe, index) in shoppingList.targetRecipes" :key="index" style="margin-right: 10px">
          {{recipe}}
        </el-tag>
      </div>

      <el-tabs v-model="activeCategory">
        <el-tab-pane 
          v-for="(items, category) in shoppingList.itemsByCategory" 
          :key="category" 
          :label="category + ' (' + items.length + ')'" 
          :name="category">
          
          <el-table :data="items" style="width: 100%">
            <el-table-column prop="shicaiName" label="食材名称" width="150"></el-table-column>
            <el-table-column prop="suggestedQuantity" label="建议采购量" width="120">
              <template slot-scope="scope">
                {{scope.row.suggestedQuantity}} {{scope.row.unit}}
              </template>
            </el-table-column>
            <el-table-column prop="currentStock" label="当前库存" width="120">
              <template slot-scope="scope">
                {{scope.row.currentStock}} {{scope.row.unit}}
              </template>
            </el-table-column>
            <el-table-column prop="gapQuantity" label="缺口量" width="120">
              <template slot-scope="scope">
                {{scope.row.gapQuantity}} {{scope.row.unit}}
              </template>
            </el-table-column>
            <el-table-column prop="mustBuy" label="类型" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.mustBuy ? 'danger' : 'info'" size="small">
                  {{scope.row.mustBuy ? '必买' : '可选'}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="80">
              <template slot-scope="scope">
                <el-tag :type="getPriorityType(scope.row.priority)" size="small">
                  {{getPriorityText(scope.row.priority)}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="shelfLifeTip" label="保质期提示" min-width="200"></el-table-column>
            <el-table-column prop="usedInRecipes" label="用于食谱" min-width="200"></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-empty v-if="!shoppingList && !loading" description="请选择食谱并生成采购清单"></el-empty>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: localStorage.getItem('userid') || 11,
      recipeList: [],
      selectedRecipes: [],
      threshold: 0.3,
      shoppingList: null,
      activeCategory: '',
      loading: false
    }
  },
  created() {
    this.loadRecipes();
  },
  methods: {
    loadRecipes() {
      this.$http.get('caipuxinxi/list', {
        params: {
          page: 1,
          limit: 100
        }
      }).then(res => {
        if (res.data.code === 0) {
          this.recipeList = res.data.data.list;
        }
      });
    },
    
    generateList() {
      if (this.selectedRecipes.length === 0) {
        this.$message.warning('请至少选择一个食谱');
        return;
      }
      
      this.loading = true;
      const recipeIds = this.selectedRecipes.join(',');
      
      this.$http.get('shopping/suggest', {
        params: {
          userId: this.userId,
          recipeIds: recipeIds,
          threshold: this.threshold,
          includeSafetyStock: true,
          safetyStockFactor: 1.2
        }
      }).then(res => {
        this.loading = false;
        if (res.data.code === 0) {
          this.shoppingList = res.data.data;
          if (this.shoppingList.itemsByCategory) {
            const categories = Object.keys(this.shoppingList.itemsByCategory);
            if (categories.length > 0) {
              this.activeCategory = categories[0];
            }
          }
          this.$message.success('采购清单生成成功');
        } else {
          this.$message.error(res.data.msg || '生成失败');
        }
      }).catch(err => {
        this.loading = false;
        this.$message.error('生成失败：' + err.message);
      });
    },
    
    exportList() {
      if (!this.shoppingList) {
        this.$message.warning('请先生成采购清单');
        return;
      }
      
      // 方法1：直接下载当前清单数据
      this.downloadAsJson();
      
      // 方法2：调用后端导出接口（备用）
      // window.open(`${this.$config.baseUrl}shopping/export?userId=${this.userId}&format=json`, '_blank');
    },
    
    handleExport(command) {
      if (!this.shoppingList) {
        this.$message.warning('请先生成采购清单');
        return;
      }
      
      switch (command) {
        case 'json':
          this.downloadAsJson();
          break;
        case 'txt':
          this.downloadAsTxt();
          break;
        case 'csv':
          this.downloadAsCsv();
          break;
        default:
          this.downloadAsJson();
      }
    },
    
    exportList() {
      // 保持向后兼容
      this.downloadAsJson();
    },
    
    downloadAsJson() {
      try {
        // 创建要导出的数据
        const exportData = {
          userId: this.userId,
          generateTime: new Date().toLocaleString('zh-CN'),
          totalItems: this.shoppingList.totalItems,
          mustBuyItems: this.shoppingList.mustBuyItems,
          optionalItems: this.shoppingList.optionalItems,
          targetRecipes: this.shoppingList.targetRecipes,
          itemsByCategory: this.shoppingList.itemsByCategory,
          items: this.shoppingList.items || []
        };
        
        // 转换为JSON字符串
        const jsonStr = JSON.stringify(exportData, null, 2);
        
        // 创建Blob对象
        const blob = new Blob([jsonStr], { type: 'application/json;charset=utf-8' });
        
        // 创建下载链接
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `采购清单_${this.userId}_${new Date().getTime()}.json`;
        
        // 触发下载
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        // 释放URL对象
        window.URL.revokeObjectURL(url);
        
        this.$message.success('JSON格式采购清单导出成功');
        
      } catch (error) {
        console.error('导出失败:', error);
        this.$message.error('导出失败: ' + error.message);
      }
    },
    
    downloadAsTxt() {
      try {
        let txtContent = `智能采购清单\n`;
        txtContent += `生成时间: ${new Date().toLocaleString('zh-CN')}\n`;
        txtContent += `用户ID: ${this.userId}\n`;
        txtContent += `总计: ${this.shoppingList.totalItems}项 | 必买: ${this.shoppingList.mustBuyItems}项 | 可选: ${this.shoppingList.optionalItems}项\n\n`;
        
        if (this.shoppingList.targetRecipes && this.shoppingList.targetRecipes.length > 0) {
          txtContent += `目标食谱: ${this.shoppingList.targetRecipes.join(', ')}\n\n`;
        }
        
        // 按分类输出
        for (const [category, items] of Object.entries(this.shoppingList.itemsByCategory)) {
          txtContent += `【${category}】\n`;
          items.forEach((item, index) => {
            txtContent += `${index + 1}. ${item.shicaiName}\n`;
            txtContent += `   建议采购: ${item.suggestedQuantity} ${item.unit}\n`;
            txtContent += `   当前库存: ${item.currentStock} ${item.unit}\n`;
            txtContent += `   缺口量: ${item.gapQuantity} ${item.unit}\n`;
            txtContent += `   类型: ${item.mustBuy ? '必买' : '可选'}\n`;
            txtContent += `   优先级: ${this.getPriorityText(item.priority)}\n`;
            if (item.usedInRecipes) {
              txtContent += `   用于食谱: ${item.usedInRecipes}\n`;
            }
            txtContent += `\n`;
          });
          txtContent += `\n`;
        }
        
        // 创建Blob对象
        const blob = new Blob([txtContent], { type: 'text/plain;charset=utf-8' });
        
        // 创建下载链接
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `采购清单_${this.userId}_${new Date().getTime()}.txt`;
        
        // 触发下载
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        // 释放URL对象
        window.URL.revokeObjectURL(url);
        
        this.$message.success('文本格式采购清单导出成功');
        
      } catch (error) {
        console.error('导出失败:', error);
        this.$message.error('导出失败: ' + error.message);
      }
    },
    
    downloadAsCsv() {
      try {
        let csvContent = '\uFEFF'; // BOM for UTF-8
        csvContent += '分类,食材名称,建议采购量,单位,当前库存,缺口量,类型,优先级,保质期提示,用于食谱\n';
        
        // 遍历所有分类
        for (const [category, items] of Object.entries(this.shoppingList.itemsByCategory)) {
          items.forEach(item => {
            const row = [
              category,
              item.shicaiName,
              item.suggestedQuantity,
              item.unit,
              item.currentStock,
              item.gapQuantity,
              item.mustBuy ? '必买' : '可选',
              this.getPriorityText(item.priority),
              item.shelfLifeTip || '',
              item.usedInRecipes || ''
            ];
            
            // 处理包含逗号的字段，用双引号包围
            const csvRow = row.map(field => {
              const str = String(field);
              if (str.includes(',') || str.includes('"') || str.includes('\n')) {
                return '"' + str.replace(/"/g, '""') + '"';
              }
              return str;
            }).join(',');
            
            csvContent += csvRow + '\n';
          });
        }
        
        // 创建Blob对象
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8' });
        
        // 创建下载链接
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `采购清单_${this.userId}_${new Date().getTime()}.csv`;
        
        // 触发下载
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        // 释放URL对象
        window.URL.revokeObjectURL(url);
        
        this.$message.success('CSV格式采购清单导出成功');
        
      } catch (error) {
        console.error('导出失败:', error);
        this.$message.error('导出失败: ' + error.message);
      }
    },
    
    getPriorityType(priority) {
      const types = {1: 'danger', 2: 'warning', 3: 'success'};
      return types[priority] || 'info';
    },
    
    getPriorityText(priority) {
      const texts = {1: '高', 2: '中', 3: '低'};
      return texts[priority] || '未知';
    }
  }
}
</script>

<style scoped>
.shopping-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.form-card {
  margin-bottom: 20px;
}

.result-card {
  margin-top: 20px;
}

.target-recipes {
  margin-bottom: 20px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
