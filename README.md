# 项目搭建ing

## 当前进度：

第一版测试中，目前仅包含基础功能：注册，登录，打卡，查看打卡列表



## 引用技术：

1.SpringCloud分布式架构：将原本的单体项目拆分为两个单独的模块和若干辅助模块

2.将IP校验集成到GateWay的FilterChain，实现对来访请求的IP鉴别

3.将登录校验集成到GateWay的FilterChain，并且配合ThreadLocal实现用户id的多链路传递

3.使用OpenFeign实现rpc请求传递

3.将阿里OSS改为MinIO用于对头像等图片进行存储

4.运用Redis中间件进行对 来访请求IP存取，用户信息进行存取，验证码存取



## 碎碎念：

原本的项目是一个单体项目，但是可拓展性太差，于是决定拆成微服务架构。后面新增功能也可以直接写模块。



## 前端：

使用pnpm管理依赖项

```powershell
pnpm install # 安装依赖

pnpm dev # 研发环境启动
```





