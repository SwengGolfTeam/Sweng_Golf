package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class SavePartialOfferTest {
    @Rule
    public IntentsTestRule<MainMenuActivity> intentsTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private User user = FilledFakeDatabase.getUser(0);

    /**
     * Set up the fake database and a fake user.
     */
    @Before
    public void setup() {
        Database.setDebugDatabase(new FakeDatabase(true));
        Config.setUser(user);
    }

    private void launchCreateOffer() {
        intentsTestRule.launchActivity(new Intent());
        FragmentConverter fragment = (FragmentConverter)
                intentsTestRule.getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.centralFragment);
        fragment.replaceCentralFragment(new CreateOfferActivity());
    }

    @Test
    public void canRecoverData() {
        Offer.Builder builder = writeBuilder();
        launchCreateOffer();
        onView(withText(android.R.string.yes)).perform(click());
        onView(withId(R.id.offer_name)).check(matches(withText(builder.getTitle())));
        onView(withId(R.id.offer_description)).check((matches(withText(builder.getDescription()))));
    }

    @NonNull
    private Offer.Builder writeBuilder() {
        Offer.Builder builder = new Offer.Builder();
        builder.setDescription("description").setTitle("Title")
                .setUserId(Config.getUser().getUserId());
        Database.getInstance().write(Database.OFFERS_SAVED_PATH, Config.getUser().getUserId(),
                builder);
        return builder;
    }

    @Test
    public void dataIsDeletedIfWeDontRecover() {
        writeBuilder();
        launchCreateOffer();
        onView(withText(android.R.string.no)).perform(click());
        onView(withId(R.id.offer_name)).check(matches(withText("")));
        onView(withId(R.id.offer_description)).check((matches(withText(""))));
    }

    @Test
    public void dataIsSavedWhenWeLeaveCreateOffer() {
        launchCreateOffer();
        final String helloWorld = "Hello world";
        onView(withId(R.id.offer_description)).perform(typeText(helloWorld));
        TestUtility.pressBackButton(intentsTestRule);
        ValueListener<Offer.Builder> listener = new ValueListener<Offer.Builder>() {
            @Override
            public void onDataChange(Offer.Builder value) {
                assertEquals(helloWorld, value.getDescription());
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        Database.getInstance().read(Database.OFFERS_SAVED_PATH, Config.getUser().getUserId(),
                listener,
                Offer.Builder.class);
    }

    @Test
    public void dataIsDeletedIfWeCreateTheOffer() {
        writeBuilder();
        launchCreateOffer();
        onView(withText(android.R.string.yes)).perform(click());
        onView(withId(R.id.button_create_offer)).perform(scrollTo(), click());
        ValueListener<Offer.Builder> listener = new ValueListener<Offer.Builder>() {
            @Override
            public void onDataChange(Offer.Builder value) {
                fail();
            }

            @Override
            public void onCancelled(DbError error) {
                assertEquals(DbError.DATA_DOES_NOT_EXIST, error);
            }
        };
        Database.getInstance().read(Database.OFFERS_SAVED_PATH, Config.getUser().getUserId(),
                listener, Offer.Builder.class);
    }
}