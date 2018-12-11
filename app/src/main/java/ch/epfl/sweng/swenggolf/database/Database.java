package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

/**
 * Database used to load and save Objects.
 */
public abstract class Database {

    public static final String OFFERS_PATH = "/offers";
    public static final String USERS_PATH = "/users";
    public static final String FOLLOWERS_PATH = "/followers";
    public static final String ANSWERS_PATH = "/answers";
    public static final String NOTIFICATION_PATH = "/notifications";
    public static final String MESSAGES_PATH = "/messages";
    public static final String OFFERS_SAVED_PATH = "/offersSaved";

    private static Database database = null;

    protected Database() {
    }

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
     * Reads a single value once in a given path with a given id.
     * It returns the value using a listener.
     *
     * @param path     the path where we want to read the value
     * @param id       the id of the value
     * @param listener the onDataChange method will be called if there is a corresponding
     *                 value. Otherwise, the onCancelled method will be called
     * @param c        the class of the value
     */
    public abstract <T> void read(@NonNull String path, @NonNull String id,
                                  @NonNull ValueListener<T> listener, @NonNull Class<T> c);

    /**
     * Perform changes every time an update is done in the database.
     * It returns the new value using a listener.
     *
     * @param path     where to read the value.
     * @param id       the name of the value field.
     * @param listener the onDataChange method will be called at every change of the value if it
     *                 exists and onCancelled will be called otherwise.
     * @param c        the class of the value
     * @param <T>      the type of the value
     */
    public abstract <T> void listen(@NonNull String path, @NonNull String id,
                                    @NonNull ValueListener<T> listener, @NonNull Class<T> c);

    /**
     * Removes a listener from a reference.
     *
     * @param listener the listener to be removed.
     * @param <T>      the type of the value.
     */
    public abstract <T> void deafen(@NonNull String path, @NonNull String id,
                                    @NonNull ValueListener<T> listener);

    /**
     * Read a list of value in a given path. It return the list using a listener.
     *
     * @param path     the path where we want to read the list
     * @param listener the onDataChange method will be called if there is a corresponding
     *                 list. Otherwise, the onCancelled method will be called
     * @param c        the class of the values
     */
    public abstract <T> void readList(@NonNull String path,
                                      @NonNull ValueListener<List<T>> listener,
                                      @NonNull Class<T> c);

    /**
     * Read a list of value in a given path. It return the list using a listener. It only return
     * values with an attribute equals to the value given in the filter.
     *
     * @param path     the path where we want to read the list
     * @param listener the onDataChange method will be called if there is a corresponding
     *                 list. Otherwise, the onCancelled method will be called
     * @param c        the class of the values
     * @param filter   must contains the attribute to filter on and the value to filter.
     */
    public abstract <T> void readList(@NonNull String path,
                                      @NonNull ValueListener<List<T>> listener,
                                      @NonNull Class<T> c, AttributeFilter filter);

    /**
     * Read a list of value in a given path. It return the list using a listener. It only return
     * values in the order specified in ordering. It also take only the first n values specified
     * in the ordering.
     *
     * @param path     the path where we want to read the list
     * @param listener the onDataChange method will be called if if there is a corresponding
     *                 list. Otherwise, the onCancelled method will be called
     * @param c        the class of the values
     * @param ordering the ordering which give information about how we order the data
     */
    public abstract <T> void readList(@NonNull String path,
                                      @NonNull ValueListener<List<T>> listener,
                                      @NonNull Class<T> c, AttributeOrdering ordering);

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
     * @param listener   the onDataChange method will be called if there is a corresponding
     *                   list. Otherwise, the onCancelled method will be called
     * @param categories the list of categories that we want
     */
    public abstract void readOffers(@NonNull final ValueListener<List<Offer>> listener,
                                    final List<Category> categories);

    /**
     * Read the list of all offers. It return the list using a listener.
     *
     * @param listener the onDataChange method will be called if there is a corresponding
     *                 list. Otherwise, the onCancelled method will be called
     */
    public void readOffers(@NonNull ValueListener<List<Offer>> listener) {
        readOffers(listener, Arrays.asList(Category.values()));
    }

    /**
     * Read the list of all offers of an user in the given categories. It return the list using a
     * listener.
     *
     * @param listener     the onDataChange method will be called if there is a corresponding
     *                     list. Otherwise, the onCancelled method will be called
     * @param categories   the list of categories that we want
     * @param offerCreator the creator of the use
     */
    public void readOffers(@NonNull final ValueListener<List<Offer>> listener,
                           @NonNull final List<Category> categories, @NonNull String offerCreator) {
        ValueListener<List<Offer>> listenerFilterCategories = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> value) {
                List<Offer> list = filterOffers(value, categories);
                listener.onDataChange(list);
            }

            @Override
            public void onCancelled(DbError error) {
                listener.onCancelled(error);
            }
        };
        readList(OFFERS_PATH, listenerFilterCategories, Offer.class,
                new AttributeFilter("userId", offerCreator));

    }


    // TODO add meaningful javadoc
    public abstract void readFollowers(@NonNull ValueListener<Map<String, List<String>>> listener);

    @NonNull
    private List<Offer> filterOffers(List<Offer> value, @NonNull List<Category> categories) {
        List<Offer> list = new ArrayList<>();
        //Use set for efficiency of comparison
        Set<Category> s = new HashSet<>();
        s.addAll(categories);
        for (Offer offer : value) {
            if (s.contains(offer.getTag())) {
                list.add(offer);
            }
        }
        return list;
    }
}
