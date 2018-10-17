package ch.epfl.sweng.swenggolf;

import org.junit.Test;
import org.mockito.Mockito;

import ch.epfl.sweng.swenggolf.database.DbError;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DbErrorTest {

    private void compare(DbError error, int errorCode) {
        com.google.firebase.database.DatabaseError firebaseError =
                mock(com.google.firebase.database.DatabaseError.class);
        Mockito.when(firebaseError.getCode()).thenReturn(errorCode);
        assertThat(DbError.getError(firebaseError), is(error));
    }

    @Test
    public void getErrorDataStale() {
        compare(DbError.DATA_STALE, com.google.firebase.database.DatabaseError.DATA_STALE);
    }

    @Test
    public void getErrorDisconected() {
        compare(DbError.DISCONNECTED, com.google.firebase.database.DatabaseError.DISCONNECTED);
    }


    @Test
    public void getErrorExpiredToken() {
        compare(DbError.EXPIRED_TOKEN, com.google.firebase.database.DatabaseError.EXPIRED_TOKEN);
    }


    @Test
    public void getErrorInvalidToken() {
        compare(DbError.INVALID_TOKEN, com.google.firebase.database.DatabaseError.INVALID_TOKEN);
    }


    @Test
    public void getErrorMaxRetries() {
        compare(DbError.MAX_RETRIES, com.google.firebase.database.DatabaseError.MAX_RETRIES);
    }


    @Test
    public void getErrorNetworkError() {
        compare(DbError.NETWORK_ERROR, com.google.firebase.database.DatabaseError.NETWORK_ERROR);
    }


    @Test
    public void getErrorOperationFailed() {
        compare(DbError.OPERATION_FAILED, com.google.firebase.database.DatabaseError.OPERATION_FAILED);
    }


    @Test
    public void getErrorOveriddenBySet() {
        compare(DbError.OVERRIDDEN_BY_SET, com.google.firebase.database.DatabaseError.OVERRIDDEN_BY_SET);
    }


    @Test
    public void getErrorPermissionDenied() {
        compare(DbError.PERMISSION_DENIED, com.google.firebase.database.DatabaseError.PERMISSION_DENIED);
    }

    @Test
    public void getErrorUnavailable() {
        compare(DbError.UNAVAILABLE, com.google.firebase.database.DatabaseError.UNAVAILABLE);
    }


    @Test
    public void getErrorUnknownError() {
        compare(DbError.UNKNOWN_ERROR, com.google.firebase.database.DatabaseError.UNKNOWN_ERROR);
    }


    @Test
    public void getErrorUserCodeException() {
        compare(DbError.USER_CODE_EXCEPTION, com.google.firebase.database.DatabaseError.USER_CODE_EXCEPTION);
    }


    @Test
    public void getErrorWriteCancelled() {
        compare(DbError.WRITE_CANCELED, com.google.firebase.database.DatabaseError.WRITE_CANCELED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getErrorThrowsException() {
        com.google.firebase.database.DatabaseError firebaseError =
                mock(com.google.firebase.database.DatabaseError.class);
        Mockito.when(firebaseError.getCode()).thenReturn(0);
        DbError.getError(firebaseError);
    }


}
