package weatherbot.dao;

import java.util.*;
import java.sql.*;

/**
 * Interface for data storage in the database. Provides methods for creating, reading, updating, 
 * deleting and listing objects. 
 * @param <T> represents the object being handled
 * @param <K> represents key value for database operations
 */
public interface Dao<T, K> {

    /**
     * Creates an object in the database and returns its updated value (in case the id is generated automatically)
     * @param object object to be added to the database
     * @return returns the object added to the database
     * @throws SQLException if object to be added does not represent the T entity
     */
    T create(T object) throws SQLException;

    /**
     * Fetches an object from the database using the key
     * @param key long or integer value representing object id
     * @return returns object found from the database by the key
     * @throws SQLException if object is not found by id
     */
    T read(K key) throws SQLException;

    /**
     * Updates the database value of the existing object 
     * @param object object to be updated in the database
     * @return returns an updated object value 
     * @throws SQLException if object is not found by id
     */
    T update(T object) throws SQLException;

    /**
     * Deletes the existing object from the database
     * @param key long or integer value representing object id
     * @throws SQLException if object is not found by id
     */
    void delete(K key) throws SQLException;

    /**
     * Lists all the objects found from the current database table
     * @return list of objects added to the current database table
     * @throws SQLException if the table representing T objects is not found or the database is not initialised
     */
    List<T> list() throws SQLException;

}
