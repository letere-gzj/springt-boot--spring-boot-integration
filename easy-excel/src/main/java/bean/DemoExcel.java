package bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author gaozijie
 * @since 2024-05-06
 */
@Data
public class DemoExcel {

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("日期")
    private LocalDateTime date;

    @ExcelProperty("分数")
    private Double score;
}
