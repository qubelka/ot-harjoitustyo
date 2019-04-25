package weatherbot.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import weatherbot.domain.Location;

@Component
public class LocationDao implements Dao<Location, Integer> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Location create(Location location) throws SQLException {
        String sql = "INSERT INTO Locations (location, user_id) "
                + "VALUES (?, ?)";
        int userId = (int) location.getUserId();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, location.getLocation());
            stmt.setInt(2, userId);
            return stmt;
        }, keyHolder);

        Integer id = (int) keyHolder.getKey();
        location.setId(id);

        return location;
    }

    /**
     * Returns Location-object found from the database based on the provided
     * location id. For test purposes location object is first assigned null
     * value. Thus the cases when object is not found will not end in exception.
     *
     * @param key Automatically generated number which each location receives
     * after it is added to the database
     * @throws java.sql.SQLException if object is not found
     * @see weatherbot.domain.Location
     * @return returns location, i.e. city and possible user id, or null if
     * there is no such key in the database
     */
    @Override
    public Location read(Integer key) throws SQLException {
        Location location = null;
        try {
            location = jdbcTemplate.queryForObject(
                    "SELECT * FROM Locations WHERE id = ?",
                    new BeanPropertyRowMapper<>(Location.class),
                    key);
        } catch (DataAccessException e) {
            return location;
        }

        return location;
    }

    @Override
    public Location update(Location location) throws SQLException {
        jdbcTemplate.update("UPDATE Locations SET location = ?, user_id = ? WHERE id = ?",
                location.getLocation(),
                location.getUserId(),
                location.getId());

        return location;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM Locations WHERE id = ?", key);
    }

    @Override
    public List<Location> list() throws SQLException {
        return jdbcTemplate.query("SELECT * FROM Locations", new BeanPropertyRowMapper<>(Location.class));
    }

    public List<Location> listLocations(Long key) throws SQLException {
        String sqlQuery = "SELECT * FROM Locations WHERE user_id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Location.class), key);
    }
}
