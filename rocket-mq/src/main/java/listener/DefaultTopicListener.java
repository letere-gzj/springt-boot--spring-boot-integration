package listener;

import constant.RocketMqConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 默认主题消息监听
 * @author gaozijie
 * @since 2024-01-11
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.DEFAULT_TOPIC, consumerGroup = RocketMqConstant.DEFAULT_GROUP)
public class DefaultTopicListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println(LocalDateTime.now() + s);
    }
}
