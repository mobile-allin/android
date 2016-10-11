package br.com.allin.mobile.pushnotification.entity;

import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;

/**
 * Entity configuration options.
 */
public class ConfigurationEntity {
    private String senderId = null;
    private NotificationEntity notificationEntity = null;

    /**
     * <p>
     * default constructor of {@link ConfigurationEntity} class.
     *
     * <p>
     * To use the library, you must complete the basic information for use
     * in the project from the methods {@link #setSenderId (String)}.
     */
    public ConfigurationEntity() {
    }

    public ConfigurationEntity(String senderId) throws NotNullAttributeOrPropertyException {
        setSenderId(senderId);
    }

    /**
     * <p>
     * Builder standard class {@link ConfigurationEntity}
     * with all the necessary settings for the function library.
     * @param senderId It is the id of the notification delivery project (project number) in
     * <a href="https://console.developers.google.com">Google Developers Console</a>.
     *
     * @param notificationEntity Are the
     *                             notifications settings that are displayed as color and icon
     */
    public ConfigurationEntity(String senderId, NotificationEntity notificationEntity)
            throws NotNullAttributeOrPropertyException {
        setSenderId(senderId);
        setNotificationEntity(notificationEntity);
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
    public NotificationEntity getNotificationEntity() {
        return notificationEntity;
    }

    /**
     * Configure notifications to be displayed (color and symbol)
     *
     * @param notificationEntity Settings that will be added to display the notification.
     */
    public void setNotificationEntity(NotificationEntity notificationEntity) {
        this.notificationEntity = notificationEntity;
    }
}
