package br.com.allin.mobile.pushnotification.entity;

/**
 * Class color setting and notification of images
 */
public class NotificationEntity {
    private String colorBackground;
    private int whiteIcon;
    private int icon;

    /**
     * Initialize the object with the background color and the only icon.
     * This will form the simplest and unique symbol (standard WhatsApp)
     *
     * @param colorBackground Color notification of image background
     * @param whiteIcon Client white icon
     */
    public NotificationEntity(String colorBackground, int whiteIcon) {
        this.colorBackground = colorBackground;
        this.whiteIcon = whiteIcon;
    }

    /**
     * Initialize the object with the background color of the notification icon (the lowest),
     * the largest image of the notification and the smaller image (white)
     *
     * @param colorBackground Color notification of image background
     * @param icon Client normal icon
     * @param whiteIcon Client white icon
     */
    public NotificationEntity(String colorBackground, int icon, int whiteIcon) {
        this.colorBackground = colorBackground;
        this.icon = icon;
        this.whiteIcon = whiteIcon;
    }

    /**
     * @return Background color of the notification icon
     */
    public String getColorBackground() {
        return colorBackground;
    }

    /**
     * Set color notification of image background
     *
     * @param colorBackground Color notification of image background
     */
    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    /**
     * @return Client white icon
     */
    public int getWhiteIcon() {
        return whiteIcon;
    }

    /**
     * Set client white icon
     *
     * @param whiteIcon Client white icon
     */
    public void setWhiteIcon(int whiteIcon) {
        this.whiteIcon = whiteIcon;
    }

    /**
     * @return Client normal icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Set client normal icon
     *
     * @param icon Client normal icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }
}
