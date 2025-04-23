import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis工具
 * @author gaozijie
 * @since 2024-01-08
 */
@Component
public class RedisClient {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 设置值
     * @param key 关键词
     * @param value 值
     */
    public void set(String key, Object value) {
        try {
            String valStr = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, valStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置值
     * @param key 关键词
     * @param value 值
     * @param expireTime 过期时间（单位：秒）
     */
    public void set(String key, Object value, Long expireTime) {
        try {
            String valStr = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, valStr, expireTime);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量设置值
     * @param map key-value映射map
     */
    public void mset(Map<String, Object> map) {
        try {
            Map<String, String> strMap = new HashMap<>(16);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                strMap.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
            stringRedisTemplate.opsForValue().multiSet(strMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置值（当值不存在时）
     * @param key 关键词
     * @param value 值
     * @return 是否设置成功
     */
    public Boolean setnx(String key, Object value) {
        try {
            String valStr = objectMapper.writeValueAsString(value);
            return stringRedisTemplate.opsForValue().setIfAbsent(key, valStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置值（当值不存在时）
     * @param key 关键词
     * @param value 值
     * @param expireTime 过期时间（单位：秒）
     * @return 是否设置成功
     */
    public Boolean setnx(String key, Object value, Long expireTime) {
        try {
            String valStr = objectMapper.writeValueAsString(value);
            return stringRedisTemplate.opsForValue().setIfAbsent(key, valStr, expireTime, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为指定key设置过期时间
     * @param key 关键词
     * @param expireTime 过期时间（单位：秒）
     * @return 是否设置成功
     */
    public Boolean expire(String key, Long expireTime) {
        return stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 删除值
     * @param key 关键词
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 批量删除值
     * @param keys 关键词集合
     */
    public void del(List<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /**
     * 获取值
     * @param key 关键词
     * @param type 返回值类型
     * @return 值
     * @param <T> 返回值类型
     */
    public <T> T get(String key, TypeReference<T> type) {
        try {
            String valStr = stringRedisTemplate.opsForValue().get(key);
            if (Objects.isNull(valStr)) {
                return null;
            }
            return objectMapper.readValue(valStr, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量获取值
     * @param keys 关键词集合
     * @param type 返回数据类型
     * @return 值集合
     * @param <T> 返回数据类型
     */
    public <T> List<T> mget(List<String> keys, TypeReference<T> type) {
        try {
            List<String> valueStrs = stringRedisTemplate.opsForValue().multiGet(keys);
            if (CollectionUtils.isEmpty(valueStrs)) {
                return new ArrayList<>(0);
            }
            List<T> values = new ArrayList<>(valueStrs.size());
            for (String valueStr : valueStrs) {
                if (Objects.isNull(valueStr)) {
                    values.add(null);
                    continue;
                }
                values.add(objectMapper.readValue(valueStr, type));
            }
            return values;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取符合匹配条件的关键词（阻塞）
     * @param pattern 匹配条件
     * @return 关键词集合
     */
    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * 获取符合匹配条件的关键词（非阻塞）
     * @param pattern 匹配条件
     * @return 关键词集合
     */
    public Set<String> scan(String pattern) {
        Set<String> keys = new HashSet<>(16);
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(1000)
                .build();
        try (Cursor<String> cursor = stringRedisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }
        return keys;
    }

    /**
     * 执行lua脚本
     * @param scriptStr 脚本内容
     * @param keys 关键词集合
     * @param params 参数集合
     * @param type 返回数据类型
     * @return 脚本返回内容
     * @param <T> 返回数据类型泛型
     */
    public <T> T eval(String scriptStr, List<String> keys, List<Object> params, TypeReference<T> type) {
        // 构建脚本对象
        DefaultRedisScript<Object> script = new DefaultRedisScript<>(scriptStr, Object.class);
        // 执行脚本
        return this.evalScript(script, keys, params, type);
    }

    /**
     * 执行lua脚本
     * @param scriptPath 脚本路径
     * @param keys 关键词集合
     * @param params 参数集合
     * @param type 返回数据类型
     * @return 脚本返回内容
     * @param <T> 返回数据类型泛型
     */
    public <T> T evalScriptPath(String scriptPath, List<String> keys, List<Object> params, TypeReference<T> type) {
        // 构建脚本对象
        DefaultRedisScript<Object> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(scriptPath)));
        script.setResultType(Object.class);
        // 执行脚本
        return this.evalScript(script, keys, params, type);
    }

    /**
     * 执行lua脚本
     * @param script 脚本对象
     * @param keys 关键词集合
     * @param params 参数集合
     * @param type 返回数据类型
     * @return 脚本返回内容
     * @param <T> 返回数据类型泛型
     */
    private <T> T evalScript(RedisScript<Object> script, List<String> keys, List<Object> params, TypeReference<T> type) {
        // 执行脚本
        Object result = stringRedisTemplate.execute(script, keys, params.toArray(new Object[0]));
        if (Objects.isNull(result)) {
            return null;
        }
        // 数据转换返回
        try {
            return objectMapper.readValue(result.toString(), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
