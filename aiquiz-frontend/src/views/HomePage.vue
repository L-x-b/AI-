<template>
  <div class="homePage">
    <div class="searchBar">
      <a-input-search
        :style="{ width: '320px' }"
        placeholder="快速发现答题应用"
        button-text="搜索"
        search-button
        size="large"
      />
    </div>

    <div class="dropdownList">
      <a-list
        class="list-demo-action-layout"
        :grid-props="{ gutter: [20, 20], sm: 24, md: 12, lg: 8, xl: 6 }"
        :bordered="false"
        :data="dataList"
        :pagination-props="{
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
        }"
        :on-page-change="onPageChange"
      >
        <template #item="{ item }">
          <AppCard :app="item" />
        </template>
      </a-list>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import AppCard from "@/components/AppCard.vue";
import API from "@/api";
import { REVIEW_STATUS_ENUM } from "@/constant/app";
import { listAppByPageUsingPost } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const searchParams = ref<API.AppQueryRequest>({
  ...initSearchParams,
});

const dataList = ref<API.AppVO[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const params = {
    reviewStatus: REVIEW_STATUS_ENUM.PASS,
    ...searchParams,
  };
  const res = await listAppByPageUsingPost(params);
  console.log("res:", res);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 切换页面触发事件
 * @param page
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams,
    current: page,
  };
};

/**
 * 当searchParams变化时，重新加载数据
 */
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
.searchBar {
  margin-bottom: 28px;
  text-align: center;
}

.list-demo-action-layout .image-area {
  width: 183px;
  height: 119px;
  overflow: hidden;
  border-radius: 2px;
}

.list-demo-action-layout .list-demo-item {
  padding: 20px 0;
  border-bottom: 1px solid var(--color-fill-3);
}

.list-demo-action-layout .image-area img {
  width: 100%;
}

.list-demo-action-layout .arco-list-item-action .arco-icon {
  margin: 0 4px;
}
</style>
