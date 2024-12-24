package constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 达达响应码
 * @see "https://newopen.imdada.cn/#/development/file/code"
 * @author gaozijie
 * @since 2024-12-10
 */
@Getter
@AllArgsConstructor
public enum DadaRespCodeEnum {

    /**
     * app_key无效
     */
    APP_KEY_ERROR(2001),

    /**
     * 没有绑定正式商户,请检查接口中source_id值
     */
    SOURCE_ID_ERROR(2002),

    /**
     * 门店不存在
     */
    STATION_NOT_EXIST(2402),

    /**
     * 达达商家app账号已存在,请通过绑定处理
     */
    APP_ACCOUNT_EXIST(8808),

    /**
     * 默认枚举类（用于避免switch空指针异常）
     */
    DEFAULT_ENUM(-1);

    /**
     * 状态码
     */
    private final Integer code;

    public static DadaRespCodeEnum getInstance(Integer code) {
        for (DadaRespCodeEnum respCodeEnum : DadaRespCodeEnum.values()) {
            if (Objects.equals(respCodeEnum.getCode(), code)) {
                return respCodeEnum;
            }
        }
        return DEFAULT_ENUM;
    }
}
