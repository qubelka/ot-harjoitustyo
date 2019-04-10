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

    @Autowired
    WeatherDao weatherDao;

    @Autowired
    UserDao userDao;

    private final String errorMessage = "No weather information found for your request, please check the spelling.\nFor help: /help";
    private Weather weather;
    private HashSet<Long> users;
    private HashMap<String, Integer> cities;

    public WeatherService() {
        this.weather = new Weather();
        this.users = new HashSet<>();
        this.cities = new HashMap<>();
    }

    public String getWeather(String city, long userID) throws IOException, JSONException, SQLException {
        if (!checkFormat(city)) {
            return errorMessage;
        }

        if (this.cities.containsKey(city)) {
            if (this.users.contains(userID)) {
                if (userDao.read((int) userID).getUnits() == 1) {
                    return weatherDao.read(cities.get(city)).toString();
                } else {
                    return weatherDao.read(cities.get(city)).toStringFarenheit();
                }
            }
            return weatherDao.read(cities.get(city)).toString();
        }

        String weatherInfo = readUrl(city);
        parseInfo(weatherInfo);

        createId(city);
        weatherDao.create(weather);

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
        cities.put(city, cities.size() + 1);
        weather.setId(cities.get(city));
    }
    
    public void setUnits(User user, int units) {
        
    }
}
