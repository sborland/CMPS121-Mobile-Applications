package com.mobileapp.sab.localchat;
import com.mobileapp.sab.localchat.MainActivity;
import com.mobileapp.sab.localchat.response.GetMessage;
import com.mobileapp.sab.localchat.response.PostMessage;
import com.mobileapp.sab.localchat.response.ResultList;

import android.Manifest;
import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userPost;
    ListView message;
    Button refreshButton;
    Button postButton;
    String userid;
    String username;
    List<ResultList> resultList;
    String Result;

    public static String LOG_TAG = "App Messages";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //building everything
        userPost = (EditText) findViewById(R.id.postText);
        message = (ListView) findViewById(R.id.messages);
        refreshButton =(Button) findViewById(R.id.RefreshButton);
        postButton=(Button) findViewById(R.id.PostButton);
       refreshButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        //grabbing user data
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        userid = settings.getString("user_id", null);
        username = settings.getString("user_nickname", null);
    }


    @Override
    public void onClick(View v) {
            hideSoftKeyboard();
        switch (v.getId()){
            case R.id.RefreshButton:
                Toast.makeText(ChatActivity.this, "Refreshing messages, meow!", Toast.LENGTH_SHORT).show();
                refreshMessages();
                break;
            case R.id.PostButton:
                posting();
                break;
           default:
               break;
        }
    }

    //grabs messages from the recent location
    public void refreshMessages(){
        Retrofit retrofit = accessService();
        RefreshService refreshservice =retrofit.create(RefreshService.class);
        Location loc = LocationData.getLocationData().getLocation();
        float longit = (float)loc.getLongitude();
        float latit = (float)loc.getLatitude();
        Call<GetMessage> call = refreshservice.getMessage(resultList, Result,latit,longit,userid);
        call.enqueue(new Callback<GetMessage>() {
            @Override
            public void onResponse(Response<GetMessage> response) {
                int httpCheck = response.code();
                Log.i(LOG_TAG, "Code is: " + response.code());
                if (httpCheck!=200) {
                    Log.i(LOG_TAG, "SERVER ERROR");
                } else {
                    Log.i(LOG_TAG, "SERVER GOOD. Result: " + response.body().getResult());
                }

            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void displayMessages(){
        

    }

    //posts the user's message if not empty
    public void posting(){
        String post = userPost.getText().toString();
        //if user posts an empty string, let them know
        if (post.isEmpty()){
            Log.i(LOG_TAG, "User's post is empty!");
            Toast.makeText(ChatActivity.this, "Whoops! It looks like your post is empty. Please type something in to post it.", Toast.LENGTH_SHORT).show();
        } else{
            Retrofit retrofit = accessService();
            PostService postservice =retrofit.create(PostService.class);
            //get all the data needed to post the message
            Location loc = LocationData.getLocationData().getLocation();
            float longit = (float)loc.getLongitude();
            float latit = (float)loc.getLatitude();
            PostMessage msg = new PostMessage(latit,longit,userid, username, post);
            Call<PostMessage> call = postservice.postMessage(msg);
            call.enqueue(new Callback<PostMessage>() {
                @Override
                public void onResponse(Response<PostMessage> response) {
                    int httpCheck = response.code();
                    //500 is server error while 200 is a-okay
                    Log.i(LOG_TAG, "Code is: " + response.code());
                    if (httpCheck!=200) {
                        Log.i(LOG_TAG, "SERVER ERROR");
                    } else{
                        Log.i(LOG_TAG, "SERVER GOOD. Result: "+ response.body().getResult());
                        userPost.setText("");
                        refreshMessages();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                    Log.i(LOG_TAG, "onFailure: "+t);

                }
            });


        }

    }

    //calls retrofit to server
    public Retrofit accessService(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        return retrofit;

    }


    //small function to hide keyboard once the user either refreshes or posts something
    private void hideSoftKeyboard(){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userPost.getWindowToken(), 0);
    }

    //Toast helper
    private void notify(String message){
        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    //public interface for posting a message
    public interface PostService {
        @POST("post_message/")
        Call<PostMessage> postMessage(@Body PostMessage msg);
    }

    //public interface to getting messages
    public interface RefreshService {
        @GET("get_messages/")
        Call<GetMessage> getMessage(@Query("result_list") List<ResultList> resultList,
                                 @Query("result") String result,
                                 @Query ("lat") float lat,
                                 @Query ("lng") float lng,
                                 @Query ("user_id") String user_id);

    }





}
