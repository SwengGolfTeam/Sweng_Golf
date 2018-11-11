package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.net.Uri;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.storage.Storage;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StorageTest {

    @Test
    public void conditionActivityResultFalseOnPickRequestWithNullIntent() {
        assertFalse(Storage.conditionActivityResult(Storage.PICK_IMAGE_REQUEST, RESULT_OK, null));
    }

    @Test
    public void conditionActivityResultFalseOnResultFailure() {
        assertFalse(Storage.conditionActivityResult(Storage.PICK_IMAGE_REQUEST, RESULT_CANCELED, new Intent()));
    }

    @Test
    public void conditionActivityResultTrueOnOkTakePictureIntent() {
        assertTrue(Storage.conditionActivityResult(Storage.CAPTURE_IMAGE_REQUEST, RESULT_OK, new Intent()));
    }

    @Test
    public void conditionActivityResultFalseOnFailedPictureIntent() {
        assertFalse(Storage.conditionActivityResult(Storage.CAPTURE_IMAGE_REQUEST, RESULT_CANCELED,  new Intent()));
    }

    @Test
    public void conditionActivityResultFalseOnNullData() {
        Intent dataIntent = new Intent();
        assertFalse(Storage.conditionActivityResult(Storage.PICK_IMAGE_REQUEST, RESULT_OK, dataIntent));
    }

    @Test
    public void conditionActivityResultTrueOnValidPickRequest() {
        Intent dataIntent = new Intent();
        dataIntent.setData(Uri.EMPTY);
        assertTrue(Storage.conditionActivityResult(Storage.PICK_IMAGE_REQUEST, RESULT_OK, dataIntent));
    }
}
