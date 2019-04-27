package weatherbot.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weatherbot.domain.Weather;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = weatherbot.domain.BotApp.class)
public class WeatherDaoTest {

    @Autowired
    WeatherDao weatherDao;

    Weather weather;
    Timestamp timestamp;

    public WeatherDaoTest() {
        timestamp = new Timestamp(System.currentTimeMillis());
        weather = new Weather("Oslo", "2.0", "50", "Clouds", "04d", timestamp);
    }

    @Before
    public void setUp() throws SQLException {
        weather = weatherDao.create(weather);
    }

         /**
     * Since Autowired-object does not exist at the moment of construction of a class, it is not possible to 
     * use it for creating a weather in the constructor once before all classes. On the other hand Autowired-object can not be used
     * in static methods, so it is not possible to use it in @BeforeClass method. Since adding weather to the database before each test case 
     * would result in a long list of weather-objects, it is reasonable to use tearDown() to make testing easier. 
     */
    
    @After
    public void tearDown() throws SQLException {
        weatherDao.clear();
    }

    @Test
    public void weatherExistsAfterCreation() throws SQLException {
        List<Weather> createdWeatherConditions = weatherDao.list();
        assertThat(createdWeatherConditions.size(), is(1));
        assertTrue(weatherDao.contains(weather.getCity().toLowerCase()));
    }

    @Test
    public void existingWeatherIsFound() throws SQLException {
        Weather created = weatherDao.read(weather.getId());
        assertThat(created, is(notNullValue()));
    }

    @Test
    public void nonExistingWeatherIsNotFound() {
        boolean found = weatherDao.contains("Barcelona");
        assertFalse(found);
    }

    @Test
    public void correctWeatherIsReturned() throws SQLException {
        Weather newWeather = createWeatherCondition(); 

        Weather returnedWeather = weatherDao.read(newWeather.getId());
        assertEquals("50d", newWeather.getIcon());
        assertEquals("Boston", newWeather.getCity());
    }

    @Test
    public void weatherUpdateWorksCorrectly() throws SQLException {
        weather.setTemperature("8.0");
        weather.setMain("Clear sky");
        weather.setIcon("01d");

        weatherDao.update(weather);
        Weather updated = weatherDao.read(weather.getId());

        assertEquals("8.0", updated.getTemperature());
        assertEquals("01d", updated.getIcon());
    }

    @Test
    public void weatherDoesNotExistAfterDeletion() throws SQLException {
        Weather newWeather = createWeatherCondition(); 
        List<Weather> weatherObjektit = weatherDao.list();
        assertThat(weatherObjektit.size(), is(2));
        
        weatherDao.delete(newWeather.getId());
        weatherObjektit = weatherDao.list();
        assertThat(weatherObjektit.size(), is(1));
        assertFalse(weatherDao.contains(newWeather.getCity().toLowerCase()));
    }

    public Weather createWeatherCondition() throws SQLException {
        Weather newWeather = new Weather("Boston", "15.0", "40", "Mist", "50d", timestamp);
        newWeather = weatherDao.create(newWeather);
        return newWeather;
    }

}
