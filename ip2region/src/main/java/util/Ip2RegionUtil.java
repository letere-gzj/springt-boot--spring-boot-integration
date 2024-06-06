package util;

import lombok.Data;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gaozijie
 * @since 2024-06-06
 */
public class Ip2RegionUtil {

    private final static Logger logger = LoggerFactory.getLogger(Ip2RegionUtil.class);

    /** ip2region.xdb文件位置 */
    private static final String DB_RELATIVE_PATH = "src/main/resources/ip2region/ip2region.xdb";

    /**
     * 缓存
     */
    private static byte[] vectorIndex;

    static {
        // 将vectorIndex预先加载
        try {
            vectorIndex = Searcher.loadVectorIndexFromFile(DB_RELATIVE_PATH);
        } catch (Exception e) {
            logger.error("failed to load vector index from `{}`: {}", DB_RELATIVE_PATH, e);
        }
    }

    @Data
    public static class Location {
        /**
         * 国家
         */
        private String country;
        /**
         * 省
         */
        private String province;
        /**
         * 市
         */
        private String city;
        /**
         * 服务提供商
         */
        private String isp;

        public Location(String region) {
            // 模板：中国|0|广东省|广州市|电信
            String[] locations = region.split("\\|");
            this.country = "0".equals(locations[0]) ? null : locations[0];
            this.province = "0".equals(locations[2]) ? null :locations[2];
            this.city = "0".equals(locations[3]) ? null : locations[3];
            this.isp = "0".equals(locations[4]) ? null : locations[4];
        }
    }

    /**
     * 通过ip地址，寻找对应的省，市位置
     * @param ip ip地址
     * @return ip所属位置
     */
    public static Location getLocationFromIp(String ip) {
        Location location = null;
        try {
            Searcher searcher = Searcher.newWithVectorIndex(DB_RELATIVE_PATH, vectorIndex);
            // 查询并封装为Location
            String search = searcher.search(ip);
            if (search != null && search.trim().length() > 0) {
                location = new Location(search);
            }
            searcher.close();
        } catch (Exception e) {
            logger.error("failed to find location from ip[{}]", ip);
        }
        return location;
    }
}
