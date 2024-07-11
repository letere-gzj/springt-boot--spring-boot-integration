package constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gaozijie
 * @since 2024-07-11
 */
@Getter
@AllArgsConstructor
public enum ThirdLoginTypeEnum {
    /**
     * 谷歌
     */
    GOOGLE(),
    /**
     * 脸书
     */
    FACEBOOK();
}
