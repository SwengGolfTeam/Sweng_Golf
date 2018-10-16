package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeDatabase extends Database {
    private final Map<String, Object> database;
    private final boolean working;

    public FakeDatabase(boolean working) {
        database = new HashMap<>();
        this.working = working;
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
            listener.onComplete(DatabaseError.NONE);
        } else {
            listener.onComplete(DatabaseError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void read(@NonNull String path, @NonNull String id,
                         @NonNull ValueListener<T> listener, @NonNull Class<T> c) {
        if (working) {
            String key = path + "/" + id;
            if (database.containsKey(key)) {
                listener.onDataChange((T) database.get(key));
            } else {
                listener.onDataChange(null);
            }
        } else {
            listener.onCancelled(DatabaseError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c) {
        if (working) {
            List<T> list = getList(path);
            listener.onDataChange(list);
        } else {
            listener.onCancelled(DatabaseError.UNKNOWN_ERROR);
        }
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
}
