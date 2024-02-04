### 说明
+ 个人与第三方工具整合配置，方便后续项目二开直接挪用

---

### 子项目介绍
+ awssdk: 整合亚马逊s3协议，通过s3协议对多种云存储服务(常见包括minio, oss, cos, obs, kodo)进行统一管理，进行文件上传操作
+ canal-rocketmq：canal与rocketmq整合，监听数据库对应表的数据变化，发送消息给MQ，MQ再发送消息给对应的消费者
+ java-mail：java自带邮件发送类整合
+ mybatis-plus：整合mp，操作sql
+ redis：整合redis，操作缓存
+ rocket-mq：整合rocket-mq，发送消息