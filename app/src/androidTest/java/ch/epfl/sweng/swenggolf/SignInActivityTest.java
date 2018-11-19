package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.SignInActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

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
