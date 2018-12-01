package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.AttributeOrdering;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FireDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class FireDatabaseTest {
    private static final String PATH = "path";
    private static final String ID = "id";

    private static final Offer OFFER1 = (new Offer.Builder()).setUserId("id").setTitle("title")
            .setDescription("description").build();
    private static final Offer OFFER2 = (new Offer.Builder()).setUserId("id2").setTitle("title2")
            .setDescription("description2").build();

    private static final List<Offer> LIST = Arrays.asList(OFFER1, OFFER2);

    @Test
    public void listenAddsListener() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        FireDatabase d = new FireDatabase(database);
        final DatabaseReference idReference = setUpPath(database);
        ValueListener<Offer> listenerOffer = mockRead(idReference, OFFER1);
        d.listen(PATH, ID, listenerOffer, Offer.class);
        verify(idReference).addValueEventListener(any(ValueEventListener.class));
    }

    @Test
    public void deafenRemovesListener() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        FireDatabase d = new FireDatabase(database);
        final DatabaseReference idReference = setUpPath(database);
        ValueListener<Offer> listenerOffer = mockRead(idReference, OFFER1);
        d.listen(PATH, ID, listenerOffer, Offer.class);
        d.deafen(PATH, ID, listenerOffer);
        verify(idReference).removeEventListener(any(ValueEventListener.class));
    }

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


        mockWrite(idReference, OFFER1);
        ValueListener<Offer> listenerOffer = mockRead(idReference, OFFER1);

        FireDatabase d = new FireDatabase(database);
        d.write(PATH, ID, OFFER1, listener);
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

    @Test
    public void writeWithoutListener() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        DatabaseReference idReference = setUpPath(database);

        Answer<Void> ans = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                assertThat((Offer) invocation.getArgument(0), is(OFFER1));
                return null;
            }
        };
        doAnswer(ans).when(idReference).setValue(any(Offer.class));

        FireDatabase d = new FireDatabase(database);
        d.write(PATH, ID, OFFER1);
    }

    @Test
    public void readListReturnCorrectValues() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        DatabaseReference categoryReference = mock(DatabaseReference.class);
        when(database.getReference(anyString())).thenReturn(categoryReference);

        setUpReadListData(categoryReference);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> value) {
                assertThat(value, is(LIST));
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        FireDatabase d = new FireDatabase(database);
        d.readList(PATH, listener, Offer.class);
    }

    @Test
    public void readListWithAscendingOrderingReturnCorrectValues() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        setUpQuery(database);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                assertThat(offers, is(LIST));
            }

            @Override
            public void onCancelled(DbError error) {
            }
        };
        FireDatabase d = new FireDatabase(database);
        AttributeOrdering o = AttributeOrdering.ascendingOrdering("id", 100);
        d.readList(Database.OFFERS_PATH, listener, Offer.class, o);
    }

    @Test
    public void readListWithDescendingOrderingReturnCorrectValues() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        setUpQuery(database);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                List<Offer> expected = LIST;
                Collections.reverse(expected);
                assertThat(offers, is(expected));
            }

            @Override
            public void onCancelled(DbError error) {
            }
        };
        FireDatabase d = new FireDatabase(database);
        AttributeOrdering o = AttributeOrdering.descendingOrdering("id", 100);
        d.readList(Database.OFFERS_PATH, listener, Offer.class, o);
    }

    private void setUpReadListData(DatabaseReference categoryReference) {
        Answer<Void> ans = setUpListDataSnapshot();
        doAnswer(ans).when(categoryReference)
                .addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    @NonNull
    private Answer<Void> setUpListDataSnapshot() {
        List<DataSnapshot> data = new ArrayList<>();
        for (Offer offer : LIST) {
            DataSnapshot snapshot = mock(DataSnapshot.class);
            when(snapshot.getValue(Offer.class)).thenReturn(offer);
            data.add(snapshot);
        }
        final DataSnapshot list = mock(DataSnapshot.class);
        when(list.getChildren()).thenReturn(data);
        when(list.exists()).thenReturn(true);

        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ValueEventListener listener = invocation.getArgument(0);
                listener.onDataChange(list);
                return null;
            }
        };
    }

    @Test
    public void readOffersReturnsCorrectValues() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);

        setUpQuery(database);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                assertTrue(offers.equals(LIST) || offers.size() == 0);
            }

            @Override
            public void onCancelled(DbError error) {
            }
        };

        FireDatabase d = new FireDatabase(database);
        d.readOffers(listener); // test both versions
    }

    @Test
    public void readOffersWithCategoryReturnsCorrectValues() {
        FirebaseDatabase database = mock(FirebaseDatabase.class);
        setUpQuery(database);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                assertEquals(0, offers.size());
            }

            @Override
            public void onCancelled(DbError error) {
            }
        };

        FireDatabase d = new FireDatabase(database);
        d.readOffers(listener, new ArrayList<Category>());
    }

    private void setUpQuery(FirebaseDatabase database) {
        DatabaseReference ref = mock(DatabaseReference.class);
        when(database.getReference(anyString())).thenReturn(ref);
        Query idQuery = mock(Query.class);
        Query tagQuery = mock(Query.class);
        when(ref.orderByChild(anyString())).thenReturn(tagQuery);
        when(tagQuery.equalTo(anyString())).thenReturn(idQuery);
        when(tagQuery.limitToLast(anyInt())).thenReturn(idQuery);
        when(tagQuery.limitToFirst(anyInt())).thenReturn(idQuery);

        Answer<Void> queryListener = setUpListDataSnapshot();
        doAnswer(queryListener).when(idQuery)
                .addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    @NonNull
    private ValueListener<Offer> mockRead(DatabaseReference idReference, final Offer offer) {
        Answer<Void> readAnswer = mockGetData(offer);
        doAnswer(readAnswer).when(idReference)
                .addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(readAnswer).when(idReference)
                .addValueEventListener(any(ValueEventListener.class));

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

    @NonNull
    private Answer<Void> mockGetData(final Offer offer) {
        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ValueEventListener listener1 = invocation.getArgument(0);
                DataSnapshot snapshot = mock(DataSnapshot.class);
                when(snapshot.getValue(Offer.class)).thenReturn(offer);
                listener1.onDataChange(snapshot);
                return null;
            }
        };
    }

    private void mockWrite(DatabaseReference idReference, final Offer offer) {

        Answer<Void> writeAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                assertThat((Offer) invocation.getArgument(0), is(offer));
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
