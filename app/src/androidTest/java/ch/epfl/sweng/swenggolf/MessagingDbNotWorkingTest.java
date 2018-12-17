package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class MessagingDbNotWorkingTest {
    private final User author = FilledFakeDatabase.getUser(0);
    private final Offer offer = new Offer.Builder(
            FilledFakeDatabase.getOfferOfUser(author.getUserId()))
            .setIsClosed(true).build();
    private FakeDatabase database;

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Write some answers to the offer in the database, with the first one accepted.
     */
    @Before
    public void setUp() {
        database = (FakeDatabase) FilledFakeDatabase.fakeDatabaseCreator();
        Database.setDebugDatabase(database);
        Answers answers = new Answers(Collections.singletonList(
                new Answer(FilledFakeDatabase.getUser(1).getUserId(), "blablabla")),
                0);
        Database.getInstance().write(Database.ANSWERS_PATH,
                offer.getUuid(), answers);
        mActivityRule.launchActivity(new Intent());
        Config.setUser(author);
        mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }

    @Test
    public void showsToastOnError() {
        TestUtility.showOfferCustomScrollTo();
        database.setWorking(false);
        onView(withId(R.id.open_discussion)).perform(click());
        TestUtility.testToastShow(mActivityRule, R.string.error_load_messages);
    }
}
