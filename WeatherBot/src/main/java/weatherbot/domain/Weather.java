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

    /**
     * Empty constructor that enables Spring work with databases.
     */
    public Weather() {

    }
    
    /**
     * Constructor to create a weather object with unique id number received from the database. 
     * @param id automatically generated id received when the object is added to the database
     * @param city city for which weather information is requested
     * @param temperature current temperature in the requested city
     * @param humidity current humidity percent  
     * @param main current weather condition
     * @param icon the id of the icon representing current weather condition 
     * @param time the timestamp from the moment when the weather object is created
     */
    public Weather(Integer id, String city, String temperature, String humidity, String main, String icon, Timestamp time) {
        this.id = id;
        this.city = city; 
        this.temperature = temperature;
        this.humidity = humidity;
        this.main = main;
        this.icon = icon;
        this.time = time;
    }
    
    /**
     * Constructor to create a weather object without id.
     * @param city city for which weather information is requested
     * @param temperature current temperature in the requested city
     * @param humidity current humidity percent  
     * @param main current weather condition
     * @param icon the id of the icon representing current weather condition 
     * @param time the timestamp from the moment when the weather object is created
     */
    public Weather(String city, String temperature, String humidity, String main, String icon, Timestamp time) {
        this(0, city, temperature, humidity, main, icon, time);
    }

    /**
     * Sets the id for the weather object.
     * @param id id automatically generated by the database
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Sets the weather condition.
     * @param main current weather condition
     */
    public void setMain(String main) {
        this.main = main;
    }

    /**
     * Sets the id for the main icon.
     * @param icon the id of the icon representing current weather condition 
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Sets current temperature retrieved from the openweathermap api.
     * @param temperature current temperature in the requested city
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Sets humidity percent.
     * @param humidity current humidity percent  
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Sets city name.
     * @param city city for which weather information is requested
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns unique id value for this weather object.
     * @return returns id
     */
    public Integer getId() {
        return id;
    }

    /** 
     * Returns current weather condition in the requested city, i.e. "clear sky / snow".
     * @return returns weather condition
     */
    public String getMain() {
        return main;
    }

    /**
     * Returns the id of the icon representing current weather condition.
     * @return the id number of icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns current temperature in the requested city in Celsius units.
     * @return current temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Returns current humidity percent in the requested city. 
     * @return returns humidity percent
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     * Returns the name of the city for which weather information is requested.
     * @return returns city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the timestamp with time when this weather object was created.
     * @return returns time of object creation
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * Sets the timestamp with time when the weather object is created.
     * @param time timestamp containing information about time of object creation
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }   

 /**
 * Method creates a representation of weather information which is shown to user
 * in telegram application.
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
 * in telegram application.
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
