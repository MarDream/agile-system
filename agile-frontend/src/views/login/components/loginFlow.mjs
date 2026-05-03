export function buildLoginPayload(form) {
  return {
    userName: form.user,
    password: form.password,
    captcha: '',
    clientId: 'bpmDevPlatform',
    clientSecret: '1',
    grantType: 'password',
  }
}

export function extractAccessToken(loginResponse) {
  return (
    loginResponse?.data?.access_token ||
    loginResponse?.data?.data?.access_token ||
    loginResponse?.access_token ||
    ''
  )
}

export function createTokenRecord(accessToken, issuedAt = new Date()) {
  return {
    access_token: accessToken,
    create_time: issuedAt.getTime(),
  }
}

export function transformUserMenus(resourceList, topPath = '', parentPath = '') {
  const menuList = []
  let childrenPathList = []

  resourceList.forEach((item) => {
    const routeMenu = {
      path: item.url || item.code,
      name: item.code,
      component: item.component,
      redirect: item.redirect,
      meta: {
        title: item.name,
        icon: item.icon,
        hidden: item.enable === 0,
        topMenuCode: topPath || item.code,
        parentPath,
      },
      isopen: item.opened === 1,
    }

    if (item.children && item.children.length > 0) {
      const result = transformUserMenus(
        item.children,
        routeMenu.meta.topMenuCode,
        routeMenu.path || routeMenu.name
      )
      routeMenu.children = result.menuList
      routeMenu.childrenPathList = result.childrenPathList
    } else {
      routeMenu.childrenPathList = [routeMenu.path]
    }

    childrenPathList = childrenPathList.concat(routeMenu.childrenPathList)
    menuList.push(routeMenu)
  })

  return {
    menuList,
    childrenPathList,
  }
}

export function flatAsyncRoutes(routes) {
  const result = []
  routes.forEach((route) => {
    if (route.children && route.children.length > 0) {
      result.push(route)
      result.push(...flatAsyncRoutes(route.children))
      return
    }
    result.push(route)
  })
  return result
}

export function filterAsyncRouter(routerMap, loadComponent) {
  return routerMap.map((item) => {
    const meta = item.meta || {}
    const routePath = meta.type === 'iframe' ? `/i/${item.name}` : item.path
    const routeMeta =
      meta.type === 'iframe'
        ? {
            ...meta,
            url: item.path,
          }
        : meta

    return {
      path: routePath,
      name: item.name,
      meta: routeMeta,
      redirect: item.redirect,
      children: item.children
        ? filterAsyncRouter(item.children, loadComponent)
        : [],
      component: loadComponent(item.component),
    }
  })
}

export function resolveViewComponent(component, modules) {
  if (!component) {
    return null
  }

  const normalizedPath = component.replace(/^\.\.\/views\//, '/src/views/')
  return modules[normalizedPath] || null
}

export function buildLoginSuccessState(
  loginResponse,
  userInfoResponse,
  loadComponent,
  issuedAt = new Date()
) {
  const accessToken = extractAccessToken(loginResponse)
  const { menuList } = transformUserMenus(userInfoResponse.userMenuList || [])
  const platformMenus = flatAsyncRoutes(
    filterAsyncRouter(menuList, loadComponent)
  )

  return {
    authToken: accessToken,
    tokenRecord: createTokenRecord(accessToken, issuedAt),
    menuList,
    platformMenus,
    user: {
      userName: userInfoResponse.user.fullName,
      userNameF: userInfoResponse.user.fullName,
    },
    abUser: userInfoResponse.user,
    currentOrg: userInfoResponse.currentOrg,
    buttonPermission: userInfoResponse.buttonPermission,
    redirectPath: '/dashboard',
  }
}
