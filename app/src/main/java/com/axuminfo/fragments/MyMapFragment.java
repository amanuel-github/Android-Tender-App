package com.axuminfo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.thecodecity.mapsdirection.directionhelpers.FetchURL;
import com.thecodecity.mapsdirection.directionhelpers.TaskLoadedCallback;


public class MyMapFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    public static LatLng latLngPlace1, latLngPlace2;
    public static String directionMode = /*"walking";*/"driving";

    Button getDirection;
    TextView latLngTextView;
    private Polyline currentPolyline;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    LatLng latLng=null;
    Fragment fragmentContext;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view=inflater.inflate(R.layout.fragment_map, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }
        fragmentContext=this;
        latLngTextView = view.findViewById(R.id.latLngTextView);
        //Toast.makeText(getContext(), Index_officeFragment.selectedItem.getName(), Toast.LENGTH_LONG).show();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.appContext);
        getLastLocation();

        if(latLngPlace1==null)
            latLngPlace1 = new LatLng(13.5101933, 39.456705);
        if(latLngPlace2==null)
            latLngPlace2 = new LatLng(13.5201933, 39.466705);

        place1 = new MarkerOptions().position(latLngPlace1).title(getString(R.string.your_location));
        place2 = new MarkerOptions().position(latLngPlace2).title(getString(R.string.office_location));

        getDirection = view.findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(fragmentContext).execute(getUrl(place1.getPosition(), place2.getPosition(), directionMode), directionMode);
            }
        });

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.mapArea);
        mapFragment.getMapAsync(this);

        Button backToDetailBtn=view.findViewById(R.id.backToDetailBtn);
        backToDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.to_index)
                    MainActivity.mainActivity.replaceFragment(new Index_officeFragment());
                if(!MainActivity.to_index)
                    MainActivity.mainActivity.replaceFragment(new Search_officeFragment());
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        //mMap.addMarker(place1);//add after location is found
        mMap.addMarker(place2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngPlace2, 18F));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18F));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F));
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        //mMap.moveCamera( CameraUpdateFactory.zoomTo( 17F ) );

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"  + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.appContext);
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                //if (location == null) {
                                    requestNewLocationData();
                                /*} else {
                                    onResultFound(location);
                                }*/
                            }
                        }
                );
            } else {
                Toast.makeText(MainActivity.appContext, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.appContext.startActivity(intent);
            }
        } else {
            requestPermissions();
        }

    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.appContext);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

        Toast.makeText(MainActivity.appContext, "Requesting New Location", Toast.LENGTH_LONG).show();

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();

            onResultFound(location);
            Toast.makeText(MainActivity.appContext, "location found", Toast.LENGTH_LONG).show();

            if(latLngTextView!=null)
                latLngTextView.setText(location.getLatitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                MainActivity.mainActivity,//getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) MainActivity.appContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    private void onResultFound(Location location){
        latLng=new LatLng(location.getLatitude(), location.getLongitude());
        if(latLngTextView!=null)
            latLngTextView.setText("Latitude: "+location.getLatitude()+"\nLongitude: "+location.getLongitude());
        if (latLng != null) {
            if(mMap!=null) {
                mMap.addMarker(place1);
            }
            if(Create_officeFragment.latitudeTf!=null && Create_officeFragment.longitudeTf!=null){
                Create_officeFragment.latitudeTf.setText(String.valueOf(latLng.latitude));
                Create_officeFragment.longitudeTf.setText(String.valueOf(latLng.longitude));
            }
            if(Edit_officeFragment.latitudeTf!=null && Edit_officeFragment.longitudeTf!=null){
                Edit_officeFragment.latitudeTf.setText(String.valueOf(latLng.latitude));
                Edit_officeFragment.longitudeTf.setText(String.valueOf(latLng.longitude));
            }
        }
    }

}
