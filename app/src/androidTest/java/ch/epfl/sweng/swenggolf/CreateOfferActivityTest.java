package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;

import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class CreateOfferActivityTest {

    @Rule
    public IntentsTestRule<MainMenuActivity> intentsTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    private static FragmentManager manager;

    /**
     * Sets up a fake database and a fake storage, enables TestMode and launches activity.
     */
    @Before
    public void setTest() {
        ListOfferActivityTest.setUpFakeDatabase();
        Storage.setDebugStorage(new FakeStorage(true));
        Config.goToTest();
        intentsTestRule.launchActivity(new Intent());
        manager = intentsTestRule.getActivity().getSupportFragmentManager();
    }

    private void goToCreateOffer(boolean hasOffer) {
        FragmentTransaction transaction = manager.beginTransaction();
        CreateOfferActivity fragment = new CreateOfferActivity();
        if(hasOffer) {
            Bundle bundle = new Bundle();
            Offer offer = new Offer(Config.getUser().getUserId(),"20","20", "20", "20");
            bundle.putParcelable("offer", offer);
            fragment.setArguments(bundle);
            Database.getInstance().write("/offers",offer.getUuid(), offer);
        }
        transaction.replace(R.id.centralFragment, fragment).commit();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void errorMessageDisplayed() {
        goToCreateOffer(false);
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.error_message));
        onView(withId(R.id.offer_name)).perform(closeSoftKeyboard());
        onView(withId(R.id.button)).perform(scrollTo(),click());
        onView(withId(R.id.error_message)).perform(scrollTo())
                .check(matches(withText(R.string.error_create_offer_invalid)));
    }

    private void fillOffer() {
        onView(withId(R.id.offer_name)).perform(typeText("title test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_description)).perform(typeText("description test"))
                .perform(closeSoftKeyboard());

        // Answer to gallery intent
        Intent resultData = new Intent();
        resultData.setData(Uri.parse("drawable://" + R.drawable.img));
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(not(isInternal())).respondWith(result);

        onView(withId(R.id.offer_picture)).perform(click());
        onView(withId(R.id.button)).perform(click());
    }

    private void assertDisplayedFragment(Class expectedClass) {
        String currentFragmentName = manager.getFragments().get(0).getClass().getName();
        assertThat(currentFragmentName, is(expectedClass.getName()));
    }

    @Test
    public void createOfferShowOfferWhenValidInput() {
        goToCreateOffer(false);
        fillOffer();
        assertDisplayedFragment(ShowOfferActivity.class);
    }

    @Test
    public void showMessageErrorWhenCantCreateOffer() {
        Database.setDebugDatabase(new FakeDatabase(false));
        goToCreateOffer(false);
        fillOffer();
        onView(withId(R.id.error_message))
                .check(matches(withText(R.string.error_create_offer_database)));
    }

    private void goToShowOffer(boolean setToOtherThanOwner) {
        Offer testOffer = new Offer(Config.getUser().getUserId(),"Test","Test");
        Database.getInstance().write("/offers",testOffer.getUuid(), testOffer);
        Fragment offer = FragmentConverter.createShowOfferWithOffer(testOffer);
        if(setToOtherThanOwner) {
            User u = new User("username",
                    "id" + Config.getUser().getUserId(), "username@example.com", "nophoto");
            Config.setUser(u);
            DatabaseUser.addUser(u);
        }
        manager.beginTransaction().replace(R.id.centralFragment, offer).commit();
    }

    @Test
    public void modifyingOfferViaShowOfferWorks() {
        goToShowOffer(false);
        onView(withId(R.id.button_modify_offer)).perform(click());
        fillOffer();
        assertDisplayedFragment(ShowOfferActivity.class);
    }

    private void worksOnlyOnCreator(int id) {
        goToShowOffer(true);
        onView(withId(id)).check(doesNotExist());
    }

    @Test
    public void modifyingOfferViaShowOfferWorksOnlyOnCreator() {
        worksOnlyOnCreator(R.id.button_modify_offer);
    }

    @Test
    public void deleteOfferViaShowOfferWorksOnlyOnCreator() {
        worksOnlyOnCreator(R.id.button_delete_offer);
    }

    private void assertBackFrom(boolean hasOffer, Class expectedDisplayedClass) {
        goToCreateOffer(hasOffer);
        assertDisplayedFragment(CreateOfferActivity.class);
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        assertDisplayedFragment(expectedDisplayedClass);
    }

    @Test
    public void backFromEmptyOfferIsListOffer() {
        assertBackFrom(false, ListOfferActivity.class);
    }

    @Test
    public void backFromModifyOfferIsShowOffer() {
        assertBackFrom(true, ShowOfferActivity.class);
    }

    @Test
    public void defineOfferOnCreation(){
        final String cat = Category.values()[1].toString();

        goToCreateOffer(false);
        //onView(withId(R.id.create_offer_button)).perform(click());
        onView(withId(R.id.button)).perform(scrollTo(), closeSoftKeyboard());
        onView(withId(R.id.category_spinner)).check(matches(allOf(isEnabled(), isClickable())))
                .perform(customClick());
        onView(withText(cat)).perform(click());
        onView(withText(R.string.offer_name)).perform(scrollTo());
        fillOffer();

        onView(withText(cat)).check(matches(isDisplayed()));
    }

    private ViewAction customClick() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isEnabled(); // requires matches(allOf( isEnabled(), isClickable())
            }

            @Override
            public String getDescription() {
                return "click button without the 90% constraint";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        };
    }
}