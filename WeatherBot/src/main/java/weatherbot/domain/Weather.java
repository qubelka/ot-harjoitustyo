package weatherbot.domain;

import java.sql.Timestamp;

/**
 * This class stores weather-related information.
 */

public class Weather {

    private Integer id;
    private String city;
    private String temperature;
    private String humidity;
    private String main;
    private String icon;
    private Timestamp time;

    public Weather() {

    }
    
    public Weather(Integer id, String city, String temperature, String humidity, String main, String icon, Timestamp time) {
        this.id = id;
        this.city = city; 
        this.temperature = temperature;
        this.humidity = humidity;
        this.main = main;
        this.icon = icon;
        this.time = time;
    }
    
    public Weather(String city, String temperature, String humidity, String main, String icon, Timestamp time) {
        this(0, city, temperature, humidity, main, icon, time);
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getId() {
        return id;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
    
    

 /**
 * Method creates a representation of weather information which is shown to user
 * in telegram application
 * @return object of a String-type containing weather-information in Celsius scale
 */
    
    @Override
    public String toString() {
        return "City: " + this.getCity() + "\n"
                + "Temperature: " + this.temperature + "°C\n"
                + "Humidity: " + this.humidity + "%\n"
                + "Main: " + this.main + "\n"
                + "http://openweathermap.org/img/w/" + this.getIcon() + ".png";
    }

    
 /**
 * Method creates a representation of weather information which is shown to user
 * in telegram application
 * @return object of a String-type containing weather-information in Fahrenheit scale
 */
    
    public String toStringFarenheit() {
        String tempInFahrenheit = String.format("%.2f", Double.valueOf(this.temperature) * 1.8 + 32);
        return "City: " + this.getCity() + "\n"
                + "Temperature: " + tempInFahrenheit + "°F\n"
                + "Humidity: " + this.humidity + "%\n"
                + "Main: " + this.main + "\n"
                + "http://openweathermap.org/img/w/" + this.getIcon() + ".png";
    }

}
