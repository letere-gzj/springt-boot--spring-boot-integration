package bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author gaozijie
 * @since 2024-02-21
 */
@Document(indexName = "demo")
@Data
public class EsDemo {

    @Id
    private Integer demoId;

    private String demoName;

    private LocalDateTime createTime;

}
