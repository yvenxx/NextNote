<template>
  <el-row class="nav-header" type="flex" justify="left" :gutter="30">
    <el-col :offset="4" :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
      <div class="nav-left">
        <div class="nav-brand">
          <a href="/" style="text-decoration: none; color: #333;">{{ siteConfig.brand }}</a>
        </div>
        <div class="nav-subtitle">{{ siteConfig.subtitle }}</div>
      </div>
    </el-col>
    <el-col :xs="24" :sm="24" :md="4" :lg="4" :xl="4" class="nav-links">
      <a href="#" class="nav-item">关于</a>
      <a href="https://github.com/yvenxx/NextNote" target="_blank" class="nav-item">
        <img src="@/assets/images/github-mark.svg" alt="github" width="17" height="17" style="margin-top: 4px;"/>
      </a>
    </el-col>
  </el-row>
</template>

<script>
import siteConfig from "@/config/site";

export default {
  name: "AppHeader",
  data() {
    return {
      siteConfig: siteConfig
    };
  }
};
</script>

<style scoped>
.nav-header {
  padding: 1rem 0;
  border-bottom: 1px solid #eee;
  background: #fff;
  width: 100%;
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
  justify-content: flex-start;
}

.nav-item {
  color: #333;
  text-decoration: none;
}
</style> 