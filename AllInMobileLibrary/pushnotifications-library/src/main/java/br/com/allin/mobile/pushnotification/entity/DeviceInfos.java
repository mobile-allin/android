package br.com.allin.mobile.pushnotification.entity;

/**
 * Object that returns server information
 */
public class DeviceInfos {
    private String deviceId;
    private boolean renewId;

    /**
     * Starts the object by setting the device Token
     *
     * @param deviceId Device Token
     * @param renewId Flag device that checks whether the Token has changed or not
     */
    public DeviceInfos(String deviceId, boolean renewId) {
        this.deviceId = deviceId;
        this.renewId = renewId;
    }

    /**
     * @return Token device sitting on the object
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @return Flag that verifies that the token has been changed or not
     */
    public boolean isRenewId() {
        return renewId;
    }
}
