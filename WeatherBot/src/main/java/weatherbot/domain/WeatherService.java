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

public class WeatherService {
    
    private String errorMessage = "No weather information found for your request, please check the spelling.\nFor help: /help";
    private WeatherDao weatherDao;
    public UserDao userDao;
    private LocationDao locationDao;
    
    public WeatherService(WeatherDao weatherDao, UserDao userDao, LocationDao locationDao) {
        this.weatherDao = weatherDao;
        this.userDao = userDao;
        this.locationDao = locationDao;
    }
    
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

    /*
    {"coord":{"lon":-0.13,"lat":51.51},
    "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],
    "base":"stations","main":{"temp":282.58,"pressure":1005,"humidity":76,"temp_min":281.48,"temp_max":284.26},
    "visibility":10000,"wind":{"speed":5.1,"deg":50},
    "clouds":{"all":90},"dt":1554561216,
    "sys":{"type":1,"id":1414,"message":0.0074,"country":"GB","sunrise":1554528315,"sunset":1554576026},
    "id":2643743,
    "name":"London",
    "cod":200}
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

    public String getLocations(long userID) throws SQLException {
        List<Location> locations = locationDao.listLocations(userID);
        if (locations.isEmpty()) {
            return "No locations found. Please add new location to your locations list by pressing \"add new location\" or search by city name";
        }
        
        return locations.toString().replace("[", "").replace("]", "");
    }

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
