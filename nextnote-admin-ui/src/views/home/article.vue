<template>
  <div class="article-container">
    <h1>{{ article.title }}</h1>
    <div v-html="article.content"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      article: {}
    }
  }
}
</script>

<style scoped>
.article-container {
  width: 90%;
  margin: 0 auto;
  margin-top: 20px;
  padding: 2rem 0;
}
</style>

