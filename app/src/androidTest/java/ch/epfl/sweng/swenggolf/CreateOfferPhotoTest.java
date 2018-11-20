package ch.epfl.sweng.swenggolf;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.test.espresso.intent.ActivityResultFunction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class CreateOfferPhotoTest {

    @Rule
    public IntentsTestRule<MainMenuActivity> mActivitiyRule =
            new IntentsTestRule<>(MainMenuActivity.class);

    private static void onActivityResultAnswer(
            final IntentsTestRule<MainMenuActivity> rule, final Parcel parcel) {
        final Instrumentation.ActivityResult pictureResult =
                new Instrumentation.ActivityResult(RESULT_OK, null);
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWithFunction(
                new ActivityResultFunction() {
                    @Override
                    public Instrumentation.ActivityResult apply(Intent intent) {
                        Uri photoUri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
                        Uri.writeToParcel(parcel, photoUri);
                        parcel.setDataPosition(0);
                        File cacheDirectory = rule.getActivity().getCacheDir();
                        File photoFile = new File(cacheDirectory, photoUri.getLastPathSegment());
                        assertThat(photoFile.exists(), is(true));
                        return pictureResult;
                    }
                });
    }

    /**
     * Set up the environnement to work with CreateOfferActivity.
     */
    @Before
    public void setUp() {
        FragmentManager manager = mActivitiyRule.getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.centralFragment, new CreateOfferActivity()).commit();
    }

    @Test
    public void takePictureButtonTriggersIntent() {
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
                .respondWithFunction(new ActivityResultFunction() {
                    @Override
                    public Instrumentation.ActivityResult apply(Intent intent) {
                        assertThat(intent.getExtras(), not(is((Bundle) null)));
                        assertThat((Uri) intent.getExtras()
                                .get(MediaStore.EXTRA_OUTPUT), not(is((Uri) null)));
                        return new Instrumentation.ActivityResult(RESULT_OK, null);
                    }
                });
        onView(withId(R.id.take_picture)).perform(scrollTo(), click());
    }

    @Test
    public void onActivityResultCorrectlyDisplayPicture() {
        onActivityResultAnswer(mActivitiyRule, Parcel.obtain());
        onView(withId(R.id.take_picture)).perform(scrollTo(), click());
    }

    @Test
    public void onActivityResultCorrectlyRemovesPreviousPicture() {
        Parcel parcel = Parcel.obtain();
        onActivityResultAnswer(mActivitiyRule, parcel);
        onView(withId(R.id.take_picture)).perform(scrollTo(), click());
        Uri firstUri = Uri.CREATOR.createFromParcel(parcel);
        onView(withId(R.id.take_picture)).perform(scrollTo(), click());
        File firstPhotoFile = new File(firstUri.getPath());
        assertThat(firstPhotoFile.exists(), is(false));
    }

}
