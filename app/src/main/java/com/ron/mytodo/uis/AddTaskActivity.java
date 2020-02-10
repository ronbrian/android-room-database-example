package com.ron.mytodo.uis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ron.mytodo.Database.DatabaseClient;
import com.ron.mytodo.model.Task;
import com.ron.mytodo.rest.UserService;

import net.simplifiedcoding.mytodo.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTaskActivity extends AppCompatActivity implements LocationListener {
    String TAG = "placeautocomplete";
    TextView txtView;
    String LocationLatLng, locationCoord ;

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


    private EditText editTextTask, editTextDesc, editTextFinishBy, editTextLocation;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        txtView = findViewById(R.id.txtView);









        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyDTQ67ZZMnns_OlsSKe5qPqiqmeKRJuBSg");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(AddTaskActivity.this, ""+place.getLatLng(), Toast.LENGTH_SHORT).show();
                //editTextLocation.setText(place.getName());
                LocationLatLng = place.getLatLng().toString();



                String[] output = LocationLatLng.split(" ");
                //System.out.println(output[0]);
                //System.out.println(output[1]);
                locationCoord= output[1];
                locationCoord = locationCoord.replace("(", "");
                locationCoord = locationCoord.replace(")", "");

                editTextLocation.setText(locationCoord);



                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }




        });

















        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);
        editTextLocation = findViewById(R.id.editTextLocation);




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



    @Override
    public void onLocationChanged(Location location) {
        txtLat = (TextView) findViewById(R.id.textView1);
        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        this.location = location.getLatitude()+", "+location.getLongitude();


        double latitude1 = location.getLatitude();
        double longitude1 = location.getLongitude();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude1, longitude1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }



        for(Address addresses1 : addresses ){



            //editTextLocation.setText(addresses1.getAddressLine(0));

        }




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
        Log.d("Latitude", "status");
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









}
