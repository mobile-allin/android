package br.com.allin.mobile.pushnotification.entity;

/**
 * Entity configuration options.
 */
public class ConfigurationEntity {
    private String senderId = null;
    private NotificationEntity notification = null;

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

    public ConfigurationEntity(String senderId) {
        this.senderId = senderId;
    }

    /**
     * <p>
     * Builder standard class {@link ConfigurationEntity}
     * with all the necessary settings for the function library.
     * @param senderId It is the id of the notification delivery project (project number) in
     * <a href="https://console.developers.google.com">Google Developers Console</a>.
     *
     * @param notification Are the
     *                             notifications settings that are displayed as color and icon
     */
    public ConfigurationEntity(String senderId, NotificationEntity notification) {
        this.senderId = senderId;
        this.notification = notification;
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
    public NotificationEntity getNotification() {
        return notification;
    }

    /**
     * Configure notifications to be displayed (color and symbol)
     *
     * @param notificationEntity Settings that will be added to display the notification.
     */
    public void setNotification(NotificationEntity notificationEntity) {
        this.notification = notificationEntity;
    }
}
