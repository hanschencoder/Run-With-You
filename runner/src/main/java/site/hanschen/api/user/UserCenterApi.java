package site.hanschen.api.user;

/**
 * @author HansChen
 */
public interface UserCenterApi {

    /**
     * 登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    LoginReply login(final String username, final String password);

    /**
     * 请求注册验证码
     *
     * @param email 接收验证码的邮箱地址
     * @return 验证码
     */
    VerificationReply requestVerificationCode(final String email);

    /**
     * 注册接口
     *
     * @param email    注册邮箱
     * @param password 密码
     * @return 注册结果
     */
    RegisterReply register(final String email, final String verificationCode, final String password);

    /**
     * 请求修改密码的权限
     *
     * @param username 用户名
     * @param password 密码
     * @return 权限码
     */
    AuthorizationReply requestAuthorization(final String username, final String password);

    /**
     * 修改密码
     *
     * @param token         用户标识
     * @param newPassword   新密码
     * @param authorization 权限码
     * @return 修改结果
     */
    NewPasswordReply changePassword(final String token, final String newPassword, final String authorization);

    /**
     * 获取用户信息
     *
     * @param token 用户标识
     * @return 用户信息
     */
    UserInfo requestUserInfo(final String token);

    /**
     * 更新用户信息
     *
     * @param token    用户标识
     * @param userInfo 新的用户信息
     * @return 操作结果
     */
    ResultReply updateUserInfo(final String token, final UserInfo userInfo);
}
