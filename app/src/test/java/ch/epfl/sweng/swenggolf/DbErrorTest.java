package ch.epfl.sweng.swenggolf;

import android.util.Log;

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

    /**
     * Assert that toString from a DbError returns the good value.
     *
     * @param error DbError under test
     * @param errorMsg expected error message
     */
    public void toStringAssert(DbError error, String errorMsg) {
        assertThat(error.toString(), is(errorMsg));
    }


    @Test
    public void toStringNone() {
        toStringAssert(DbError.NONE,"Everything went fine");
    }

    @Test
    public void toStringDataStale() {
        toStringAssert(DbError.DATA_STALE, "Data is outdated");
    }

    @Test
    public void toStringDisconected() {
        toStringAssert(DbError.DISCONNECTED,"Connection interrupted");
    }


    @Test
    public void toStringExpiredToken() {
        toStringAssert(DbError.EXPIRED_TOKEN, "Connection as expired");
    }


    @Test
    public void toStringInvalidToken() {
        toStringAssert(DbError.INVALID_TOKEN, "Connection is invalid");
    }


    @Test
    public void toStringMaxRetries() {
        toStringAssert(DbError.MAX_RETRIES, "Max trials reached");
    }


    @Test
    public void toStringNetworkError() {
        toStringAssert(DbError.NETWORK_ERROR, "Network unavailable");
    }

    @Test
    public void toStringOperationFailed() {
        toStringAssert(DbError.OPERATION_FAILED, "Operation failed");
    }

    @Test
    public void toStringOveriddenBySet() {
        toStringAssert(DbError.OVERRIDDEN_BY_SET, "Overriden by set");
    }

    @Test
    public void toStringPermissionDenied() {
        toStringAssert(DbError.PERMISSION_DENIED, "Permission denied");
    }

    @Test
    public void toStringUnavailable() {
        toStringAssert(DbError.UNAVAILABLE, "Service unavailable");
    }


    @Test
    public void toStringUnknownError() {
        toStringAssert(DbError.UNKNOWN_ERROR, "Unknown error");
    }


    @Test
    public void toStringCodeException() {
        toStringAssert(DbError.USER_CODE_EXCEPTION, "User code exception");
    }


    @Test
    public void toStringWriteCancelled() {
        toStringAssert(DbError.WRITE_CANCELED, "Write was cancelled");
    }

    /**
     * Checks that the message isn't to long to be displayed as a Log debug tag.
     *
     * @param error the DbError under test
     */
    public void isLoggableAssert(DbError error) {
        assertThat(error.toString().length() <= 23, is(true));
    }

    @Test
    public void isLoggableNone() {
        isLoggableAssert(DbError.NONE);
    }

    @Test
    public void isLoggableDataStale() {
        isLoggableAssert(DbError.DATA_STALE);
    }

    @Test
    public void isLoggableDisconected() {
        isLoggableAssert(DbError.DISCONNECTED);
    }


    @Test
    public void isLoggableExpiredToken() {
        isLoggableAssert(DbError.EXPIRED_TOKEN);
    }


    @Test
    public void isLoggableInvalidToken() {
        isLoggableAssert(DbError.INVALID_TOKEN);
    }


    @Test
    public void isLoggableMaxRetries() {
        isLoggableAssert(DbError.MAX_RETRIES);
    }


    @Test
    public void isLoggableNetworkError() {
        isLoggableAssert(DbError.NETWORK_ERROR);
    }

    @Test
    public void isLoggableOperationFailed() {
        isLoggableAssert(DbError.OPERATION_FAILED);
    }

    @Test
    public void isLoggableOveriddenBySet() {
        isLoggableAssert(DbError.OVERRIDDEN_BY_SET);
    }

    @Test
    public void isLoggablePermissionDenied() {
        isLoggableAssert(DbError.PERMISSION_DENIED);
    }

    @Test
    public void isLoggableUnavailable() {
        isLoggableAssert(DbError.UNAVAILABLE);
    }


    @Test
    public void isLoggableUnknownError() {
        isLoggableAssert(DbError.UNKNOWN_ERROR);
    }


    @Test
    public void isLoggableCodeException() {
        isLoggableAssert(DbError.USER_CODE_EXCEPTION);
    }


    @Test
    public void isLoggableWriteCancelled() {
        isLoggableAssert(DbError.WRITE_CANCELED);
    }
}

