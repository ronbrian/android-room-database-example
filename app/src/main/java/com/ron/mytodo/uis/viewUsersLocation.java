package com.ron.mytodo.uis;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ron.mytodo.DirectionsJSONParser;
import com.ron.mytodo.R;
import com.ron.mytodo.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public class viewUsersLocation extends AppCompatActivity implements  OnMapReadyCallback {
    private GoogleMap mMap;
    private static DecimalFormat df = new DecimalFormat("0.00");

    protected Context context;
    public String location;
    Geocoder geocoder;
    List<Address> addresses;


    MarkerOptions options = new MarkerOptions();
    MarkerOptions options2 = new MarkerOptions();
    TextView  textViewUsername, textViewLocation, textViewDistance;

    ImageButton buttonDirection;


    RelativeLayout relativeLayout1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1;
    protected LocationManager mlocationManager;

    private FirebaseAuth auth;
    String Semail, Spassword, Susername;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    public List<String> users = new ArrayList<String>();
    public List<String> mylocation = new ArrayList<String>();
    List<Marker> markers = new ArrayList<Marker>();


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_location);


        relativeLayout1 = findViewById(R.id.relativeLayout1);
        relativeLayout1.setVisibility(View.GONE);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewDistance = findViewById(R.id.textViewDistance);
        textViewLocation = findViewById(R.id.textViewLocation);
        buttonDirection = findViewById(R.id.buttonDirection);


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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                getMarkerPositions();
            }
        }, 3000);


    }

    private final LocationListener mLocationListener = new LocationListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(final Location location) {
            //textView.setText(""+location.getLatitude());  //test whether it's working

            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            String locationn = (location.getLatitude() + "," + location.getLongitude());

            UpdateLocation(locationn);
            mylocation.add(locationn);

            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1, 1, mLocationListener);
        }


        private void UpdateLocation(String location) {

            // Log.e("TAG", "this the email"+auth.getCurrentUser().getEmail());

            Query query = mFirebaseDatabase.orderByChild("email").equalTo(auth.getCurrentUser().getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().child("location").setValue(location);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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

//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(true);
        //mMap.getUiSettings().setMapToolbarEnabled(true);


        mMap.setMapType(MAP_TYPE_NORMAL);
        LatLng myLocation = new LatLng(-1.2937, 36.7981);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(myLocation.latitude, myLocation.longitude));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onMarkerClick(Marker marker) {
                double latitude = marker.getPosition().latitude;
                double longitude = marker.getPosition().longitude;
                String name = marker.getTitle();

                //Getting address from the Latitude and Longitude
                geocoder = new Geocoder(viewUsersLocation.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0);



                String myLocation = mylocation.get(0);
                String[] parts1 = myLocation.split(",");
                double lat = Double.valueOf(parts1[0]);
                double lng = Double.valueOf(parts1[1]);


                LatLng targetLocation = new LatLng(latitude, longitude);
                LatLng myLocation2 = new LatLng(lat, lng);

                String distance =  CalculationByDistance(myLocation2, targetLocation);


                textViewUsername.setText(name);
                textViewLocation.setText(address);
                textViewDistance.setText(distance+" Km Away");

                String url = getDirectionsUrl(myLocation2, targetLocation);

                DownloadTask downloadTask = new DownloadTask();


                downloadTask.execute(url);

                getDirectionsUrl(myLocation2,targetLocation );

                relativeLayout1.setVisibility(View.VISIBLE);
                return false;
            }
        });


    }


    //Getting Distance between two Locations
    public String CalculationByDistance(LatLng StartP, LatLng EndP) {
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

        double distance = Radius*c + kmInDec;
        String dist1 = df.format(distance);

        return ""+dist1;

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


    public void fetchLocations() {
        List<User> locationlist = new ArrayList<>();
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //String _location = dataSnapshot1.getValue(String.class);
                    // locationlist.add(_location);
                    User user = dataSnapshot1.getValue(User.class);
//                    locationlist.add(user.location);

                    if (user.location.isEmpty()) {

                    } else {
                        String[] parts1 = user.location.split(",");
                        double lat = Double.valueOf(parts1[0]);
                        double lng = Double.valueOf(parts1[1]);


                        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

                        LatLng myLocation = new LatLng(lat, lng);
                        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);

                        //SET WHATEVER TEXT YOU WANT TO DISPLAY ON THE MARKER
                        numTxt.setText(user.username);

                        Marker customMarker = mMap.addMarker(new MarkerOptions()
                                .position(myLocation)
                                .title(user.username)
                                .snippet("Description")
                                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(viewUsersLocation.this, marker))));

                        markers.add(customMarker);
                        markers.size();

                    }
                    //Log.e("TAG", "list of location" +info);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //mapLocations();
        // getMarkerPositions();


    }


    public void getMarkerPositions() {

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : markers) {
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.35); // offset from edges of the map 10% of screen

        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);


    }

    /*  Method to download JSON DATA FROM URL  */

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           ParserTask parserTask = new ParserTask();


            parserTask.execute(result);



        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(0);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng  position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(false);

                //lineOptions.addAll(points);

            }

// Drawing polyline in the Google Map for the i-th route
            //mMap.addPolyline(lineOptions);


            if(points.size()!=0)mMap.addPolyline(lineOptions);//to avoid crash

        }


    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=AIzaSyDTQ67ZZMnns_OlsSKe5qPqiqmeKRJuBSg";


        return url;
    }

    /*  Method to download JSON DATA FROM URL  */

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



}

