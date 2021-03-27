关于后端写得太傻逼，四月准备两周内重构，架构如下：

模式：SpringAOP

数据库：mysql

缓存：redis

支持异步高并发（SpringWebFlux）

切面鉴权：JWT

项目结构：

    ——src
         —— Controller
            ——v1
              ---manyController
         —— Model
            ——Models
            ——DAO
              ——JPARespository
         ——Redis
            ——redisConfig
         ——Until
            ——Email
            ——VerifyCode
            ——JWT
   