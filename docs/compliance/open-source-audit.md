# 开源合规审计

## 本次已完成

- 将前端重命名为 `agile-frontend`
- 将后端模块统一收拢到 `agile-backend`
- 清理前端历史嵌套仓库结构，统一由根仓库管理
- 清理大量首方 Java 源码中的作者、公司、所有者注释
- 将上游免责声明、补充协议等历史文件迁移到 `docs/archive/upstream-legal`
- 重写主 README 与安装文档，移除营销、群聊、联系方式等内容

## 已归档的历史文件

- [免责声明.original.md](/E:/Project/Vue_demo/agile-bpm-basic/docs/archive/upstream-legal/免责声明.original.md)
- [LICENSE_补充协议.original.md](/E:/Project/Vue_demo/agile-bpm-basic/docs/archive/upstream-legal/LICENSE_补充协议.original.md)
- [agile-frontend.LICENSE_补充协议.original.md](/E:/Project/Vue_demo/agile-bpm-basic/docs/archive/upstream-legal/agile-frontend.LICENSE_补充协议.original.md)

## 仍需人工复核

- 仓库根目录仍保留原始 [LICENSE](/E:/Project/Vue_demo/agile-bpm-basic/LICENSE)。
  这一步没有被自动改写，因为许可证变更需要确认源码权利归属。
- 部分源码包含第三方来源痕迹或二次集成产物，建议在正式对外发布前逐项确认再授权条件。
  典型位置包括：
  [AuthConstant.java](/E:/Project/Vue_demo/agile-bpm-basic/agile-backend/ab-auth/ab-auth-spring-security-oauth2/src/main/java/com/dstz/auth/constant/AuthConstant.java)
  [agile-frontend/agilebpm](/E:/Project/Vue_demo/agile-bpm-basic/agile-frontend/agilebpm)
  [bb.umd.min.js](/E:/Project/Vue_demo/agile-bpm-basic/agile-frontend/src/views/news/bb.umd.min.js)
- SQL 初始化脚本中仍保留部分示例组织、管理员、业务样例数据。
  如果计划对外完整开源发布，建议再做一轮示例数据脱敏。
- 仍有少量历史兼容标识保留在技术常量、包名或本地组件依赖名中。
  这些标识当前更多承担兼容作用，若要彻底去品牌化，建议在独立分支中配合数据库与前端依赖做专项迁移。

## 建议发布策略

1. 先确认项目主许可证是否具备改写权限。
2. 为第三方依赖和二次集成代码整理 `THIRD_PARTY_NOTICES`。
3. 对 SQL、图片、示例账号、演示文案做最终脱敏。
4. 完成一次人工法务复核后再公开分发。
