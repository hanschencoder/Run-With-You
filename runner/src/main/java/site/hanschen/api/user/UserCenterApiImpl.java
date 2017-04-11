package site.hanschen.api.user;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import site.hanschen.runwithyou.utils.MD5Utils;

/**
 * @author HansChen
 */
public class UserCenterApiImpl implements UserCenterApi {


    private final ManagedChannel                        channel;
    private final UserCenterGrpc.UserCenterBlockingStub blockingStub;
    private final UserCenterGrpc.UserCenterStub         asyncStub;


    /**
     * Construct client for accessing UserCenter server at {@code host:port}.
     */
    public UserCenterApiImpl(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
    }

    /**
     * Construct client for accessing UserCenter server using the existing channel.
     */
    public UserCenterApiImpl(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = UserCenterGrpc.newBlockingStub(channel);
        asyncStub = UserCenterGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Override
    public LoginReply login(String username, String password) {
        LoginInfo info = LoginInfo.newBuilder().setUsername(username).setPasswordMd5(MD5Utils.getMD5(password)).build();
        return blockingStub.login(info);
    }

    @Override
    public RegisterReply register(String username, String password, String phone) {
        RegisterInfo info = RegisterInfo.newBuilder()
                                        .setUsername(username)
                                        .setPasswordMd5(MD5Utils.getMD5(password))
                                        .setPhone(phone)
                                        .build();
        return blockingStub.register(info);
    }

    @Override
    public AuthorizationReply requestAuthorization(String username, String password) {
        AuthorizationRequest info = AuthorizationRequest.newBuilder()
                                                        .setUsername(username)
                                                        .setPasswordMd5(MD5Utils.getMD5(password))
                                                        .build();
        return blockingStub.requestAuthorization(info);
    }

    @Override
    public NewPasswordReply changePassword(String token, String newPassword, String authorization) {
        NewPassword password = NewPassword.newBuilder()
                                          .setToken(token)
                                          .setNewPassword(MD5Utils.getMD5(newPassword))
                                          .setAuthorization(authorization)
                                          .build();
        return blockingStub.changePassword(password);
    }

    @Override
    public UserInfo requestUserInfo(String token) {
        OperateToken operateToken = OperateToken.newBuilder().setToken(token).build();
        return blockingStub.requestUserInfo(operateToken);
    }

    @Override
    public ResultReply updateUserInfo(String token, UserInfo userInfo) {
        UpdateRequest request = UpdateRequest.newBuilder().setToken(token).setUserInfo(userInfo).build();
        return blockingStub.updateUserInfo(request);
    }
}
