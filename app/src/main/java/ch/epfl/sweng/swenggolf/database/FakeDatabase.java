package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

public class FakeDatabase extends Database {
    private final Map<String, Object> database;
    Set<String> workingOnEntry;
    private boolean working;

    /**
     * Create a new FakeDatabase that can be used to mock the Database.
     *
     * @param working if the database work or send errors
     */
    public FakeDatabase(boolean working) {
        this.database = new TreeMap<>();
        this.working = working;
        workingOnEntry = new HashSet<>();
    }

    @Override
    public void write(@NonNull String path, @NonNull String id, @NonNull Object object) {
        if (working) {
            database.put(path + "/" + id, object);
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
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener, @NonNull Class<T> c, String attribute, String value) {
        if (working) {

            List<T> list = getList(path);
            try {
                //Use reflection to check the attribute
                Field field = c.getDeclaredField(attribute);
                field.setAccessible(true);
                List<T> newList = new ArrayList<>();
                for (T object : list) {
                    if (!field.get(object).equals(value)) {
                        newList.add(object);
                    }
                }
                field.setAccessible(false);
                listener.onDataChange(newList);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("The attribute " + attribute + " doesn't exist");
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("The attribute " + attribute + " doesn't exist");
            }
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public void remove(@NonNull String path, @NonNull String id,
                       @NonNull CompletionListener listener) {
        if (working) {
            database.remove(path + "/" + id);
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
        return list.isEmpty() ? null : list;
    }

    /**
     * Creates a database already filled with users and offers.
     *
     * @return an instance of FilledFakeDatabase.
     */
    public static Database fakeDatabaseCreator() {
        return new FilledFakeDatabase();
    }

    /**
     * Set the database to a "non-working" mode if false.
     *
     * @param w working
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
