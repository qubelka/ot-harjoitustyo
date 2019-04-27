package weatherbot.dao;

import weatherbot.domain.User;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * This class is used to store and retrieve information about user locations
 * using database. UserDao implements Dao interface and represents User entity.
 *
 * @see weatherbot.dao.Dao
 */
@Component
public class UserDao implements Dao<User, Long> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Keeps track of the users added to the database. Prevents from making
     * unnecessary database searches.
     */
    public HashMap<Long, User> users;

    /**
     * Creates an empty HashMap-structure when the class is constructed.
     */
    public UserDao() {
        users = new HashMap<>();
    }

    @Override
    public User create(User user) throws SQLException {
        users.put(user.getId(), user);

        jdbcTemplate.update("INSERT INTO User"
                + " (id, units)"
                + " VALUES (?, ?)",
                user.getId(),
                user.getUnits());

        return user;
    }

    @Override
    public User read(Long key) throws SQLException {
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM User WHERE id = ?",
                new BeanPropertyRowMapper<>(User.class),
                key);

        return user;
    }

    @Override
    public User update(User user) throws SQLException {
        users.put(user.getId(), user);

        jdbcTemplate.update("UPDATE User SET units = ? WHERE id = ?",
                user.getUnits(),
                user.getId());

        return user;
    }

    @Override
    public void delete(Long key) throws SQLException {
        users.remove(key);
        jdbcTemplate.update("DELETE FROM User WHERE id = ?", key);
    }

    @Override
    public List<User> list() throws SQLException {
        return jdbcTemplate.query("SELECT * FROM User", new BeanPropertyRowMapper<>(User.class));
    }

    /**
     * Loads users and their preferences from the database when the class is
     * created
     *
     * @throws SQLException if database is not initialised
     */
    public void load() throws SQLException {
        List<User> usersInDatabase = list();
        if (usersInDatabase.isEmpty()) {
            return;
        }

        for (User user : usersInDatabase) {
            users.put(user.getId(), user);
        }
    }

    /**
     * Checks whether a user with certain id is already added to the database.
     *
     * @param key user id
     * @return returns true if user has been added to the database, otherwise
     * returns false
     */
    public boolean contains(Long key) {
        return users.containsKey(key);
    }

    @Override
    public void clear() throws SQLException {
        jdbcTemplate.update("DELETE FROM User");
        users.clear();
    }
}
