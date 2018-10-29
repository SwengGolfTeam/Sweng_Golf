package ch.epfl.sweng.swenggolf.storage;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ch.epfl.sweng.swenggolf.offer.Offer;

public final class FireStorage extends Storage {

    private final FirebaseStorage storage;

    protected FireStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public FireStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public void write(Uri uri, String path, OnCompleteListener<Uri> listener) {
        final StorageReference ref = storage.getReference().child(path);

        ref.putFile(uri)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                            throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(listener);
    }

    @Override
    public void remove(String linkPicture) {
        storage.getReferenceFromUrl(linkPicture).delete();
    }
}
