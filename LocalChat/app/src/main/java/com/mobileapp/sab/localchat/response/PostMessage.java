package com.mobileapp.sab.localchat.response;

import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
import java.math.BigInteger;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostMessage {

    @SerializedName("lat")
    @Expose
    private float latitude;
    @SerializedName("lng")
    @Expose
    private float longitude;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_id")
    @Expose
    private String message_id;

    @SerializedName("result")
    @Expose
    private String result;

    public PostMessage(float latitude, float longitude, String user_id, String nickname, String message){
        this.user_id = user_id;
        this.longitude =longitude;
        this.latitude = latitude;
        this.nickname = nickname;
        this.message = message;
        SecureRandom random = new SecureRandom();
        BigInteger b = new BigInteger(130,random);
        this.message_id = b.toString(32);
    }

    //get all the post message data
    public String getUserId(){return user_id;}
    public float getLatitude(){return latitude;}
    public float getLongitude(){return longitude;}

    public String getResult() {
        return result;
    }


/*
    //set all the post message data
    public void setUserId(String userId) {
        this.user_id = user_id;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(float longitude){
        this.longitude=longitude;
    }*/
}
