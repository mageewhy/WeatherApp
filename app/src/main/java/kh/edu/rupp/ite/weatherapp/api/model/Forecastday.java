package kh.edu.rupp.ite.weatherapp.api.model;

public class Forecastday {
    private Day day;

    private Hour[] hour;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Hour[] getHour() {
        return hour;
    }

    public void setHour(Hour[] hour) {
        this.hour = hour;
    }
}
