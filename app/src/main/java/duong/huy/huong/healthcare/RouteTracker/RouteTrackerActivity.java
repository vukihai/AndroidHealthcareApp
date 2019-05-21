package duong.huy.huong.healthcare.RouteTracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.Heart_Rate;
import duong.huy.huong.healthcare.db.Heart_RateDao;
import duong.huy.huong.healthcare.db.Walking;
import duong.huy.huong.healthcare.db.WalkingDao;

public class RouteTrackerActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private GoogleMap map;
    private Polyline gpsTrack;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private LatLng lastKnownLatLng;
    private boolean firstLat;
    private TextView mTextView;
    private long startTime;
    private long finishTime;
    double distance;
    private boolean onGoing = false;
    private ListView roadHis = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_tracker);
        firstLat = true;
        distance = 0;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        roadHis = (ListView) findViewById(R.id.roadHistory);
        loadHistory();
        roadHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                gpsTrack = StringToGpsTrack(WalkingDao.loadAllRecords().get((int)id).getroad());

            }
        });
    }
    public void loadHistory() {
        ArrayList<Walking> mWalkings = WalkingDao.loadAllRecords();
        while(mWalkings.size() > 20 ) {
            WalkingDao.deleteRecord(mWalkings.get(0));
            mWalkings.remove(0);
        }
        Collections.reverse(mWalkings);
        ArrayAdapter<Walking> arrayAdapter
                = new ArrayAdapter<Walking>(this, android.R.layout.simple_list_item_1 , (Walking[]) mWalkings.toArray(new Walking[mWalkings.size()]));
        roadHis.setAdapter(arrayAdapter);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng calymayor = new LatLng(19.345822, -99.152274);
        map.moveCamera(CameraUpdateFactory.newLatLng(calymayor));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(calymayor, 15));
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(15);
        gpsTrack = map.addPolyline(polylineOptions);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        mTextView = findViewById(R.id.textView);
    }


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Location tmp = new Location("");
        if(!firstLat) {
            if(!onGoing) return;
            tmp.setLongitude(lastKnownLatLng.longitude);
            tmp.setLatitude(lastKnownLatLng.latitude);
        }
        lastKnownLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(firstLat) {
            map.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLatLng));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng, 15));
            firstLat = false;
            mTextView.setText("đã tìm thấy vị trí hiện tại");
        } else {
            distance += tmp.distanceTo(location);
            mTextView.setText(String.valueOf(distance) + " m");
        }

        updateTrack();
    }

    protected void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    private void updateTrack() {
        if(!onGoing) return;
        List<LatLng> points = gpsTrack.getPoints();
        points.add(lastKnownLatLng);
        gpsTrack.setPoints(points);
    }
    public void onStartBtn(View v) {
        if(!onGoing) onGoing = true;
        startTime = System.currentTimeMillis();
    }
    private String gpsTrackToString(Polyline gpsTrack) {
        String ret = "";
        List<LatLng> points = gpsTrack.getPoints();
        for(int i=0; i<points.size(); i++) {
            ret += String.valueOf(points.get(i).latitude);
            ret += " ";
            ret += String.valueOf(points.get(i).longitude);
            ret += " ";
        }
        return ret;
    }
    private Polyline StringToGpsTrack(String s) {
        List<LatLng> points = gpsTrack.getPoints();
        points.clear();
        double lat, log;
        String[] tmp = s.split(" ");
        for(int i=0; i<tmp.length; i+=2) {
            lat = Double.valueOf(tmp[i]);
            log = Double.valueOf(tmp[i+1]);
            points.add(new LatLng(lat, log));
        }
        gpsTrack.setPoints(points);
        return gpsTrack;
    }

    public void onStopBtn(View v) throws IOException, ClassNotFoundException {
        onGoing = false;
        Walking mWalking = new Walking();
        mWalking.setdistance(String.valueOf(distance));

        List<LatLng> points = gpsTrack.getPoints();

        mWalking.setroad(String.valueOf(gpsTrackToString(gpsTrack)));
        mWalking.settime_begin(String.valueOf(startTime));
        mWalking.settime_end(String.valueOf(finishTime));
        WalkingDao.insertRecord(mWalking);

        gpsTrack = StringToGpsTrack(WalkingDao.loadAllRecords().get(0).getroad());
    }

}