package ch.epfl.sweng.swenggolf;

import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.view.animation.Animation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CreateOfferPhotoTest {

    @Rule
    public IntentsTestRule<MainMenuActivity> mActivitiyRule = new IntentsTestRule<>(MainMenuActivity.class);

    @Before
    public void setUp() {
        FragmentManager manager = mActivitiyRule.getActivity().getSupportFragmentManager();
        manager.beginTransaction().setCustomAnimations(0, 0).replace(R.id.centralFragment, new CreateOfferActivity()).commit();
    }

    @Test
    public void takePictureButtonTriggersIntent() {
        onView(withId(R.id.take_picture)).perform(scrollTo(), click());
        intended(hasAction());
    }
}
