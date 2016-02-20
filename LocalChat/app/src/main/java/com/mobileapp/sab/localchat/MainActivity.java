package com.mobileapp.sab.localchat;

import com.mobileapp.sab.localchat.CreateUserID;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String user_id;
    public String user_nickname="user";
    public static String LOG_TAG = "App Messages";
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private LocationData locationData = LocationData.getLocationData();

    //all the handy layout variables
    EditText input;
    TextView topText;
    TextView bottomText;
    Button chatButton;

    //variable for the button states
    // 0 = for submitting new nickname
    // 1 = for refreshing location
    // 2 = to enter chat
    private int buttonVar =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.UserInput);
        topText = (TextView) findViewById(R.id.Prompt);
        bottomText = (TextView) findViewById(R.id.LobbyText);
        chatButton = (Button) findViewById(R.id.ChatButton);
        chatButton.setOnClickListener(this);

        // Gets the settings, and creates a random user id if missing.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);
        Log.i(LOG_TAG,"User id:"+user_id);
        if (user_id == null) {
            Log.i(LOG_TAG,"Creating new user id");
            // Creates a random one, and sets it.
            CreateUserID srs = new CreateUserID();
            user_id = srs.nextString();
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_id", user_id);
            e.commit();
            //sets the button text and displays all the layout
            buttonVar=0;
            chatButton.setText("Submit Nickname!");
            topText.setText("Enter a Nickname, Meow!");
            bottomText.setVisibility(View.INVISIBLE);
        } else{
            user_nickname = settings.getString("user_nickname", null);
            buttonVar = 1;
            chatButton.setText("Refresh Location");
            topText.setText("Welcome to Cat Chat, "+user_nickname+"!");
            input.setVisibility(View.INVISIBLE);
            bottomText.setText("Getting User Location...");
            bottomText.setVisibility(View.VISIBLE);
            getLocation();
        }
    }

    //submit nickname into user preferences
    public void getNickname(){
        String name = input.getText().toString();
        if (name.isEmpty()){
            bottomText.setText("Error: Please enter at least 1 character for a nickname.");
            bottomText.setVisibility(View.VISIBLE);
        } else{
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_nickname", name);
            Log.i(LOG_TAG, "TESTING " + settings.getString("user_id", null));
            e.commit();
            user_nickname = name;
            //changes layout to lobby!
            buttonVar = 1;
            chatButton.setText("Refresh Location");
            topText.setText("Welcome to Cat Chat, "+user_nickname+"!");
            input.setVisibility(View.INVISIBLE);
            bottomText.setText("Getting User Location...");
            bottomText.setVisibility(View.VISIBLE);
            getLocation();
        }

    }

    //Once app retrieves user nickname, get the user location
    public  void getLocation(){
        updateLocation();
        Log.i(LOG_TAG, "LOCATION: " + locationData.getLocation());
        if (locationData.getLocation()==null) {
            bottomText.setText("Location not found! Please allow the app to retrieve your location then click refresh.");
        } else{
            buttonVar = 3;
            Location loc = locationData.getLocation();
            bottomText.setText("Location found!\n Enjoy chatting with friends, meow!\nLatitude:"+loc.getLatitude()+"\nLongitude:"+loc.getLongitude());
            chatButton.setText("Enter Chat");
        }


    }

    //updates location
    public void updateLocation(){
        //sets up location listener
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

                Log.i(LOG_TAG, "requesting location update");
            }
            else {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Log.i(LOG_TAG, "Getting user permission");

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_FINE_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.


                }
            }
        } else{
            Log.i(LOG_TAG, "requesting location update from user");
            //prompt user to enable location
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }


//button listener
    @Override
    public void onClick(View v) {
        //entering the nickname
        if (buttonVar==0){
            getNickname();
        //refreshing location
        }else if (buttonVar==1){
            getLocation();
        //entering chat
        }else{
            Intent Chat= new Intent(MainActivity.this, ChatActivity.class);
            MainActivity.this.startActivity(Chat);

        }

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Location lastLocation = locationData.getLocation();

            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();
            Log.i(LOG_TAG, "Accuracy is: "+newAccuracy);

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);

                //Now we have the location.////////////////////////////////////////////

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

}
