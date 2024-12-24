package bean.order;

import lombok.Data;

/**
 * 查询运费后下单请求
 * @author gaozijie
 * @since 2024-11-28
 */
@Data
public class AddOrderAfterQueryReq {

    /**
     * 平台订单编号
     */
    private String deliveryNo;

    /**
     * 	是否重置订单备注等字段。true：重置，false或缺失：不重置
     */
    private Boolean enableReset;

    /**
     * 订单备注内容
     */
    private String info;
}
