package bean.shop;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取城市码响应
 * @author gaozijie
 * @since 2024-11-28
 */
@Data
public class ListCityCodeResp implements Serializable {

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 城市编码
     */
    private String cityCode;
}
