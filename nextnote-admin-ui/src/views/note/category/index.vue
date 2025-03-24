<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
      style="text-align: center"
    >
      <el-form-item label="分类名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入分类名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
      <!-- 新增按钮放在搜索的最右边 -->
      <el-button
        type="primary"
        plain
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd(null)"
        style="float: right"
        >新增</el-button
      >
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['note:category:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['note:category:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['note:category:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['note:category:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row> -->
    <!-- 展开折叠 -->
    <el-button size="mini" @click="expandAll">展开/折叠</el-button>
    <el-table
      v-loading="loading"
      :data="categoryList"
      row-key="id"
      ref="categoryTable"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="分类名" align="center" prop="name" />
      <el-table-column label="排序" align="center" prop="orderNum" width="80" />
      <el-table-column
        label="备注"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['note:category:add']"
            >新增</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['note:category:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父节点" prop="pid">
          <el-select
            v-model="form.pid"
            placeholder="请选择父节点"
            clearable
            style="width: 100%"
          >
            <el-option :value="0" label="默认" />
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.treeName"
              :value="item.id"
            >
              <span v-html="item.treeName"></span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类名" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCategory,
  getCategory,
  delCategory,
  addCategory,
  updateCategory,
} from "@/api/note/category";
import { handleTree } from "@/utils/ruoyi";

export default {
  name: "Category",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 分类表格数据
      categoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        pid: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      categoryOptions: [],
      isExpand: false, // 添加展开状态标记
    };
  },
  created() {
    this.getList();
    this.getCategoryOptions();
  },
  methods: {
    /** 展开折叠 */
    expandAll() {
      this.isExpand = !this.isExpand;
      const toggleRows = (data) => {
        data.forEach((item) => {
          this.$refs.categoryTable.toggleRowExpansion(item, this.isExpand);
          if (item.children && item.children.length > 0) {
            toggleRows(item.children);
          }
        });
      };
      toggleRows(this.categoryList);
    },
    /** 查询分类列表 */
    getList() {
      this.loading = true;
      listCategory(this.queryParams)
        .then((response) => {
          // 添加数据检查和默认值
          if (response.code === 200) {
            // 确保 response.data 是数组
            const data = response.rows || [];
            this.categoryList =
              data.length > 0 ? handleTree(data, "id", "pid") : [];
          } else {
            this.categoryList = [];
          }
          this.loading = false;
        })
        .catch((error) => {
          console.error("获取分类列表失败:", error);
          this.categoryList = [];
          this.loading = false;
        });
    },
    /** 查询分类列表 */
    getCategoryOptions() {
      listCategory().then((response) => {
        if (response.code === 200) {
          this.categoryOptions = handleTree(response.rows, "id", "pid");
          // 为每个节点添加缩进显示
          this.categoryOptions.forEach((item) => {
            this.setTreeName(item, "");
          });
        }
      });
    },
    // 设置树形显示名称
    setTreeName(item, prefix) {
      item.treeName = prefix + item.name;
      if (item.children && item.children.length) {
        item.children.forEach((child) => {
          this.setTreeName(child, prefix + "　└ ");
        });
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        pid: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 列表中新增按钮操作 */
    handleAdd(row) {
      if (row) {
        this.reset();
        this.form.pid = row.id;
        this.open = true;
        this.title = "添加分类";
      } else {
        this.reset();
        this.form.pid = 0;
        this.open = true;
        this.title = "添加分类";
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCategory(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分类";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateCategory(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCategory(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除分类编号为"' + ids + '"的数据项？')
        .then(function () {
          return delCategory(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "note/category/export",
        {
          ...this.queryParams,
        },
        `category_${new Date().getTime()}.xlsx`
      );
    },
    // 表单重置
    resetForm() {
      this.form = {
        id: undefined,
        name: undefined,
        pid: undefined,
        orderNum: undefined,
        // ... 其他字段
      };
    },
  },
};
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
