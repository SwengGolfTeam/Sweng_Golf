package ch.epfl.sweng.swenggolf;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Offer;


public final class FakeFirebaseStorage {

    /**
     * Return a fake FirebaseDatabase used for read and write offers. Support only a limited number
     * of operations.
     *
     * @return a fake FirebaseDatabase
     */
    public static FirebaseStorage firebaseStorage() {
        return firebaseStorage(true);
    }

    /**
     * Return a fake FirebaseDatabase used for read and write offers. Support only a limited number
     * of operations.
     *
     * @param working if false the database will return error when trying to access it.
     * @return a fake FirebaseDatabase
     */
    public static FirebaseStorage firebaseStorage(final boolean working) {
        FirebaseStorage d = Mockito.mock(FirebaseStorage.class);
        StorageReference root = Mockito.mock(StorageReference.class);
        StorageReference values = Mockito.mock(StorageReference.class);
        Mockito.when(d.getReference()).thenReturn(root);

        //Handle the write on the database
        setUpOfferWrite(working, root, values);
        return d;
    }

    private static void setUpOfferWrite(final boolean working, StorageReference root,
                                        StorageReference values) {

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

        Mockito.when(root.child("images")).thenReturn(values);
        StorageReference writeRef = Mockito.mock(StorageReference.class);
        Mockito.when(values.child(ArgumentMatchers.anyString())).thenReturn(writeRef);

        Mockito.doAnswer(answerWrite).when(writeRef)
                .putFile(ArgumentMatchers.any(Uri.class),
                        ArgumentMatchers.any(StorageMetadata.class));
    }
}
