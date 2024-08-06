<template>
  <div id="GlobalHeader">
    <a-row align="center" :wrap="false">
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          :selected-keys="selectedKeys"
          @menu-item-click="doMenuClick"
        >
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <div class="titleBar">
              <img class="logo" src="../assets/logo.png" />
              <div class="title">AI答题应用</div>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in visibleRouter" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div v-if="loginUserStore.loginUser.id">
          {{ loginUserStore.loginUser.userName ?? "无名" }}
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";

const router = useRouter();

const loginUserStore = useLoginUserStore();
const visibleRouter = computed(() => {
  return routes.filter((item) => {
    // 隐藏菜单
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

/**
 * 当前选中的菜单
 */
const selectedKeys = ref(["/"]);
// 页面跳转后修改菜单选中状态
router.afterEach((to) => {
  selectedKeys.value = [to.path];
});

/**
 * 菜单点击事件
 * @param key
 */
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<style scoped>
#GlobalHeader {
}

.titleBar {
  display: flex;
  align-items: center;
}

.titleBar .title {
  margin-left: 16px;
  color: black;
}

.titleBar .logo {
  height: 64px;
}
</style>
