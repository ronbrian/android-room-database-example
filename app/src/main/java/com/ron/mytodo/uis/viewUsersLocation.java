package com.ron.mytodo.uis;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ron.mytodo.R;
import com.ron.mytodo.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class viewUsersLocation extends AppCompatActivity implements  OnMapReadyCallback {
    private GoogleMap mMap;
    String LocationLatLng, locationCoord ;
    private static DecimalFormat df = new DecimalFormat("0.00");

    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public String location;
    List<Address> addresses;
    Double latitude1, longitude1;
    public LatLng myLocation, myLocation2;
    Marker[] markers;
    MarkerOptions options = new MarkerOptions();
    MarkerOptions options2 = new MarkerOptions();
    TextView textView;
    Button button2;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1;
    protected LocationManager mlocationManager;

    private FirebaseAuth auth;
    String Semail,Spassword,Susername;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    public List<String> users = new ArrayList<String>();








    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_location);




        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10,
                10, mLocationListener);



      



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        auth = FirebaseAuth.getInstance();



        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        //mFirebaseInstance.getReference("Location View").setValue("Realtime Database");






        fetchLocations();


    }

    private final LocationListener mLocationListener = new LocationListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(final Location location) {
            //textView.setText(""+location.getLatitude());  //test whether it's working

            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

       /*     View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);


            LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);

            //SET WHATEVER TEXT YOU WANT TO DISPLAY ON THE MARKER
            numTxt.setText("AA");

            Marker customMarker = mMap.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title("Title")
                    .snippet("Description")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(viewUsersLocation.this, marker))));
*/
            String locationn = (location.getLatitude()+","+location.getLongitude());

            UpdateLocation(locationn);


            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 50, 10, mLocationListener);
        }


        private void UpdateLocation(String location) {

           // Log.e("TAG", "this the email"+auth.getCurrentUser().getEmail());

            Query query = mFirebaseDatabase.orderByChild("email").equalTo(auth.getCurrentUser().getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot  ds: dataSnapshot.getChildren()){
                        ds.getRef().child("location").setValue(location);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // In real apps this userId should be fetched
            // by implementing firebase auth
            //mFirebaseDatabase.child("users").child(auth.getCurrentUser().getUid()).child("latitude").setValue(latitude);
            //mFirebaseDatabase.child("users").child(auth.getCurrentUser().getUid()).child("longitude").setValue(longitude);

            //mFirebaseDatabase.updateChildren();

//            mFirebaseDatabase.child("users").child(userId).child("username").setValue(name);



        }




        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {



        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMapToolbarEnabled(true);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng myLocation = new LatLng(-1.2937,36.7981);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(myLocation.latitude,myLocation.longitude));

        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

/*

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);


        //SET WHATEVER TEXT YOU WANT TO DISPLAY ON THE MARKER
        numTxt.setText("AA");

        Marker customMarker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Title")
                .snippet("Description")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));


*/

    }


   /* @Override
    public void onLocationChanged(Location location) {
        this.location = location.getLatitude()+", "+location.getLongitude();

        latitude1 = location.getLatitude();
        longitude1 = location.getLongitude();
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());

        textView.setText(""+location.getLatitude());

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);


        //SET WHATEVER TEXT YOU WANT TO DISPLAY ON THE MARKER
        numTxt.setText("01");

        Marker customMarker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Title")
                .snippet("Description")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));



    }

  */







    //Getting Distance between two Locations
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c +kmInDec;
    }


    //Drawing a Custom Marker
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    public void fetchLocations(){
        List<User> locationlist = new ArrayList<>();
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    //String _location = dataSnapshot1.getValue(String.class);
                   // locationlist.add(_location);
                    User user = dataSnapshot1.getValue(User.class);
//                    locationlist.add(user.location);

                    String[] parts1 = user.location.split(",");
                    double lat = Double.valueOf(parts1[0]);
                    double lng = Double.valueOf(parts1[1]);

                    View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);




                    LatLng myLocation = new LatLng(lat,lng);
                    TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);

                    //SET WHATEVER TEXT YOU WANT TO DISPLAY ON THE MARKER
                    numTxt.setText(user.username);

                    Marker customMarker = mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("Title")
                            .snippet("Description")
                            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(viewUsersLocation.this, marker))));


                    //Log.e("TAG", "list of location" +info);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //mapLocations();




    }




}
