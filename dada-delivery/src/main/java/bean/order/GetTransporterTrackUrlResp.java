package bean.order;

import lombok.Data;

/**
 * 获取骑士配送信息H5页面响应
 * @author gaozijie
 * @since 2024-11-28
 */
@Data
public class GetTransporterTrackUrlResp {

    /**
     * 骑士配送信息H5页面链接
     */
    private String trackUrl;
}
