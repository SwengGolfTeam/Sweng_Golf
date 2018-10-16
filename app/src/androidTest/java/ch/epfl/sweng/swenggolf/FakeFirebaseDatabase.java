package ch.epfl.sweng.swenggolf;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Offer;


public final class FakeFirebaseDatabase {


    private static final User[] users = {
            new User("Eric", "uid", "email", "photo")
    };

    private static final String lorem = ListOfferActivityTest.lorem;

    private static final Offer[] offers = {
            new Offer("Robin", "6-pack beers for ModStoch homework", lorem),
            new Offer("Eric", "Chocolate for tractor", lorem),
            new Offer("Ugo", "ModStoch help for food", lorem),
            new Offer("Elsa", "Pizzas for beer", lorem),
            new Offer("Seb", "Everything for a canton that doesn't suck and some "
                    + "more text to overflow the box", lorem),
            new Offer("Markus", "My kingdom for a working DB", lorem)};

    /**
     * Return a fake FirebaseDatabase used for read and write offers. Support only a limited number
     * of operations.
     *
     * @return a fake FirebaseDatabase
     */
    public static FirebaseDatabase firebaseDatabaseOffers() {
        return firebaseDatabaseOffers(true);
    }






    /**
     * Return a fake FirebaseDatabase used for read and write offers. Support only a limited number
     * of operations.
     *
     * @param working if false the database will return error when trying to access it.
     * @return a fake FirebaseDatabase
     */
    public static FirebaseDatabase firebaseDatabaseOffers(final boolean working) {
        FirebaseDatabase d = Mockito.mock(FirebaseDatabase.class);
        DatabaseReference root = Mockito.mock(DatabaseReference.class);
        DatabaseReference valuesOffers = Mockito.mock(DatabaseReference.class);
        DatabaseReference valuesUsers = Mockito.mock(DatabaseReference.class);
        final DataSnapshot offerSnapshot = Mockito.mock(DataSnapshot.class);
        final DataSnapshot userSnapshot = Mockito.mock(DataSnapshot.class);
        Mockito.when(d.getReference()).thenReturn(root);

        //Set up the offer list for read
        setUpOfferRead(working, d, valuesOffers, valuesUsers, offerSnapshot, userSnapshot);

        //Handle the write on the database
        setUpOfferWrite(working, root,valuesOffers, valuesUsers);
        return d;
    }

    private static void setUpOfferWrite(final boolean working, DatabaseReference root,
                                        DatabaseReference valuesOffers, DatabaseReference valuesUsers) {
        Answer answerWrite = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                DatabaseReference.CompletionListener listener = invocation.getArgument(1);
                if (working) {
                    listener.onComplete(null, null);
                } else {
                    listener.onComplete(Mockito.mock(DatabaseError.class), null);
                }

                return null;
            }
        };
        Mockito.when(root.child("offers")).thenReturn(valuesOffers);
        Mockito.when(root.child("users")).thenReturn(valuesUsers);
        DatabaseReference writeRef = Mockito.mock(DatabaseReference.class);
        Mockito.when(valuesOffers.child(ArgumentMatchers.anyString())).thenReturn(writeRef);
        Mockito.when(valuesUsers.child(ArgumentMatchers.anyString())).thenReturn(writeRef);

        Mockito.doAnswer(answerWrite).when(writeRef)
                .setValue(ArgumentMatchers.any(Object.class),
                        ArgumentMatchers.any(DatabaseReference.CompletionListener.class));
    }

    private static void setUpOfferRead(final boolean working, FirebaseDatabase d,
                                       DatabaseReference valuesOffers,DatabaseReference valuesUsers, final DataSnapshot offerSnapshot, final DataSnapshot userSnapshot) {
        List<Offer> offerList = Arrays.asList(offers);
        List<User> userList = Arrays.asList(users);
        List<DataSnapshot> dataList = new ArrayList<>();
        for (Offer offer : offerList) {
            DataSnapshot data = Mockito.mock(DataSnapshot.class);
            Mockito.when(data.getValue(Offer.class)).thenReturn(offer);
            dataList.add(data);
        }
        for (User user : userList) {
            DataSnapshot data = Mockito.mock(DataSnapshot.class);
            Mockito.when(data.getValue(User.class)).thenReturn(user);
            dataList.add(data);
        }
        Mockito.when(offerSnapshot.getChildren()).thenReturn(dataList);

        Mockito.when(d.getReference("/offers")).thenReturn(valuesOffers);
        Mockito.when(d.getReference("/users")).thenReturn(valuesUsers);

        Answer readAnswerOffers = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                ValueEventListener listener = invocation.getArgument(0);
                if (working) {
                    listener.onDataChange(offerSnapshot);
                } else {
                    listener.onCancelled(Mockito.mock(DatabaseError.class));
                }
                return null;
            }
        };

        Answer readAnswerUsers = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                ValueEventListener listener = invocation.getArgument(0);
                if (working) {
                    listener.onDataChange(offerSnapshot);
                } else {
                    listener.onCancelled(Mockito.mock(DatabaseError.class));
                }
                return null;
            }
        };



        Mockito.doAnswer(readAnswerOffers).when(valuesOffers)
                .addListenerForSingleValueEvent(ArgumentMatchers.any(ValueEventListener.class));

        Mockito.doAnswer(readAnswerUsers).when(valuesUsers)
                .addListenerForSingleValueEvent(ArgumentMatchers.any(ValueEventListener.class));


    }

}
