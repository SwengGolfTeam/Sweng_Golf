package ch.epfl.sweng.swenggolf.database;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class StorageConnection {
    private static FirebaseStorage st = null;
    private static StorageConnection storageConnection = null;

    /**
     * Create a storageConnection using a storage.
     *
     * @param firebaseStorage the storage
     */
    private StorageConnection(FirebaseStorage firebaseStorage) {
        setUpStorage(firebaseStorage);
    }

    private static void setUpStorage(FirebaseStorage firebaseStorage) {
        if (st == null) {
            st = firebaseStorage;
        }
    }

    /**
     * Return the instance of storageConnection.
     *
     * @return the storageConnection
     */
    public static StorageConnection getInstance() {
        if (storageConnection == null) {
            storageConnection = new StorageConnection(FirebaseStorage.getInstance());
        }
        return storageConnection;
    }

    /**
     * Configure storageConnection to use a fake storage.
     *
     * @param firebaseStorage the fake storage
     */
    public static void setDebugStorage(FirebaseStorage firebaseStorage) {
        st = firebaseStorage;
    }

    /**
     * Writes a new offer in the storage.
     *
     * @param uri the element we want to add to the storage
     */
    public Task<Uri> writeFile(Uri uri) {
        final StorageReference ref =
                st.getReference().child("images/" + UUID.randomUUID().toString());
        return ref.putFile(uri)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                            throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                });
    }
}