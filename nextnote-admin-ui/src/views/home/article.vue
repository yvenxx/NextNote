<template>
  <div class="home-container">
    <app-header />
    <el-row class="main-container" :gutter="30" type="flex" justify="left">
        <el-col class="article-content" :offset="4" :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <h1>{{ article.title }}</h1>
            <div>
              <span>{{ article.createBy }} &nbsp;/&nbsp;</span>
              <span>{{ article.createTime }}</span>
            </div>
            <div style="padding-top: 20px;" v-html="article.content"></div>
        </el-col>
        <el-col :xs="24" :sm="24" :md="4" :lg="4" :xl="4">
          <!-- 根据文章内容生成可点击跳转到相应位置的大纲 -->
          <div class="article-outline" v-if="outlineItems.length > 0">
            <h4>大纲</h4>
            <ul class="outline-list">
              <li v-for="(item, index) in outlineItems" :key="index" 
                  :class="['outline-item', `level-${item.level}`]">
                <a :href="`#${item.id}`" @click.prevent="scrollToHeading(item.id)">
                  {{ item.text }}
                </a>
              </li>
            </ul>
          </div>
        </el-col>
    </el-row>
    <app-footer />
  </div>
</template>

<script>
import { getArticleById } from '@/api/note/home'
import AppHeader from "@/components/Header.vue";
import AppFooter from "@/components/Footer.vue";
export default {
  data() {
    return {
      article: {},
      outlineItems: []
    }
  },
  components: {
    AppHeader,
    AppFooter
  },
  created() {
    const articleId = this.$route.params.id;
    if (articleId) {
      this.getArticle(articleId);
    } else {
      this.$router.push('/');
    }
  },
  methods: {
    getArticle(articleId) {
      getArticleById(articleId).then(response => {
        this.article = response.data;
        // 文章加载完成后生成大纲
        this.$nextTick(() => {
          this.generateOutline();
        });
      })
    },
    
    // 生成文章大纲
    generateOutline() {
      // 获取文章内容区域
      const contentEl = document.querySelector('.main-container');
      if (!contentEl) return;
      
      // 查找所有标题元素
      const headings = contentEl.querySelectorAll('h1, h2, h3, h4, h5, h6');
      this.outlineItems = [];
      
      headings.forEach((heading, index) => {
        // 为每个标题添加唯一ID
        const id = `heading-${index}`;
        heading.id = id;
        
        // 获取标题级别
        const level = parseInt(heading.tagName.substring(1));
        
        // 添加到大纲项目中
        this.outlineItems.push({
          id,
          text: heading.textContent,
          level
        });
      });
    },
    
    // 滚动到指定标题
    scrollToHeading(id) {
      const element = document.getElementById(id);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
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
.article-content{
  border-right: 1px solid #eee;
}
.article-outline {
  position: sticky;
  top: 20px;
  padding: 15px;
  margin-bottom: 20px;
}

.outline-list {
  list-style: none;
  padding-left: 0;
}

.outline-item {
  margin: 8px 0;
}

.outline-item a {
  color: #333;
  text-decoration: none;
  transition: color 0.3s;
}

.outline-item a:hover {
  color: #409EFF;
}

/* 不同级别标题的缩进 */
.level-1 { padding-left: 0; }
.level-2 { padding-left: 10px; }
.level-3 { padding-left: 20px; }
.level-4 { padding-left: 30px; }
.level-5 { padding-left: 40px; }
.level-6 { padding-left: 50px; }

</style>

