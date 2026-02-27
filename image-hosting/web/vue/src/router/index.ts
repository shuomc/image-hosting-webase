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
import FavoritesView from '@/views/workplace/Favorite.vue';
import ImagesDetailView from '@/views/workplace/SjyImagesDetailView.vue';
import UserProfile from '@/views/userui/SjyUserProfile.vue';
import NotFound from '@/views/SjyNotFoundView.vue'
import Profile from '@/views/workplace/Profile.vue';
import AdminLogin from '@/views/bgadmin/adminLogin.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/admin/login',
      name: 'AdminLogin',
      component: AdminLogin,
      meta: { requiresAuth: false }
    },
    {
      path: '/admin',
      component: () => import('@/views/bgadmin/AdminLayout.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/bgadmin/AdminDashboard.vue')
        },
        {
          path: 'users',
          name: 'AdminUsers',
          component: () => import('@/views/bgadmin/AdminUserList.vue')
        },
        {
          path: 'images',
          name: 'AdminImages',
          component: () => import('@/views/bgadmin/AdminImageList.vue')
        },
        {
          path: 'notices',
          name: 'AdminNotices',
          component: () => import('@/views/bgadmin/AdminNoties.vue')
        },
        {
          path: 'nft-transactions',
          name: 'AdminNFTTransactions',
          component: () => import('@/views/nft/NFTTransactions.vue')
        },
        {
          path: 'settings',
          name: 'AdminSettings',
          component: () => import('@/views/bgadmin/AdminSettings.vue')
        },
        {
          path: '',
          redirect: '/admin/dashboard'
        }
      ]
    },
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
          path: 'dashboard',
          name: 'UserDashboard',
          component: () => import('@/views/workplace/UserDashboard.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'notices',
          name: 'notices',
          component: () => import('@/views/userui/UserNotices.vue'),
          meta: { requiresAuth: true }
        },
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
          path: 'favorites',
          name: 'Favorites',
          component: FavoritesView,
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
    },
    {
      path: '/design/userhome',
      name: 'DesignUserHome',
      component: () => import('@/views/design/DesignUserHome.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/upload-image',
      name: 'DesignUploadImage',
      component: () => import('@/views/design/DesignUploadImage.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/workplace',
      name: 'DesignWorkplace',
      component: () => import('@/views/design/DesignWorkplace.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/my-images',
      name: 'DesignMyImages',
      component: () => import('@/views/design/DesignMyImages.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/image-detail',
      name: 'DesignImageDetail',
      component: () => import('@/views/design/DesignImageDetail.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/my-nft',
      name: 'DesignMyNFT',
      component: () => import('@/views/design/DesignMyNFTView.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/nft-detail',
      name: 'DesignMyNFTDetail',
      component: () => import('@/views/design/DesignMyNFTDetail.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/nft-market',
      name: 'DesignNFTMarket',
      component: () => import('@/views/design/DesignNFTMarket.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/buy-nft-detail',
      name: 'DesignBuyNFTDetail',
      component: () => import('@/views/design/DesignBuyNFTDetail.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/nft-trans',
      name: 'DesignNFTTrans',
      component: () => import('@/views/design/DesignNFTTrans.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/my-wallet',
      name: 'DesignMyWallet',
      component: () => import('@/views/design/DesignMyWallet.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/profile',
      name: 'DesignProfile',
      component: () => import('@/views/design/DesignProfile.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/admin-dashboard',
      name: 'DesignAdminDashboard',
      component: () => import('@/views/design/DesignAdminDashboard.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/admin-user',
      name: 'DesignAdminUser',
      component: () => import('@/views/design/DesignAdminUser.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/admin-image',
      name: 'DesignAdminImage',
      component: () => import('@/views/design/DesignAdminImage.vue'),
      meta: { title: 'Design' }
    },
    {
      path: '/design/admin-setting',
      name: 'DesingAdminSetting',
      component: () => import('@/views/design/DesingAdminSetting.vue'),
      meta: { title: 'Design' }
    },
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

    // Check for admin requirement
    if (to.meta.requiresAdmin && userStore.userInfo?.userRole !== 'admin') {
      ElMessage.error('无权访问管理员区域')
      next({ path: '/workplace' })
      return
    }

    // 已登录并且加载了用户信息，继续访问
    next()
  }
  // 如果是登录页，且已登录，则重定向到工作区
  else if ((to.name === 'login' || to.name === 'register') && userStore.isLoggedIn) {
    next({ path: '/workplace' })
  }
  // 如果是管理员登录页，且已登录为管理员，重定向到管理员工作区
  else if (to.name === 'AdminLogin' && userStore.isLoggedIn && userStore.userInfo?.userRole === 'admin') {
    next({ path: '/admin/dashboard' })
  }
  // 其他情况，允许访问
  else {
    next()
  }
})

export default router
