package br.com.allin.mobile.pushnotification.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

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
        Context context = AlliNPush.getInstance().getContext();

        String strCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
        String strFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;

        int coarseLocation = ContextCompat.checkSelfPermission(context, strCoarseLocation);
        int fineLocation = ContextCompat.checkSelfPermission(context, strFineLocation);

        if (coarseLocation == PackageManager.PERMISSION_GRANTED || fineLocation == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location != null) {
                onAllInLocationChange.locationFound(location.getLatitude(), location.getLongitude());
            } else {
                onAllInLocationChange.locationNotFound();
            }
        } else {
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
