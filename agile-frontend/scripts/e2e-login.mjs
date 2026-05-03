import assert from 'node:assert/strict'
import fs from 'node:fs/promises'
import path from 'node:path'
import { chromium } from 'playwright-core'

const frontendBaseUrl = process.env.FRONTEND_BASE_URL || 'http://127.0.0.1:4173'
const username = process.env.E2E_USER || 'admin'
const password = process.env.E2E_PASSWORD || '1'

const browserCandidates = [
  process.env.E2E_BROWSER_PATH,
  'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',
  'C:/Program Files/Microsoft/Edge/Application/msedge.exe',
  'C:/Program Files/Google/Chrome/Application/chrome.exe',
].filter(Boolean)

const browserPath = browserCandidates[0]

assert.ok(browserPath, 'No local browser executable was found for E2E testing')

const healthResponse = await fetch(`${frontendBaseUrl}/__health`)
assert.equal(healthResponse.status, 200, 'frontend dist server should be healthy')

const browser = await chromium.launch({
  executablePath: browserPath,
  headless: true,
})

const page = await browser.newPage({
  viewport: { width: 1440, height: 900 },
})

const pageErrors = []
const consoleErrors = []

page.on('pageerror', (error) => {
  pageErrors.push(error.message)
})

page.on('console', (message) => {
  if (message.type() === 'error') {
    consoleErrors.push(message.text())
  }
})

await page.goto(`${frontendBaseUrl}/login`, {
  waitUntil: 'networkidle',
  timeout: 30000,
})

const inputs = page.locator('input')
const inputCount = await inputs.count()
assert.ok(inputCount >= 2, 'login page should render username and password inputs')

await inputs.nth(0).fill(username)
await inputs.nth(1).fill(password)

const loginResponsePromise = page.waitForResponse(
  (response) =>
    response.url().includes('/ab-org/auth/login') &&
    response.request().method() === 'POST',
  { timeout: 30000 }
)

const userMsgResponsePromise = page.waitForResponse(
  (response) =>
    response.url().includes('/ab-bpm/sys/userResource/userMsg') &&
    response.request().method() === 'GET',
  { timeout: 30000 }
)

await page.locator('button').filter({ hasText: '登录' }).click()

const loginResponse = await loginResponsePromise
const userMsgResponse = await userMsgResponsePromise

assert.equal(loginResponse.status(), 200, 'login request should return 200')
assert.equal(userMsgResponse.status(), 200, 'userMsg request should return 200')

await page.waitForURL('**/dashboard', { timeout: 30000 })
await page.waitForLoadState('networkidle', { timeout: 30000 })

const dashboardVisible =
  (await page.locator('text=控制台').count()) > 0 ||
  (await page.locator('text=办公').count()) > 0

assert.ok(dashboardVisible, 'dashboard shell should render after login')
assert.equal(pageErrors.length, 0, `page errors: ${pageErrors.join(' | ')}`)
assert.equal(
  consoleErrors.length,
  0,
  `console errors: ${consoleErrors.join(' | ')}`
)

const artifactDir = path.resolve('.codex-artifacts')
await fs.mkdir(artifactDir, { recursive: true })
const screenshotPath = path.join(artifactDir, 'e2e-dashboard.png')
await page.screenshot({ path: screenshotPath, fullPage: false })

console.log('PASS browser login e2e')
console.log(`url=${page.url()}`)
console.log(`screenshot=${screenshotPath}`)

await browser.close()
