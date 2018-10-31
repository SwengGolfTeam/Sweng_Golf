package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowOfferActivityTest {
    @Rule
    public final IntentsTestRule<ShowOfferActivity> mActivityRule =
            new IntentsTestRule<>(ShowOfferActivity.class, false, false);

    private User user = FilledFakeDatabase.getUser(0);
    private Offer offer = FilledFakeDatabase.getOffer(0);
    private static int testOrder = 0;

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.setUser(user);
        Intent intent = new Intent();
        intent.putExtra("offer", offer);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void canOpenProfileFromOffer() {
        onView(withId(R.id.show_offer_author)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
    }

    private void addAnswer(String answer) {
        onView(withId(R.id.answer_description_))
                .perform(scrollTo(), typeText(answer), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(scrollTo(), click());

    }

    private String getContentDescription(String type) {
        return type + Integer.toString(testOrder);
    }

    @Test
    public void textOfAnswerIsCorrect() {
        String answer = "my answer";
        addAnswer(answer);
        onView(withContentDescription(getContentDescription("description")))
                .check(matches(withText(answer)));
        testOrder++;
    }

    @Test
    public void authorOfAnswerIsCorrect() {
        addAnswer("I wrote this");
        onView(withContentDescription(getContentDescription("username")))
                .check(matches(withText(Config.getUser().getUserName())));
        testOrder++;
    }

    @Test
    public void authorCanSelectAndDeselectFavorite() {
        addAnswer("test");
        ViewInteraction favButton = onView(withContentDescription(getContentDescription("fav")));
        // user is author
        favButton.check(matches(isClickable()));
        favButton.perform(click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite))));
        favButton.perform(click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite_border))));
        testOrder++;
    }

    @Test
    public void onlyAuthorCanChooseFavorite() {
        Config.setUser(FilledFakeDatabase.getUser(1));
        // user is not author
        addAnswer("hey!");
        onView(withContentDescription(getContentDescription("fav")))
                .check(matches(not(isClickable())));
        testOrder++;
    }



}
