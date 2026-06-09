# Farm Server

农场经营 + 摆摊小游戏 Java 后端。MVP 阶段采用 Spring Boot 3 单体架构，内部按业务模块拆分，后续可平滑拆分为服务。

## 技术栈

- Java 21
- Spring Boot 3.3.x
- MySQL 8
- Redis 6
- Flyway
- MyBatis-Plus
- Springdoc OpenAPI / Swagger UI

## 本地 Maven

项目默认目标 Java 21。当前本机如果还没有 JDK 21，可以先用已安装的 JDK 17 和 `local-jdk17` profile 开发：

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17.0.8'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
D:\lcInstall\Maven\apache-maven-3.6.3\bin\mvn.cmd -s D:\Farm\code\farm-server\settings-workspace.xml -P local-jdk17 clean package
```

如果当前 Windows 环境对 `target` 目录清理或 Spring Boot repackage 重命名报拒绝访问，可先验证普通构建：

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17.0.8'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
D:\lcInstall\Maven\apache-maven-3.6.3\bin\mvn.cmd -s D:\Farm\code\farm-server\settings-workspace.xml -P local-jdk17 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package
```

优先使用：

```powershell
D:\lcInstall\Maven\apache-maven-3.6.3\bin\mvn.cmd -s D:\lcInstall\Maven\apache-maven-3.6.3\conf\settings.xml -Dmaven.repo.local=D:\code\Maven_repo\repository clean package
```

如果本地仓库不可用，再切换到工作区仓库：

```powershell
D:\lcInstall\Maven\apache-maven-3.6.3\bin\mvn.cmd -s D:\Farm\code\farm-server\settings-workspace.xml clean package
```

## Swagger

启动后访问：

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

OpenAPI JSON 可导入 Apifox 或 Postman。

## 数据库初始化

首次部署时先执行：

```sql
scripts/00_create_database.sql
```

应用启动后 Flyway 会自动执行：

```text
src/main/resources/db/migration
```

## 关键文档

- [开发约束](docs/DEVELOPMENT_CONVENTIONS.md)
- [MVP 功能大纲](docs/MVP_OUTLINE.md)
- [数据库说明](docs/DATABASE.md)
- [接口说明](docs/API.md)
- [开发日志](logs/CHANGELOG.md)
