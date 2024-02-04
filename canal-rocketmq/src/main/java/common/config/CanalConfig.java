package common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import common.bean.CanalHandler;
import common.bean.CanalHandlerFactory;
import common.constant.CanalConstant;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

/**
 * Canal配置
 * @author gaozijie
 * @since 2024-01-31
 */
@Configuration
public class CanalConfig {

    @Autowired
    private CanalHandlerFactory canalHandlerFactory;

    @Bean
    public ObjectMapper canalMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper;
    }

    @PostConstruct
    public void initHandler() {
        // 查找指定包名下所有继承了"CanalHandler"的类，并进行初始化
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(CanalHandler.class));
        Set<BeanDefinition> components = provider.findCandidateComponents(CanalConstant.HANDLER_PACKAGE);
        for (BeanDefinition component : components) {
            try {
                Class<?> cls = Class.forName(component.getBeanClassName());
                CanalHandler<?> canalHandler = (CanalHandler<?>) cls.getConstructor().newInstance();
                canalHandler.init(canalHandlerFactory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
