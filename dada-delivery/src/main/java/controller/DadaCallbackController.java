package controller;

import bean.order.OrderStatusCallback;
import client.DadaClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaozijie
 * @since 2024-12-23
 */
@Slf4j
@RestController
@RequestMapping("/dada/callback")
public class DadaCallbackController {

    private final ObjectMapper objectMapper;

    @Autowired
    private DadaClient dadaClient;

    {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PostMapping("/orderStatus")
    public void orderStatusCallback(@RequestBody String json) {
        log.info("达达订单状态回调 <=== " + json);
        // json蛇形转驼峰命名
        OrderStatusCallback callback;
        try {
            callback = objectMapper.readValue(json, OrderStatusCallback.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 签名校验
        if (!dadaClient.verifySignature(callback)) {
            // 签名有误，不往下处理
            log.info("达达回调请求签名异常：{}", callback.getSignature());
            return;
        }
        // TODO: 达达订单状态回调相关业务逻辑处理
    }
}
