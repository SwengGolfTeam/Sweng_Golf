package ch.epfl.sweng.swenggolf;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class FakeFirebaseDatabase {

    private static final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            + "Nam ut quam ornare, fringilla nunc eget, facilisis lectus."
            + "Curabitur ut nunc nec est feugiat commodo. Nulla vel porttitor justo."
            + "Suspendisse potenti. Morbi vehicula ante nibh,"
            + " at tristique tortor dignissim non."
            + "In sit amet ligula tempus, mattis massa dictum, mollis sem."
            + "Mauris convallis sed mauris ut sodales."
            + "Nullam tristique vel nisi a rutrum. Sed commodo nec libero sed volutpat."
            + "Fusce in nibh pharetra nunc pellentesque tempor id interdum est."
            + "Sed rutrum mauris in ipsum consequat, nec scelerisque nulla facilisis.";

    private static final Offer[] offers =  {
        new Offer("Robin", "6-pack beers for ModStoch homework", lorem),
                new Offer("Eric", "Chocolate for tractor", lorem),
                new Offer("Ugo", "ModStoch help for food", lorem),
                new Offer("Elsa", "Pizzas for beer", lorem),
                new Offer("Seb", "Everything for a canton that doesn't suck and some "
                        + "more text to overflow the box", lorem),
                new Offer("Markus", "My kingdom for a working DB", lorem)};

    /**
     * Return a fake FirebaseDatabase used for read and write offers. Support only a limited number of
     * operations.
     * @return a fake FirebaseDatabase
     */
    public static FirebaseDatabase firebaseDatabaseOffers(){
        return firebaseDatabaseOffers(true);
    }

    /**
     * Return a fake FirebaseDatabase used for read and write offers. Support only a limited number of
     * operations.
     * @param working if false the database will return error when trying to access it.
     * @return a fake FirebaseDatabase
     */
    public static FirebaseDatabase firebaseDatabaseOffers(final boolean working){
        FirebaseDatabase d = mock(FirebaseDatabase.class);
        DatabaseReference root = mock(DatabaseReference.class);
        DatabaseReference values = mock(DatabaseReference.class);
        final DataSnapshot offerSnapshot = mock(DataSnapshot.class);
        when(d.getReference()).thenReturn(root);
        
        //Set up the offer list for read
        setUpOfferRead(working, d, values, offerSnapshot);
        
        //Handle the write on the database
        setUpOfferWrite(working, root, values);
        return d;
    }

    private static void setUpOfferWrite(final boolean working, DatabaseReference root, DatabaseReference values) {
        Answer answerWrite = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation){
                DatabaseReference.CompletionListener listener = invocation.getArgument(1);
                if(working){
                    listener.onComplete(null, null);
                }
                else{
                    listener.onComplete(mock(DatabaseError.class),null);
                }

                return null;
            }
        };
        when(root.child("offers")).thenReturn(values);
        DatabaseReference writeRef = mock(DatabaseReference.class);
        when(values.child(anyString())).thenReturn(writeRef);

        doAnswer(answerWrite).when(writeRef)
                .setValue(any(Object.class), any(DatabaseReference.CompletionListener.class));
    }

    private static void setUpOfferRead(final boolean working, FirebaseDatabase d, DatabaseReference values, final DataSnapshot offerSnapshot) {
        List<Offer> offerList = Arrays.asList(offers);
        List<DataSnapshot> dataList = new ArrayList<>();
        for(Offer offer : offerList) {
            DataSnapshot data = mock(DataSnapshot.class);
            when(data.getValue(Offer.class)).thenReturn(offer);
            dataList.add(data);
        }
        when(offerSnapshot.getChildren()).thenReturn(dataList);

        when(d.getReference("/offers")).thenReturn(values);
        Answer readAnswer = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                ValueEventListener listener = invocation.getArgument(0);
                if(working) {
                    listener.onDataChange(offerSnapshot);
                }
                else{
                    listener.onCancelled(mock(DatabaseError.class));
                }
                return null;
            }
        };
        doAnswer(readAnswer).when(values).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }
}
