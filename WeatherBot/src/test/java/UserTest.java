
import domain.User;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserTest {

    @Test
    public void canIdentifySameUserByID() {
        User user1 = new User(99999999l);
        User user2 = new User(99999999l);

        assertThat(user1, is(user2));
    }
    
    @Test
    public void canIdentifyDifferentUsersByID() {
        User user1 = new User(99999999l);
        User user2 = new User(88888888l);
        
        assertThat(user1, is(not(user2)));
    }

}
