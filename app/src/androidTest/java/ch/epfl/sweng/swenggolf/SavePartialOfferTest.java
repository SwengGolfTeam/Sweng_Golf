package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SavePartialOfferTest {
    @Rule
    public IntentsTestRule<MainMenuActivity> intentsTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private User user = FilledFakeDatabase.getUser(0);
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
        Offer.Builder builder = new Offer.Builder();
        builder.setDescription("description").setTitle("Title")
                .setUserId(Config.getUser().getUserId());
        Database.getInstance().write(Database.OFFERS_SAVED, Config.getUser().getUserId(), builder);
        launchCreateOffer();
        //onView(withId(R.id.offer_name)).check(matches(withText(builder.getTitle())));
    }
}
