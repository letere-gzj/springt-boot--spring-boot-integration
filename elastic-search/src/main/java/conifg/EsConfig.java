package conifg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author gaozijie
 * @since 2024-02-21
 */
public class EsConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Override
    public ClientConfiguration clientConfiguration() {
        // 切割uris并去除http开头
        String[] uriArr = uris.split(",");
        for (int i=0; i<uriArr.length; i++) {
            uriArr[i] = uriArr[i].split("://")[1];
        }
        return ClientConfiguration.builder()
                .connectedTo(uris)
                .build();
    }

    /**
     * 日期相关数据存入ES会自动变成时间戳，手动注入一个将Long类型转成LocalDateTime的转换器
     * @param mappingContext 映射上下文
     * @return Es转换器
     */
    @Primary
    @Bean
    ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(longToLocalDateTimeConverter());
        return new MappingElasticsearchConverter(mappingContext, defaultConversionService);
    }

    /**
     * Long类型转LocalDateTime转换器
     * @return 转换器
     */
    private Converter<Long, LocalDateTime> longToLocalDateTimeConverter() {
        return new Converter<>() {
            @Override
            public LocalDateTime convert(Long source) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(source), ZoneId.systemDefault());
            }
        };
    }

}
