package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;

import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.setUpFakeDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseTest {

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Before
    public void init(){
        setUpFakeDatabase();
        Config.goToTest();
        mActivityRule.launchActivity(new Intent());
    }


    @Test
    public void writeAndReadAllCategoriesTest(){
        LocalDatabase localDb = new LocalDatabase(mActivityRule.getActivity().getApplicationContext(), null, 1);

        List<Category> allCategories = Arrays.asList(Category.values());
        localDb.writeCategories(allCategories);

        List<Category> recoverCategories = localDb.readCategories();

        assertEquals(allCategories, recoverCategories);
    }
}
