package ch.epfl.sweng.swenggolf.database;

import com.google.firebase.database.DatabaseError;

public enum DBError {
    NONE, DATA_STALE, DISCONNECTED, EXPIRED_TOKEN, INVALID_TOKEN, MAX_RETRIES, NETWORK_ERROR,
    OPERATION_FAILED, OVERRIDDEN_BY_SET, PERMISSION_DENIED, UNAVAILABLE, UNKNOWN_ERROR,
    USER_CODE_EXCEPTION, WRITE_CANCELED;

    /**
     * Return the DatabaseError given a firebase error.
     *
     * @param error the firebase error
     * @return the corresponding DatabaseError
     */
    public static DBError getError(com.google.firebase.database.DatabaseError error) {
        switch (error.getCode()) {
            case DatabaseError.DATA_STALE: return DATA_STALE;
            case DatabaseError.DISCONNECTED: return DISCONNECTED;
            case DatabaseError.EXPIRED_TOKEN: return EXPIRED_TOKEN;
            case DatabaseError.INVALID_TOKEN: return INVALID_TOKEN;
            case DatabaseError.MAX_RETRIES: return MAX_RETRIES;
            case DatabaseError.NETWORK_ERROR: return NETWORK_ERROR;
            case DatabaseError.OPERATION_FAILED: return OPERATION_FAILED;
            case DatabaseError.OVERRIDDEN_BY_SET: return OVERRIDDEN_BY_SET;
            case DatabaseError.PERMISSION_DENIED: return PERMISSION_DENIED;
            case DatabaseError.UNAVAILABLE: return UNAVAILABLE;
            case DatabaseError.UNKNOWN_ERROR: return UNKNOWN_ERROR;
            case DatabaseError.USER_CODE_EXCEPTION: return USER_CODE_EXCEPTION;
            case DatabaseError.WRITE_CANCELED: return WRITE_CANCELED;
            default:
                throw new IllegalArgumentException("The error code is incorrect.");
        }
    }
}
