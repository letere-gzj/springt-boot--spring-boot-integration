package bean.shop;

import lombok.Data;

/**
 * 更新门店请求
 * @author gaozijie
 * @since 2024-11-27
 */
@Data
public class UpdateShopReq {

    /**
     * 门店编号
     */
    private String originShopId;

    /**
     * 新的门店编号
     */
    private String newShopId;

    /**
     * 门店名称
     */
    private String stationName;

    /**
     * 支持配送的物品品类
     * @see "https://newopen.imdada.cn/#/development/file/categoryList"
     */
    private Integer business;

    /**
     * 门店地址
     */
    private String stationAddress;

    /**
     * 门店经度
     */
    private Double lng;

    /**
     * 门店纬度
     */
    private Double lat;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String phone;

    /**
     * 门店状态（1-门店激活，0-门店下线）
     */
    private Integer status;

    /**
     * 	门店独立结算(0-非独立结算, 1-独立结算，默认为0)
     */
    private Integer settlementType;
}
