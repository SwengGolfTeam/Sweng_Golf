package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.SignInActivity;
import ch.epfl.sweng.swenggolf.network.Network;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {

    @Rule
    public final ActivityTestRule<SignInActivity> mActivityRule =
            new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void goToSignInActivityTest() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        intended(hasComponent(SignInActivity.class.getName()));
        Intents.release();
    }
}
