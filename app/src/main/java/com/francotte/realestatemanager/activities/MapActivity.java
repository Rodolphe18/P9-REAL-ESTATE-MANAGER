package com.francotte.realestatemanager.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.francotte.realestatemanager.repositories.LocationRepository;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.injection.Injection;
import com.francotte.realestatemanager.injection.ViewModelFactory;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.ui.RealEstateManagerViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Serializable {

    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
    private static final long HOUSE_ID = 1;
    private GoogleMap googleMap;
    private LocationRepository locationRepository;
    private FusedLocationProviderClient client;
    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                return;
            }
            for (android.location.Location location : locationResult.getLocations()) {
            }
        }
    };
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private List<House> houseList = new ArrayList<>();
    private LatLng houseLatLng;
    private LatLng currentPosition;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        //Initialize location client
        client = LocationServices.getFusedLocationProviderClient(this);

        this.configureViewModel();
        this.getAllHousesFromDatabase();


    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);

    }

    private void checkCondition() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }

    }

    private LocationRepository getCurrentLocation() {

        locationRepository = new LocationRepository(getApplicationContext());
        if (locationRepository.canGetLocation()) {
            client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(100000);
            locationRequest.setFastestInterval(2000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            checkSettingsAndStartLocationUpdates();

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12);
                       // googleMap.moveCamera(cameraUpdate);
                    }
                }
            };
        } else {
            locationRepository.showSettingsAlert();
        }
        return locationRepository;
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getApplicationContext());

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MapActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void setMarker() {
        for (House house : houseList) {
            String address = house.getAddress();
            Geocoder coder = new Geocoder(this);
            List<Address> addresses;
            try {
                addresses = coder.getFromLocationName(address, 10);
                if (addresses == null) {
                }
                Address location = addresses.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                houseLatLng = new LatLng(lat, lng);
                if (houseLatLng != null) {
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(houseLatLng));
                    marker.setTag(house.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (currentPosition != null) {
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("My position"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 12));
        } else if (houseLatLng != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(houseLatLng, 12));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        checkCondition();
        getAllHousesFromDatabase();
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Long id = (Long) marker.getTag();
        if (id != null) {
            House house = getHouseById(id);
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_HOUSE_CLICKED, house);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "This is my location", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void getAllHousesFromDatabase() {
        this.realEstateManagerViewModel.getAll().observe(this, this::updateList);
    }

    private void updateList(List<House> houses) {
        houseList = new ArrayList<>();
        houseList.addAll(houses);
        setMarker();
    }

    public House getHouseById(Long id) {
        for (House house : houseList) {
            if (house.getId() == id)
                return house;
        }
        return null;
    }
}