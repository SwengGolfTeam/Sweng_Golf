package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

/**
 * Mocked Database which works locally.
 */
public class FakeDatabase extends Database {
    private final Map<String, Object> database;
    private final Map<String, List<ValueListener>> listeners;
    private Set<String> workingOnEntry;
    private boolean working;

    /**
     * Create a new FakeDatabase that can be used to mock the Database.
     *
     * @param working the working state of the Database, the DataBase will send
     *                error when working is set at false and will work as
     *                expected otherwise.
     */
    public FakeDatabase(boolean working) {
        this.database = new TreeMap<>();
        this.listeners = new TreeMap<>();
        this.working = working;
        this.workingOnEntry = new HashSet<>();
    }

    private static void handleError(String attribute) {
        throw new IllegalArgumentException("The attribute " + attribute + " doesn't exist");
    }

    /**
     * Creates a Database already filled with users and offers.
     *
     * @return an instance of FilledFakeDatabase.
     */
    public static Database fakeDatabaseCreator() {
        return new FilledFakeDatabase();
    }

    private static String getGetter(String attribute) {
        return "get" + Character.toUpperCase(attribute.charAt(0))
                + attribute.substring(1, attribute.length());
    }

    @NonNull
    private static <T> Comparator<T> getComparator(final Method method) {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                Object attribute1 = invokeGetter(method, o1);
                Object attribute2 = invokeGetter(method, o2);

                return compareAttributes(attribute1, attribute2);
            }
        };
    }

    private static <T> Object invokeGetter(Method method, T invokedOn) {
        try {
            return method.invoke(invokedOn);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't access the attribute");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot call method on generic parameter T");
        }
    }

    private static <T> int compareAttributes(T attribute1, T attribute2) {
        if (attribute1 instanceof Comparable && attribute2 instanceof Comparable) {
            return ((Comparable) attribute1).compareTo(attribute2);
        }
        throw new IllegalArgumentException("The attribute is not comparable");
    }

    @Override
    public void write(@NonNull String path, @NonNull String id, @NonNull Object object) {
        if (working) {
            path = path + "/" + id;
            database.put(path, object);
            if(listeners.containsKey(path)) {
                for (ValueListener listener : listeners.get(path)) {
                    listener.onDataChange(object);
                }
            }
        }
    }

    @Override
    public void write(@NonNull String path, @NonNull String id, @NonNull Object object,
                      @NonNull CompletionListener listener) {
        if (working) {
            write(path, id, object);
            listener.onComplete(DbError.NONE);
        } else {
            listener.onComplete(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void read(@NonNull String path, @NonNull String id,
                         @NonNull ValueListener<T> listener, @NonNull Class<T> c) {
        String key = path + "/" + id;
        if (isWorkingforEntry(key)) {

            if (database.containsKey(key)) {
                listener.onDataChange((T) database.get(key));
            } else {
                listener.onDataChange(null);
            }
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void listen(@NonNull String path, @NonNull String id,
                           @NonNull ValueListener<T> listener, @NonNull Class<T> c) {
        String pathToListen = path + "/" + id;
        if(listeners.containsKey(pathToListen)) {
            listeners.get(pathToListen).add(listener);
        } else {
            List<ValueListener> pathListeners = new ArrayList<>();
            pathListeners.add(listener);
            listeners.put(pathToListen, pathListeners);
        }
        read(path, id, listener, c);
    }

    @Override
    public <T> void deafen(@NonNull String path, @NonNull String id,
                           @NonNull ValueListener<T> listener) {
        path = path + "/" + id;
        if(listeners.containsKey(path)) {
            listeners.get(path).remove(listener);
        }
    }

    private boolean isWorkingforEntry(String key) {
        return working && !workingOnEntry.contains(key);
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c) {
        if (working) {
            List<T> list = getList(path);
            listener.onDataChange(list);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c, AttributeFilter filter) {
        if (working) {

            List<T> list = getList(path);

            List<T> newList = filterList(c, filter.getAttribute(), filter.getValue(), list);
            listener.onDataChange(newList);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c, AttributeOrdering ordering) {
        if (working) {
            List<T> unsortedList = getList(path);
            List<T> list = sortList(c, ordering, unsortedList);
            listener.onDataChange(list);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @NonNull
    private <T> List<T> sortList(@NonNull Class<T> c, AttributeOrdering ordering,
                                 List<T> unsortedList) {
        final Method method;
        String getterName = getGetter(ordering.getAttribute());
        try {
            method = c.getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The getter for the attribute does not exist");
        }
        Comparator<T> comparator = getComparator(method);
        Collections.sort(unsortedList, comparator);
        if (ordering.isDescending()) {
            Collections.reverse(unsortedList);
        }
        int minSize = Math.min(ordering.getNumberOfElements(), unsortedList.size());
        return unsortedList.subList(0, minSize);
    }

    private <T> List<T> filterList(@NonNull Class<T> c, String attribute, String value,
                                   List<T> list) {
        List<T> filtered = new ArrayList<>();
        try {

            //Use reflection to check the attribute
            Method method = c.getDeclaredMethod(getGetter(attribute));
            filtered = createFilteredList(method, list, value);

        } catch (IllegalAccessException e) {
            handleError(attribute);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No getter for " + attribute + " attribute");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Generic type T has no getter for this attribute");
        }
        return filtered;
    }

    private <T> List<T> createFilteredList(Method getter, List<T> list, String value)
            throws InvocationTargetException, IllegalAccessException {
        List<T> newList = new ArrayList<>();
        for (T object : list) {
            if (getter.invoke(object).equals(value)) {
                newList.add(object);
            }
        }
        return newList;
    }

    @Override
    public void remove(@NonNull String path, @NonNull String id,
                       @NonNull CompletionListener listener) {
        if (working) {
            path = path + "/" + id;
            database.remove(path);
            if(listeners.containsKey(path)) {
                for(ValueListener pathListener : listeners.get(path)) {
                    pathListener.onDataChange(null);
                }
            }
            listener.onComplete(DbError.NONE);
        } else {
            listener.onComplete(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public void readOffers(@NonNull final ValueListener<List<Offer>> listener,
                           final List<Category> categories) {
        List<Offer> offers = getList(Database.OFFERS_PATH);

        if (working) {
            offers = removeOffersWrongCategories(offers, categories);
            listener.onDataChange(offers);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    private List<Offer> removeOffersWrongCategories(List<Offer> offers, List<Category> categories) {
        List<Offer> list = new ArrayList<>();
        for (Offer o : offers) {
            if (categories.contains(o.getTag())) {
                list.add(o);
            }
        }
        return list;
    }

    @Nullable
    private <T> List<T> getList(@NonNull String path) {
        List<T> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : database.entrySet()) {
            if (entry.getKey().startsWith(path)) {
                list.add((T) entry.getValue());
            }
        }
        return list;
    }

    /**
     * Set working state of the Database.
     *
     * @param w the working state of the Database, the DataBase will send
     *          error when working is set at false and will work as
     *          expected otherwise.
     */
    public void setWorking(boolean w) {
        working = w;
    }


    /**
     * Allow to disable a specific entry for reading. When reading this entry, there will be an
     * error.
     *
     * @param path the path of the value
     * @param id   the id of the value
     */
    public void setEntryNotWorking(String path, String id) {
        workingOnEntry.add(path + "/" + id);
    }

}
