package ch.epfl.sweng.swenggolf;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import ch.epfl.sweng.swenggolf.database.DatabaseError;

public class DatabaseErrorTest {

    private void compare(DatabaseError error, int errorCode) {
        com.google.firebase.database.DatabaseError firebaseError =
                mock(com.google.firebase.database.DatabaseError.class);
        Mockito.when(firebaseError.getCode()).thenReturn(errorCode);
        assertThat(DatabaseError.getError(firebaseError), is(error));
    }

    @Test
    public void getErrorDataStale() {
        compare(DatabaseError.DATA_STALE, com.google.firebase.database.DatabaseError.DATA_STALE);
    }

    @Test
    public void getErrorDisconected() {
        compare(DatabaseError.DISCONNECTED, com.google.firebase.database.DatabaseError.DISCONNECTED);
    }


    @Test
    public void getErrorExpiredToken() {
        compare(DatabaseError.EXPIRED_TOKEN, com.google.firebase.database.DatabaseError.EXPIRED_TOKEN);
    }


    @Test
    public void getErrorInvalidToken() {
        compare(DatabaseError.INVALID_TOKEN, com.google.firebase.database.DatabaseError.INVALID_TOKEN);
    }


    @Test
    public void getErrorMaxRetries() {
        compare(DatabaseError.MAX_RETRIES, com.google.firebase.database.DatabaseError.MAX_RETRIES);
    }


    @Test
    public void getErrorNetworkError() {
        compare(DatabaseError.NETWORK_ERROR, com.google.firebase.database.DatabaseError.NETWORK_ERROR);
    }


    @Test
    public void getErrorOperationFailed() {
        compare(DatabaseError.OPERATION_FAILED, com.google.firebase.database.DatabaseError.OPERATION_FAILED);
    }


    @Test
    public void getErrorOveriddenBySet() {        compare(DatabaseError.OVERRIDDEN_BY_SET, com.google.firebase.database.DatabaseError.OVERRIDDEN_BY_SET);
    }


    @Test
    public void getErrorPermissionDenied() {
        compare(DatabaseError.PERMISSION_DENIED, com.google.firebase.database.DatabaseError.PERMISSION_DENIED);
    }

    @Test
    public void getErrorUnavailable() {
        compare(DatabaseError.UNAVAILABLE, com.google.firebase.database.DatabaseError.UNAVAILABLE);
    }


    @Test
    public void getErrorUnknownError() {
        compare(DatabaseError.UNKNOWN_ERROR, com.google.firebase.database.DatabaseError.UNKNOWN_ERROR);
    }


    @Test
    public void getErrorUserCodeException() {
        compare(DatabaseError.USER_CODE_EXCEPTION, com.google.firebase.database.DatabaseError.USER_CODE_EXCEPTION);
    }


    @Test
    public void getErrorWriteCancelled() {
        compare(DatabaseError.WRITE_CANCELED, com.google.firebase.database.DatabaseError.WRITE_CANCELED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getErrorThrowsException() {
        com.google.firebase.database.DatabaseError firebaseError =
                mock(com.google.firebase.database.DatabaseError.class);
        Mockito.when(firebaseError.getCode()).thenReturn(0);
        DatabaseError.getError(firebaseError);
    }



}
