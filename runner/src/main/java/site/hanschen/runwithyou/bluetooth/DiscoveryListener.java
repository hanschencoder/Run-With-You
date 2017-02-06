package site.hanschen.runwithyou.bluetooth;

/**
 * @author HansChen
 */
public interface DiscoveryListener {

    void onDiscoveryStart();

    void onDeviceFound(String name, String address);

    void onDiscoveryFinish();

}
