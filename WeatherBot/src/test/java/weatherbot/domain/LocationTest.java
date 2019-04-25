package weatherbot.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class LocationTest {
    Location location;
    
    @Before
    public void setUp() {
        location = new Location(1, "Helsinki", 99999999l);
    }
    
    @Test
    public void locationExistsAfterCreating() {
        assertThat(location, is(notNullValue()));
    }
    
    @Test
    public void idIsRight() {
        assertThat(location.getId(), is(1));
    }
    
    @Test
    public void idCanBeChanged() {
        location.setId(2);
        assertThat(location.getId(), is(2));
    }
    
    @Test
    public void idCanBeAssignedAfterConstruction() {
        Location newLocation = new Location("Oslo", 12345678l);
        newLocation.setId(3);
        assertThat(newLocation.getId(), is(3));
    }
    
    @Test
    public void userIdIsRight() {
        assertThat(location.getUserId(), is(99999999l));
    }
    
    @Test
    public void cityNameIsRight() {
        assertThat(location.getLocation(), is("Helsinki"));
    }
    
    @Test
    public void canIdentifyNullLocation() {
        Location empty = null;
        assertThat(location, is(not(empty)));
    }
    
    @Test
    public void canIdentifySameLocationByCityName() {
        Location newLocation = new Location("Helsinki", 12345678l);
        assertThat(newLocation, is(location));
    }
    
    @Test
    public void canIdentifyDifferentLocationsByCityName() {
        Location newLocation = new Location("Berlin", 12345678l);      
        assertThat(newLocation, is(not(location)));
    }
}
