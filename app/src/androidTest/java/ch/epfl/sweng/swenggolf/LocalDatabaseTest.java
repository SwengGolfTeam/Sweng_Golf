package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
    public final ActivityTestRule<MainMenuActivity> activityRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, true);


    @Test
    public void writeAndReadCategoriesTest() {
        LocalDatabase localDb = new LocalDatabase(activityRule.getActivity(), null, 1);
        List<Category> allCategories = Arrays.asList(Category.values());
        localDb.writeCategories(allCategories);
        List<Category> readCategories = localDb.readCategories();
        assertEquals(allCategories, readCategories);
    }

    @Test
    public void writeAndReadLevelTest() {
        LocalDatabase localDb = new LocalDatabase(activityRule.getActivity(), null, 1);
        int level = 5;
        localDb.writeLevel(level);
        int readLevel = localDb.readLevel();
        assertEquals(level, readLevel);
    }

    @Test
    public void upgradeDatabaseDoesWorkTest() {
        String upgradeDatabaseName = "TEST_DATABASE.db";
        LocalDatabase localDb1 = new LocalDatabase(activityRule.getActivity(), null,
                1, upgradeDatabaseName);
        LocalDatabase localDb2 = new LocalDatabase(activityRule.getActivity(), null,
                2, upgradeDatabaseName);
        List<Category> allCategories = Arrays.asList(Category.values());
        localDb2.writeCategories(allCategories);
        List<Category> readCategories = localDb2.readCategories();
        assertEquals(allCategories, readCategories);
        //We never use it in this project but it is a good idea to test anyway
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
