package bean.shop;

import lombok.Data;

import java.util.List;

/**
 * 创建门店响应
 * @author gaozijie
 * @since 2024-11-27
 */
@Data
public class AddShopResp {

    /**
     * 	成功导入门店的数量
     */
    private Integer success;

    /**
     * 成功导入的门店
     */
    private List<SuccessStation> successList;

    /**
     * 导入失败门店编号以及原因
     */
    private List<FailStation> failedList;


    @Data
    public static class SuccessStation {

        /**
         * 联系电话
         */
        private String phone;

        /**
         * 配送品类
         */
        private Integer business;

        /**
         * 经度
         */
        private Double lng;

        /**
         * 纬度
         */
        private Double lat;

        /**
         * 门店名称
         */
        private String stationName;

        /**
         * 门店编号
         */
        private String originShopId;

        /**
         * 联系人名称
         */
        private String contactName;

        /**
         * 门店地址
         */
        private String stationAddress;

        /**
         * 城市名
         */
        private String cityName;

        /**
         * 地区名
         */
        private String areaName;
    }

    @Data
    public static class FailStation {

        /**
         * 门店编号
         */
        private String shopNo;

        /**
         * 消息
         */
        private String msg;

        /**
         * 门店名称
         */
        private String shopName;

        /**
         * 响应编码
         */
        private Integer code;
    }
}
