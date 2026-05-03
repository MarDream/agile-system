import assert from 'node:assert/strict'

import {
  buildLoginPayload,
  buildLoginSuccessState,
  extractAccessToken,
  resolveViewComponent,
} from '../src/views/login/components/loginFlow.mjs'

function runTest(name, fn) {
  try {
    fn()
    console.log(`PASS ${name}`)
  } catch (error) {
    console.error(`FAIL ${name}`)
    throw error
  }
}

runTest('extractAccessToken supports wrapper and raw backend response shapes', () => {
  assert.equal(
    extractAccessToken({
      data: {
        access_token: 'token-from-wrapper',
      },
    }),
    'token-from-wrapper'
  )
  assert.equal(
    extractAccessToken({
      data: {
        data: {
          access_token: 'token-from-backend',
        },
      },
    }),
    'token-from-backend'
  )
  assert.equal(extractAccessToken({ access_token: 'token-flat' }), 'token-flat')
  assert.equal(extractAccessToken({}), '')
})

runTest('buildLoginPayload maps form fields to the backend contract', () => {
  assert.deepEqual(
    buildLoginPayload({
      user: 'admin',
      password: 'secret',
    }),
    {
      userName: 'admin',
      password: 'secret',
      captcha: '',
      clientId: 'bpmDevPlatform',
      clientSecret: '1',
      grantType: 'password',
    }
  )
})

runTest('buildLoginSuccessState prepares menus, user state and homepage redirect', () => {
  const issuedAt = new Date('2026-05-01T08:00:00.000Z')
  const loadedComponents = []
  const loginState = buildLoginSuccessState(
    {
      data: {
        access_token: 'token-123',
      },
    },
    {
      user: {
        fullName: '管理员',
      },
      currentOrg: {
        id: 'org-1',
      },
      buttonPermission: ['sys:add'],
      userMenuList: [
        {
          name: '首页',
          code: 'home',
          url: '/home',
          icon: 'HomeFilled',
          enable: 1,
          opened: 1,
          children: [
            {
              name: '控制台',
              code: 'dashboard',
              url: '/dashboard',
              icon: 'Menu',
              enable: 1,
              opened: 0,
              component: '../views/home/index.vue',
              children: [],
            },
          ],
        },
        {
          name: '带"引号"的菜单',
          code: 'quoted',
          url: '/quoted',
          icon: 'Document',
          enable: 0,
          opened: 0,
          children: [],
        },
      ],
    },
    (component) => {
      loadedComponents.push(component || null)
      return component ? `loaded:${component}` : 'loaded:empty'
    },
    issuedAt
  )

  assert.equal(loginState.authToken, 'token-123')
  assert.deepEqual(loginState.tokenRecord, {
    access_token: 'token-123',
    create_time: issuedAt.getTime(),
  })
  assert.equal(loginState.redirectPath, '/dashboard')
  assert.deepEqual(loginState.user, {
    userName: '管理员',
    userNameF: '管理员',
  })
  assert.deepEqual(loginState.currentOrg, { id: 'org-1' })
  assert.deepEqual(loginState.buttonPermission, ['sys:add'])
  assert.equal(loginState.menuList[0].meta.topMenuCode, 'home')
  assert.equal(loginState.menuList[0].children[0].meta.parentPath, '/home')
  assert.deepEqual(loginState.menuList[0].children[0].childrenPathList, [
    '/dashboard',
  ])
  assert.equal(loginState.menuList[1].meta.hidden, true)
  assert.equal(loginState.menuList[1].meta.title, '带"引号"的菜单')
  assert.deepEqual(
    loginState.platformMenus.map((item) => item.path),
    ['/home', '/dashboard', '/quoted']
  )
  assert.deepEqual(loadedComponents, ['../views/home/index.vue', null, null])
})

runTest('resolveViewComponent matches backend view paths to vite glob keys', () => {
  const modules = {
    '/src/views/home/index.vue': 'dashboard-component',
  }

  assert.equal(
    resolveViewComponent('../views/home/index.vue', modules),
    'dashboard-component'
  )
  assert.equal(resolveViewComponent('', modules), null)
})
