package bean.order;

import lombok.Data;

/**
 * 获取骑士配送信息H5页面请求
 * @author gaozijie
 * @since 2024-11-28
 */
@Data
public class GetTransporterTrackUrlReq {

    /**
     * 第三方订单号
     */
    private String orderId;
}
