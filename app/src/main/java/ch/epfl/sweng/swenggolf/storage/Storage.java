package ch.epfl.sweng.swenggolf.storage;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;

import java.io.IOException;

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

    public static Storage getInstance() {
        if (storage == null) {
            storage = new FireStorage();
        }
        return storage;
    }

    protected Storage() {
    }

    public abstract void write(Uri uri, String path, OnCompleteListener<Uri> listener);

    public abstract void remove(String linkPicture);

    public static Intent choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select Picture");
    }

    public static Uri showPicture(ImageView imageView, Intent data, ContentResolver contentRes) {
        Uri dataUri = data.getData();

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentRes, dataUri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataUri;
    }

    public static boolean conditionActivityResult(int requestCode, int resultCode, Intent data) {
        return requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null;
    }

}
