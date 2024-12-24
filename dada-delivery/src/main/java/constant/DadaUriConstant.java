package constant;

/**
 * 达达请求Uri常量
 * @author gaozijie
 * @since 2024-11-27
 */
public interface DadaUriConstant {

    /**
     * 订单状态回调地址
     */
    String ORDER_STATUS_CALLBACK = "/notice/dada/orderStatus/callback";

    /**
     * 直接下单
     */
    String ADD_ORDER = "/api/order/addOrder";

    /**
     * 查询运费
     */
    String QUERY_DELIVERY_FEE = "/api/order/queryDeliverFee";

    /**
     * 查询运费后下单
     */
    String ADD_ORDER_AFTER_QUERY = "/api/order/addAfterQuery";

    /**
     * 获取骑士配送信息H5页面
     */
    String GET_TRANSPORT_TRACK_URL = "/api/order/transporter/track";

    /**
     * 查询城市码
     */
    String LIST_CITY_CODE = "/api/cityCode/list";

    /**
     * 创建门店
     */
    String ADD_SHOP = "/api/shop/add";

    /**
     * 更新门店
     */
    String UPDATE_SHOP = "/api/shop/update";
}
