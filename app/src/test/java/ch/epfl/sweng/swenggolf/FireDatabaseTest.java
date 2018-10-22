package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FireDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class FireDatabaseTest {

    private static final String PATH = "path";
    private static final String ID = "id";
    public static final Offer OFFER = new Offer("id", "title", "description");

    @Test
    public void writeAndReadReturnCorrectValues() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);

        DatabaseReference idReference = setUpPath(database);

        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(DbError.NONE));
            }
        };


        mockWrite(idReference, OFFER);
        ValueListener<Offer> listenerOffer = mockRead(idReference, OFFER);

        FireDatabase d = new FireDatabase(database);
        d.write(PATH, ID, listener);
        d.read(PATH, ID, listenerOffer, Offer.class);

    }

    private DatabaseReference setUpPath(FirebaseDatabase database) {
        DatabaseReference idReference = mock(DatabaseReference.class);
        DatabaseReference categoryReference = mock(DatabaseReference.class);
        when(database.getReference(anyString())).thenReturn(categoryReference);
        when(categoryReference.child(anyString())).thenReturn(idReference);
        return idReference;
    }

    @Test
    public void removeReturnNoError() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        DatabaseReference idReference = setUpPath(database);
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(DbError.NONE));
            }
        };

        Answer<Void> removeAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                DatabaseReference.CompletionListener firebaseListener =
                        invocation.getArgument(0);
                firebaseListener.onComplete(null, null);
                return null;
            }
        };
        doAnswer(removeAnswer).when(idReference)
                .removeValue(any(DatabaseReference.CompletionListener.class));

        FireDatabase d = new FireDatabase(database);
        d.remove(PATH, ID, listener);
    }

    @NonNull
    private ValueListener<Offer> mockRead(DatabaseReference idReference, final Offer offer) {
        Answer<Void> readAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ValueEventListener listener1 =invocation.getArgument(0);
                DataSnapshot snapshot = mock(DataSnapshot.class);
                when(snapshot.getValue(Offer.class)).thenReturn(offer);
                listener1.onDataChange(snapshot);
                return null;
            }
        };
        doAnswer(readAnswer).when(idReference)
                .addListenerForSingleValueEvent(any(ValueEventListener.class));

        return new ValueListener<Offer>() {
            @Override
            public void onDataChange(Offer value) {
                assertThat(value, is(offer));
            }

            @Override
            public void onCancelled(DbError error) {

            }
        };
    }

    private void mockWrite(DatabaseReference idReference,  final Offer offer) {

        Answer<Void> writeAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                assertThat( (Offer) invocation.getArgument(0), is(offer));
                DatabaseReference.CompletionListener firebaseListener =
                        invocation.getArgument(1);
                firebaseListener.onComplete(null, null);
                return null;
            }
        };
        doAnswer(writeAnswer).when(idReference)
                .setValue(any(Offer.class), any(DatabaseReference.CompletionListener.class));
    }
}
