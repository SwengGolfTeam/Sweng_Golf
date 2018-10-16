package ch.epfl.sweng.swenggolf.database;

public enum DatabaseError {
    NONE,DATA_STALE, DISCONNECTED, EXPIRED_TOKEN, INVALID_TOKEN , MAX_RETRIES, NETWORK_ERROR,
    OPERATION_FAILED, OVERRIDDEN_BY_SET, PERMISSION_DENIED, UNAVAILABLE, UNKNOWN_ERROR,
    USER_CODE_EXCEPTION, WRITE_CANCELED;

    /**
     * Return the DatabaseError given a firebase error.
     * @param error the firebase error
     * @return the corresponding DatabaseError
     */
    public static DatabaseError getError(com.google.firebase.database.DatabaseError error) {
        switch(error.getCode()){
            case com.google.firebase.database.DatabaseError.DATA_STALE :
                return DATA_STALE;
            case com.google.firebase.database.DatabaseError.DISCONNECTED :
                return DISCONNECTED;
            case com.google.firebase.database.DatabaseError.EXPIRED_TOKEN :
                return EXPIRED_TOKEN;
            case com.google.firebase.database.DatabaseError.INVALID_TOKEN :
                return INVALID_TOKEN;
            case com.google.firebase.database.DatabaseError.MAX_RETRIES :
                return MAX_RETRIES;
            case com.google.firebase.database.DatabaseError.NETWORK_ERROR :
                return NETWORK_ERROR;
            case com.google.firebase.database.DatabaseError.OPERATION_FAILED :
                return OPERATION_FAILED;
            case com.google.firebase.database.DatabaseError.OVERRIDDEN_BY_SET :
                return OVERRIDDEN_BY_SET;
            case com.google.firebase.database.DatabaseError.PERMISSION_DENIED :
                return PERMISSION_DENIED;
            case com.google.firebase.database.DatabaseError.UNAVAILABLE :
                return UNAVAILABLE;
            case com.google.firebase.database.DatabaseError.UNKNOWN_ERROR :
                return UNKNOWN_ERROR;
            case com.google.firebase.database.DatabaseError.USER_CODE_EXCEPTION :
                return USER_CODE_EXCEPTION;
            case com.google.firebase.database.DatabaseError.WRITE_CANCELED :
                return WRITE_CANCELED;
            default:
                throw new IllegalArgumentException("The error code is incorrect.");
        }
    }
}
