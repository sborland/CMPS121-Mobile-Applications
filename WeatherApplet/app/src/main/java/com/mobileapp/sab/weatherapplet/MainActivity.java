package com.mobileapp.sab.weatherapplet;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileapp.sab.weatherapplet.response.Conditions;
import com.mobileapp.sab.weatherapplet.response.ObservationLocation;
import com.mobileapp.sab.weatherapplet.response.Weather;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.prefs.Preferences;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String LOG_TAG = "My Log Tag";
    String result;
    Conditions condition;

/// intialize values from the menu screen
    TextView WeatherText;
    TextView CityText;
    TextView LongText;
    TextView LatText;
    TextView ElevationText;
    TextView WindText;
    TextView WindAvgText;
    TextView TempCText;
    TextView TempFText;
    TextView DewpointCText;
    TextView DewpointFText;
    TextView WindchillCText;
    TextView WindchillFText;
    TextView HumidityText;
    TextView PressureText;
    GridLayout TopGrid;
    GridLayout BottomGrid;
    ImageView ErrorImg;
    Button Refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //text in activity_main.xml
        WeatherText = (TextView) findViewById(R.id.Weather);
        CityText = (TextView) findViewById(R.id.City);
        LongText = (TextView) findViewById(R.id.Longitude);
        LatText = (TextView) findViewById(R.id.Latitude);
        ElevationText = (TextView) findViewById(R.id.Elevation);
        WindText = (TextView) findViewById(R.id.Windgust);
        WindAvgText = (TextView) findViewById(R.id.WindAvg);
        TempCText = (TextView) findViewById(R.id.TempC);
        TempFText = (TextView) findViewById(R.id.TempF);
        DewpointCText = (TextView) findViewById(R.id.DewpointC);
        DewpointFText = (TextView) findViewById(R.id.DewpointF);
        WindchillCText = (TextView) findViewById(R.id.WindchillC);
        WindchillFText = (TextView) findViewById(R.id.WindchillF);
        HumidityText = (TextView) findViewById(R.id.Humidity);
        PressureText= (TextView) findViewById(R.id.Pressure);

        //Grids that contain the data
        TopGrid = (GridLayout) findViewById(R.id.gridLayout2);
        BottomGrid = (GridLayout) findViewById(R.id.gridLayout);

        //Refresh button
        Refresh = (Button) findViewById(R.id.findWeather);
        Refresh.setOnClickListener(this);

        //The image that will display when there is an error
        ErrorImg = (ImageView) findViewById(R.id.errorImg);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://luca-teaching.appspot.com/weather/default/get_weather/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        getWeatherData(service);

    }
    //gets the weather data depending on the server and result conditions
    public void getWeatherData(WeatherService service){
        Call<Weather> queryResponseCall = service.getWeather(condition,result);

        //Call retrofit asynchronously
        queryResponseCall.enqueue(new Callback <Weather>() {
            @Override
            public void onResponse(Response<Weather> response) {
                int httpCheck = response.code();
                //500 is server error while 200 is a-okay
                Log.i(LOG_TAG, "Code is: " + response.code());
                if (httpCheck!=200){
                    //Display server error screen
                    Log.i(LOG_TAG, "SERVER ERROR");
                    ErrorImg.setImageResource(R.drawable.sererror);
                    ErrorImg.setVisibility(View.VISIBLE);
                    TopGrid.setVisibility(View.GONE);
                    BottomGrid.setVisibility(View.GONE);
                }else{
                    //check if the result is 'error' or 'ok'
                    String resultCheck = response.body().getResponse().getResult();
                    Log.i(LOG_TAG, "The result is: " + resultCheck);
                    if (resultCheck.equals("ok")){
                        ErrorImg.setVisibility(View.INVISIBLE);
                        TopGrid.setVisibility(View.VISIBLE);
                        BottomGrid.setVisibility(View.VISIBLE);
                        Conditions con = response.body().getResponse().getConditions();
                        ObservationLocation loc = con.getObservationLocation();
                        GetConditionData(con);
                        GetLocationData(loc);
                    }else {
                        Log.i(LOG_TAG, "RESPONSE ERROR ");
                        ErrorImg.setImageResource(R.drawable.reperror);
                        ErrorImg.setVisibility(View.VISIBLE);
                        TopGrid.setVisibility(View.GONE);
                        BottomGrid.setVisibility(View.GONE);
                        //Display response error screen
                    }

                }
            }
            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.i(LOG_TAG, "onFailure: "+t);
            }
        });
    }

    //gets the condition data from the JSON file
    public void GetConditionData(Conditions c){
        WeatherText.setText(c.getWeather());
        WindAvgText.setText("Wind(Gust): " +String.valueOf(c.getWindGustMph()));
        WindText.setText("Wind(MPH): "+String.valueOf(c.getWindMph()));
        TempCText.setText(String.valueOf(c.getTempC()));
        TempFText.setText(String.valueOf(c.getTempF()));
        DewpointCText.setText(String.valueOf(c.getDewpointC()));
        DewpointFText.setText(String.valueOf(c.getDewpointF()));
        WindchillCText.setText(String.valueOf(c.getWindchillC()));
        WindchillFText.setText(String.valueOf(c.getWindchillF()));
        HumidityText.setText("Humidity: "+ String.valueOf(c.getRelativeHumidity()));
        PressureText.setText("Pressure: "+ String.valueOf(c.getPressureMb()));

    }

    //gets location data from JSON file
    public void GetLocationData(ObservationLocation l){
        CityText.setText(l.getFull() +", "+l.getCountry());
        ElevationText.setText(("Elevation: "+l.getElevation()));
        LongText.setText(String.valueOf("Longitude: " + l.getLongitude()));
        LatText.setText(String.valueOf("Latitude: "+l.getLatitude()));
    }



    @Override
    //Each time you click refresh, it'll call the information from the server.
    public void onClick(View v) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://luca-teaching.appspot.com/weather/default/get_weather/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        getWeatherData(service);


    }

    /////Interface for Weather Service
    public interface WeatherService {
            @GET("default/get_weather/")
            Call<Weather> getWeather(@Query("conditions") Conditions condit,
                                        @Query("result") String result);
        }
}
