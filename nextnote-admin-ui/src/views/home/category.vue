<template>
  <div class="home-container">
    <app-header />
    <el-row class="main-container" :gutter="30" type="flex" justify="left">
        <el-col :offset="4" :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <h1>分类 : {{ category.name }}</h1>
            <div v-for="article in articles" :key="article.id">
                <a :href="'/article/' + article.id">{{ article.title }}</a>
            </div>
        </el-col>
    </el-row>
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "@/components/Header.vue";
import AppFooter from "@/components/Footer.vue";
import { listArticle, getCategoryById } from "@/api/note/home";
export default {
  name: 'Category',
  components: {
    AppHeader,
    AppFooter
  },
  data() {
    return {
      category: {},
      articles: []
    };
  },
  created() {
    const categoryId = this.$route.params.id;
    if (categoryId) {
      // 根据分类ID加载文章
      this.loadCategory(categoryId);
      this.loadArticlesByCategory(categoryId);
    } else {
      // 跳回首页
      this.$router.push('/');
    }
  },
  methods: {
    loadCategory(categoryId) {
      getCategoryById(categoryId).then(response => {
        this.category = response.data;
      });
    },
    loadArticlesByCategory(categoryId) {
      // 实现加载指定分类下文章的逻辑
      listArticle(categoryId).then(response => {
        this.articles = response.rows;
      });
    }
  }
}
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


</style>
