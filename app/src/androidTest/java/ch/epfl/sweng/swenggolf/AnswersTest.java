package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Answer;
import ch.epfl.sweng.swenggolf.offer.Answers;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AnswersTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    private Offer offer = FilledFakeDatabase.getOffer(0);
    private User author = FilledFakeDatabase.getUser(Integer.parseInt(offer.getUserId()));
    private User otherUser = FilledFakeDatabase.getUser(1);

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.setUser(author);
        mActivityRule.launchActivity(new Intent());
        FragmentTransaction transaction = mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.centralFragment,
                FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }

    @Test
    public void textOfAnswerIsCorrect() {
        String answer = "my answer";
        addAnswer(answer);
        onView(withContentDescription("description0"))
                .check(matches(withText(answer)));
    }

    @Test
    public void answerHasEmptyConstructorForFirebase() {
        Answer answer = new Answer();
        Answers answers = new Answers();
    }

    @Test
    public void authorOfAnswerIsCorrect() {
        addAnswer("I wrote this");
        onView(withContentDescription("username0"))
                .check(matches(withText(Config.getUser().getUserName())));
    }

    @Test
    public void authorCanSelectAndDeselectFavorite() {
        addAnswer("test");
        ViewInteraction favButton = onView(withContentDescription("fav0"));
        // user is author
        favButton.check(matches(isClickable()));
        favButton.perform(click());
        onView(withText("Accept answer")).check(matches(isDisplayed()));
        onView(withText(android.R.string.yes)).perform(click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite))));
        favButton.perform(click());
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite_border))));
    }

    @Test
    public void onlyAuthorCanChooseFavorite() {
        Config.setUser(otherUser);
        // user is not author
        addAnswer("hey!");
        onView(withContentDescription("fav0"))
                .check(matches(not(isClickable())));
    }

    private void addAnswer(String answer) {
        onView(withId(R.id.react_button)).perform(scrollTo(), click());
        onView(withId(R.id.answer_description_))
                .perform(scrollTo(), typeText(answer), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(scrollTo(), click());

    }
}
