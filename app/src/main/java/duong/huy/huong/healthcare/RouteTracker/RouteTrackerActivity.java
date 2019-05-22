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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import duong.huy.huong.healthcare.R;
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
    private boolean foundLocation = false;
    double distance;
    private boolean onGoing = false;
    private ListView roadHis = null;

    /**
     * Hàm khởi tạo. init các giá trị.
     * @param savedInstanceState
     */
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

        // init lich su
        roadHis = (ListView) findViewById(R.id.roadHistory);
        loadHistory();
        roadHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ArrayList<Walking> walkings = WalkingDao.loadAllRecords();
                gpsTrack = StringToGpsTrack(walkings.get(walkings.size()-(int)id-1).getroad());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(gpsTrack.getPoints().get(0), 19));
                Log.d("id", String.valueOf(walkings.size()-(int)id-1));
            }
        });

        // disable button khi chua tim thay vi tri
        if(!foundLocation) {
            Button start = (Button) findViewById(R.id.start_btn);
            Button end = (Button) findViewById(R.id.end_button);
            start.setEnabled(false);
            end.setEnabled(false);
        }
    }

    /**
     * Hàm load các chuyến đi trong lịch sử.
     */
    public void loadHistory() {
        ArrayList<Walking> mWalkings = WalkingDao.loadAllRecords();
        while(mWalkings.size() > 20 ) {
            WalkingDao.deleteRecord(mWalkings.get(0));
            mWalkings.remove(0);
        }
        Collections.reverse(mWalkings);
        ArrayAdapter<Walking> arrayAdapter
                = new ArrayAdapter<Walking>(this, android.R.layout.simple_list_item_1 , (Walking[]) mWalkings.toArray(new Walking[mWalkings.size()])){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };
        roadHis.setAdapter(arrayAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng defaultCamera = new LatLng(21.028511, 105.804817);
        map.moveCamera(CameraUpdateFactory.newLatLng(defaultCamera));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCamera, 19));
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(20);
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

    /**
     * Hàm update quãng đường khi vị trí thay đổi.
     * @param location
     */
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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng, 19));
            firstLat = false;
            mTextView.setText("đã tìm thấy vị trí hiện tại");
            Button start = (Button) findViewById(R.id.start_btn);
            start.setEnabled(true);
        } else {
            distance += tmp.distanceTo(location);
            mTextView.setText(String.valueOf(Math.round(distance*10)/10) + " m");
        }

        updateTrack();
    }

    /**
     * Cấu hình gửi yêu cầu update vị trí.
     */
    @SuppressWarnings("deprecation")
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

    /**
     * tạm dừng update vị trí.
     */
    @SuppressWarnings("deprecation")
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    /**
     * updat đường đi.
     */
    private void updateTrack() {
        if(!onGoing) return;
        List<LatLng> points = gpsTrack.getPoints();
        points.add(lastKnownLatLng);
        gpsTrack.setPoints(points);
    }

    /**
     * Hàm oncick của nút bắt đầu hành trình.
     * @param v
     */
    public void onStartBtn(View v) {
        if(!onGoing) onGoing = true;
        startTime = System.currentTimeMillis();
        Button start = (Button) findViewById(R.id.start_btn);
        Button end = (Button) findViewById(R.id.end_button);
        start.setEnabled(false);
        end.setEnabled(true);
        List<LatLng> points = gpsTrack.getPoints();
        points.clear();
        gpsTrack.setPoints(points);
    }

    /**
     * convert tọa độ đường đi gps sang string để lưu vào csdl.
     * @param gpsTrack
     * @return
     */
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

    /**
     * đọc string trong cơ sở dữ liệu và convert sang đường đi.
     * @param s
     * @return
     */
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

    /**
     * Hàm onclick của nút kết thúc hành trình.
     * @param v
     */
    public void onStopBtn(View v) {
        onGoing = false;
        long finishTime = System.currentTimeMillis();
        Walking mWalking = new Walking();
        mWalking.setdistance(String.valueOf(distance));
        mWalking.setroad(String.valueOf(gpsTrackToString(gpsTrack)));
        mWalking.settime_begin(String.valueOf(startTime));
        mWalking.settime_end(String.valueOf(finishTime));
        WalkingDao.insertRecord(mWalking);
        Button start = (Button) findViewById(R.id.start_btn);
        Button end = (Button) findViewById(R.id.end_button);
        start.setEnabled(true);
        end.setEnabled(false);
        loadHistory();
    }
}