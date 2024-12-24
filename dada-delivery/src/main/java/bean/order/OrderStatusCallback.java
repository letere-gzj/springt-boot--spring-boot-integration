package bean.order;

import lombok.Data;

/**
 * 订单状态回调
 * @author gaozijie
 * @since 2024-12-02
 */
@Data
public class OrderStatusCallback {

    /**
     * 达达物流订单号
     */
    private String clientId;

    /**
     * 	第三方订单ID
     */
    private String orderId;

    /**
     * 订单状态(待接单＝1,待取货＝2,骑士到店=100,配送中＝3,已完成＝4,已取消＝5, 已追加待接单=8,妥投异常之物品返回中=9, 妥投异常之物品返回完成=10, 售后取件单送达门店=6, 创建达达运单失败=1000）
     */
    private String orderStatus;

    /**
     * 	重复回传状态原因(1-重新分配骑士，2-骑士转单)
     */
    private String repeatReasonType;

    /**
     * 订单取消原因
     */
    private String cancelReason;

    /**
     * 	订单取消原因来源(1:达达配送员取消；2:商家主动取消；3:系统或客服取消；0:默认值)
     */
    private String cancelFrom;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 签名
     */
    private String signature;

    /**
     * 	达达配送员id
     */
    private Integer dmId;

    /**
     * 	配送员姓名
     */
    private String dmName;

    /**
     * 配送员手机号
     */
    private String dmMobile;

    /**
     * 收货码
     */
    private String finishCode;
}
