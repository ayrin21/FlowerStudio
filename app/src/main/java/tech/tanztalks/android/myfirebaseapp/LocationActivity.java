package tech.tanztalks.android.myfirebaseapp;
//implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback  {

    boolean isPermissionGranted;
    GoogleMap mGoogleMap;
//    FloatingActionButton fab;
//    private FusedLocationProviderClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getSupportActionBar().setTitle("Location");

        // fab = findViewById(R.id.fab);


        checkMyPermission();
        if (isPermissionGranted){
          SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            supportMapFragment.getMapAsync(this);
      }


    }

//        initMap();
//
//        mLocationClient = new FusedLocationProviderClient(this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCurrentLoc();
//            }
//        });
//
//
//
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getCurrentLoc() {
//        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
//            if (task.isSuccessful()){
//                Location location= task.getResult();
//                gotoLocation(location.getLatitude(), location.getLongitude());
//            }
//        });
//    }
//
//    private void gotoLocation(double latitude, double longitude) {
//        LatLng LatLng = new LatLng(latitude, longitude);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng, 18);
//        mGoogleMap.moveCamera(cameraUpdate);
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//    }
//
//    private void initMap() {
//        if (isPermissionGranted){
//            SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
//            supportMapFragment.getMapAsync(this);
//        }
//    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(LocationActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
             permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mGoogleMap= googleMap;
//        mGoogleMap.setMyLocationEnabled(true);


    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
}