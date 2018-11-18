package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static ch.epfl.sweng.swenggolf.storage.Storage.CAPTURE_IMAGE_REQUEST;
import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;
import static ch.epfl.sweng.swenggolf.storage.Storage.conditionActivityResult;
import static ch.epfl.sweng.swenggolf.storage.Storage.takePicture;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StorageTest {
    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityTestRule =
            new ActivityTestRule<>(MainMenuActivity.class);

    @Test
    public void conditionActivityResultFalseOnPickRequestWithNullIntent() {
        assertFalse(conditionActivityResult(PICK_IMAGE_REQUEST, RESULT_OK, null));
    }

    @Test
    public void conditionActivityResultFalseOnResultFailure() {
        assertFalse(conditionActivityResult(PICK_IMAGE_REQUEST, RESULT_CANCELED, new Intent()));
    }

    @Test
    public void conditionActivityResultTrueOnOkTakePictureIntent() {
        assertTrue(conditionActivityResult(CAPTURE_IMAGE_REQUEST, RESULT_OK, new Intent()));
    }

    @Test
    public void conditionActivityResultFalseOnFailedPictureIntent() {
        assertFalse(conditionActivityResult(CAPTURE_IMAGE_REQUEST, RESULT_CANCELED, new Intent()));
    }

    @Test
    public void conditionActivityResultFalseOnNullData() {
        Intent dataIntent = new Intent();
        assertFalse(conditionActivityResult(PICK_IMAGE_REQUEST, RESULT_OK, dataIntent));
    }

    @Test
    public void conditionActivityResultTrueOnValidPickRequest() {
        Intent dataIntent = new Intent();
        dataIntent.setData(Uri.EMPTY);
        assertTrue(conditionActivityResult(PICK_IMAGE_REQUEST, RESULT_OK, dataIntent));
    }

    @Test
    public void intentTakePictureCreatesFile() throws IOException {
        Intent uriIntent = takePicture(mActivityTestRule.getActivity());
        Uri photoUri = (Uri) uriIntent.getExtras().get(MediaStore.EXTRA_OUTPUT);
        String fileName = photoUri.getLastPathSegment();
        File cacheDirectory = mActivityTestRule.getActivity().getCacheDir();
        File photoFile = new File(cacheDirectory, fileName);
        assertTrue(photoFile.delete());
    }
}
