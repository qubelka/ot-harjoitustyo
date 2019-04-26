package weatherbot.domain;

/**
 * This class is used to create and handle User-objects.
 */
public class User {

    private long id;
    private int units;

    /**
     * Empty constructor that enables Spring work with databases
     */
    public User() {

    }

    /**
     * Constructor used to create a user who has not set unit preferences
     * @param id the id of the user to be created
     */
    public User(long id) {
        this(id, 0);
    }

    /**
     * Constructor used to create a user who has set unit preferences
     * @param id the id of the user to be created
     * @param units the units chosen by the user (1 for Celsius and 2 for Fahrenheit)
     */
    public User(long id, int units) {
        this.id = id;
        this.units = units;
    }

    /**
     * Returns the id that the user has received via telegram api (each user is assigned a unique long value)
     * @return the id of the user 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the user
     * @param id the id that the user has received via telegram api
     */
    public void setId(long id) {
        this.id = id;
    }

    /** 
     * Returns the number representing the units chosen by the user
     * @return returns 0 if the user has not chosen units yet, 1 if the user has chosen Celsius and 2 if Fahrenheit
     */
    public int getUnits() {
        return units;
    }

    /**
     * Sets the units chosen by the use r
     * @param units number (1 or 2) representing Celsius and Fahrenheit 
     */
    public void setUnits(int units) {
        this.units = units;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
