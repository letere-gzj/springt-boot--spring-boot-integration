package common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Canal表注解
 * @author gaozijie
 * @since 2024-01-19
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CanalTable {

    String database();

    String table();
}
