import router from "@/router";
import { useLoginUserStore } from "@/store/userStore";
import accessEnum from "@/access/accessEnum";
import AccessEnum from "@/access/accessEnum";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";

/**
 * 进入页面前，进行权限校验
 */
router.beforeEach(async (to, from, next) => {
  // 获取登入用户
  const loginUserStore = useLoginUserStore();
  let loginUser = loginUserStore.loginUser;
  // 如果之前没有尝试过登录用户信息，才自动登录
  if (!loginUser || !loginUser.userRole) {
    await loginUserStore.fetchLoginUser();
    loginUser = loginUserStore.loginUser;
  }

  // 获取需要跳转页面的权限
  const needAccess = (to.meta?.access as string) ?? accessEnum.NOT_LOGIN;
  // 要跳转的页面必须登录
  if (needAccess !== AccessEnum.NOT_LOGIN) {
    // 如果没有登录，则跳转到登录页面
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
    }
    // 如果已经登录了，判断权限是否足够，如果不足够，跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  next();
});
