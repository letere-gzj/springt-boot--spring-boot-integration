package entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author letere
 * @since 2023-08-04
 */
@Data
public class Demo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 关键词
     */
    private String key;
    /**
     * 值
     */
    private String value;
}
