package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.SignInActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.network.Network;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class OfflineTests {

    @Rule
    public final ActivityTestRule<SignInActivity> signInRule =
            new ActivityTestRule<>(SignInActivity.class);

    @Rule
    public final ActivityTestRule<MainMenuActivity> listOfferRule =
            new ActivityTestRule<>(MainMenuActivity.class);


    @Test
    public void signInActivityCheckDialog() {
        Config.quitTest(); // to make sure
        Intents.init();
        signInRule.launchActivity(new Intent());
        Network.setFalseforTest(true);
        Config.goToTest();
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withText(android.R.string.yes)).perform(click());
        intended(toPackage("com.android.settings"));
        signInRule.finishActivity();
        Network.setFalseforTest(false);
        Intents.release();
    }

    @Test
    public void listOfferCheckDialog() {
        Config.quitTest();
        Intents.init();
        Network.setFalseforTest(true);
        Config.goToTest();
        Database.setDebugDatabase(FilledFakeDatabase.fakeDatabaseCreator());
        Config.setUser(FilledFakeDatabase.getUser(0));
        listOfferRule.launchActivity(new Intent());
        onView(withText(android.R.string.yes)).perform(click());
        intended(toPackage("com.android.settings"));
        listOfferRule.finishActivity();
        Network.setFalseforTest(false);
        Config.quitTest();
        Intents.release();
    }
}
