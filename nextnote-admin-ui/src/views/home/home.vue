<template>
  <div class="blog-layout">
    <!-- 顶部导航 -->
    <el-row class="nav-header" type="flex" justify="center" :gutter="30">
      <el-col :offset="2" :xs="24" :sm="24" :md="14" :lg="14" :xl="14">
        <div class="nav-left">
          <div class="nav-brand">
            <a href="/" style="text-decoration: none; color: #333;">{{ siteConfig.brand }}</a>
          </div>
          <div class="nav-subtitle">{{ siteConfig.subtitle }}</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="5" :lg="5" :xl="5" class="nav-links">
        <!-- <a href="#" class="nav-item">社区</a> -->
        <a href="#" class="nav-item">关于</a>
        <!-- <a href="/login" class="nav-item">登录</a> -->
        <a href="https://github.com/yvenxx/NextNote" target="_blank" class="nav-item">
          <img src="@/assets/images/github-mark.svg" alt="github" width="17" height="17" style="margin-top: 4px;"/>
        </a>
      </el-col>
    </el-row>

    <el-row class="main-container" :gutter="30" type="flex" justify="center">
      <!-- 文章列表区域-->
      <el-col
        :offset="2"
        :xs="24"
        :sm="24"
        :md="14"
        :lg="14"
        :xl="14"
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
          <h2 class="post-title">{{ article.title }}</h2>
          <div class="post-content" v-html="article.content"></div>
          <a class="read-more" :href="'/article/' + article.id">继续阅读</a>
          <div class="post-meta">
            <span class="post-author"><i class="el-icon-user"></i>&nbsp;{{ article.createBy }}&nbsp;/&nbsp;</span>
            <span class="post-date"><i class="el-icon-time"></i>&nbsp;{{ article.createTime }}</span>
          </div>
        </div>
      </el-col>

      <!-- 侧边栏 -->
      <el-col :xs="24" :sm="24" :md="5" :lg="5" :xl="5" class="sidebar-col">
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
  </div>
</template>

<script>
import siteConfig from "@/config/site";
import { listArticle, listCategory } from "@/api/note/home";

export default {
  name: "Home",
  data() {
    return {
      articles: [],
      categories: [],
      searchQuery: "",
      siteConfig: siteConfig,
    };
  },
  computed: {
    rootCategories() {
      return this.categories.filter(category => !category.pid || category.pid === 0);
    }
  },
  methods: {
    async fetchArticle() {
      try {
        const response = await listArticle();
        this.articles = response.rows;
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
    }
  },
  created() {
    this.fetchArticle();
    this.fetchCategories();
  },
};
</script>

<style scoped>
.blog-layout {
  min-height: 100vh;
  background-color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.nav-header {
  padding: 1rem 0;
  border-bottom: 1px solid #eee;
  background: #fff;
  width: 90%;
  margin: 0 auto;
}

.nav-left {
  display: flex;
  flex-direction: column;
}

.nav-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.nav-subtitle {
  font-size: 0.8rem;
  color: #666;
}

.nav-links {
  font-size: 14px;
  display: flex;
  gap: 1.5rem;
  align-items: center;
  justify-content: flex-end;
}

.nav-item {
  color: #333;
  text-decoration: none;

}

.main-container {
  width: 90%;
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
</style>
