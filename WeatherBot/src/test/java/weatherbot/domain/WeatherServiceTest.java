package weatherbot.domain;

import weatherbot.dao.UserDao;
import weatherbot.dao.WeatherDao;
import weatherbot.domain.WeatherService;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weatherbot.dao.LocationDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = weatherbot.domain.BotApp.class)
public class WeatherServiceTest {

    @Autowired
    UserDao userDao;
    
    @Autowired
    WeatherDao weatherDao;
    
    @Autowired
    LocationDao locationDao;

    User user;
    WeatherService weatherService;
    
    public WeatherServiceTest() {
        user = new User(12345678l, 2);       
    }
    
    @Before
    public void setUp() throws SQLException { 
        userDao.create(user);
        weatherService = new WeatherService(weatherDao, userDao, locationDao);      
    }
        
    @After
    public void tearDown() throws SQLException {
        userDao.delete(user.getId());
    }
    
    @Test
    public void createdWeatherServiceNotNull() {
        assertThat(weatherService, is(notNullValue()));
    }

    @Test
    public void returnsErrorMessageOnWrongFormattedInput() throws IOException, JSONException, SQLException {
        String errorMessage = "No weather information found for your request, please check the spelling.\nFor help: /help";
        assertThat(weatherService.getWeather("H3lsinki", 12345678l), is(errorMessage));
    }
    
    @Test
    public void returnsCelsiusStringByDefault() throws IOException, JSONException, SQLException {
        User newUser = new User(99999999l, 1);
        userDao.create(newUser);
        String weatherInformation = weatherService.getWeather("Athens", 99999999l);
        assertTrue(weatherInformation.contains("°C"));
        userDao.delete(user.getId());
    }
    
    @Test
    public void returnsFahrenheitIfUserHasPreferences() throws IOException, JSONException, SQLException {
        String weatherInformation = weatherService.getWeather("Helsinki", 12345678l);
        assertTrue(weatherInformation.contains("°F"));
    }
    
    @Test
    public void updatesWeatherIfTenMinutesExpired() throws SQLException, IOException, JSONException {
        Weather weather = createWeather();
        weatherService.getWeather("Oslo", user.getId());
        assertThat(weather, is(not(weatherDao.read(1))));
    }
    
    @Test
    public void createsNewLocationIfDoesntExistYet() throws SQLException {     
        String reply = "Location Helsinki has been successfully added to your locations";
        assertThat(weatherService.addLocation("Helsinki", 12345678l), is(reply));
        locationDao.delete(1);
    }
    
    @Test
    public void unitsAreSetCorrectlyForExistingUser() throws SQLException {
        weatherService.setUnits(12345678l, 1);
        assertThat(userDao.read(user.getId()).getUnits(), is(1));
    }
    
    @Test
    public void unitsAreSetCorrectlyForNewUser() throws SQLException {        
        weatherService.setUnits(99999999l, 2);
        assertThat(userDao.read(99999999l).getUnits(), is(2));
    }
    
    @Test
    public void unitsCheckReturnsZeroIfUserDoesNotExist() throws SQLException {
       assertThat(weatherService.unitsChosen(77777777l), is(0));
    }
    
    @Test
    public void setUnitsCreatesNewUserIfNotOnTheList() throws SQLException {
         weatherService.setUnits(77777777l, 1);
         assertTrue(userDao.contains(77777777l));
         userDao.delete(77777777l);
    }
    
    @Test
    public void recognizesIfUserHasNotSavedAnyLocations() throws SQLException {
        String reply = "No locations found. Please add new location to your locations list by pressing \"add new location\" or search by city name";
        assertThat(weatherService.getLocations(12345678l), is(reply));
    }
    
    @Test
    public void returnsErrorWhenUserTriesToAddWrongFormattedLocation() throws SQLException {
        String errorMessage = "City not recognized, please check the spelling";
        assertThat(weatherService.addLocation("H3lsinki", user.getId()), is(errorMessage));
    }
    
    @Test
    public void recognizesIfUserTriesToAddExistingLocation() throws SQLException {
        String errorMessage = "Location is already added to your locations!";
        Location location = new Location("Berlin", 12345678l);
        location = locationDao.create(location);
        assertThat(weatherService.addLocation("Berlin", user.getId()), is(errorMessage));
        locationDao.delete(location.getId());
    }
    
    @Test
    public void returnsUsersLocationsCorrectly() throws SQLException {
        Location location = new Location("Berlin", 12345678l);
        location = locationDao.create(location);
        assertFalse(weatherService.getLocations(12345678l).isEmpty());
        locationDao.delete(location.getId());
    }

    private Weather createWeather() throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()-700000);
        Weather weather = new Weather("Oslo", "2.0", "50", "Clouds", "04d", timestamp);
        weather = weatherDao.create(weather);
        return weather;
    }
}
