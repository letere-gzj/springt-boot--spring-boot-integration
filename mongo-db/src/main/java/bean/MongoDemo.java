package bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author gaozijie
 * @since 2024-04-28
 */
@Data
@Document("demo")
public class MongoDemo {

    @Id
    private Integer demoId;

    private String demoName;

    private LocalDateTime createTime;
}
