package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;

public class OfferPatternTest {
    @Rule
    public IntentsTestRule<MainMenuActivity> intentsTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private User user = FilledFakeDatabase.getUser(3);
    private FakeDatabase database;

    /**
     * Set up the fake database and a fake user.
     */
    @Before
    public void setup() {
        database = new FakeDatabase(true);
        Database.setDebugDatabase(database);
        Config.setUser(user);
        intentsTestRule.launchActivity(new Intent());
        FragmentConverter fragment = (FragmentConverter)
                intentsTestRule.getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.centralFragment);
        fragment.replaceCentralFragment(new CreateOfferActivity());
    }

    @Test
    public void canRetrievePattern() {
        Offer.Builder builder1 = new Offer.Builder().setDescription("Description")
                .setTitle("Title").setTag(Category.DRINKS);
        Offer.Builder builder2 = new Offer.Builder().setTitle("This is a title");
        String name1 = "Pattern 1";
        String name2 = "Pattern 2";
        String path = Database.OFFERS_PATTERN_PATH + "/" + Config.getUser().getUserId();
        Database database = Database.getInstance();
        database.write(path, name1, builder1);
        database.write(path, name2, builder2);

        openPatternChoice();
        onView(withText(name2)).perform(click());
        onView(withText(R.string.accept)).perform(click());

        onView(withId(R.id.offer_name)).check(matches(withText(builder2.getTitle())));
    }

    private void openPatternChoice() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.choose_offer_template)).perform(click());
    }

    @Test
    public void saveAsPatternSaveDataInDatabase() {
        final String title = "Title";
        final String description = "Description";
        final String patternName = "My super pattern";
        onView(withId(R.id.offer_name)).perform(typeText(title));
        onView(withId(R.id.offer_description)).perform(typeText(description));
        closeSoftKeyboard();
        onView(withId(R.id.button_save_pattern)).perform(scrollTo(), click());
        onView(withId(R.id.dialog_choose_offer_name_edit)).perform(typeText(patternName));
        closeSoftKeyboard();
        onView(withText(R.string.save)).perform(click());

        Database database = Database.getInstance();
        database.read(Database.OFFERS_PATTERN_PATH + "/" + Config.getUser().getUserId(),
                patternName,
                new ValueListener<Offer.Builder>() {

                    @Override
                    public void onDataChange(Offer.Builder value) {
                        assertThat(value.getTitle(), is(title));
                        assertThat(value.getDescription(), is(description));
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        fail();
                    }
                },
                Offer.Builder.class);
    }

    @Test
    public void showToastWhenOfferIsEmpty() {
        onView(withId(R.id.button_save_pattern)).perform(scrollTo(), click());
        TestUtility.testToastShow(intentsTestRule, R.string.create_pattern_error_empty);
    }

    @Test
    public void showToastWhenNoPatternSaved() {
        openPatternChoice();
        TestUtility.testToastShow(intentsTestRule, R.string.error_no_pattern_saved);
    }

    @Test
    public void canNotCreatePatternWithEmptyName() {
        onView(withId(R.id.offer_name)).perform(typeText("Hello"));
        closeSoftKeyboard();
        onView(withId(R.id.button_save_pattern)).perform(scrollTo(), click());
        final ViewInteraction editText = onView(withId(R.id.dialog_choose_offer_name_edit));
        editText.perform(typeText("This is a pattern name"));
        editText.perform(clearText());
        onView(withText(R.string.save)).check(matches(not(isEnabled())));
    }

    @Test
    public void showToastWhenCantLoadPattern() {
        Offer.Builder builder = new Offer.Builder();
        String patternName = "name";
        Database.getInstance().write(Database.OFFERS_PATTERN_PATH + "/"
                        + Config.getUser().getUserId(),patternName, builder);

        openPatternChoice();
        database.setWorking(false);
        onView(withText(patternName)).perform(click());
        onView(withText(R.string.accept)).perform(click());
        TestUtility.testToastShow(intentsTestRule, R.string.can_not_load_pattern);
    }

    @Test
    public void showToastWhenCantloadPatternList() {
        database.setWorking(false);
        openPatternChoice();
        TestUtility.testToastShow(intentsTestRule, R.string.can_not_load_pattern_list);
    }

}
