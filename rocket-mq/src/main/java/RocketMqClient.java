import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RocketMQ工具类
 * @author gaozijie
 * @since 2024-01-10
 */
@Component
public class RocketMqClient {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    /**
     * 构建rocketMqTemplate
     * @param topic 消息主题
     * @return rocketMQTemplate
     */
    private RocketMQTemplate getRocketMqTemplate(String topic) {
        RocketMQTemplate rocketMqTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMqProducer = new DefaultMQProducer(topic);
        defaultMqProducer.setNamesrvAddr(nameServer);
        rocketMqTemplate.setProducer(defaultMqProducer);
        try {
            defaultMqProducer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return rocketMqTemplate;
    }

    /**
     * 发送同步消息
     * @param topic 消息主题
     * @param msg 消息内容
     * @return 发送状态
     */
    public SendResult sendSyncMsg(String topic, Object msg) {
        RocketMQTemplate rocketMqTemplate = this.getRocketMqTemplate(topic);
        return rocketMqTemplate.syncSend(topic, msg);
    }

    /**
     * 发送异步消息
     * @param topic 消息主题
     * @param msg 消息内容
     * @param sendCallback 异步回调逻辑（onSuccess, onFail）
     */
    public void sendAsyncMsg(String topic, Object msg, SendCallback sendCallback) {
        RocketMQTemplate rocketMqTemplate = this.getRocketMqTemplate(topic);
        rocketMqTemplate.asyncSend(topic, msg, sendCallback);
    }

    /**
     * 发送延迟消息
     * @param topic 消息主题
     * @param msg 消息内容
     * @param delayTime 延迟时间（单位：秒）
     * @return 发送状态
     */
    public SendResult sendDelayMsg(String topic, Object msg, Long delayTime) {
        RocketMQTemplate rocketMqTemplate = this.getRocketMqTemplate(topic);
        return rocketMqTemplate.syncSendDelayTimeSeconds(topic, msg, delayTime);
    }
}
