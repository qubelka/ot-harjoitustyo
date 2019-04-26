package weatherbot.domain;

import weatherbot.dao.UserDao;
import weatherbot.dao.WeatherDao;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weatherbot.dao.LocationDao;

/**
 * This class handles weather requests. It provides methods for checking weather, setting units and creating locations. 
 * 
 */
public class WeatherService {
    
    private String errorMessage = "No weather information found for your request, please check the spelling.\nFor help: /help";
    private WeatherDao weatherDao;

    /**
     * UserDao requests user related information from the database. 
     */
    public UserDao userDao;
    private LocationDao locationDao;
    
    /**
     *
     * @param weatherDao weatherDao stores and retrieves from the database weather information
     * @param userDao userDao stores and retrieves from the database user information
     * @param locationDao locationDao stores and retrieves from the database location information
     */
    public WeatherService(WeatherDao weatherDao, UserDao userDao, LocationDao locationDao) {
        this.weatherDao = weatherDao;
        this.userDao = userDao;
        this.locationDao = locationDao;
    }
    
    /**
     * Returns weather information on a users request
     * @param city the city for which the weather information is requested 
     * @param userID the id of the user who has made the weather request
     * @return returns String with weather information for requested city
     * @throws IOException if openweathermap api can not find the city
     * @throws JSONException if weather information returned from the api can not be parsed
     * @throws SQLException if the object to be added to the database or retrieved from the database is not a weather object 
     */
    public String getWeather(String city, long userID) throws IOException, JSONException, SQLException {
        if (!checkFormat(city)) {
            return errorMessage;
        }
        
        if (!weatherDao.contains(city.toLowerCase())) {
            Weather weather = fetchWeather(city);
            weatherDao.create(weather);
        }
        
        if (checkTime(city)) {
            return weatherWithUnits(city, userID);
        }
        
        Weather updated = fetchWeather(city);
        weatherDao.update(updated);
        return weatherWithUnits(city, userID);
    }
    
    /**
     * Checks that the weather request contains only letters, as it supposed to be in a city name.
     * @param url city name used in the weather request
     * @return returns true if the city name is correct (i.e. contains only letters), otherwise returns false
     */
    public boolean checkFormat(String url) {
        String pattern = "[A-Z-ÖÄÅa-z-öäå]*";
        return url.matches(pattern);
    }
    
    private String readUrl(String request) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + request + "&units=metric&appid=93487a5ce457fe83a42a60b6aa0d7c93");
        Scanner reader = new Scanner((InputStream) url.getContent());
        return reader.nextLine();
        
    }
    
    private Weather parseInfo(String weatherInfo) throws JSONException {
        Weather weather = new Weather();
        JSONObject object = new JSONObject(weatherInfo);
        weather.setCity(object.getString("name"));
        
        JSONObject main = object.getJSONObject("main");
        weather.setTemperature(main.getString("temp"));
        weather.setHumidity(main.getString("humidity"));
        
        JSONArray mainDescription = object.getJSONArray("weather");
        JSONObject mainObject = mainDescription.getJSONObject(0);
        weather.setMain(mainObject.getString("main"));
        weather.setIcon(mainObject.getString("icon"));
        weather.setTime(new Timestamp(System.currentTimeMillis()));
        
        return weather;
    }
    
    /**
     * Returns user unit preferences 
     * @param userID the id of the user whose parameters need to be retrieved
     * @return returns 0 if user has not chosen unit, returns 1 for Celsius and 2 for Fahrenheit
     * @throws SQLException if user with requested id does not exist in the database
     */
    public int unitsChosen(long userID) throws SQLException {
        if (userDao.contains(userID)) {
            if (userDao.read(userID).getUnits() == 1) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }
    
    private String weatherWithUnits(String city, long userID) throws SQLException {
        if (unitsChosen(userID) == 2) {
            return weatherDao.read(weatherDao.cities.get(city.toLowerCase())).toStringFarenheit();
        } else {
            return weatherDao.read(weatherDao.cities.get(city.toLowerCase())).toString();
        }
    }

    /**
     * Sets user's units option
     * @param userID the id of the user whose unit settings need to be changed
     * @param units units chosen (Celsius or Fahrenheit)
     * @throws SQLException if user with requested id does not exist in the database
     */
    public void setUnits(Long userID, int units) throws SQLException {
        if (userDao.contains(userID)) {
            User updatedUser = userDao.read(userID);
            updatedUser.setUnits(units);
            userDao.update(updatedUser);
        } else {
            userDao.create(new User(userID, units));
        }
    }
    
    private Weather fetchWeather(String city) throws JSONException, SQLException, IOException {
        String weatherInfo = readUrl(city);
        Weather weather = parseInfo(weatherInfo);       
        return weather;
    }
    
    private boolean checkTime(String city) throws SQLException {
        Long currentTime = System.currentTimeMillis();
        Long cityAdded = weatherDao.read(weatherDao.cities.get(city.toLowerCase())).getTime().getTime();
        if ((currentTime - cityAdded) > 600000) {
            return false;
        }        
        return true;
    }

    /**
     * Returns all the locations saved by the user 
     * @param userID the id of the user whose locations list need to be fetched
     * @return returns a list of locations saved by the user
     * @throws SQLException if user with requested id does not exist in the database
     */
    public String getLocations(long userID) throws SQLException {
        List<Location> locations = locationDao.listLocations(userID);
        if (locations.isEmpty()) {
            return "No locations found. Please add new location to your locations list by pressing \"add new location\" or search by city name";
        }
        
        return locations.toString().replace("[", "").replace("]", "");
    }

    /**
     * Adds new location to the list of user's locations
     * @param city city to be added to the database
     * @param userID user to whose location list the city is added
     * @return returns error message if city name is formatted wrong or if the city is already on the list,
     * otherwise returns a message that the operation succeeded 
     * @throws SQLException if user with requested id does not exist in the database
     */
    public String addLocation(String city, long userID) throws SQLException {
        if (!checkFormat(city)) {
            return "City not recognized, please check the spelling";
        }
        
        List<Location> locations = locationDao.listLocations(userID);
        if (locations.contains(new Location(city))) {
            return "Location is already added to your locations!";
        }
        
        if (!userDao.contains(userID)) {
            userDao.create(new User(userID));
        }
        
        Location location = new Location(city, userID);
        locationDao.create(location);
        
        return "Location " + city + " has been successfully added to your locations";
    }
    
}
