package listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gaozijie
 * @since 2024-05-11
 */
public abstract class BaseExcelListener<T> implements ReadListener<T> {

    private List<ExcelProperty> headers;

    /**
     * 每一条表头数据解析都会来调用
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> headerMap = ConverterUtils.convertToStringMap(headMap, context);
        if (headers == null) {
            this.getExcelHeaders();
        }
        // 表头校验
        if (!Objects.equals(headerMap.size(), headers.size())) {
            throw new RuntimeException("excel模板错误，跟excel实体类不一致");
        }
        int row = context.readRowHolder().getRowIndex();
        for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
            if (!Objects.equals(entry.getValue(), headers.get(entry.getKey()).value()[row])) {
                throw new RuntimeException("excel模板错误，跟excel实体类不一致");
            }
        }
    }

    /**
     * 获取excel实体类表头
     */
    private void getExcelHeaders() {
        // 获取泛型Class
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> tClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        // 通过反射记录表头名
        headers = Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelProperty.class))
                .map(field -> field.getAnnotation(ExcelProperty.class))
                .collect(Collectors.toList());
    }
}
