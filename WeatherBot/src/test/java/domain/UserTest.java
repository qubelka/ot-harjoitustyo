package domain;


import domain.User;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;

public class UserTest {
    User user;
    
    @Before
    public void setUp() {
        user = new User(99999999l);
    }
    
    @Test
    public void userExistsAfterCreating() {
        assertThat(user, is(notNullValue()));
    }
    
    @Test
    public void idIsRight() {
        assertThat(user.getId(), is(99999999l));
    }
    
    @Test
    public void idCanBeChanged() {
        user.setId(12345678l);
        assertThat(user.getId(), is(12345678l));
    }
    
    @Test
    public void thereIsNoUnitsInfoBeforeSettingUp() {
        assertThat(user.getUnits(), is(0));
    }
    
    @Test
    public void unitsCanBeChanged() {
        user.setUnits(1);
        assertThat(user.getUnits(), is(1));
    }
    
    @Test
    public void canIdentifyNullUser() {
        User empty = null;
        assertThat(user, is(not(empty)));
    }
    
    @Test
    public void canIdentifySameUserByID() {
        User user2 = new User(99999999l);
        assertThat(user, is(user2));
    }
    
    @Test
    public void canIdentifyDifferentUsersByID() {
        User user2 = new User(88888888l);       
        assertThat(user, is(not(user2)));
    }
    
}
