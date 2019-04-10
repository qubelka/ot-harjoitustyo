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

    public String getMain() {
        return main;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getCity() {
        return city;
    }
    
    @Override
    public String toString() {
        return "City: " + this.getCity() + "\n"
                + "Temperature: " + this.getTemperature() + "°C\n"
                + "Humidity: " + this.humidity + "%\n"
                + "Main: " + this.main + "\n"
                + "http://openweathermap.org/img/w/" + this.getIcon() + ".png";
                
    }

}
