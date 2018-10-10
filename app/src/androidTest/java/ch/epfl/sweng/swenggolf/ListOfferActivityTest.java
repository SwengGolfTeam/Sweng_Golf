package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ListOfferActivityTest {

    private final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            + "Nam ut quam ornare, fringilla nunc eget, facilisis lectus."
            + "Curabitur ut nunc nec est feugiat commodo. Nulla vel porttitor justo."
            + "Suspendisse potenti. Morbi vehicula ante nibh,"
            + " at tristique tortor dignissim non."
            + "In sit amet ligula tempus, mattis massa dictum, mollis sem."
            + "Mauris convallis sed mauris ut sodales."
            + "Nullam tristique vel nisi a rutrum. Sed commodo nec libero sed volutpat."
            + "Fusce in nibh pharetra nunc pellentesque tempor id interdum est."
            + "Sed rutrum mauris in ipsum consequat, nec scelerisque nulla facilisis.";
    private void setDebug(){
        FirebaseDatabase d = mock(FirebaseDatabase.class);
        DatabaseReference root = mock(DatabaseReference.class);
        DatabaseReference values = mock(DatabaseReference.class);
        final DataSnapshot offerSnapshot = mock(DataSnapshot.class);

        Offer[] offers = {
                new Offer("Robin", "6-pack beers for ModStoch homework", lorem),
                new Offer("Eric", "Chocolate for tractor", lorem),
                new Offer("Ugo", "ModStoch help for food", lorem),
                new Offer("Elsa", "Pizzas for beer", lorem),
                new Offer("Seb", "Everything for a canton that doesn't suck and some "
                        + "more text to overflow the box", lorem),
                new Offer("Markus", "My kingdom for a working DB", lorem)};
        List<Offer> offerList = Arrays.asList(offers);
        List<DataSnapshot> dataList = new ArrayList<>();
        for(Offer offer : offerList) {
            DataSnapshot data = mock(DataSnapshot.class);
            when(data.getValue(Offer.class)).thenReturn(offer);
            dataList.add(data);
        }
        when(offerSnapshot.getChildren()).thenReturn(dataList);

        when(d.getReference()).thenReturn(root);
        when(d.getReference("/offers")).thenReturn(values);
        Answer answer = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                ValueEventListener listener = invocation.getArgument(0);
                listener.onDataChange(offerSnapshot);
                return null;
            }
        };
        doAnswer(answer).when(values).addListenerForSingleValueEvent(any(ValueEventListener.class));

        //when(root.child("offers")).thenReturn();
        DatabaseConnection.setDebugDatabase(d);
    }
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        setDebug();
    }
    /**
     * Checks if the offer is correctly displayed after a short or long click
     * on the list.
     *
     * @param longClick if the click should be long
     */
    public void offerCorrectlyDisplayedAfterAClickOnList(boolean longClick) {
        onView(withId(R.id.show_offers_button)).perform(click());

        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(ListOfferActivity.offerList.get(0).getTitle())),
                longClick ? longClick() : click()
        ));
        onView(withId(R.id.show_offer_title))
                .check(matches(withText(ListOfferActivity.offerList.get(0).getTitle())));
    }

    @Test
    public void offerCorrectlyDisplayedAfterClickOnList() {
        offerCorrectlyDisplayedAfterAClickOnList(false);
    }

    @Test
    public void offerCorrectlyDisplayedAfterLongPressOnList() {
        offerCorrectlyDisplayedAfterAClickOnList(true);
    }

    @Test
    public void listShowCorrectly(){
        onView(withId(R.id.show_offers_button)).perform(click());
    }
}
