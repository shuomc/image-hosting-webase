import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import HomeView from '../views/SjyHomeView.vue'
import RegisterView from '../views/register/SjyRegisterView.vue';
import LoginView from '../views/auth/SjyLoginView.vue';
import FindPasswordView from '../views/auth/SjyFindPasswordView.vue';
import WorkplaceView from '@/views/workplace/SjyWorkplaceView.vue';
import RecommendedView from '@/views/workplace/SjyRecommendedView.vue';
import AboutView from '../views/workplace/SjyAboutView.vue';
import SettingsView from '@/views/workplace/SjySettingsView.vue';
import AccountSettingsView from '@/views/workplace/SjyAccountSettingsView.vue';
import UploadFileView from '@/views/workplace/SjyUploadFileView.vue';
import UploadImageView from '@/views/workplace/SjyUploadImageView.vue';
import MyImagesView from '@/views/workplace/SjyMyImagesView.vue';
import MyFilesView from '@/views/workplace/SjyMyFilesView.vue';
import ImagesDetailView from '@/views/workplace/SjyImagesDetailView.vue';
import UserProfile from '@/views/userui/SjyUserProfile.vue';
import NotFound from '@/views/SjyNotFoundView.vue'
import Profile from '@/views/workplace/Profile.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/userhome'
    },
    {
      path: '/auth',
      children: [
        {
          path: 'login',
          name: 'login',
          component: LoginView,
          meta: { requiresAuth: false }
        },
        {
          path: 'find-password',
          name: 'findPassword',
          component: FindPasswordView,
          meta: { requiresAuth: false }
        }
      ]
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { requiresAuth: false }
    },
    {
      path: '/workplace',
      name: 'workplace',
      component: WorkplaceView,
      meta: { requiresAuth: true },
      children: [
        {
          path: 'recommended',
          name: 'Recommended',
          component: RecommendedView,
          meta: { requiresAuth: true }
        },
        {
          path: 'my-images',
          name: 'MyImages',
          component: MyImagesView,
          meta: { requiresAuth: true, activeMenu: 'MyImages'}
        },
        {
          path: 'my-images/:imageId', // Child path
          name: 'ImageDetail', // Route name (can keep same if unique within children)
          component: ImagesDetailView, // Image detail component
          meta: { requiresAuth: true, activeMenu: 'MyImages'} // Ensure requires auth if needed
        },
        {
          path: 'upload-image',
          name: 'UploadImage',
          component: UploadImageView,
          meta: { requiresAuth: true }
        },
        {
          path: 'upload-file',
          name: 'UploadFile',
          component: UploadFileView,
          meta: { requiresAuth: true }
        },
        {
          path: 'account',
          name: 'AccountSettings',
          component: AccountSettingsView,
          meta: { requiresAuth: true }
        },
        {
          path: 'settings',
          name: 'Settings',
          component: SettingsView,
          meta: { requiresAuth: true }
        },
        {
          path: 'about',
          name: 'About',
          component: AboutView,
        },
        {
          path: 'my-files',
          name: 'MyFiles',
          component: MyFilesView,
          meta: { requiresAuth: true }
        },
        {
          path: '/nft-market',
          name: 'NFTMarket',
          component: () => import('@/views/nft/NFTMarket.vue'),
          meta: { title: 'NFTMarket' }
        },
        {
          path: '/my-nft',
          name: 'MyNFT',
          component: () => import('@/views/nft/MyNFTView.vue'),
          meta: { title: 'MyNFTView' }
        },
        {
          path: '/nft/detail/:nftId',
          name: 'NFTDetail',
          component: () => import('@/views/nft/NFTDetail.vue'),
          meta: { title: 'NFTDetail' }
        },
        {
          path: '/nft-transactions',
          name: 'NFTTransactions',
          component: () => import('@/views/nft/NFTTransactions.vue'),
          meta: { title: 'NFTTransactions' }
        },
        {
          path: '/nft-balance',
          name: 'NFTBalance',
          component: () => import('@/views/nft/NFTBalance.vue'),
          meta: { title: 'NFTBalance' }
        },
        {
          path: 'profile',
          name: 'Profile',
          component: Profile,
          meta: { requiresAuth: true }
        },
      ],
    },
    {
      path: '/userhome',
      name: 'Userhome',
      component: () => import('@/views/userui/SjyUserHome.vue'),
      meta: { title: 'Home' }
    },
    {
    path: '/user/:userId',
    name: 'UserProfile',
    component: UserProfile,
    props: true // This passes route.params as props to the component, though we use useRoute directly
    },
    {
      path: '/licence',
      name: 'UserLicence',
      component: () => import('@/views/userui/SjyUserLicence.vue'),
      meta: { title: 'User' }
    }
    ,
     {
    path: '/:pathMatch(.*)*', // 匹配所有未匹配到的路径
    name: 'NotFound',
    component: NotFound
  }
  ]
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

  // 如果路由需要认证
  if (requiresAuth) {
    // 检查是否已登录
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      next({ name: 'login' })
      return
    }

    // 如果已登录但未加载用户信息，则加载用户信息
    if (!userStore.userInfoLoaded) {
      try {
        const success = await userStore.loadUserInfo()
        if (!success) {
          ElMessage.error('获取用户信息失败，请重新登录')
          // 清除登录状态
          userStore.clearLoginState()
          next({ name: 'login' })
          return
        }
      } catch (error) {
        console.error('加载用户信息出错:', error)
        // 清除登录状态
        userStore.clearLoginState()
        next({ name: 'login' })
        return
      }
    }

    // 已登录并且加载了用户信息，继续访问
    next()
  }
  // 如果是登录页，且已登录，则重定向到工作区
  else if ((to.name === 'login' || to.name === 'register') && userStore.isLoggedIn) {
    next({ path: '/workplace' })
  }
  // 其他情况，允许访问
  else {
    next()
  }
})

export default router
