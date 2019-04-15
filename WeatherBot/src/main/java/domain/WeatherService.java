package domain;

import dao.UserDao;
import dao.WeatherDao;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import org.springframework.stereotype.Component;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class WeatherService {

    private WeatherDao weatherDao;
    private UserDao userDao;
    private final String errorMessage = "No weather information found for your request, please check the spelling.\nFor help: /help";
    private Weather weather;

    @Autowired
    public WeatherService(WeatherDao wd, UserDao ud) {
        this.weatherDao = wd;
        this.userDao = ud;
        this.weather = new Weather();
    }

    public String getWeather(String city, long userID) throws IOException, JSONException, SQLException {
        if (!checkFormat(city)) {
            return errorMessage;
        }

        String weatherInfo = readUrl(city);
        parseInfo(weatherInfo);

        if (userDao.contains(userID)) {
            System.out.println("User founded from HashMap");
            if (userDao.read(userID).getUnits() == 1) {
                System.out.println("Users units equals 1");
                return weather.toString();
            } else {
                return weather.toStringFarenheit();
            }
        }

        // TRIGGER 
        return weather.toString();
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

    private void parseInfo(String weatherInfo) throws JSONException {
        JSONObject object = new JSONObject(weatherInfo);
        weather.setCity(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        weather.setTemperature(main.getString("temp"));
        weather.setHumidity(main.getString("humidity"));

        JSONArray mainDescription = object.getJSONArray("weather");
        JSONObject mainObject = mainDescription.getJSONObject(0);
        weather.setMain(mainObject.getString("main"));
        weather.setIcon(mainObject.getString("icon"));
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
    private void createId(String city) {
        weatherDao.cities.put(city, weatherDao.cities.size() + 1);
        weather.setId(weatherDao.cities.get(city));
    }

    public void setUnits(Long userID, int units) {
        if (userDao.contains(userID)) {
            User updatedUser = userDao.read(userID);
            updatedUser.setUnits(units);
            userDao.update(updatedUser);
        } else {
            userDao.create(new User(userID, units));
        }
    }

    // unsupported functions: 
    /* -- > requires DB for weather storage < --
        if (weatherDao.contains(city)) {
            if (userDao.contains(userID)) {
               if (userDao.read(userID).getUnits() == 1) {
                    return weatherDao.read(weatherDao.cities.get(city)).toString();
                } else {
                    return weatherDao.read(weatherDao.cities.get(city)).toStringFarenheit();
                }
            }
            return weatherDao.read(weatherDao.cities.get(city)).toString();
        }
     */
    // -- > requires DB for weather storage < --       
//        createId(city);
//        weatherDao.create(weather);
}
