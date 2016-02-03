
package com.mobileapp.sab.weatherapplet.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Response {

    @SerializedName("conditions")
    @Expose
    public Conditions conditions;
    //public String conditions;
    @SerializedName("result")
    @Expose
    public String result;

    /**
     * 
     * @return
     *     The conditions
     */
    public Conditions getConditions() {
        return conditions;
    }

    /**
     * 
     * @param conditions
     *     The conditions
     */
    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }

    /**
     * 
     * @return
     *     The result
     */
    public String getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    public void setResult(String result) {
        this.result = result;
    }

}
