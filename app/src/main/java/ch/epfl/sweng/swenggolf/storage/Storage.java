package ch.epfl.sweng.swenggolf.storage;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public abstract class Storage {

    public static final int PICK_IMAGE_REQUEST = 71;
    public static final int CAPTURE_IMAGE_REQUEST = 1;

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
     * @param uri      the local URI of the image
     * @param path     the writing path
     * @param listener the OnCompleteListener that performs operations when done with the uploading
     */
    public abstract void write(@NonNull Uri uri, String path,
                               @NonNull OnCompleteListener<Uri> listener);

    public abstract void remove(@NonNull String linkPicture);

    /**
     * Creates an intent that asks Android to fetch an image.
     *
     * @return the mentioned intent
     */
    public static Intent choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select Picture");
    }

    /**
     * Creates an intent to take a photo and put it into the app storage.
     *
     * @param activity the activity in which the intent is created.
     * @return an intent with the file Uri.
     */
    public static Intent takePicture(FragmentActivity activity) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = takePictureCreateFile(activity);
        Uri takePicUri = FileProvider.getUriForFile(activity,
                "ch.epfl.sweng.swenggolf.fileprovider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePicUri);
        return takePictureIntent;
    }

    private static File takePictureCreateFile(FragmentActivity activity) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return File.createTempFile(imageFileName, null, activity.getCacheDir());
    }

    public static boolean conditionActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE_REQUEST: {
                    return true;
                }
                case PICK_IMAGE_REQUEST: {
                    return data != null && (data.getData() != null);
                }
                default: {
                    return false;
                }
            }
        }
        return false;
    }

}
