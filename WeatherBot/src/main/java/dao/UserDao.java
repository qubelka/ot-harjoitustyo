package dao;

import domain.User;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao implements Dao<User, Integer> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void create(User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO User"
                + " (id, units)"
                + " VALUES (?, ?)",
                user.getId(),
                user.getUnits());
    }

    @Override
    public User read(Integer key) throws SQLException {
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM User WHERE id = ?",
                new BeanPropertyRowMapper<>(User.class),
                key);

        return user;
    }

    @Override
    public User update(User user) throws SQLException {
        jdbcTemplate.update("UPDATE User SET units = ? WHERE id = ?",
                user.getUnits(),
                user.getId());

        return user;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM User WHERE id = ?", key);
    }

    @Override
    public List<User> list() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
