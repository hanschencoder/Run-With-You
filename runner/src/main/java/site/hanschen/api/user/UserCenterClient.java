package site.hanschen.api.user;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

/**
 * @author HansChen
 */
public class UserCenterClient {

    private final ManagedChannel                        channel;
    private final UserCenterGrpc.UserCenterBlockingStub blockingStub;
    private final UserCenterGrpc.UserCenterStub         asyncStub;


    /**
     * Construct client for accessing UserCenter server at {@code host:port}.
     */
    public UserCenterClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
    }

    /**
     * Construct client for accessing UserCenter server using the existing channel.
     */
    public UserCenterClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = UserCenterGrpc.newBlockingStub(channel);
        asyncStub = UserCenterGrpc.newStub(channel);
    }

    /**
     * Blocking unary call example.  Calls getFeature and prints the response.
     */
    public void login() {

        LoginInfo request = LoginInfo.newBuilder().setUsername("hanschen").setPasswordMd5("123456789").build();

        LoginReply feature;
        try {
            System.out.println("Sent request to server:");
            System.out.println(request.toString());
            feature = blockingStub.login(request);
            System.out.println("Received response from server:");
            System.out.println(feature.toString());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: {0}" + e.getStatus());
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {

        UserCenterClient client = new UserCenterClient("localhost", 8980);
        client.login();
        client.shutdown();
    }
}
