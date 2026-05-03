import assert from 'node:assert/strict'

const backendBaseUrl = process.env.BACKEND_BASE_URL || 'http://localhost:8080'
const frontendBaseUrl = process.env.FRONTEND_BASE_URL || 'http://127.0.0.1:4173'
const loginPayload = {
  userName: process.env.E2E_USER || 'admin',
  password: process.env.E2E_PASSWORD || '1',
  captcha: '',
  clientId: 'bpmDevPlatform',
  clientSecret: '1',
  grantType: 'password',
}

function logStep(name, detail) {
  console.log(`PASS ${name}`)
  if (detail) {
    console.log(detail)
  }
}

async function fetchJson(url, options) {
  const response = await fetch(url, options)
  const text = await response.text()
  let data

  try {
    data = JSON.parse(text)
  } catch {
    data = text
  }

  return {
    ok: response.ok,
    status: response.status,
    data,
  }
}

const loginResult = await fetchJson(`${backendBaseUrl}/ab-org/auth/login`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
  body: JSON.stringify(loginPayload),
})

assert.equal(loginResult.status, 200, 'login request should return 200')
assert.equal(loginResult.data.code, 'Success', 'login API should succeed')
assert.ok(
  loginResult.data.data?.access_token,
  'login API should return an access token'
)
logStep('backend login', `tokenType=${loginResult.data.data.token_type}`)

const userMsgResult = await fetchJson(
  `${backendBaseUrl}/ab-bpm/sys/userResource/userMsg`,
  {
    headers: {
      Authorization: `Bearer ${loginResult.data.data.access_token}`,
    },
  }
)

assert.equal(userMsgResult.status, 200, 'userMsg request should return 200')
assert.equal(userMsgResult.data.code, 'Success', 'userMsg API should succeed')
assert.ok(
  Array.isArray(userMsgResult.data.data?.userMenuList),
  'userMsg should return a menu list'
)
assert.ok(
  userMsgResult.data.data.userMenuList.length > 0,
  'userMsg should return at least one menu'
)
assert.equal(
  userMsgResult.data.data.userMenuList[0]?.children?.[0]?.url,
  '/dashboard',
  'first menu child should point to /dashboard'
)
logStep(
  'backend menu chain',
  `menus=${userMsgResult.data.data.userMenuList.length}, firstChild=${userMsgResult.data.data.userMenuList[0]?.children?.[0]?.url}`
)

const frontendHealth = await fetchJson(`${frontendBaseUrl}/__health`)
assert.equal(frontendHealth.status, 200, 'frontend dist server health should be 200')
assert.equal(frontendHealth.data.ok, true, 'frontend dist server should be healthy')
logStep('frontend dist server', frontendBaseUrl)

const loginPage = await fetch(`${frontendBaseUrl}/login`)
const loginHtml = await loginPage.text()
assert.equal(loginPage.status, 200, 'frontend login page should return 200')
assert.match(loginHtml, /<title>/i, 'frontend login page should contain HTML content')
logStep('frontend login page')

const proxiedLoginResult = await fetchJson(
  `${frontendBaseUrl}/api/ab-org/auth/login`,
  {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
  body: JSON.stringify(loginPayload),
  }
)

assert.equal(proxiedLoginResult.status, 200, 'proxied login should return 200')
assert.equal(
  proxiedLoginResult.data.code,
  'Success',
  'proxied login API should succeed'
)
assert.ok(
  proxiedLoginResult.data.data?.access_token,
  'proxied login should return an access token'
)
logStep('frontend proxy login')

const proxiedUserMsgResult = await fetchJson(
  `${frontendBaseUrl}/api/ab-bpm/sys/userResource/userMsg`,
  {
    headers: {
      Authorization: `Bearer ${proxiedLoginResult.data.data.access_token}`,
    },
  }
)

assert.equal(
  proxiedUserMsgResult.status,
  200,
  'proxied userMsg request should return 200'
)
assert.equal(
  proxiedUserMsgResult.data.code,
  'Success',
  'proxied userMsg API should succeed'
)
assert.equal(
  proxiedUserMsgResult.data.data.userMenuList[0]?.children?.[0]?.url,
  '/dashboard',
  'proxied first menu child should point to /dashboard'
)
logStep('frontend proxy menu chain')
