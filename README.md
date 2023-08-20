# easychat-server

## 介绍

easychat-server 是 EasyChat 项目的后端部分，采用微服务架构，使用 Java 语言，基于 Spring Cloud 开发。

EasyChat 是一个可以在线聊天的即时通讯平台，完全由个人设计和开发，初衷主要是觉得好玩儿，顺便学习一些新东西，所以网站有些简陋，属实是本人的能力和财力有限。网站没有任何付费内容，所有内容完全免费，欢迎大家使用体验，也期待大佬们的交流与反馈。

体验地址：[https://toollong.icu](https://toollong.icu)（网站已于 2023.08.18 下线，运行时长 382 天）

> ### 功能介绍

- 注册
- 登录
- 找回密码
- 添加聊天
- 删除聊天
- 发送文本消息
- 发送 Emoji 表情（Windows 7 可能无法正常显示）
- 发送图片（批量发送）
- 发送文件（同步发送，有点慢）
- 新消息通知（有提示音）
- 搜索用户
- 修改好友备注
- 好友申请
- 好友验证
- 好友删除（单向删除）
- 验证消息通知（有提示音）
- 查看资料
- 编辑资料
- 设置（头像、隐身、标签、修改密码）
- 夜间模式
- 小抽屉（搜一搜、日历）


> ### 技术栈

- 前端：Vue 3，Vue Router，Vuex，Element Plus，Socket.IO，Axios，VueUse...

- 后端：Spring Cloud，Nacos，MyBatis-Plus，Netty-socketio，MinIO，Gson，MySQL，Redis，Docker...

> ### 注意

- 本网站仅供学习交流使用，由于网站的安全保障和加密措施并不完善，**请勿在网站中输入敏感信息**，避免信息泄露的风险。

- 我的邮箱：toollong@163.com

- 我的博客：[https://blog.csdn.net/weixin_49523761](https://blog.csdn.net/weixin_49523761)

## 导入

- 将项目导入喜欢的 IDE 中，等待相关依赖和环境加载完成。
- 在 MySQL 数据库中新建数据库并导入 sql 文件夹中的表结构。

## 运行

需要修改的配置：

- 各服务的配置文件 application.yml 中的 mysql 配置、redis 配置、nacos 配置、minio 配置；
- easychat-gateway 服务的配置文件 application.yml 中的 CORS 配置；
- easychat-auth 服务的 AuthController 类中的两处 Cookie 作用域改为自己的域名或ip；
- easychat-user 服务的 UserController 类中的 Cookie 作用域改为自己的域名或ip，还有 EmailServiceImpl 类中的邮箱设置也需要修改。

配置完成后启动运行各个服务的启动类即可。

## 打包

```
mvn clean install
```
