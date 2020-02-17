package com.ron.mytodo;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyPlacesAdapter extends BaseAdapter implements Filterable, LocationListener {
    private LayoutInflater mInflater;
    Context con;
    ArrayList<MyGooglePlaces> places=new ArrayList<MyGooglePlaces>();

    String LocationLatLng, locationCoord ;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public String location;
    List<Address> addresses;
    Double latitude1, longitude1;



    public MyPlacesAdapter(Context context) {
        super();
        mInflater = LayoutInflater.from(context);
        con=context;
    }
    // get total count of array
    @Override
    public int getCount() {
        return places.size();
    }
    //get item id
    @Override
    public long getItemId(int position) {
        return position;
    }
    // get Mygoogleplaces object at given position
    @Override
    public MyGooglePlaces getItem(int position) {
        return places.get(position);
    }
    //Filter to filter the results
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results=new FilterResults();
                if(constraint!=null)
                {
                    places=getPredictions(constraint.toString());
                    if(places!=null)
                    {
                        results.values=places;
                        results.count=places.size();
                    }
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }






    // method to get different places nearby search location
    private ArrayList<MyGooglePlaces> getPredictions(String constraint)
    {
        //pass your current latitude and longitude to find nearby and ranky=distance means places will be found in ascending order
        //Use the Location Listener from The Main Activity to get the current Latitude and Longitude
        // according to distance


        double latitude=-1.2937;
        double longitude=36.7969;



        String API_KEY="AIzaSyDTQ67ZZMnns_OlsSKe5qPqiqmeKRJuBSg";

        //String url="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+constraint+"&key="+API_KEY;
        String url="https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+constraint+"&inputtype=textquery&fields=name,geometry,formatted_address&key="+API_KEY;

        return getPlaces(url);

    }
    private ArrayList<MyGooglePlaces> getPlaces(String constraint)
    {
        //code for API level 23 as httpclient is depricated in API 23
        StringBuffer sb=null;
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(constraint);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            sb=new StringBuffer("");
            while (data != -1) {
                sb.append((char)data);
                //char current = (char) data;
                data = isw.read();
                // System.out.print(current);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return   parseGoogleParse(sb.toString());
    }


    // method to parse the json returned by googleplaces api
    private static ArrayList parseGoogleParse(final String response) {
        ArrayList<MyGooglePlaces> temp = new ArrayList();
        try {
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);
            // make an jsonObject in order to parse the response
            if (jsonObject.has("candidates")) {
                JSONArray jsonArray = jsonObject.getJSONArray("candidates");
                for (int i = 0; i < jsonArray.length(); i++) {
                    MyGooglePlaces poi = new MyGooglePlaces();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("geometry"))
                        {
                            if (jsonArray.getJSONObject(i).getJSONObject("geometry").has("location"))
                            {
                                if (jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lat"))
                                {
                                    poi.setLatLng(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat")), Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng")));
                                }
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("formatted_address")) {
                            poi.setVicinity(jsonArray.getJSONObject(i).optString("formatted_address"));
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                    }
                    //if(temp.size()<5)
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return temp;
    }
    // View method called for each row of the result
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // The ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.autocomplete, null);
            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.myplaces);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            // Bind the data efficiently with the holder.
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.text.setText(Html.fromHtml("<b>"+places.get(position).getName()+"<b>"));
            holder.address.setText(places.get(position).getVicinity());
            // If weren't re-ordering this you could rely on what you set last time
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return convertView;

    }
    // viewholder class to hold adapter views
    static class ViewHolder {
        TextView text,address;
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
        Log.d("Latitude", "status");
    }



}