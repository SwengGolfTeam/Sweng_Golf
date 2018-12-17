package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.answer.Answer;
import ch.epfl.sweng.swenggolf.offer.answer.Answers;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)

public class MessagingTest {
    private static final User author = FilledFakeDatabase.getUser(0);
    private static final User acceptedUser = FilledFakeDatabase.getUser(1);
    private static final User otherUser = FilledFakeDatabase.getUser(2);
    private static final Offer offer = new Offer.Builder(
            FilledFakeDatabase.getOfferOfUser(author.getUserId()))
            .setIsClosed(true).build();

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Write some answers to the offer in the database, with the first one accepted.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FilledFakeDatabase.fakeDatabaseCreator());
        Answers answers = new Answers(Arrays.asList(
                new Answer(acceptedUser.getUserId(), "I am blue"),
                new Answer(otherUser.getUserId(), "dabedidabeda")),
                0);
        Config.setUser(author);
        Database.getInstance().write(Database.ANSWERS_PATH,
                offer.getUuid(), answers);
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void canOpenDiscussionAndSendMessages() {
        setUpUserAndLaunchShowOffer(author);
        TestUtility.showOfferCustomScrollTo();
        onView(withId(R.id.open_discussion)).perform(click());
        try {
            onView(withId(R.id.open_discussion)).perform(click()); // the first click might not work
        } catch (NoMatchingViewException e) {
            // do nothing
        }
        String greeting = "Hello";
        onView(withId(R.id.message_content))
                .perform(typeText(greeting), closeSoftKeyboard());
        onView(withId(R.id.send_message_button)).perform(click());
        onView(withText(greeting)).check(matches(isDisplayed()));
    }

    @Test
    public void onlyAcceptedUserCanOpenDiscussion() {
        setUpUserAndLaunchShowOffer(acceptedUser);
        onView(withId(R.id.open_discussion)).check(matches(isClickable()));
        // change user to another whose answer did not get accepted
        setUpUserAndLaunchShowOffer(otherUser);
        onView(withId(R.id.open_discussion)).check(matches(not(isDisplayed())));
    }

    private void setUpUserAndLaunchShowOffer(User user) {
        Config.setUser(user);
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }
}
