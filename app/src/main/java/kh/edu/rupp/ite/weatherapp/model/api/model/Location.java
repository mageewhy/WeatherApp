package kh.edu.rupp.ite.weatherapp.model.api.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("name")
    private String name;
    @SerializedName("country")
    private String country;
    @SerializedName("localtime")
    private String localtime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }
    public String getLocaltime() {
        return localtime;
    }


}
