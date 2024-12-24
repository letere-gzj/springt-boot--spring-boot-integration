package bean.order;

import lombok.Data;

import java.util.List;

/**
 * 直接下单请求
 * @author gaozijie
 * @since 2024-12-03
 */
@Data
public class AddOrderReq {

    /**
     * 门店编号
     */
    private String shopNo;

    /**
     * 第三方订单Id
     */
    private String originId;

    /**
     * 订单金额（单位：元）
     */
    private Double cargoPrice;

    /**
     * 是否需要垫付（1:是 0:否）
     */
    private Integer isPrepay;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人地址
     */
    private String receiverAddress;

    /**
     * 收货人地址纬度
     */
    private Double receiverLat;

    /**
     * 收货人地址经度
     */
    private Double receiverLng;

    /**
     * 收货人手机号
     */
    private String receiverPhone;

    /**
     * 收货人座机号
     */
    private String receiverTel;

    /**
     * 回调地址
     */
    private String callback;

    /**
     * 订单重量（单位：kg）
     */
    private Double cargoWeight;

    /**
     * 小费（单位：元，精确小数点后一位，小费金额不能高于订单金额）
     */
    private Double tips;

    /**
     * 订单备注
     */
    private String info;

    /**
     * 支持配送的物品品类见链接
     */
    private Integer cargoType;

    /**
     * 订单商品数量
     */
    private Integer cargoNum;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 订单来源标识
     */
    private String originMark;

    /**
     * 订单来源编号
     */
    private String originMarkNo;

    /**
     * 是否使用保价费（0：不使用保价，1：使用保价)
     */
    private Integer isUseInsurance;

    /**
     * 是否需要收货码（0：不需要；1：需要）
     */
    private Integer isFinishCodeNeeded;

    /**
     * 预约发单时间（unix时间戳(10位)，精确到分）
     */
    private Integer delayPublishTime;

    /**
     * 是否根据期望送达时间预约发单（0：否，即时发单；1：是，预约发单）
     */
    private Integer isExpectFinishOrder;

    /**
     * 期望送达时间（单位秒，不早于当前时间）
     */
    private Long expectFinishTimeLimit;

    /**
     * 是否选择直拿直送（0：不需要；1：需要）
     */
    private Integer isDirectDelivery;

    /**
     * 订单商品明细
     */
    private List<DadaProd> productList;

    /**
     * 货架信息
     */
    private String pickUpPos;

    /**
     * 取货码
     */
    private String fetchCode;

    /**
     * 物流配送方向(0：正向送货单、2：售后退货取件单)
     */
    private Integer businessType;

    /**
     * 发货人姓名
     */
    private String supplierName;

    /**
     * 发货人地址
     */
    private String supplierAddress;

    /**
     * 发货人电话
     */
    private String supplierPhone;

    /**
     * 发货人纬度
     */
    private String supplierLat;

    /**
     * 发货人经度
     */
    private String supplierLng;

    @Data
    public static class DadaProd {
        /**
         * 商品名称，限制长度128
         */
        private String skuName;

        /**
         * 	商品编码，限制长度64
         */
        private String srcProductNo;

        /**
         * 商品数量，精确到小数点后两位
         */
        private Double count;

        /**
         * 商品单位，默认：件
         */
        private String unit;
    }
}
