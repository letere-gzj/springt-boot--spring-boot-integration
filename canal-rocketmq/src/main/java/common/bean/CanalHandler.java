package common.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.annotation.CanalTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Canal处理器抽象类
 * @author gaozijie
 * @since 2024-01-19
 */
public abstract class CanalHandler<T> {

    private static final Log logger = LogFactory.getLog(CanalHandler.class);

    private Class<T> tClass;

    /**
     * 初始化
     * @param canalHandlerFactory canal处理器工厂
     */
    @SuppressWarnings("unchecked")
    public void init(CanalHandlerFactory canalHandlerFactory) {
        // 记录泛型Class
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        tClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        // handler放入缓存
        CanalTable annotation = this.getClass().getAnnotation(CanalTable.class);
        canalHandlerFactory.register(annotation.database(), annotation.table(), this);
        logger.info(String.format("database:[%s], table:[%s] --- handler:[%s], 注册成功", annotation.database(), annotation.table(), this.getClass().getName()));
    }

    /**
     * canal消息处理
     * @param canalMapper canal json转换类
     * @param canalMessage canal消息
     */
    public void handleMsg(ObjectMapper canalMapper, CanalMessage canalMessage) {
        // 数据json转换
        JavaType javaType = canalMapper.getTypeFactory().constructParametricType(List.class, tClass);
        List<T> dataList;
        List<T> oldList;
        try {
            dataList = canalMapper.readValue(canalMapper.writeValueAsString(canalMessage.getData()), javaType);
            oldList = canalMapper.readValue(canalMapper.writeValueAsString(canalMessage.getOld()), javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 遍历处理数据
        switch (canalMessage.getType()) {
            case "INSERT":
                for (T data : dataList) {
                    this.insert(data);
                }
                break;
            case "UPDATE":
                for (int i=0; i<dataList.size(); i++) {
                    this.update(oldList.get(i), dataList.get(i));
                }
                break;
            case "DELETE":
                for (T data : dataList) {
                    this.delete(data);
                }
                break;
            default:
        }
    }

    /**
     * 插入
     * @param data 数据
     */
    protected void insert(T data) {};

    /**
     * 更新
     * @param beforeData 更新前数据
     * @param afterData 更新后数据
     */
    protected void update(T beforeData, T afterData) {};

    /**
     * 删除
     * @param data 数据
     */
    protected void delete(T data) {};
}
