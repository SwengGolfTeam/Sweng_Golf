package ch.epfl.sweng.swenggolf.database;

public interface CompletionListener {

    /**
     * Return NONE if there was no error. Otherwise return the error.
     *
     * @param error the error
     */
    void onComplete(DbError error);
}
