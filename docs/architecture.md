# 架构与目录说明

## 总览

项目采用前后端分离结构：

- `agile-frontend`
  Vue 3、TypeScript、Vite、Pinia、Element Plus
- `agile-backend`
  Maven 多模块、Spring Boot、MyBatis Plus、Activiti

## 后端模块

- `ab-base`：基础能力、通用模型、公共工具
- `ab-component`：缓存、消息、上传、脚本等组件能力
- `ab-org`：组织、用户、角色相关能力
- `ab-auth`：认证与授权
- `ab-sys`：系统配置、字典、日志、文件等
- `ab-cms`：公告、新闻等内容模块
- `ab-code-generator`：代码生成相关功能
- `ab-demo`：示例与演示模块
- `ab-spring-boot`：Spring Boot 启动与整合层

## 前端目录

- `src/api`：接口定义
- `src/components`：通用组件
- `src/layout`：整体布局
- `src/router`：路由配置
- `src/store`：状态管理
- `src/utils`：工具函数
- `src/views`：页面模块
- `scripts`：测试、构建辅助脚本

## 文档目录

- `docs/sql`：数据库脚本
- `docs/tushi`：截图与展示资料
- `docs/compliance`：开源与合规审计
- `docs/archive`：历史说明、旧协议、旧目录残片归档

## 其他目录

- `libraries`：后端聚合打包依赖的本地二进制库
