package br.com.allin.mobile.pushnotification.entity.allin;

/**
 * Entity configuration options.
 */
public class AIConfiguration {
    private String senderId = null;
    private AINotification notification = null;

    /**
     * <p>
     * default constructor of {@link AIConfiguration} class.
     *
     * <p>
     * To use the library, you must complete the basic information for use
     * in the project from the methods {@link #setSenderId (String)}.
     */
    public AIConfiguration() {
    }

    public AIConfiguration(String senderId) {
        this.senderId = senderId;
    }

    /**
     * <p>
     * Builder standard class {@link AIConfiguration}
     * with all the necessary settings for the function library.
     * @param senderId It is the id of the notification delivery project (project number) in
     * <a href="https://console.developers.google.com">Google Developers Console</a>.
     *
     * @param notification Are the
     *                             notifications settings that are displayed as color and icon
     */
    public AIConfiguration(String senderId, AINotification notification) {
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
    public AINotification getNotification() {
        return notification;
    }

    /**
     * Configure notifications to be displayed (color and symbol)
     *
     * @param AINotification Settings that will be added to display the notification.
     */
    public void setNotification(AINotification AINotification) {
        this.notification = AINotification;
    }
}
