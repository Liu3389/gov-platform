# 智慧政务一体化便民服务平台

> 实现"一网通办、一窗受理、协同办理"的政务服务新模式

## 项目结构

```text
gov-platform/
├── 00-项目文档/              ← 项目文档（组长维护）
├── docker/                   ← Docker 本地环境
│   ├── docker-compose.yml    ← 一键启动所有中间件
│   ├── mysql/init/           ← MySQL 初始化脚本
│   └── nginx/nginx.conf      ← Nginx 配置
├── gov-platform-parent/      ← 父工程（版本锁定）
├── gov-common/               ← 公共模块
├── gov-gateway/              ← 网关服务（8091）    
├── gov-user-service/         ← 用户认证服务（8081）【模板】
├── gov-item-service/         ← 事项管理服务（8092）
├── gov-reception-service/    ← 统一受理服务（8083）
├── gov-activiti-service/     ← 审批流转服务（8084）【核心】
├── gov-license-service/      ← 电子证照服务（8085）
├── gov-complaint-service/    ← 投诉建议服务（8086）
├── gov-open-service/         ← 政务公开服务（8087）
├── gov-datashare-service/    ← 数据共享服务（8088）
├── gov-message-service/      ← 消息通知服务（8089）
└── gov-monitor-service/      ← 监察审计服务（8090）
```

## 快速启动

### 1. 启动 Docker 中间件

```bash
cd docker
docker-compose up -d
```

等待约 2 分钟，检查服务状态：

```bash
docker-compose ps
```

### 2. 初始化 Nacos 配置

1. 打开 Nacos 控制台：http://localhost:8848/nacos
2. 登录：用户名 `nacos`，密码 `nacos`
3. 创建命名空间：`gov-platform`
4. 导入配置：将 `docker/nacos/nacos_config_export.zip` 导入

### 3. 启动微服务

在 IDEA 中按顺序启动：

1. `gov-gateway`（8091）
2. `gov-user-service`（8081）
3. 其他业务服务

### 4. 验证

- 网关 Knife4j：http://localhost:8080/doc.html
- 用户服务 Knife4j：http://localhost:8081/doc.html

## 技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Spring Boot | 2.7.18 |
| 微服务 | Spring Cloud | 2021.0.8 |
| 微服务 | Spring Cloud Alibaba | 2021.0.5.0 |
| 注册中心 | Nacos | 2.3.2 |
| 持久层 | MyBatis-Plus | 3.5.5 |
| 数据库 | MySQL | 8.0.33 |
| 缓存 | Redis | 7.x |
| 工作流 | Activiti | 7.7.0 |
| 接口文档 | Knife4j | 4.4.0 |

## 团队分工

| 角色 | 负责服务 | 端口 |
|------|----------|------|
| 组长 | gov-gateway、gov-monitor、gov-common、父工程 | 8080、8090 |
| 组员A | gov-user-service、gov-item-service | 8081、8092 |
| 组员B | gov-reception-service、gov-activiti-service | 8083、8084 |
| 组员C | gov-license-service、gov-complaint-service | 8085、8086 |
| 组员D | gov-open-service、gov-datashare-service | 8087、8088 |

## 开发规范

详见 `00-项目文档/` 目录：

- [README.md](00-项目文档/README.md) - 文档总览
- [项目介绍.md](00-项目文档/项目介绍.md) - 项目总览
- [开发规范.md](00-项目文档/开发规范.md) - 工程规范
- [数据库设计文档.md](00-项目文档/数据库设计文档.md) - 表结构
- [工作流设计文档.md](00-项目文档/工作流设计文档.md) - BPMN 流程

## 常用命令

```bash
# 启动 Docker
docker-compose up -d

# 查看日志
docker-compose logs -f mysql

# 停止所有服务
docker-compose down

# 重新构建
docker-compose up -d --build

# Maven 打包
mvn clean package -DskipTests

# Maven 安装公共模块
cd gov-common && mvn clean install -DskipTests
```

## 注意事项

1. **Mac 用户**：Docker Desktop 需要开启 Kubernetes（可选）
2. **Windows 用户**：建议使用 WSL2 运行 Docker
3. **端口冲突**：检查 3306、6379、8848、8080 是否被占用
4. **首次启动**：MySQL 初始化需要约 1 分钟，请等待后再启动微服务