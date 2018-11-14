package ch.epfl.sweng.swenggolf;

import android.app.Activity;

import org.junit.Test;

import java.util.concurrent.Executor;

import ch.epfl.sweng.swenggolf.storage.FakeStorage;

import static org.junit.Assert.assertNull;

public class FakeStorageTest {

    private static final FakeStorage.FakeTask FAKE_TASK = new FakeStorage.FakeTask(null, true);

    @Test
    public void unusedElementsAreNull() throws Throwable {
        assertNull(FAKE_TASK.addOnFailureListener(null));
        assertNull(FAKE_TASK.addOnFailureListener((Activity) null, null));
        assertNull(FAKE_TASK.addOnFailureListener((Executor) null, null));
        assertNull(FAKE_TASK.addOnSuccessListener(null));
        assertNull(FAKE_TASK.addOnSuccessListener((Activity) null, null));
        assertNull(FAKE_TASK.addOnSuccessListener((Executor) null, null));
        assertNull(FAKE_TASK.getResult(null));
    }

    @Test(expected = Exception.class)
    public void throwsCorrectException() throws Exception {
        Exception e = FAKE_TASK.getException();
        throw e;
    }


}
