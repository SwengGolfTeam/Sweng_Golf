package ch.epfl.sweng.swenggolf.database;

import com.google.firebase.database.DatabaseError;

public enum DbError {
    NONE("Everything went fine"),
    DATA_STALE("Data is outdated"),
    DISCONNECTED("Connection interrupted"),
    EXPIRED_TOKEN("Connection as expired"),
    INVALID_TOKEN("Connection is invalid"),
    MAX_RETRIES("Max trials reached"),
    NETWORK_ERROR("Network unavailable"),
    OPERATION_FAILED("Operation failed"),
    OVERRIDDEN_BY_SET("Overriden by set"),
    PERMISSION_DENIED("Permission denied"),
    UNAVAILABLE("Service unavailable"),
    UNKNOWN_ERROR("Unknown error"),
    USER_CODE_EXCEPTION("User code exception"),
    WRITE_CANCELED("Write was cancelled");

    DbError(String message) {
        this.message = message;
    }

    private final String message;

    @Override
    public String toString() {
        return message;
    }

    /**
     * Return the DbError given a firebase error.
     *
     * @param error the firebase error
     * @return the corresponding DbError
     */
    public static DbError getError(com.google.firebase.database.DatabaseError error) {
        switch (error.getCode()) {
            case DatabaseError.DATA_STALE:
                return DATA_STALE;
            case DatabaseError.DISCONNECTED:
                return DISCONNECTED;
            case DatabaseError.EXPIRED_TOKEN:
                return EXPIRED_TOKEN;
            case DatabaseError.INVALID_TOKEN:
                return INVALID_TOKEN;
            case DatabaseError.MAX_RETRIES:
                return MAX_RETRIES;
            case DatabaseError.NETWORK_ERROR:
                return NETWORK_ERROR;
            case DatabaseError.OPERATION_FAILED:
                return OPERATION_FAILED;
            case DatabaseError.OVERRIDDEN_BY_SET:
                return OVERRIDDEN_BY_SET;
            case DatabaseError.PERMISSION_DENIED:
                return PERMISSION_DENIED;
            case DatabaseError.UNAVAILABLE:
                return UNAVAILABLE;
            case DatabaseError.UNKNOWN_ERROR:
                return UNKNOWN_ERROR;
            case DatabaseError.USER_CODE_EXCEPTION:
                return USER_CODE_EXCEPTION;
            case DatabaseError.WRITE_CANCELED:
                return WRITE_CANCELED;
            default:
                throw new IllegalArgumentException("The error code is incorrect.");
        }
    }
}
