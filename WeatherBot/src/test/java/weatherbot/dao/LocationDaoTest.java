package weatherbot.dao;

import java.sql.SQLException;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weatherbot.domain.Location;
import weatherbot.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = weatherbot.domain.BotApp.class)
public class LocationDaoTest {
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    LocationDao locationDao;
    
    User user;
    Location location;
    
    public LocationDaoTest() {
        user = new User(12345678l, 1);
        location = new Location("Helsinki", 12345678l);
    }
    
    @Before
    public void setUp() throws SQLException {
        user = userDao.create(user);
        location = locationDao.create(location);
    }
    
         /**
     * Since Autowired-object does not exist at the moment of construction of a class, it is not possible to 
     * use it for creating a weather in the constructor once before all classes. On the other hand Autowired-object can not be used
     * in static methods, so it is not possible to use it in @BeforeClass method. Since adding weather to the database before each test case 
     * would result in a long list of weather-objects, it is reasonable to use tearDown() to make testing easier. 
     */
    
    @After
    public void tearDown() throws SQLException {
        locationDao.delete(location.getId());
        userDao.delete(user.getId());
    }
    
    @Test
    public void locationExistsAfterCreation() throws SQLException {
        List<Location> createdLocations = locationDao.list();
        assertThat(createdLocations.size(), is(1));
    }

    @Test
    public void existingLocationIsFound() throws SQLException {
        Location created = locationDao.read(location.getId());
        assertThat(created, is(notNullValue()));
    }

    @Test
    public void nonExistingLocationIsNotFound() throws SQLException {
        Location created = locationDao.read(99);
        assertThat(created, is(nullValue()));
    }

    @Test
    public void correctLocationIsReturned() throws SQLException {
        Location newLocation = createLocation();
        Location returnedLocation = locationDao.read(newLocation.getId());
        assertEquals("Praha", returnedLocation.getLocation());
        locationDao.delete(newLocation.getId());
    }

    @Test
    public void locationUpdateWorksCorrectly() throws SQLException {
        location.setLocation("Oslo");
        locationDao.update(location);
        Location updated = locationDao.read(location.getId());
        assertEquals("Oslo", updated.getLocation());
    }

    @Test
    public void locationDoesNotExistAfterDeletion() throws SQLException {
        Location newLocation = createLocation();
        List<Location> createdLocations = locationDao.list();
        assertThat(createdLocations.size(), is(2));
        locationDao.delete(newLocation.getId());
        assertThat(locationDao.read(newLocation.getId()), is(nullValue()));
    }

    @Test
    public void userLocationsAreReturnedCorrectly() throws SQLException {
        Location newLocation = createLocation();
        List<Location> usersLocations = locationDao.listLocations(user.getId());
        assertThat(usersLocations.size(), is(2));
        locationDao.delete(newLocation.getId());
    }
    
    public Location createLocation() throws SQLException {
        Location newLocation = new Location("Praha", user.getId());
        newLocation = locationDao.create(newLocation);
        return newLocation;
    }
    

}
