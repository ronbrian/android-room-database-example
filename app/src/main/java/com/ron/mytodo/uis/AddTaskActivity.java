package com.ron.mytodo.uis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ron.mytodo.Database.DatabaseClient;
import com.ron.mytodo.DirectionsJSONParser;
import com.ron.mytodo.MyGooglePlaces;
import com.ron.mytodo.MyPlacesAdapter;
import com.ron.mytodo.R;
import com.ron.mytodo.editUser;
import com.ron.mytodo.model.Task;
import com.ron.mytodo.rest.UserService;

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

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTaskActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    private GoogleMap mMap;

    AutoCompleteTextView places,places2;
    MyPlacesAdapter adapter;


    String TAG = "placeautocomplete";
    TextView txtView;
    String LocationLatLng, locationCoord ;
    private static DecimalFormat df = new DecimalFormat("0.00");

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public String location;
    List<Address> addresses;
    Double latitude1, longitude1;
    public LatLng myLocation, myLocation2;
    Marker[] markers;

    MarkerOptions options = new MarkerOptions();
    MarkerOptions options2 = new MarkerOptions();
    RelativeLayout relativeLayout1;


    private EditText editTextTask, editTextDesc, editTextFinishBy, editTextLocation;
    private ImageView imageviewLocationIcon1, imageviewLocationIcon,imageviewLocationIcon2;
    private TextView textViewDistance, textViewTo, textViewFrom;
    private FloatingActionButton btnRedirecttoViewUsers;


    


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);
        editTextLocation = findViewById(R.id.editTextLocation);
        imageviewLocationIcon1 = findViewById(R.id.imageView1);
        imageviewLocationIcon2 = findViewById(R.id.imageView1);
        textViewFrom = findViewById(R.id.textViewFrom);
        textViewTo = findViewById(R.id.textViewTo);;
        textViewDistance = findViewById(R.id.textViewDistance);
        relativeLayout1 = findViewById(R.id.relativeLayout1);

        btnRedirecttoViewUsers = findViewById(R.id.floatingActionButton);





        places=(AutoCompleteTextView)findViewById(R.id.places);
        places2=(AutoCompleteTextView)findViewById(R.id.places2);

        places2.setVisibility(View.GONE);//makes it disappear
        relativeLayout1.setVisibility(View.GONE);//makes it disappear


        adapter=new MyPlacesAdapter(AddTaskActivity.this);
        places.setAdapter(adapter);
        places2.setAdapter(adapter);
        textChange();

        // handling click of autotextcompleteview items
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                places.setText(googlePlaces.getName());


                myLocation = new LatLng(googlePlaces.getLat(),googlePlaces.getLng());



                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                //mMap.setMinZoomPreference(15);

                options.position(myLocation);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(options.title(googlePlaces.getName())).showInfoWindow();
                textViewFrom.setText(googlePlaces.getName());



                places2.setVisibility(View.VISIBLE);//makes it reappear



            }
        });
        
        
        btnRedirecttoViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskActivity.this, viewUsersLocation.class);
                startActivity(intent);
            }
        });

        places2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                places2.setText(googlePlaces.getName());

                myLocation2 = new LatLng(googlePlaces.getLat(),googlePlaces.getLng());



                //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation2));
                //mMap.setMinZoomPreference(12);

                options2.position(myLocation2);
                options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(options2.title(googlePlaces.getName())).showInfoWindow();
                textViewTo.setText(googlePlaces.getName());


                LatLng origin = myLocation;
                LatLng dest = myLocation2;

                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                downloadTask.execute(url);
/*
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //the include method will calculate the min and max bound.
                builder.include(options.getPosition());
                builder.include(options2.getPosition());

                LatLngBounds bounds = builder.build();

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.40); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                mMap.animateCamera(cu);*/

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.35); // offset from edges of the map 10% of screen


                LatLngBounds NAIROBI = builder.build();

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(NAIROBI,width,height,padding));


                Double dist = CalculationByDistance(origin, dest);
                String dist1 = df.format(dist);

                textViewDistance.setText(dist1+" Km");

                relativeLayout1.setVisibility(View.VISIBLE);//makes it disappear




       /*         LatLngBounds NAIROBI = new LatLngBounds(
                        new LatLng(origin.longitude,origin.latitude), new LatLng(dest.longitude,dest));
                mMap.setLatLngBoundsForCameraTarget(NAIROBI);*/

                // Set the camera to the greatest possible zoom level that includes the
                // bounds




            }
        });




        txtView = findViewById(R.id.txtView);




        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTask();

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }



    private void textChange() {
        // text changed listener to get results precisely according to our search
        places.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //calling getfilter to filter the results
                adapter.getFilter().filter(s);
                //notify the adapters after results changed
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.actionViewOtherUsers) {
            Intent intent = new Intent(this, viewUsersLocation.class);
            startActivity(intent);
        }
        if (id == R.id.actionEditMyInfo) {
            Intent intent = new Intent(this, editUser.class);
            startActivity(intent);        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMapToolbarEnabled(true);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng myLocation = new LatLng(-1.2937,36.7981);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(myLocation.latitude,myLocation.longitude));

        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


        /*//IconGenerator iconGen = new IconGenerator(this);
        IconGenerator iconFactory =  new IconGenerator(this);
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Custom text"))).
                position(new LatLng(-1.2937, 36.7967)).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions).showInfoWindow();


*/


      /*  View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);


        //SET WHATEVER TEXT YOU WANT TO DISPLAY ON THE MARKER
        numTxt.setText("AA");

        Marker customMarker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Title")
                .snippet("Description")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
*/

        //mMap.setMinZoomPreference(15);
        //mMap.setMaxZoomPreference(14.0f);

         //LatLngBounds NAIROBI = new LatLngBounds(
                //new LatLng(-1.429104,36.606102), new LatLng(-1.165157,37.095337));
        //mMap.setLatLngBoundsForCameraTarget(NAIROBI);

        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(NAIROBI, 0));


    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location.getLatitude()+", "+location.getLongitude();


         latitude1 = location.getLatitude();
         longitude1 = location.getLongitude();


    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "User");
    }



    public void saveTask() {
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();
        final String sLocation = locationCoord;



        if (sTask.isEmpty()) {
            editTextTask.setError("Task required");
                editTextTask.requestFocus();
            return;
        }

               if (sDesc.isEmpty()) {
              editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        if (sFinishBy.isEmpty()) {
                     editTextFinishBy.setError("Finish by required");
            editTextFinishBy.requestFocus();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.setTask(sTask);
                task.setDescription(sDesc);
                    task.setFinishBy(sFinishBy);
                    task.setLocation(locationCoord);
                task.setFinished(false);





                //adding to Room DB database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);


                //adding to MySQL Database
                saveTask2(sTask,sDesc,sFinishBy,false);



                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }




        SaveTask st = new SaveTask();
        st.execute();
    }


    public void saveTask2(String sTask, String sDesc, String sFinishBy , boolean finished){   //Save task to MySQL Server
        finished = false;
        Task task2 = new Task();
        task2.setTask(sTask);
        task2.setDescription(sDesc);
        task2.setFinishBy(sFinishBy);
        task2.setFinished(finished);
        task2.setLocation(locationCoord);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.148:9786/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        UserService service = retrofit.create(UserService.class);

        // Calling '/api/users/2'
        Call callSync = service.addTask(task2);
        try {
            callSync.execute();
        } catch (Exception e) {


        }


    }









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




}
