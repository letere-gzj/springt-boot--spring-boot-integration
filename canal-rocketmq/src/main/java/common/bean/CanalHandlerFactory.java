package common.bean;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Canal处理器工厂
 * @author gaozijie
 * @since 2024-01-31
 */
@Component
public class CanalHandlerFactory {

    private final ConcurrentMap<String, CanalHandler<?>> cache = new ConcurrentHashMap<>(16);

    /**
     * 注册canal处理器
     * @param database 数据库名
     * @param table 表名
     * @param canalHandler canal处理器
     */
    public void register(String database, String table, CanalHandler<?> canalHandler) {
        synchronized (cache) {
            cache.put(this.getCacheName(database, table), canalHandler);
        }
    }

    /**
     * 获取canal处理器
     * @param database 数据库名
     * @param table 表名
     * @return canal处理器
     */
    public CanalHandler<?> get(String database, String table) {
        return cache.get(this.getCacheName(database, table));
    }

    /**
     * 获取缓存名称
     * @param database 数据库名
     * @param table 表名
     * @return 缓存名
     */
    private String getCacheName(String database, String table) {
        return database + ":" + table;
    }
}
