package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.location.FakeLocation;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;
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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class CreateOfferActivityTest {

    private static FragmentManager manager;
    private final long beginingTime = 1515625200000L;
    private final long timeDiff = 10L;
    @Rule
    public IntentsTestRule<MainMenuActivity> intentsTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private static void fillNameAndDescription() {
        onView(withId(R.id.offer_name)).perform(typeText("title test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_description)).perform(typeText("description test"))
                .perform(closeSoftKeyboard());
    }

    /**
     * Fills the field to create an offer in CreateOfferActivity.
     * The offer created has a title, description, picture, location and date.
     */
    public static void fillOffer() {
        fillNameAndDescription();

        // Answer to gallery intent
        Intent resultData = new Intent();
        resultData.setData(Uri.parse("drawable://" + R.drawable.img));
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(not(isInternal())).respondWith(result);

        onView(withId(R.id.fetch_picture)).perform(scrollTo(), click());

        // We ensure that the unchecking works
        onView(withId(R.id.offer_position_status)).perform(scrollTo(), click());
        onView(withId(R.id.offer_position_status)).perform(scrollTo(), click());
        onView(withId(R.id.offer_position_status)).perform(scrollTo(), click());

        onView(withId(R.id.button)).perform(scrollTo(), click());
    }

    /**
     * Sets up a fake database and a fake storage, enables TestMode and launches activity.
     */
    @Before
    public void setTest() {
        ListOfferActivityTest.setUpFakeDatabase();
        Storage.setDebugStorage(new FakeStorage(true));
        AppLocation.setDebugLocation(FakeLocation.fakeLocationCreator());
        Config.goToTest();
        intentsTestRule.launchActivity(new Intent());
        manager = intentsTestRule.getActivity().getSupportFragmentManager();
    }

    private void goToCreateOffer(boolean hasOffer) {
        FragmentTransaction transaction = manager.beginTransaction();
        CreateOfferActivity fragment = new CreateOfferActivity();
        if (hasOffer) {
            Bundle bundle = new Bundle();

            Offer offer = new Offer(Config.getUser().getUserId(), "20",
                    "20", "20", "20", Category.FOOD, beginingTime,
                    beginingTime + timeDiff);

            bundle.putParcelable("offer", offer);
            fragment.setArguments(bundle);
            Database.getInstance().write("/offers", offer.getUuid(), offer);
        }
        transaction.replace(R.id.centralFragment, fragment).commit();
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void errorMessageDisplayed() {
        goToCreateOffer(false);
        onView(withId(R.id.button)).perform(scrollTo(), click());
        onView(withId(R.id.error_message));
        onView(withId(R.id.offer_name)).perform(closeSoftKeyboard());
        onView(withId(R.id.button)).perform(scrollTo(), click());
        onView(withId(R.id.error_message))

                .check(matches(withText(R.string.error_create_offer_invalid)));
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
        Offer testOffer = new Offer(Config.getUser().getUserId(), "Test", "Test");
        Database.getInstance().write("/offers", testOffer.getUuid(), testOffer);
        Fragment offer = FragmentConverter.createShowOfferWithOffer(testOffer);
        if (setToOtherThanOwner) {
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
    public void defineOfferOnCreation() {
        final String cat = Category.values()[1].toString();

        goToCreateOffer(false);
        //onView(withId(R.id.create_offer_button)).perform(click());
        onView(withId(R.id.button)).perform(scrollTo(), closeSoftKeyboard());
        onView(withId(R.id.category_spinner)).check(matches(allOf(isEnabled(), isClickable())))
                .perform(customClick());
        onView(withText(cat)).perform(scrollTo(), click());
        onView(withText(R.string.offer_name)).perform(scrollTo());
        fillOffer();

        onView(withText(cat)).perform(scrollTo());
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

    private void setDate(int year, int monthOfYear, int dayOfMonth) {
        onView(withId(R.id.pick_date)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void dateTest() {
        goToCreateOffer(false);
        setDate(2020, 1, 1);
        onView(withId(R.id.showDate)).check(matches(withText("Wednesday, 01/01/2020")));
    }

    @Test
    public void unvalidDateTest() {
        goToCreateOffer(false);
        setDate(2000, 1, 1);

    }

}