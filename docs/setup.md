# 安装与运行

## 环境要求

- JDK 8+
- Maven 3.8+
- Node.js 18 左右
- MySQL 8+

## 后端

1. 在仓库根目录执行：

```bash
mvn clean install -DskipTests
```

2. 执行初始化脚本：

- 流程平台基础版：
  [仅流程平台的完整SQL.sql](/E:/Project/Vue_demo/agile-bpm-basic/docs/sql/mysql/full/仅流程平台的完整SQL.sql)
- 含资产模块：
  [含资产管理的SQL.sql](/E:/Project/Vue_demo/agile-bpm-basic/docs/sql/mysql/full/含资产管理的SQL.sql)
- 含客户关系与资产模块：
  [客户关系&资产完整SQL.sql](/E:/Project/Vue_demo/agile-bpm-basic/docs/sql/mysql/full/客户关系&资产完整SQL.sql)

3. 修改配置文件：

- [application.yml](/E:/Project/Vue_demo/agile-bpm-basic/agile-backend/ab-spring-boot/ab-spring-boot-app/src/main/resources/application.yml)

4. 启动入口：

- [AbSpringBootApp.java](/E:/Project/Vue_demo/agile-bpm-basic/agile-backend/ab-spring-boot/ab-spring-boot-app/src/main/java/com/dstz/AbSpringBootApp.java)

## 前端

1. 进入目录：

```bash
cd agile-frontend
```

2. 安装依赖：

```bash
npm install --legacy-peer-deps --no-package-lock
```

3. 启动开发环境：

```bash
npm run dev
```

4. 构建生产包：

```bash
npm run build
```

## 地址

- 后端：`http://localhost:8080`
- 前端：`http://127.0.0.1:8088`
