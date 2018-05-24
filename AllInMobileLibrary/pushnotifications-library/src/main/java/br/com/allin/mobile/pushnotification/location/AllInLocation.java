package br.com.allin.mobile.pushnotification.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.interfaces.OnAllInLocationChange;

/**
 * Class that initializes the geolocation
 */
public class AllInLocation implements GoogleApiClient.ConnectionCallbacks,
                                        GoogleApiClient.OnConnectionFailedListener {
    private static AllInLocation allInLocation;

    /**
     * Method that initializes the geolocation
     *
     * @param onAllInLocationChange Interface that returns the geolocation case is found or not
     */
    public static void initialize(OnAllInLocationChange onAllInLocationChange) {
        if (allInLocation == null) {
            allInLocation = new AllInLocation();
        }

        allInLocation.onAllInLocationChange = onAllInLocationChange;
        allInLocation.init();
    }

    private AllInLocation() {
    }

    private GoogleApiClient googleApiClient;
    private OnAllInLocationChange onAllInLocationChange;

    private void init() {
        googleApiClient = new GoogleApiClient.Builder(AlliNPush.getInstance().getContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location != null) {
                onAllInLocationChange.locationFound(location.getLatitude(), location.getLongitude());
            } else {
                onAllInLocationChange.locationNotFound();
            }
        } catch (Exception e) {
            onAllInLocationChange.locationNotFound();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        onAllInLocationChange.locationNotFound();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        onAllInLocationChange.locationNotFound();
    }
}
