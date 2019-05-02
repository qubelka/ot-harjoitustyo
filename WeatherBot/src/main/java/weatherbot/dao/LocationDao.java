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

/**
 * This class is used to store and retrieve information about user locations
 * using database. LocationDao implements Dao interface and represents Location
 * entity.
 *
 * @see weatherbot.dao.Dao
 */
@Component
public class LocationDao implements Dao<Location, Integer> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Creates Location-object in the database and assigns automatically
     * generated id to each location.
     *
     * @param location location to be added to the database
     * @return returns location
     * @throws SQLException if object to be added does not represent the
     * Location entity
     */
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
     * @param key automatically generated id which each location receives after
     * it has been added to the database
     * @see weatherbot.domain.Location
     * @return returns location, i.e. city and possible user id, or null if
     * there is no such key in the database
     * @throws SQLException if object is not found by id
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

    /**
     * Updates location, i.e. city name and userId associated with this
     * location.
     *
     * @param location location to be updated
     * @return returns location
     * @throws SQLException if object is not found by id
     */
    @Override
    public Location update(Location location) throws SQLException {
        jdbcTemplate.update("UPDATE Locations SET location = ?, user_id = ? WHERE id = ?",
                location.getLocation(),
                location.getUserId(),
                location.getId());

        return location;
    }

    /**
     * Removes location from the database using a location id. 
     * @param key automatically generated id which each location receives after
     * it has been added to the database
     * @throws SQLException if object is not found by id
     */
    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM Locations WHERE id = ?", key);
    }

    /**
     * Returns a list with all the locations represented in the database.
     * @return returns a list of all the locations added to the database
     * @throws SQLException if the table Locations is not found
     */
    @Override
    public List<Location> list() throws SQLException {
        return jdbcTemplate.query("SELECT * FROM Locations", new BeanPropertyRowMapper<>(Location.class));
    }

    /**
     * Returns a list of locations saved by user.
     *
     * @param key automatically generated id which each location receives after
     * it has been added to the database
     * @return list of user specific locations
     * @throws SQLException if the table Locations or the user id is not found
     * or the database is not initialised
     */
    public List<Location> listLocations(Long key) throws SQLException {
        String sqlQuery = "SELECT * FROM Locations WHERE user_id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Location.class), key);
    }

    @Override
    public void clear() throws SQLException {
        jdbcTemplate.update("DELETE FROM Locations");
    }
}
