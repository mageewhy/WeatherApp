package kh.edu.rupp.ite.weatherapp.model.api.model;

public class Condition {
    private String text;
    private String icon;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return "https:" + icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
