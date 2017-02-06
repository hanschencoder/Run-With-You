package site.hanschen.runwithyou.bluetooth;

/**
 * @author HansChen
 */
public interface ConnectListener {

    void onConnectStart();

    void onConnectSucceed();

    void onConnectFailed();

    void onConnectLost();
}
