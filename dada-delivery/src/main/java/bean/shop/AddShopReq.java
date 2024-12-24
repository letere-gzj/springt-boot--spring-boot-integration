package bean.shop;

import lombok.Data;

/**
 * 创建门店请求
 * @author gaozijie
 * @since 2024-11-27
 */
@Data
public class AddShopReq {

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
     * 门店编码
     * (可自定义,但必须唯一;若不填写,则系统自动生成)
     */
    private String originShopId;

    /**
     * 联系人身份证
     */
    private String idCard;

    /**
     * 达达商家app账号
     */
    private String username;

    /**
     * 达达商家app密码
     */
    private String password;

    /**
     * 门店独立结算
     * (0-非独立结算, 1-独立结算，默认为0)
     */
    private Integer settlementType;
}
