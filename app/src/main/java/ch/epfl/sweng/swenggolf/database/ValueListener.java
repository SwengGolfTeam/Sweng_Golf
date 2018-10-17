package ch.epfl.sweng.swenggolf.database;

public interface ValueListener<T> {

    /**
     * Return the value found in the database.
     *
     * @param value the value
     */
    void onDataChange(T value);

    /**
     * If there was an error, this method will be called by the database.
     *
     * @param error the error
     */
    void onCancelled(DbError error);
}
