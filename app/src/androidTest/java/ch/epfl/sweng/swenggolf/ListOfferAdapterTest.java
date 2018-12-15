package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.list.ListOfferAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ListOfferAdapterTest {

    @Rule
    public final ActivityTestRule<MainMenuActivity> recyclerViewActivity =
            new ActivityTestRule<>(MainMenuActivity.class);

    private ListOfferAdapter a;
    private final List<Offer> offerList = Arrays.asList(FilledFakeDatabase.getOffer(0),
            FilledFakeDatabase.getOffer(1));

    /**
     * Reinitialises the adapter used for tests.
     * Creates a RecyclerView to be used with the adapter.
     */
    @Before
    public void setUp() {
        a = new ListOfferAdapter();
        RecyclerView rv = new RecyclerView(recyclerViewActivity.getActivity());
        rv.setAdapter(a);
    }

    @Test
    public void testSizeList() {
        a.add(offerList);
        assertThat(a.getItemCount(), is(offerList.size()));
    }

    @Test
    public void testFilter() {
        a.add(offerList);
        a.filter(offerList.get(0).getTitle());
        assertThat(a.getItemCount(), is(1));
    }

    @Test
    public void testAddReturnsCorrectValueOnNonEmptyAddition() {
        assertThat(a.add(offerList), is(false));
    }

    @Test
    public void testAddReturnsCorrectValueOnEmptyAddition() {
        assertThat(a.add(new ArrayList<Offer>()), is(true));
    }

    @Test
    public void addOnFilteredListReturnsCorrectValue() {
        a.filter(offerList.get(1).getTitle());
        assertThat(a.add(Collections.singletonList(offerList.get(0))), is(true));
    }
}