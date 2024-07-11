### 说明
+ 个人与第三方工具整合配置，方便后续项目二开直接挪用

---

### 子项目介绍
+ awssdk：整合亚马逊s3协议，通过s3协议对多种云存储服务(常见包括minio, oss, cos, obs, kodo)进行统一管理，进行文件上传操作
+ canal-rocketmq：canal与rocketmq整合，监听数据库对应表的数据变化，发送消息给MQ，MQ再发送消息给对应的消费者
+ elastic-search：官方整合es，增删查改es数据
+ ip2region：整合ip2region，通过ip地址查询对应地区位置
+ java-mail：java自带邮件发送类整合
+ just-auth：整合just-auth，来实现第三方登录功能
+ mybatis-plus：整合mp，操作sql
+ redis：整合redis，操作缓存
+ rocket-mq：整合rocket-mq，发送消息
+ sensitive-word：整合敏感词过滤功能，使用DFA算法实现
+ websocket：整合websocket，客户端，服务端双端通信