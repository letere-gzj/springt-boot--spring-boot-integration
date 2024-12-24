package bean;

import lombok.Data;

/**
 * 达达配送响应类
 * @author gaozijie
 * @since 2024-11-27
 */
@Data
public class DadaResponse<T> {

    /**
     * 	响应状态，成功为"success"，失败为"fail"
     */
    private String status;

    /**
     * 响应返回码
     * @see "https://newopen.imdada.cn/#/development/file/code"
     */
    private Integer code;

    /**
     * 响应描述
     */
    private String msg;

    /**
     * 响应结果
     */
    private T result;

    /**
     * 错误编码，与code一致
     * @see "https://newopen.imdada.cn/#/development/file/code"
     */
    private Integer errorCode;
}
