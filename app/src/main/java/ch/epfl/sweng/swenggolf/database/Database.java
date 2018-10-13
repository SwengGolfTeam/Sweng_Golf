package ch.epfl.sweng.swenggolf.database;


import java.util.List;

public abstract class Database {

    /**
     * Write an object into the database.
     * @param path the path where to write the object
     * @param id the id of the object
     * @param object the object to write
     */
    abstract void write(String path, String id, Object object);

    /**
     * Write an object into the database. Use the listener to check if the write could be executed.
     * @param path the path where to write the object
     * @param id the id of the object
     * @param object the object to write
     * @param listener the onComplete method of the listener will be called with true if it was a
     *                 success or false otherwise.
     */
    abstract void write(String path, String id, Object object, CompletionListener listener);

    /**
     * Read a single value in a given path with a given id. It return the value using a listener.
     * @param path the path where we want to read the value
     * @param id the id of the value
     * @param listener the onDataChange method will be called if we find the value. Otherwise, the
     *                 onCancelled method will be called
     * @param c the class of the value
     */
    abstract <T> void read(String path, String id, ValueListener<T> listener, Class<T> c);

    /**
     * Read a list of value in a given path. It return the value using a listener.
     * @param path the path where we want to read the value
     * @param listener the onDataChange method will be called if we find the value. Otherwise, the
     *                 onCancelled method will be called
     * @param c the class of the value
     */
    abstract <T> void readList(String path, ValueListener<List<T>> listener, Class<T> c);

}
