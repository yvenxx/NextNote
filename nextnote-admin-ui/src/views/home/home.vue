<template>
  <div class="home-container">
    <!-- 顶部导航 -->
    <app-header />

    <el-row class="main-container" :gutter="30" type="flex" justify="left">
      <!-- 文章列表区域-->
      <el-col
        :offset="4"
        :xs="24"
        :sm="24"
        :md="12"
        :lg="12"
        :xl="12"
        class="content-col"
      >
        <div v-if="articles.length === 0" class="no-posts">
          <el-empty description="暂无文章"></el-empty>
        </div>
        <div
          v-else
          v-for="article in articles"
          :key="article.id"
          class="post-item"
        >
          <div class="post-category">{{ categories.find(category => category.id === article.category).name }}</div>
          <a class="post-title" :href="'/article/' + article.id">{{ article.title }}</a>
          <div class="post-content" v-html="article.content"></div>
          <a class="read-more" :href="'/article/' + article.id">继续阅读</a>
          <div class="post-meta">
            <span class="post-author"><i class="el-icon-user"></i>&nbsp;{{ article.createBy }}&nbsp;/&nbsp;</span>
            <span class="post-date"><i class="el-icon-time"></i>&nbsp;{{ article.createTime }}</span>
          </div>
        </div>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handleCurrentChange"
          class="pagination"
        ></el-pagination>
      </el-col>

      <!-- 侧边栏 -->
      <el-col :xs="24" :sm="24" :md="4" :lg="4" :xl="4" class="sidebar-col">
        <!-- 搜索框 -->
        <el-input v-model="searchQuery" placeholder="搜索" class="search-box">
          <template #append>
            <el-button>
              <i class="el-icon-search"></i>
            </el-button>
          </template>
        </el-input>

        <!-- 分类 -->
        <div class="widget">
          <h3 style="font-weight: bold;font-size: 18px;">分类</h3>
          <ul class="category-list">
            <li v-for="category in rootCategories" :key="category.id">
              <!-- 如果count 不大于0，链接就点不了 -->
              <a v-if="category.count > 0" :href="'/category/' + category.id">{{ category.name }} ({{ category.count }})</a>
              <span v-else>{{ category.name }}</span>
              <ul v-if="getChildCategories(category.id).length > 0" class="sub-category-list">
                <li v-for="childCategory in getChildCategories(category.id)" :key="childCategory.id">
                  <a v-if="childCategory.count > 0" :href="'/category/' + childCategory.id">{{ childCategory.name }} ({{ childCategory.count }})</a>
                  <span v-else>{{ childCategory.name }}</span>
                </li>
              </ul>
            </li>
          </ul>
        </div>

        <!-- 归档 -->
        <!-- <div class="widget">
          <h3>归档</h3>
          <ul class="archive-list">
            <li v-for="archive in archives" :key="archive.year">
              <a href="#">{{ archive.year }} ({{ archive.count }})</a>
            </li>
          </ul>
        </div> -->
      </el-col>
    </el-row>
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "@/components/Header.vue";
import AppFooter from "@/components/Footer.vue";
import { listArticle, listCategory } from "@/api/note/home";

export default {
  name: "Home",
  components: {
    AppHeader,
    AppFooter
  },
  data() {
    return {
      articles: [],
      categories: [],
      searchQuery: "",
      // 分页相关数据
      currentPage: 1,
      pageSize: 15,
      total: 0
    };
  },
  computed: {
    rootCategories() {
      return this.categories.filter(category => !category.pid || category.pid === 0);
    }
  },
  methods: {
    async fetchArticle(page = 1) {
      try {
        const response = await listArticle({
          pageNum: page,
          pageSize: this.pageSize
        });
        this.articles = response.rows;
        this.total = response.total;
      } catch (error) {
        console.error("获取文章列表失败:", error);
      }
    },
    async fetchCategories() {
      try {
        const response = await listCategory();
        this.categories = response.data;
      } catch (error) {
        console.error("获取分类列表失败:", error);
      }
    },
    getChildCategories(parentId) {
      return this.categories.filter(category => category.pid === parentId);
    },
    // 处理分页变化
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchArticle(val);
      // 滚动到页面顶部
      window.scrollTo(0, 0);
    }
  },
  created() {
    this.fetchArticle(this.currentPage);
    this.fetchCategories();
  },
};
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.main-container {
  width: 100%;
  margin: 0 auto;
  margin-top: 20px;
  padding: 2rem 0;
}

.content-col {
  border-right: 1px solid #eee;
  padding-top: 20px;
}
.sidebar-col {
  font-size: 14px;
  padding-top: 20px;
  padding-left: 30px !important;
}

.no-posts {
  padding: 2rem 0;
}

.post-item {
  margin-bottom: 2rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #eee;
}

.post-title {
  font-size: 1.8rem;
  margin: 0.5rem 0;
  color: #333;
}

.post-meta {
  font-size: 0.8rem;
  color: #666;
  margin: 1rem 0;
}

.post-author {
  font-size: 15px;
}

.post-date {
  font-size: 13px;
}

.widget {
  margin-bottom: 2rem;
}

.search-box {
  margin-bottom: 2rem;
}
.category-list{
  font-size: 16px;
}
.category-list,
.archive-list {
  list-style: none;
  padding: 0;
}

.category-list li,
.archive-list li {
  padding: 0.5rem 0;
}

.sub-category-list {
  list-style: none;
  padding-left: 1.5rem;
}

.sub-category-list li {
  padding: 0.5rem 0;
}

.category-list a,
.archive-list a {
  color: #333;
  text-decoration: none;
}
.read-more {
  color: #2194c2;
  text-decoration: none;
}
.post-category{
  font-size: 16px;
  color: #3d3d3d;
}
.pagination {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
