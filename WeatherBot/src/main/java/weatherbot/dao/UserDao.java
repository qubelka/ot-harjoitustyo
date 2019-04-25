package weatherbot.dao;

import weatherbot.domain.User;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao implements Dao<User, Long> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public HashMap<Long, User> users;

    public UserDao() throws SQLException {
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

    
    public void load() throws SQLException {
        List<User> usersInDatabase = list();
        if (usersInDatabase.isEmpty()) {
            return;
        }

        for (User user : usersInDatabase) {
            users.put(user.getId(), user);
        }
    }

    public boolean contains(Long key) {
        return users.containsKey(key);
    }
}
