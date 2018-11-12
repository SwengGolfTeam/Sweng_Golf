package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseTest {

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Before
    public void init() {
        mActivityRule.launchActivity(new Intent());
    }


    @Test
    public void noExceptionWithWriteAndRead() {
        LocalDatabase localDb = new LocalDatabase(mActivityRule.getActivity(), null, 1);
        List<Category> allCategories = Arrays.asList(Category.values());
        localDb.writeCategories(allCategories);
        localDb.readCategories();
    }

    @Test
    public void transformationFunctionsOnCategories() {
        //All categories
        List<Category> allCategories = Arrays.asList(Category.values());
        String stringAllCategories = Category.categoriesToSingleString(allCategories);
        assertEquals(allCategories, Category.singleStringToCategories(stringAllCategories));

        //Single category
        List<Category> singleCategory = Arrays.asList(Category.values()[0]);
        String stringSingleCategory = Category.categoriesToSingleString(singleCategory);
        assertEquals(singleCategory, Category.singleStringToCategories(stringSingleCategory));

        //No category
        List<Category> noCategory = new ArrayList<>();
        String stringNoCategory = Category.categoriesToSingleString(noCategory);
        assertEquals(noCategory, Category.singleStringToCategories(stringNoCategory));
    }
}
