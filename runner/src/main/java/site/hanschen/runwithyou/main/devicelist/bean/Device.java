package site.hanschen.runwithyou.main.devicelist.bean;

/**
 * @author HansChen
 */
public class Device {

    private String mName;
    private String mAddress;


    public Device(String name, String address) {
        this.mName = name;
        this.mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public void setName(String deviceName) {
        this.mName = deviceName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }
}
