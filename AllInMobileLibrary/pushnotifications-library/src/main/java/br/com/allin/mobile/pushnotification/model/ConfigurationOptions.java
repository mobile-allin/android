package br.com.allin.mobile.pushnotification.model;

import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;

/**
 * Model configuration options.
 */
public class ConfigurationOptions {
    private String senderId = null;
    private NotificationSettings notificationSettings = null;

    /**
     * <p>
     * default constructor of {@link ConfigurationOptions} class.
     *
     * <p>
     * To use the library, you must complete the basic information for use in the project from the methods {@link #setSenderId (String)}.
     */
    public ConfigurationOptions() {
    }

    public ConfigurationOptions(String senderId) throws NotNullAttributeOrPropertyException {
        setSenderId(senderId);
    }

    /**
     * <p>
     * Builder standard class {@link ConfigurationOptions} with all the necessary settings for the function library.
     * @param senderId It is the id of the notification delivery project (project number) in
     * <a href="https://console.developers.google.com">Google Developers Console</a>.
     *
     * @param notificationSettings Are the notifications settings that are displayed as color and icon
     */
    public ConfigurationOptions(String senderId, NotificationSettings notificationSettings)
            throws NotNullAttributeOrPropertyException {
        setSenderId(senderId);
        setNotificationSettings(notificationSettings);
    }

    /**
     * Returns the id of the notification delivery project (project number) in
     * <a href="https://console.developers.google.com">Google Developers Console</a>.
     *
     * @return Id notification delivery project (project number).
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Set the id of the notification delivery project (project number) in
     * <a href="https://console.developers.google.com">Google Developers Console</a>.
     *
     * <b>Required property can not be {@code null}</b>.
     *
     * @param senderId Id notification delivery project (project number).
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * @return Notifications settings.
     */
    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    /**
     * Configure notifications to be displayed (color and symbol)
     *
     * @param notificationSettings Settings that will be added to display the notification.
     */
    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }
}
