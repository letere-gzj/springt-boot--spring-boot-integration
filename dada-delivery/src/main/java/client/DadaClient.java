package client;

import bean.DadaProperties;
import bean.DadaResponse;
import bean.order.*;
import bean.shop.AddShopReq;
import bean.shop.AddShopResp;
import bean.shop.ListCityCodeResp;
import bean.shop.UpdateShopReq;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import constant.DadaConstant;
import constant.DadaUriConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaozijie
 * @since 2024-11-27
 */
@Slf4j
@Component
public class DadaClient {

    private final ObjectMapper objectMapper;

    @Autowired
    private DadaProperties dadaProperties;

    {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 直接下单
     * @see "https://newopen.imdada.cn/#/development/file/add"
     * @param addOrderReq 直接下单请求
     * @return 直接下单响应
     */
    public AddOrderResp addOrder(AddOrderReq addOrderReq) {
        return this.sendRequest(DadaUriConstant.ADD_ORDER, addOrderReq, new TypeReference<>() {});
    }

    /**
     * 查询运费
     * @see "https://newopen.imdada.cn/#/development/file/readyAdd"
     * @param queryDeliveryFeeReq 查询运费请求
     * @return 查询运费响应
     */
    public QueryDeliveryFeeResp queryDeliveryFee(QueryDeliveryFeeReq queryDeliveryFeeReq) {
        return this.sendRequest(DadaUriConstant.QUERY_DELIVERY_FEE, queryDeliveryFeeReq, new TypeReference<>() {});
    }

    /**
     * 查询运费后下单
     * @see "https://newopen.imdada.cn/#/development/file/addAfterQuery"
     * @param addOrderAfterQueryReq 查询运费后下单请求
     */
    public void addOrderAfterQuery(AddOrderAfterQueryReq addOrderAfterQueryReq) {
        this.sendRequest(DadaUriConstant.ADD_ORDER_AFTER_QUERY, addOrderAfterQueryReq, new TypeReference<>() {}, false, true);
    }

    /**
     * 获取骑士配送信息H5页面
     * @see "https://newopen.imdada.cn/#/development/file/queryKnightH5Page"
     * @param getTransporterTrackUrlReq 获取配送轨迹Url请求
     * @return 获取配送轨迹Url响应
     */
    public GetTransporterTrackUrlResp getTransporterTrackUrl(GetTransporterTrackUrlReq getTransporterTrackUrlReq) {
        return this.sendRequest(DadaUriConstant.GET_TRANSPORT_TRACK_URL, getTransporterTrackUrlReq, new TypeReference<>() {});
    }

    /**
     * 查询城市码
     * @see "https://newopen.imdada.cn/#/development/file/cityList"
     * @return 城市码集合
     */
    public List<ListCityCodeResp> listCityCode() {
        return this.sendRequest(DadaUriConstant.LIST_CITY_CODE, "", new TypeReference<>() {});
    }

    /**
     * 创建门店
     * @see "https://newopen.imdada.cn/#/development/file/shopAdd"
     * @param addShopReqList 创建门店请求集合
     * @return 创建门店响应
     */
    public AddShopResp addShop(List<AddShopReq> addShopReqList) {
        return this.sendRequest(DadaUriConstant.ADD_SHOP, addShopReqList, new TypeReference<>() {}, true, false);
    }

    /**
     * 更新门店
     * @see "https://newopen.imdada.cn/#/development/file/shopUpdate"
     * @param updateShopReq 更新门店请求
     */
    public void updateShop(UpdateShopReq updateShopReq) {
        this.sendRequest(DadaUriConstant.UPDATE_SHOP, updateShopReq, new TypeReference<DadaResponse<Void>>() {});
    }

    /**
     * 验证签名
     * @param callback 回调参数
     * @return true/false
     */
    public boolean verifySignature(OrderStatusCallback callback) {
        String signature = this.callbackSignature(callback);
        return Objects.equals(signature, callback.getSignature());
    }

    /**
     * 发送请求
     * @param uri 请求uri
     * @param body 请求体
     * @return 响应体
     */
    private <T> T sendRequest(String uri, Object body, TypeReference<DadaResponse<T>> typeReference) {
        return this.sendRequest(uri, body, typeReference, false, false);
    }

    /**
     * 发送请求
     * @param uri 请求uri
     * @param body 请求体
     * @param ignoreFail 是否忽略失败异常捕获(result有数据时)
     * @param ignoreSnakeCase body转Json时是否忽略蛇形命名
     * @return 响应体
     */
    private <T> T sendRequest(String uri, Object body, TypeReference<DadaResponse<T>> typeReference, boolean ignoreFail, boolean ignoreSnakeCase) {
        // 参数构建
        DadaResponse<T> dadaResponse = this.baseSendRequest(uri, body, typeReference, ignoreSnakeCase);
        boolean showFailMsg = Objects.equals(dadaResponse.getStatus(), DadaConstant.STATUS_FAIL)
                && (!ignoreFail || Objects.isNull(dadaResponse.getResult()));
        if (showFailMsg) {
            log.info("达达配送请求异常：{} - {}", dadaResponse.getErrorCode(), dadaResponse.getMsg());
            throw new RuntimeException("达达配送异常: " + dadaResponse.getMsg());
        }
        return dadaResponse.getResult();
    }

    /**
     * 发送请求
     * @param uri 请求uri
     * @param body 请求体
     * @param ignoreSnakeCase body转Json时是否忽略蛇形命名
     * @return 响应体
     */
    private <T> DadaResponse<T> baseSendRequest(String uri, Object body, TypeReference<DadaResponse<T>> typeReference, boolean ignoreSnakeCase) {
        // 参数构建
        Map<String, Object> params = this.generateParams(body, dadaProperties, ignoreSnakeCase);
        String url = (Objects.equals(dadaProperties.getIsTestEnv(), true) ? DadaConstant.TEST_DOMAIN : DadaConstant.RELEASE_DOMAIN) + uri;
        log.info("DadaRequest ===> {} - {}", url, params);
        // 发送请求
        String response;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAcceptCharset(List.of(StandardCharsets.UTF_8));
            HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(params), httpHeaders);
            response = new RestTemplate().postForObject(url, httpEntity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("达达配送request发送异常:" + e.getMessage());
        }
        log.info("DadaResponse <=== {} - {}", url, response);
        DadaResponse<T> dadaResponse;
        try {
            dadaResponse = objectMapper.readValue(response, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("达达配送response数据解析异常:" + e.getMessage());
        }
        return dadaResponse;
    }

    /**
     * 生成请求参数
     * @param body 请求体
     * @param dadaProperties 达达配置
     * @param ignoreSnakeCase body转json是否忽略蛇形命名
     * @return 请求参数
     */
    private Map<String, Object> generateParams(Object body, DadaProperties dadaProperties, boolean ignoreSnakeCase) {
        String appSecret = dadaProperties.getAppSecret();
        Map<String, Object> data = new HashMap<>(7);
        data.put("source_id", dadaProperties.getShopSourceId());
        data.put("app_key", dadaProperties.getAppKey());
        data.put("timestamp", Instant.now().getEpochSecond());
        data.put("format", "json");
        data.put("v", "1.0");
        // 是否驼峰命名转蛇形
        String bodyJson;
        try {
            if (ignoreSnakeCase) {
                bodyJson = objectMapper.writeValueAsString(body);
            } else {
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                bodyJson = objectMapper.writeValueAsString(body);
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        data.put("body", bodyJson);
        data.put("signature", signature(data, appSecret));
        return data;
    }

    /**
     * 生成签名
     * @param data 请求数据
     * @param appSecret 应用密钥
     * @return 签名
     */
    private String signature(Map<String, Object> data, String appSecret) {
        // 请求参数按照【属性名】字典升序排序后，按照属性名+属性值拼接
        String signStr = data.keySet().stream()
                .sorted()
                .map(it -> String.format("%s%s", it, data.get(it)))
                .collect(Collectors.joining(""));
        // 拼接后的结果首尾加上appSecret
        String finalSignStr = appSecret + signStr + appSecret;
        // MD5加密并转为大写
        return DigestUtils.md5DigestAsHex(finalSignStr.getBytes()).toUpperCase();
    }

    /**
     * 生成回调签名
     * @param callback 回调参数
     * @return 回调签名
     */
    private String callbackSignature(OrderStatusCallback callback) {
        String clientId = callback.getClientId();
        String orderId = callback.getOrderId();
        Long updateTime = callback.getUpdateTime();
        // 将签名相关字段加入list
        List<String> list = new ArrayList<>(4);
        list.add(clientId == null ? "" : clientId);
        list.add(orderId == null ? "" : orderId);
        list.add(updateTime == null ? "" : updateTime.toString());
        // 将参与签名的字段的值进行升序排列
        Collections.sort(list);
        // 将排序过后的参数，进行字符串拼接
        String joinedStr = String.join("", list);
        // 对拼接后的字符串进行md5加密
        return DigestUtils.md5DigestAsHex(joinedStr.getBytes());
    }

}
