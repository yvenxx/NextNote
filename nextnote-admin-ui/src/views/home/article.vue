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
import { marked } from 'marked';
import Prism from 'prismjs';
import 'prismjs/themes/prism.min.css';

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
        if (this.article.description) {
          this.article.content = marked(this.article.description);
        }
        this.$nextTick(() => {
          this.generateOutline();
          Prism.highlightAll();
          // 为代码块添加复制按钮
          this.addCopyButtons();
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
    },
    // 添加复制按钮方法
    addCopyButtons() {
      const codeBlocks = document.querySelectorAll('pre code');
      codeBlocks.forEach(codeBlock => {
        const pre = codeBlock.parentElement;
        // 创建复制按钮容器
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'copy-button-container';
        // 创建复制按钮
        const copyButton = document.createElement('button');
        copyButton.className = 'copy-button';
        copyButton.innerHTML = '复制';
        
        // 添加点击事件
        copyButton.addEventListener('click', () => {
          const code = codeBlock.textContent;
          navigator.clipboard.writeText(code).then(() => {
            copyButton.innerHTML = '已复制!';
            setTimeout(() => {
              copyButton.innerHTML = '复制';
            }, 2000);
          }).catch(err => {
            console.error('复制失败:', err);
            copyButton.innerHTML = '复制失败';
          });
        });
        
        buttonContainer.appendChild(copyButton);
        pre.appendChild(buttonContainer);
      });
    }
  }
}
</script>

<style>
/* 添加到 style 标签中，注意要移除 scoped 属性以使样式全局生效 */
pre {
  position: relative;
}

.copy-button-container {
  position: absolute;
  top: 5px;
  right: 5px;
}

.copy-button {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.2s ease;
}

.copy-button:hover {
  background-color: #e9ecef;
  color: #495057;
}

/* 确保代码块有足够的右边距来放置按钮 */
pre code {
  padding-right: 60px !important;
}
</style>

