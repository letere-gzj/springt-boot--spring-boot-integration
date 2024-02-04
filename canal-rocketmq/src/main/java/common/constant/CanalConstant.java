package common.constant;

/**
 * Canal相关常量
 * @author gaozijie
 * @since 2024-02-04
 */
public interface CanalConstant {

    /**
     * canal处理器所在包名(默认全项目扫描，配置了可以加快启动速度)
     */
    String HANDLER_PACKAGE = "";

    /**
     * RocketMq对应的canal消息的topic
     */
    String CANAL_MQ_TOPIC = "canal-topic";

    /**
     * RocketMQ对应的canal消息的group
     */
    String CANAL_MQ_GROUP = "canal-group";
}
