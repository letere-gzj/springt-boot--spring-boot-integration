package handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mp字段自动填充处理
 * @author gaozijie
 * @since 2023-08-07
 */
@Component
public class MybatisFieldFillHandler implements MetaObjectHandler {

    private final static String INSERT_FIELD = "createTime";

    private final static String UPDATE_FIELD = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, INSERT_FIELD, () -> now, LocalDateTime.class);
        this.strictInsertFill(metaObject, UPDATE_FIELD, () -> now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_FIELD, LocalDateTime::now, LocalDateTime.class);
    }
}
