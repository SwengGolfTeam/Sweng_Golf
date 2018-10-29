package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

public abstract class Database {

    private static Database database = null;

    /**
     * Use the given database as the return of the getInstance() method.
     *
     * @param d the fake database
     */
    public static void setDebugDatabase(Database d) {
        database = d;
    }

    /**
     * Return the singleton instance of the Database. Return a fake database if given.
     *
     * @return the database
     */
    public static Database getInstance() {
        if (database == null) {
            database = new FireDatabase();
        }
        return database;
    }

    protected Database() {
    }

    /**
     * Write an object into the database.
     *
     * @param path   the path where to write the object
     * @param id     the id of the object
     * @param object the object to write
     */
    public abstract void write(@NonNull String path, @NonNull String id, @NonNull Object object);

    /**
     * Write an object into the database. Use the listener to check if the write could be executed.
     *
     * @param path     the path where to write the object
     * @param id       the id of the object
     * @param object   the object to write
     * @param listener the onComplete method of the listener will be called with DbError.NONE
     *                 if it was a success or an error otherwise.
     */
    public abstract void write(@NonNull String path, @NonNull String id, @NonNull Object object,
                               @NonNull CompletionListener listener);

    /**
     * Read a single value in a given path with a given id. It return the value using a listener.
     *
     * @param path     the path where we want to read the value
     * @param id       the id of the value
     * @param listener the onDataChange method will be called if we find the value. Otherwise, the
     *                 onCancelled method will be called
     * @param c        the class of the value
     */
    public abstract <T> void read(@NonNull String path, @NonNull String id,
                                  @NonNull ValueListener<T> listener, @NonNull Class<T> c);

    /**
     * Read a list of value in a given path. It return the value using a listener.
     *
     * @param path     the path where we want to read the value
     * @param listener the onDataChange method will be called if we find the value. Otherwise, the
     *                 onCancelled method will be called
     * @param c        the class of the value
     */
    public abstract <T> void readList(@NonNull String path,
                                      @NonNull ValueListener<List<T>> listener,
                                      @NonNull Class<T> c);

    /**
     * Remove the value in path with the given id.
     *
     * @param path     the path where is the value we want to remove
     * @param id       the id of the value we want to remove
     * @param listener the onComplete method of the listener will be called with DbError.NONE
     *                 if it was a success or an error otherwise.
     */
    public abstract void remove(@NonNull String path, @NonNull String id,
                                @NonNull CompletionListener listener);

    /**
     * Read the list of offers of specific categories. It return the list using a listener.
     *
     * @param listener   the onDataChange method will be called if we find the value. Otherwise, the
     *                   onCancelled method will be called
     * @param categories the list of categories that we want
     */
    public abstract void readOffers(@NonNull final ValueListener<List<Offer>> listener,
                                    final List<Category> categories);

    /**
     * Read the list of all offers. It return the list using a listener.
     *
     * @param listener the onDataChange method will be called if we find the value. Otherwise, the
     *                 onCancelled method will be called
     */

    public void readOffers(@NonNull ValueListener<List<Offer>> listener) {
        readOffers(listener, Arrays.asList(Category.values()));
    }
}
