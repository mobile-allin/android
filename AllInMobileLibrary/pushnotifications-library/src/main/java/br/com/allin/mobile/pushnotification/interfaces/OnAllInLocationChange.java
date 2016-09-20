package br.com.allin.mobile.pushnotification.interfaces;

/**
 * Request the return interface
 */
public interface OnAllInLocationChange {
    /**
     * If the location is found
     *
     * @param latitude user location latitude
     * @param longitude user location longitude
     */
    void locationFound(double latitude, double longitude);

    /**
     * If the location is not found
     */
    void locationNotFound();
}
