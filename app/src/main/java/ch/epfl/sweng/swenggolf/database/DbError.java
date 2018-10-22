package ch.epfl.sweng.swenggolf.database;

import com.google.firebase.database.DatabaseError;

public enum DbError {
    NONE {
        @Override
        public String toString() {
            return "Everything went fine";
        }
    },
    DATA_STALE {
        @Override
        public String toString() {
            return "Data is outdated";
        }
    },
    DISCONNECTED {
        @Override
        public String toString() {
            return "Connection interrupted";
        }
    },
    EXPIRED_TOKEN {
        @Override
        public String toString() {
            return "Connection as expired";
        }
    },
    INVALID_TOKEN {
        @Override
        public String toString() {
            return "Connection is invalid";
        }
    },
    MAX_RETRIES {
        @Override
        public String toString() {
            return "Max trials reached";
        }
    },
    NETWORK_ERROR {
        @Override
        public String toString() {
            return "Network unavailable";
        }
    },
    OPERATION_FAILED {
        @Override
        public String toString() {
            return "Operation failed";
        }
    },
    OVERRIDDEN_BY_SET {
        @Override
        public String toString() {
            return "Overriden by set";
        }
    },
    PERMISSION_DENIED {
        @Override
        public String toString() {
            return "Permission denied";
        }
    },
    UNAVAILABLE {
        @Override
        public String toString() {
            return "Service unavailable";
        }
    },
    UNKNOWN_ERROR {
        @Override
        public String toString() {
            return "Unknown error";
        }
    },
    USER_CODE_EXCEPTION {
        @Override
        public String toString() {
            return "User code exception";
        }
    },
    WRITE_CANCELED {
        @Override
        public String toString() {
            return "Write was cancelled";
        }
    };

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
