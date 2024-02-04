package listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.bean.CanalHandler;
import common.bean.CanalHandlerFactory;
import common.bean.CanalMessage;
import common.constant.CanalConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * canal主题消息监听
 * @author gaozijie
 * @since 2024-01-11
 */
@Component
@RocketMQMessageListener(topic = CanalConstant.CANAL_MQ_TOPIC, consumerGroup = CanalConstant.CANAL_MQ_GROUP)
public class CanalTopicListener implements RocketMQListener<CanalMessage> {

    @Autowired
    private ObjectMapper canalMapper;

    @Autowired
    private CanalHandlerFactory canalHandlerFactory;

    @Override
    public void onMessage(CanalMessage canalMessage) {
        System.out.println(canalMessage);
        // 获取对应处理器，并处理消息
        CanalHandler<?> canalHandler = canalHandlerFactory.get(canalMessage.getDatabase(), canalMessage.getTable());
        if (Objects.isNull(canalHandler)) {
            return;
        }
        canalHandler.handleMsg(canalMapper, canalMessage);
    }
}
