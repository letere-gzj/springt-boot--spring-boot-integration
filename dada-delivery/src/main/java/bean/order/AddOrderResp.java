package bean.order;

import lombok.Data;

/**
 * 直接下单响应
 * @author gaozijie
 * @since 2024-12-03
 */
@Data
public class AddOrderResp {

    /**
     * 	配送距离(单位：米)
     */
    private Double distance;

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
}
