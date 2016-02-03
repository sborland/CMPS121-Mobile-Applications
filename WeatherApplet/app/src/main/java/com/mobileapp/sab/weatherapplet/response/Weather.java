
package com.mobileapp.sab.weatherapplet.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Weather {

    @SerializedName("response")
    @Expose
    private WeatherResponse response;

    /**
     * 
     * @return
     *     The response
     */
    public WeatherResponse getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The response
     */
    public void setResponse(WeatherResponse response) {
        this.response = response;
    }

}
