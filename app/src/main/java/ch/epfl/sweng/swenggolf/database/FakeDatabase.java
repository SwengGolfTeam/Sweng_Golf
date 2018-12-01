package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Mocked Database which works locally.
 */
public class FakeDatabase extends DatabaseListHandler {
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
        notifyChildren(listeningToChildren);
        notifyPathListeners(path, object);
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
        String[] paths = new String[listenningToChildren.length];
        StringBuilder currentPath = new StringBuilder();
        for (int i = 0; i < paths.length; ++i) {
            paths[i] = currentPath.toString();
            currentPath.append("/").append(listenningToChildren[i]);
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


    @Nullable
    protected <T> List<T> getList(@NonNull String path) {
        List<T> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : database.entrySet()) {
            if (entry.getKey().startsWith(path)) {
                list.add((T) entry.getValue());
            }
        }
        return list;
    }

    /**
     * The working state of the Database.
     *
     * @return the working state of the Database, the DataBase will send
     *         error when working is set at false and will work as
     *         expected otherwise.
     */
    protected boolean isWorking() {
        return working;
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
