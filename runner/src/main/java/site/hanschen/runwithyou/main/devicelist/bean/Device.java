package site.hanschen.runwithyou.main.devicelist.bean;

import java.io.Serializable;

/**
 * @author HansChen
 */
public class Device implements Serializable {

    private static final long serialVersionUID = -3779363556041275676L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Device device = (Device) o;

        return mAddress != null ? mAddress.equals(device.mAddress) : device.mAddress == null;

    }

    @Override
    public int hashCode() {
        return mAddress != null ? mAddress.hashCode() : 0;
    }
}
