package common.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * CanalMQ消息
 * @author gaozijie
 * @since 2024-02-01
 */
@Data
public class CanalMessage {

    /**
     * 数据库名
     */
    private String database;

    /**
     * 表名
     */
    private String table;

    /**
     * 操作类型(INSERT, UPDATE, DELETE, QUERY)
     */
    private String type;

    /**
     * 数据
     */
    private List<Map<String, String>> data;

    /**
     * 旧数据
     */
    private List<Map<String, String>> old;
}
