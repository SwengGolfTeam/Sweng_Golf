package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnswersTest {

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private User author = FilledFakeDatabase.getUser(0);
    private Offer offer = FilledFakeDatabase.getOfferOfUser(author.getUserId());
    private User otherUser = FilledFakeDatabase.getUser(1);

    /**
     * Posts an answer on the current offer.
     *
     * @param answer the message to be posted
     */
    public static void addAnswer(String answer) {
        onView(withId(R.id.react_button)).perform(scrollTo(), click());
        onView(withId(R.id.your_answer_description))
                .perform(scrollTo(), typeText(answer), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(scrollTo(), click());

    }

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Config.setUser(author);
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }

    @Test
    public void testAnswersRefresh() {
        List<Answer> newAnswers = new ArrayList<>();
        newAnswers.add(new Answer(offer.getUserId(), "hey !"));
        Answers a = new Answers(newAnswers, Answers.NO_FAVORITE);
        Database.getInstance().write(Database.ANSWERS_PATH, offer.getUuid(), a);
        onView(withChild(withText("hey !"))).perform(scrollTo());
        onView(withText("hey !")).check(matches(isDisplayed()));
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
        // user A adds answer
        Config.setUser(FilledFakeDatabase.getUser(1));
        addAnswer("test");

        // user B selects Answer as favorite (with refresh of Activity to generate buttons)
        Config.setUser(author);
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
        ViewInteraction favButton = onView(withContentDescription("fav0"));
        // user is author
        favButton.check(matches(isClickable()));
        favButton.perform(scrollTo(), click());
        String acceptFav = mActivityRule.getActivity().getApplicationContext()
                .getString(R.string.accept_favorite);
        onView(withText(acceptFav)).check(matches(isDisplayed()));
        onView(withText(android.R.string.yes)).perform(click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite))));
        favButton.perform(scrollTo(), click());
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

    @Test
    public void canPostMultipleAnswers() {
        addAnswer("first answer");
        // change user and reload offer
        Config.setUser(otherUser);
        FragmentTransaction transaction = mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.centralFragment,
                FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
        addAnswer("second answer");
        onView(withContentDescription("description1")).check(matches(isDisplayed()));
    }

    @Test
    public void errorMessageWhenAnswerIsTooShort() {
        addAnswer("NO");
        final MainMenuActivity activity = mActivityRule.getActivity();
        onView(withId(R.id.your_answer_description)).check(matches(
                hasErrorText(activity
                        .getString(R.string.answer_limit, Answer.COMMENT_MIN_LENGTH))));
    }

    @Test
    public void clickOnPictureLeadsToProfile() {
        Config.setUser(otherUser);
        addAnswer("blablabla");
        onView(withContentDescription("pic0")).perform(scrollTo(), click());
        // we are in the profile
        onView(withId(R.id.name)).check(matches(withText(otherUser.getUserName())));
    }
}
