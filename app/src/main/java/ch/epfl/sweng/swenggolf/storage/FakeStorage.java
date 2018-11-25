package ch.epfl.sweng.swenggolf.storage;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executor;

/**
 * Class which represents a mocked Storage.
 */
public final class FakeStorage extends Storage {

    private final Map<String, Object> storage;
    private final boolean working;

    /**
     * Construct a mock Storage.
     *
     * @param working the working state, the Storage will send
     *                error when working is set at false and will work as
     *                expected otherwise.
     */
    public FakeStorage(boolean working) {
        this.storage = new TreeMap<>();
        this.working = working;
    }

    @Override
    public void write(Uri uri, String path, OnCompleteListener<Uri> listener) {
        Task<Uri> fakeTask = new FakeTask(uri, working);
        if (fakeTask.isComplete() && !fakeTask.isCanceled()) {
            listener.onComplete(fakeTask);
        }
    }

    @Override
    public void remove(String linkPicture) {
        // TODO implement when necessary
    }

    /**
     * Class which represents a fake Task.
     */
    public static final class FakeTask extends Task<Uri> {

        private final Uri uri;
        private final boolean working;

        /**
         * Create a fake Task.
         *
         * @param uri     the uri
         * @param working the working state, the Storage will send
         *                error when working is set at false and will work as
         *                expected otherwise.
         */
        public FakeTask(Uri uri, boolean working) {
            this.uri = uri;
            this.working = working;
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return working;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public Uri getResult() {
            return uri;
        }

        @Nullable
        @Override
        public <X extends Throwable> Uri getResult(@NonNull Class<X> aClass) {
            return uri;
        }

        @Nullable
        @Override
        public Exception getException() {
            return new Exception();
        }

        @NonNull
        @Override
        public Task<Uri> addOnSuccessListener(
                @NonNull OnSuccessListener<? super Uri> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnSuccessListener(
                @NonNull Executor executor,
                @NonNull OnSuccessListener<? super Uri> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnSuccessListener(
                @NonNull Activity activity,
                @NonNull OnSuccessListener<? super Uri> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnFailureListener(
                @NonNull Executor executor,
                @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnFailureListener(
                @NonNull Activity activity,
                @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    }
}