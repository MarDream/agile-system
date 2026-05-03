import http from 'node:http'
import fs from 'node:fs/promises'
import path from 'node:path'
import url from 'node:url'

const __dirname = path.dirname(url.fileURLToPath(import.meta.url))
const distDir = path.resolve(__dirname, '..', 'dist')
const host = process.env.HOST || '127.0.0.1'
const port = Number(process.env.PORT || 4173)
const backendBaseUrl = new URL(
  process.env.BACKEND_BASE_URL || 'http://127.0.0.1:8080'
)

const mimeTypes = {
  '.css': 'text/css; charset=utf-8',
  '.html': 'text/html; charset=utf-8',
  '.ico': 'image/x-icon',
  '.jpg': 'image/jpeg',
  '.js': 'text/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.svg': 'image/svg+xml; charset=utf-8',
  '.woff': 'font/woff',
  '.woff2': 'font/woff2',
}

const resolveFilePath = async (pathname) => {
  const normalizedPath = pathname === '/' ? '/index.html' : pathname
  const absolutePath = path.join(distDir, normalizedPath)

  try {
    const stats = await fs.stat(absolutePath)
    if (stats.isDirectory()) {
      return path.join(absolutePath, 'index.html')
    }
    return absolutePath
  } catch {
    return path.join(distDir, 'index.html')
  }
}

const shouldProxy = (pathname) => {
  return pathname.startsWith('/ab-') || pathname.startsWith('/api/')
}

const resolveProxyPath = (pathname, search) => {
  const backendPath = pathname.startsWith('/api/')
    ? pathname.slice('/api'.length)
    : pathname

  return `${backendPath || '/'}${search || ''}`
}

const proxyRequest = (req, res) => {
  const requestUrl = new URL(req.url || '/', `http://${host}:${port}`)
  const options = {
    protocol: backendBaseUrl.protocol,
    hostname: backendBaseUrl.hostname,
    port: backendBaseUrl.port,
    method: req.method,
    path: resolveProxyPath(requestUrl.pathname, requestUrl.search),
    headers: req.headers,
  }

  const proxy = http.request(options, (proxyRes) => {
    res.writeHead(proxyRes.statusCode || 500, proxyRes.headers)
    proxyRes.pipe(res, { end: true })
  })

  proxy.on('error', (error) => {
    res.writeHead(502, { 'Content-Type': 'text/plain; charset=utf-8' })
    res.end(`Proxy request failed: ${error.message}`)
  })

  req.pipe(proxy, { end: true })
}

const server = http.createServer(async (req, res) => {
  const requestUrl = new URL(req.url || '/', `http://${host}:${port}`)

  if (shouldProxy(requestUrl.pathname)) {
    proxyRequest(req, res)
    return
  }

  if (requestUrl.pathname === '/__health') {
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' })
    res.end(JSON.stringify({ ok: true, distDir, backendBaseUrl }))
    return
  }

  const filePath = await resolveFilePath(decodeURIComponent(requestUrl.pathname))

  try {
    const content = await fs.readFile(filePath)
    const extname = path.extname(filePath)
    const contentType = mimeTypes[extname] || 'application/octet-stream'
    res.writeHead(200, { 'Content-Type': contentType })
    res.end(content)
  } catch (error) {
    res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' })
    res.end(`Failed to read ${filePath}\n${error}`)
  }
})

server.listen(port, host, () => {
  console.log(`dist server running at http://${host}:${port}`)
})
