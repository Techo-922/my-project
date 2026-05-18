<template>
  <div class="main-content">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item label="食材名称">
        <el-input v-model="searchForm.shicaiName" placeholder="请输入食材名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList()" icon="el-icon-search">查询</el-button>
        <el-button type="success" @click="addOrUpdateHandle()" icon="el-icon-plus">新增</el-button>
        <el-button type="danger" @click="deleteHandle()" icon="el-icon-delete" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="id" header-align="center" align="center" label="ID" width="80"></el-table-column>
      <el-table-column prop="shicaiId" header-align="center" align="center" label="食材ID" width="100"></el-table-column>
      <el-table-column prop="shicaiName" header-align="center" align="center" label="食材名称" min-width="120"></el-table-column>
      <el-table-column prop="category" header-align="center" align="center" label="分类" width="100"></el-table-column>
      <el-table-column prop="shelfLifeDays" header-align="center" align="center" label="保质期(天)" width="120">
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.shelfLifeDays }} 天</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="storageMethod" header-align="center" align="center" label="存储方法" min-width="200" show-overflow-tooltip></el-table-column>
      <el-table-column prop="addtime" header-align="center" align="center" label="添加时间" width="160"></el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
// 修改时间: 2025-12-31 13:30 - 修复删除功能，确保ID转为数字类型
import AddOrUpdate from './add-or-update'
export default {
  data() {
    return {
      searchForm: {
        shicaiName: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  mounted() {
    this.getDataList()
  },
  activated() {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: '/shicaishelflife/page',
        method: 'get',
        params: {
          page: this.pageIndex,
          limit: this.pageSize,
          shicaiName: this.searchForm.shicaiName
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list
          this.totalPage = data.data.total
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle(id) {
      let ids = id ? [Number(id)] : this.dataListSelections.map(item => {
        return Number(item.id)
      })
      this.$confirm(`确定要删除选中的${ids.length}条记录吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/shicaishelflife/delete',
          method: 'post',
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.main-content {
  padding: 20px;
  background: #fff;
}
</style>
