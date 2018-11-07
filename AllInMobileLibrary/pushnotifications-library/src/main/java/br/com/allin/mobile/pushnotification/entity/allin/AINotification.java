package br.com.allin.mobile.pushnotification.entity.allin;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Class color setting and notification of images
 */
public class AINotification {
    private @ColorRes int background;
    private @DrawableRes int whiteIcon;
    private @DrawableRes int icon;

    /**
     * Initialize the object with the background color of the notification icon (the lowest),
     * the largest image of the notification and the smaller image (white)
     *
     * @param background Color notification of image background
     * @param icon Client normal icon
     * @param whiteIcon Client white icon
     */
    public AINotification(@ColorRes int background, @DrawableRes int icon, @DrawableRes int whiteIcon) {
        this.background = background;
        this.icon = icon;
        this.whiteIcon = whiteIcon;
    }

    /**
     * @return Background color of the notification icon
     */
    public @ColorRes int getBackground() {
        return background;
    }

    /**
     * Set color notification of image background
     *
     * @param background Color notification of image background
     */
    public void setBackground(@ColorRes int background) {
        this.background = background;
    }

    /**
     * @return Client white icon
     */
    public @DrawableRes int getWhiteIcon() {
        return whiteIcon;
    }

    /**
     * Set client white icon
     *
     * @param whiteIcon Client white icon
     */
    public void setWhiteIcon(@DrawableRes int whiteIcon) {
        this.whiteIcon = whiteIcon;
    }

    /**
     * @return Client normal icon
     */
    public @DrawableRes int getIcon() {
        return icon;
    }

    /**
     * Set client normal icon
     *
     * @param icon Client normal icon
     */
    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }
}
