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
    LoginReply login(String username, String password);

    /**
     * 注册接口
     *
     * @param username 用户名
     * @param password 密码
     * @param phone    手机号
     * @return 注册结果
     */
    RegisterReply register(String username, String password, String phone);

    /**
     * 请求修改密码的权限
     *
     * @param username 用户名
     * @param password 密码
     * @return 权限码
     */
    AuthorizationReply requestAuthorization(String username, String password);

    /**
     * 修改密码
     *
     * @param token         用户标识
     * @param newPassword   新密码
     * @param authorization 权限码
     * @return 修改结果
     */
    NewPasswordReply changePassword(String token, String newPassword, String authorization);

    /**
     * 获取用户信息
     *
     * @param token 用户标识
     * @return 用户信息
     */
    UserInfo requestUserInfo(String token);

    /**
     * 更新用户信息
     *
     * @param token    用户标识
     * @param userInfo 新的用户信息
     * @return 操作结果
     */
    ResultReply updateUserInfo(String token, UserInfo userInfo);
}
