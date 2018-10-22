package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


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
     * @return a fake FirebaseStorage
     */
    public static FirebaseStorage firebaseStorage(final boolean working) {
        FirebaseStorage d = Mockito.mock(FirebaseStorage.class);
        StorageReference root = Mockito.mock(StorageReference.class);
        StorageReference values = Mockito.mock(StorageReference.class);
        Mockito.when(d.getReference()).thenReturn(root);

        //Handle the write on the database
        setUpImageWrite(working, root, values);
        return d;
    }

    private static void setUpImageWrite(final boolean working, StorageReference root,
                                        StorageReference values) {

        UploadTask uploadTask = Mockito.mock(UploadTask.class);
        final Task<Uri> taskUri = Mockito.mock(Task.class);
        Answer answerWrite = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Exception {
                OnCompleteListener<Uri> listener = invocation.getArgument(0);
                if (working)
                    listener.onComplete(taskUri);
                else
                    throw new Exception("Storage is not working");
                return null;
            }
        };

        Mockito.when(root.child(ArgumentMatchers.anyString())).thenReturn(values);
        Mockito.when(values.putFile(ArgumentMatchers.any(Uri.class)))
                .thenReturn(uploadTask);
        Mockito.when(uploadTask
                .continueWithTask(ArgumentMatchers.any(Continuation.class)))
                .thenReturn(taskUri);

        Mockito.doAnswer(answerWrite).when(taskUri).addOnCompleteListener(
                ArgumentMatchers.any(OnCompleteListener.class));
    }
}
