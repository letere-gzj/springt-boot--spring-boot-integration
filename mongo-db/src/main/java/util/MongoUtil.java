package util;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author gaozijie
 * @since 2024-04-29
 */
public class MongoUtil {

    /**
     * 根据bean中非空值构建成mongo更新类
     * @param bean 类对象
     * @return mongo更新类
     * @param <T> 泛型
     */
    public static <T> Update getUpdate(T bean) {
        Update update = new Update();
        Map<String, Object> paramMap = getParamMap(bean);
        if (CollectionUtils.isEmpty(paramMap)) {
            return update;
        }
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        return update;
    }

    /**
     * 获取类中属性值
     * @param bean 类对象
     * @return key: 属性名, value: 属性值
     * @param <T> 泛型
     */
    private static <T> Map<String, Object> getParamMap(T bean) {
        Class<?> beanClass = bean.getClass();
        Map<String, Method> methodMap = new HashMap<>(16);
        int nameIndex;
        String filedName;
        // 记录get方法
        for (Method method : beanClass.getMethods()) {
            if (method.getParameterCount() == 0 && method.getReturnType() != Void.class) {
                if (method.getName().startsWith("get")) {
                    nameIndex = 3;
                } else if (method.getName().startsWith("is")) {
                    nameIndex = 2;
                } else {
                    continue;
                }
                filedName = String.valueOf(method.getName().charAt(nameIndex)).toLowerCase() + method.getName().substring(nameIndex+1);
                methodMap.put(filedName, method);
            }
        }
        // 调用get方法取值，非空则放入paramMap中
        Map<String, Object> paramMap = new HashMap<>(16);
        for (Field field : beanClass.getDeclaredFields()) {
            Method method = methodMap.get(field.getName());
            if (method != null) {
                try {
                    Object value = method.invoke(bean);
                    if (Objects.isNull(value)) {
                        continue;
                    }
                    paramMap.put(field.getName(), value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return paramMap;
    }
}
