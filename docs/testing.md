# 测试与验证

## 前端

在 `agile-frontend` 目录下可执行：

```bash
npm run test:login-flow
npm run build
npm run serve:dist
npm run test:full-chain
npm run test:e2e-login
```

说明：

- `test:login-flow`：登录链路逻辑测试
- `serve:dist`：本地静态代理服务
- `test:full-chain`：接口与前端代理链路检查
- `test:e2e-login`：真实浏览器登录到首页的端到端检查

## 后端

常用启动方式：

```bash
mvn -pl agile-backend/ab-spring-boot/ab-spring-boot-app spring-boot:run
```

## 最近一次链路验证

- 前端构建通过
- 登录逻辑测试通过
- 前后端代理链路测试通过
- 浏览器 E2E 登录首页通过
