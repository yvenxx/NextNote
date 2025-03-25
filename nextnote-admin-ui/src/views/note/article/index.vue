<template>
  <div class="app-container">
    <!-- 左右布局容器 -->
    <el-row :gutter="20">
      <!-- 左侧列表 -->
      <el-col :span="4">
        <div class="left-panel">
          <el-input
            v-model="queryParams.category"
            placeholder="搜索分类"
            clearable
            style="margin-bottom: 20px"
          />

          <el-tree
            :data="categoryList"
            :props="defaultProps"
            @node-click="handleNodeClick"
            default-expand-all
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                <!-- 左侧图标和名称 -->
                <div class="node-content">
                  <i :class="data.type === 'category' ? 'el-icon-folder' : 'el-icon-document'" style="margin-right: 8px"></i>
                  <span v-if="!data.isEditing">{{ node.label }}</span>
                  <el-input
                    v-else
                    v-model="data.name"
                    size="mini"
                    @blur="handleNameConfirm(data)"
                    @keyup.enter.native="handleNameConfirm(data)"
                    @click.native.stop
                    ref="nameInput"
                  ></el-input>
                </div>
                
                <!-- 右侧操作按钮 -->
                <div class="node-actions" @click.stop>
                  <el-button
                    v-if="data.type === 'category'"
                    type="text"
                    size="mini"
                    @click.stop="handleAddArticle(data, node)"
                  >
                    <i class="el-icon-plus"></i>
                  </el-button>
                  <el-dropdown
                    v-else
                    trigger="click"
                    @command="handleCommand($event, data, node)"
                  >
                    <el-button type="text" size="mini">
                      <i class="el-icon-more"></i>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="rename">重命名</el-dropdown-item>
                        <el-dropdown-item command="delete">删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </template>
          </el-tree>
        </div>
      </el-col>

      <!-- 右侧表单 -->
      <el-col :span="20">
        <div class="right-panel">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" v-show="editorVisible">
            <el-row >
              <el-col :span="24">
                <el-button
                  size="mini"
                  @click="submitForm"
                  style="margin-bottom: 10px"
                  >更新</el-button
                >
                <div id="vditor"></div>
              </el-col>
              <!-- <el-col :span="4" style="margin-top: 15px">
                <div class="right-panel">大纲</div>
              </el-col> -->
            </el-row>
            <!-- <el-form-item label="" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item> -->
            <!-- <el-form-item label="内容" prop="description">
              <el-input 
                v-model="form.description" 
                type="textarea" 
                :rows="15"
                placeholder="请输入内容" 
              />
            </el-form-item> -->
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  listArticle,
  getArticle,
  delArticle,
  addArticle,
  updateArticle,
} from "@/api/note/article";
import { listCategory } from "@/api/note/category";
import Vditor from "vditor";
import "vditor/dist/index.css";

export default {
  name: "Article",
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
      // 文章管理表格数据
      articleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        category: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 分类树数据
      categoryList: [],
      // 树的配置
      defaultProps: {
        children: "children",
        label: "name",
      },
      contentEditor: {},
      editorVisible: false,
    };
  },
  created() {
    this.getList();
    this.getCategoryList();
  },
  methods: {
    /** 查询文章管理列表 */
    getList() {
      this.loading = true;
      listArticle(this.queryParams).then((response) => {
        this.articleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        title: null,
        category: null,
        description: null,
        content: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文章管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getArticle(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文章管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            this.form.description = this.contentEditor.getValue();
            this.form.content = this.contentEditor.getHTML();
            updateArticle(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addArticle(this.form).then((response) => {
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
        .confirm('是否确认删除文章管理编号为"' + ids + '"的数据项？')
        .then(function () {
          return delArticle(ids);
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
        "note/article/export",
        {
          ...this.queryParams,
        },
        `article_${new Date().getTime()}.xlsx`
      );
    },
    // 获取分类列表
    getCategoryList() {
      // 使用 Promise.all 并行请求并等待结果
      Promise.all([listCategory(), listArticle()]).then(([catRes, artRes]) => {
        const categoryList = catRes.rows;
        const articleList = artRes.rows;
        // 确保数据到位后再构建树
        this.categoryList = this.buildTree(categoryList, articleList);
      });
    },
    // 构建树形结构
    buildTree(categoryList, articleList) {
      const map = {};
      const result = [];

      // 创建一个辅助函数来在整个分类树中查找
      const findCategory = (categories, categoryId) => {
        for (let category of categories) {
          if (category.id === categoryId) {
            return category;
          }
          if (category.children) {
            const found = findCategory(category.children, categoryId);
            if (found) return found;
          }
        }
        return null;
      };

      // 构建分类树的代码保持不变
      categoryList.forEach((item) => {
        map[item.id] = item;
      });

      categoryList.forEach((item) => {
        if (item.pid === 0) {
          item.type = "category";
          result.push(item);
        } else {
          const parent = map[item.pid];
          if (parent) {
            item.type = "category";
            parent.children = parent.children || [];
            parent.children.push(item);
          }
        }
      });

      if (articleList != undefined && articleList.length > 0) {
        articleList.forEach((item) => {
          // 使用新的查找函数在整个分类树中查找
          const category = findCategory(result, item.category);

          if (category) {
            item.type = "article";
            item.name = item.title;
            category.children = category.children || [];
            category.children.push(item);
          }
        });
      }
      return result;
    },
    // 处理节点点击
    handleNodeClick(data) {
      // 如果节点正在编辑中，不触发点击事件
      if (data.isEditing) {
        return;
      }
      
      // 处理节点点击事件，加载对应的文章内容
      if (data.type === "article") {
        this.contentEditor = new Vditor("vditor", {
          height: "850px",
          width: "100%",
          theme: "light",
          outline: {
            enable: true,
            position: "right",
          },
          theme: "ant-design",
        });
        getArticle(data.id).then((response) => {
          this.contentEditor.setValue(response.data.description || "");
        });
        this.form.id = data.id;
        this.form.description = data.description;
        this.editorVisible = true;
        // 当前节点颜色标识
        this.$set(data, 'isActive', true);
      }
    },
    // 处理下拉菜单命令
    handleCommand(command, data, node) {
      if (command === 'rename') {
        // 使用 Vue.set 或 this.$set 来确保响应式更新
        this.$set(data, 'name', data.title || node.label);
        this.$set(data, 'isEditing', true);
        
        this.$nextTick(() => {
          const inputs = this.$refs.nameInput;
          if (inputs && inputs.length > 0) {
            inputs[0].focus();
          }
        });
      } else if (command === 'delete') {
        this.handleDelete(data);
      }
    },
    // 处理重命名确认
    handleNameConfirm(data) {
      // 防止重复提交
      if (data.isSubmitting) {
        return;
      }

      if (!data.name || data.name.trim() === '') {
        this.$modal.msgError("名称不能为空");
        return;
      }

      // 如果名称没有改变，直接关闭编辑状态
      if (data.name === data.title) {
        this.$set(data, 'isEditing', false);
        return;
      }

      this.$set(data, 'isSubmitting', true);

      if (data.isNew) {
        // 处理新添加的文章
        const params = {
          title: data.name,
          category: data.category
        };
        addArticle(params).then(response => {
          this.$modal.msgSuccess("添加成功");
          this.$set(data, 'id', response.data.id);
          this.$set(data, 'isNew', false);
          this.$set(data, 'isEditing', false);
          this.$set(data, 'title', data.name);
        }).catch(() => {
          const parent = this.findParentNode(this.categoryList, data);
          if (parent && parent.children) {
            const index = parent.children.findIndex(item => item === data);
            if (index !== -1) {
              parent.children.splice(index, 1);
            }
          }
        }).finally(() => {
          this.$set(data, 'isSubmitting', false);
        });
      } else {
        // 处理重命名
        const params = {
          id: data.id,
          title: data.name
        };
        updateArticle(params).then(() => {
          this.$modal.msgSuccess("更新成功");
          this.$set(data, 'title', data.name);
          this.$set(data, 'isEditing', false);
        }).catch(() => {
          this.$set(data, 'name', data.title);
          this.$set(data, 'isEditing', false);
        }).finally(() => {
          this.$set(data, 'isSubmitting', false);
        });
      }
      
      this.getCategoryList();
    },
    // 添加新文章
    handleAddArticle(category) {
      const newArticle = {
        name: '',
        title: '',
        category: category.id,
        type: 'article',
        isEditing: true,
        isNew: true
      };
      
      if (!category.children) {
        category.children = [];
      }
      category.children.unshift(newArticle);
      
      this.$nextTick(() => {
        const inputs = this.$refs.nameInput;
        if (inputs && inputs.length > 0) {
          inputs[0].focus();
        }
      });
    },
    // 查找父节点的辅助方法
    findParentNode(nodes, targetNode) {
      for (const node of nodes) {
        if (node.children && node.children.includes(targetNode)) {
          return node;
        }
        if (node.children) {
          const parent = this.findParentNode(node.children, targetNode);
          if (parent) return parent;
        }
      }
      return null;
    }
  },
  mounted() {},
};
</script>

<style scoped>
.left-panel {
  background: #fff;
  padding: 20px;
  height: calc(100vh - 120px);
  overflow: auto;
  border-right: 1px solid #dcdfe6;
}

.right-panel {
  background: #fff;
  padding: 20px;
  height: calc(100vh - 120px);
  overflow: auto;
  font-size: 14px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  width: 100%;
}

.node-content {
  display: flex;
  align-items: center;
  flex: 1;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.node-actions {
  display: none;
  margin-left: 8px;
}

.custom-tree-node:hover .node-actions {
  display: flex;
  align-items: center;
}

/* 输入框样式 */
.el-input.el-input--mini {
  width: calc(100% - 24px); /* 减去图标的宽度 */
}

/* 按钮样式 */
.node-actions .el-button--text {
  padding: 0 4px;
}

/* 图标样式 */
.el-icon-folder {
  color: #909399;
}

.el-icon-document {
  color: #606266;
}

/* 下拉菜单样式 */
.el-dropdown {
  margin-left: 4px;
}
</style>
