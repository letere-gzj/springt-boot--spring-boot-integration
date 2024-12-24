package bean.order;

import lombok.Data;

/**
 * 查询运费响应
 * @author gaozijie
 * @since 2024-11-28
 */
@Data
public class QueryDeliveryFeeResp {

    /**
     * 	配送距离(单位：米)
     */
    private Double distance;

    /**
     * 平台订单号
     */
    private String deliveryNo;

    /**
     * 实际运费(单位：元)，运费减去优惠券费用
     */
    private Double fee;

    /**
     * 运费(单位：元)
     */
    private Double deliverFee;

    /**
     * 	优惠券费用(单位：元)
     */
    private Double couponFee;

    /**
     * 	小费（单位：元，精确小数点后一位，小费金额不能高于订单金额。）
     */
    private Double tips;

    /**
     * 	保价费(单位：元)
     */
    private Double insuranceFee;

    /**
     * 过期时间（时间戳10位，精确到分）
     */
    private Long expiredTime;
}
