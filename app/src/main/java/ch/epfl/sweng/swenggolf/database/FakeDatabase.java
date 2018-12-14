package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    /**
     * Creates a Database already filled with users and offers.
     *
     * @return an instance of FilledFakeDatabase.
     */
    public static Database fakeDatabaseCreator() {
        return new FilledFakeDatabase();
    }

    @Override
    public void write(@NonNull String path, @NonNull String id, @NonNull Object object) {
        if (working) {
            database.put(path + "/" + id, object);
            notifyListeners(path, id, object);
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

    private void notifyListeners(String path, String id, Object object) {
        path = path + "/" + id;
        String[] listeningToChildren = path.split("/");
        if (!path.equals("/")) {
            notifyChildren(listeningToChildren);
            notifyPathListeners(path, object);
        } else {
            notifyPathListeners("/", getList("/"));
        }
    }

    private void notifyPathListeners(String path, Object object) {
        if (listeners.containsKey(path)) {
            List<ValueListener> objectListeners = listeners.get(path);
            for (ValueListener listener : objectListeners) {
                listener.onDataChange(object);
            }
        }
    }

    private String[] createChildrenPaths(String[] listenningToChildren) {
        String[] paths = new String[listenningToChildren.length - 1];
        StringBuilder currentPath = new StringBuilder("/");
        paths[0] = currentPath.toString();
        //1 since the first slash produces an empty string
        for (int i = 1; i < paths.length; ++i) {
            currentPath.append(listenningToChildren[i]);
            paths[i] = currentPath.toString();
            currentPath.append("/");
        }
        return paths;
    }

    private void notifyChildren(String[] listenningToChildren) {
        listenningToChildren = createChildrenPaths(listenningToChildren);
        for (String level : listenningToChildren) {
            notifyPathListeners(level, getList(level));
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
        if (listeners.containsKey(pathToListen)) {
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
        if (listeners.containsKey(path)) {
            listeners.get(path).remove(listener);
        }
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c) {
        FakeDatabaseListHandler.readList(working, this.<T>getList(path), listener);
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c, AttributeFilter filter) {
        FakeDatabaseListHandler.readList(working, this.<T>getList(path), listener, c, filter);
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c, AttributeOrdering ordering) {
        FakeDatabaseListHandler.readList(working, this.<T>getList(path), listener, c, ordering);
    }

    private boolean isWorkingforEntry(String key) {
        return working && !workingOnEntry.contains(key);
    }


    @Override
    public void remove(@NonNull String path, @NonNull String id,
                       @NonNull CompletionListener listener) {
        if (working) {
            database.remove(path + "/" + id);
            notifyListeners(path, id, null);
            listener.onComplete(DbError.NONE);
        } else {
            listener.onComplete(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public void readOffers(@NonNull ValueListener<List<Offer>> listener,
                           List<Category> categories) {
        FakeDatabaseListHandler.readOffers(working, this.<Offer>getList(OFFERS_PATH),
                listener, categories);
    }

    @Override
    public void readFollowers(@NonNull ValueListener<Map<String, List<String>>> listener) {
        Map<String, List<String>> userFollowing = new HashMap<>();
        for (Map.Entry<String, Object> entry : database.entrySet()) {
            if (entry.getKey().startsWith(FOLLOWERS_PATH)) {
                fillFollowersDirectory(userFollowing, entry);
            }
        }
        FakeDatabaseListHandler.readFollowers(working, listener, userFollowing);
    }

    private void fillFollowersDirectory(Map<String, List<String>> userFollowing,
                                       Map.Entry<String, Object> entry) {
        String s = entry.getKey().substring(1); // to remove first '/' char
        String[] children = s.split("/");
        if (children.length == 2) { // there might be other entries that are not what we want
            if (userFollowing.get(children[1]) == null) {
                userFollowing.put(children[1], new ArrayList<String>());
            }
            userFollowing.get(children[1]).add((String) entry.getValue());
        }
    }


    @Nullable
    private <T> List<T> getList(@NonNull String path) {
        List<T> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : database.entrySet()) {
            if (entry.getKey().startsWith(path + "/")) {
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
