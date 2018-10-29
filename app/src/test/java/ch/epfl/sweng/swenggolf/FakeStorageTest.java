package ch.epfl.sweng.swenggolf;

import android.app.Activity;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Executor;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
