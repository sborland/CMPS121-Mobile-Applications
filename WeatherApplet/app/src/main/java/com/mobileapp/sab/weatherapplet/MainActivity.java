package com.mobileapp.sab.weatherapplet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "My Log Tag";
    String result;
    Conditions condition;
    ObservationLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Call<Weather> queryResponseCall = service.getWeather(condition,result);

        //Call retrofit asynchronously
        queryResponseCall.enqueue(new Callback <Weather>() {
            @Override
            public void onResponse(Response<Weather> response) {
                //Log.i(LOG_TAG, "Code is: " + response.code());
                //Log.i(LOG_TAG, "The result is: " + response.body().getResponse().getConditions().getWindGustMph());
                int httpCheck = response.code();
                //500 is server error
                if (httpCheck==500){
                    //Display server error?
                    Log.i(LOG_TAG, "Code is: " + response.code()+". SERVER ERROR");
                }else{
                    //check the result for error or ok
                    String resultCheck = response.body().getResponse().getResult();
                    Log.i(LOG_TAG, "The result is: " + resultCheck);
                    if (resultCheck=="error"){
                        //Display error?
                    }else {
                        condition = response.body().getResponse().getConditions();
                        location = condition.getObservationLocation();
                        GetConditionData(condition);
                        GetLocationData(location);
                    }

                }
            }
            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }
        });


    }

    //gets the condition data from the JSON file
    public void GetConditionData(Conditions c){
        c.getWindGustMph();

    }

    //gets location data from JSON file
    public void GetLocationData(ObservationLocation l){

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ////New Stuff
    public interface WeatherService {
            @GET("default/get_weather/")
            Call<Weather> getWeather(@Query("conditions") Conditions condit,
                                        @Query("result") String result);
        }
}
