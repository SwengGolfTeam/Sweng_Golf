package ch.epfl.sweng.swenggolf.storage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static android.app.Activity.RESULT_OK;

public abstract class Storage {

    public static final int PICK_IMAGE_REQUEST = 71;

    private static Storage storage = null;

    /**
     * Use the given storage as the return of the getInstance() method.
     *
     * @param s the fake storage
     */
    public static void setDebugStorage(Storage s) {
        storage = s;
    }

    /**
     * Returns the singleton instance of the Storage, or a fake Storage if mentioned.
     *
     * @return the storage
     */
    public static Storage getInstance() {
        if (storage == null) {
            storage = new FireStorage();
        }
        return storage;
    }

    protected Storage() {
    }

    /**
     * Writes an image into the storage. Uses the listener to check if everything worked.
     *
     * @param uri the local URI of the image
     * @param path the writing path
     * @param listener the OnCompleteListener that performs operations when done with the uploading
     */
    public abstract void write(@NonNull Uri uri, String path,
                               @NonNull OnCompleteListener<Uri> listener);

    public abstract void remove(@NonNull String linkPicture);

    /**
     * Creates an intent that ask Android to fetch an image.
     *
     * @return the mentioned intent
     */
    public static Intent choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select Picture");
    }

    public static boolean conditionActivityResult(int requestCode, int resultCode, Intent data) {
        return requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null;
    }

}
