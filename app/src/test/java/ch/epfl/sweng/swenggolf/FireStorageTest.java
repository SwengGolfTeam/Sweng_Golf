package ch.epfl.sweng.swenggolf;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.swenggolf.storage.FireStorage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FireStorageTest {

    private static final String PATH = "path";
    private static final Uri URI = mock(Uri.class);

    @Test
    public void writeWorksCorrectly() {
        FireStorage s = setupStorage(true);
        s.write(URI, PATH, getListener());
    }

    @Test(expected = Exception.class)
    public void writeFailsCorrectly() {
        FireStorage s = setupStorage(false);
        s.write(URI, PATH, getListener());
    }

    private FireStorage setupStorage(boolean working) {
        FirebaseStorage storage = mock(FirebaseStorage.class);

        final StorageReference idRef = setUpPath(storage);
        mockWrite(idRef, working);

        return new FireStorage(storage);
    }

    private StorageReference setUpPath(FirebaseStorage storage) {
        StorageReference catRef = mock(StorageReference.class);
        StorageReference idRef = mock(StorageReference.class);

        when(storage.getReference()).thenReturn(catRef);
        when(catRef.child(anyString())).thenReturn(idRef);

        return idRef;
    }

    private void mockWrite(StorageReference idRef, final boolean working) {
        UploadTask uploadTask = mock(UploadTask.class);

        final Task<Uri> uriTask = mock(Task.class);
        when(idRef.getDownloadUrl()).thenReturn(uriTask);
        when(idRef.putFile(any(Uri.class))).thenReturn(uploadTask);
        when(uriTask.isSuccessful()).thenReturn(true);
        when(uriTask.getResult()).thenReturn(URI);

        Answer<Task<Uri>> continueAnswer = new Answer<Task<Uri>>() {
            @Override
            public Task<Uri> answer(InvocationOnMock invocation) throws Exception {
                Task<UploadTask.TaskSnapshot> insideTask = mock(Task.class);
                when(insideTask.isSuccessful()).thenReturn(working);

                Continuation<UploadTask.TaskSnapshot, Task<Uri>> continuation =
                        invocation.getArgument(0);
                continuation.then(insideTask);
                return uriTask;
            }
        };
        doAnswer(continueAnswer).when(uploadTask).continueWithTask(any(Continuation.class));

        Answer<Void> completeAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Exception {
                OnCompleteListener<Uri> complete = invocation.getArgument(0);
                complete.onComplete(uriTask);
                return null;
            }
        };
        doAnswer(completeAnswer).when(uriTask).addOnCompleteListener(any(OnCompleteListener.class));
    }

    private OnCompleteListener<Uri> getListener() {
        return new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    task.getResult();
                } else {
                    Assert.fail();
                }
            }
        };
    }

    @Test
    public void removeWorksCorrectly() {
        FirebaseStorage storage = mock(FirebaseStorage.class);
        StorageReference ref = mock(StorageReference.class);
        when(storage.getReferenceFromUrl(anyString())).thenReturn(ref);
        when(ref.delete()).thenReturn(null);
    }
}
