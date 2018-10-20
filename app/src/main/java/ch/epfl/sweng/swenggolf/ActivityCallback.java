package ch.epfl.sweng.swenggolf;

/**
 * ActivityCallback is used in assynchronous class to notify when a computation is done.
 */
public interface ActivityCallback {

    /**
     * Notify that the operation is done.
     */
    public void isDone();

}
