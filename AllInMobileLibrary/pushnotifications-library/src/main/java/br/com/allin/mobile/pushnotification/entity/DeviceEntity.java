package br.com.allin.mobile.pushnotification.entity;

/**
 * Object with the device information
 */
public class DeviceEntity {
    private String deviceId;
    private boolean renewId;

    /**
     * Starts the object by setting the device Token
     *
     * @param deviceId Device Token
     * @param renewId Flag device that checks whether the Token has changed or not
     */
    public DeviceEntity(String deviceId, boolean renewId) {
        this.deviceId = deviceId;
        this.renewId = renewId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return Token device sitting on the object
     */
    public String getDeviceId() {
        return deviceId;
    }

    public void setRenewId(boolean renewId) {
        this.renewId = renewId;
    }

    /**
     * @return Flag that verifies that the token has been changed or not
     */
    public boolean isRenewId() {
        return renewId;
    }
}
