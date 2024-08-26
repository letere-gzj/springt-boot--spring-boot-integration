import com.xkcoding.http.config.HttpConfig;
import constant.ThirdLoginTypeEnum;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.AuthFacebookScope;
import me.zhyd.oauth.enums.scope.AuthGoogleScope;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthFacebookRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import properties.HttpProxyProperties;
import properties.JustAuthProperties;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * JustAuth工具
 * @author gaozijie
 * @since 2024-07-01
 */
@Component
public class JustAuthClient {

    @Autowired
    private HttpProxyProperties httpProxyProperties;

    @Autowired
    private JustAuthProperties justAuthProperties;

    /**
     * 获取第三方登录地址
     * @param thirdLoginType 第三方登录类型
     * @param callbackUrl 回调地址
     * @return 第三方登录地址
     */
    public String getThirdLoginUrl(ThirdLoginTypeEnum thirdLoginType, String callbackUrl) {
        AuthRequest authRequest = this.getAuthRequest(thirdLoginType, callbackUrl);
        return authRequest.authorize(AuthStateUtils.createState());
    }

    /**
     * 解析用户信息
     * @param thirdLoginType 第三方登录类型
     * @param callbackUrl 回调地址
     * @param authCallback 授权回调数据
     * @return 用户信息
     */
    @SuppressWarnings({"unchecked"})
    public AuthUser getAuthUser(ThirdLoginTypeEnum thirdLoginType, String callbackUrl, AuthCallback authCallback) {
        AuthRequest authRequest = this.getAuthRequest(thirdLoginType, callbackUrl);
        AuthResponse<AuthUser> response = authRequest.login(authCallback);
        return response.getData();
    }

    /**
     * 获取授权请求
     * @param thirdLoginType 第三方登录类型
     * @param callbackUrl 回调地址
     * @return 授权请求
     */
    private AuthRequest getAuthRequest(ThirdLoginTypeEnum thirdLoginType, String callbackUrl) {
        switch (thirdLoginType) {
            case GOOGLE -> {
                JustAuthProperties.ClientConfig config = justAuthProperties.getGoogle();
                return new AuthGoogleRequest(AuthConfig.builder()
                        .clientId(config.getClientId())
                        .clientSecret(config.getClientSecret())
                        .redirectUri(callbackUrl)
                        .scopes(AuthScopeUtils.getScopes(AuthGoogleScope.USER_EMAIL, AuthGoogleScope.USER_PROFILE))
                        .httpConfig(this.getHttpConfig())
                        .ignoreCheckState(true)
                        .ignoreCheckRedirectUri(true)
                        .build());
            }
            case FACEBOOK -> {
                JustAuthProperties.ClientConfig config = justAuthProperties.getFacebook();
                return new AuthFacebookRequest(AuthConfig.builder()
                        .clientId(config.getClientId())
                        .clientSecret(config.getClientSecret())
                        .redirectUri(callbackUrl)
                        .scopes(AuthScopeUtils.getScopes(AuthFacebookScope.PUBLIC_PROFILE, AuthFacebookScope.EMAIL))
                        .httpConfig(this.getHttpConfig())
                        .ignoreCheckRedirectUri(true)
                        .build());
            }
            default -> {
                throw new RuntimeException("暂未实现此第三方登录");
            }
        }
    }

    /**
     * 获取http代理配置(针对国外平台登录)
     * @return http代理配置
     */
    private HttpConfig getHttpConfig() {
        return HttpConfig.builder()
                .timeout(15000)
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(httpProxyProperties.getHost(), httpProxyProperties.getPort())
                        )
                ).build();
    }
}
