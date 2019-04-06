package domain;

public class Weather {
    private String main;
    private String icon;
    private String temperature;
    private String humidity;
    private String city;

    public void setMain(String main) {
        this.main = main;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
