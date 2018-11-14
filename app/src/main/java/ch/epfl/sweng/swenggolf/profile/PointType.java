package ch.epfl.sweng.swenggolf.profile;

/**
 * Enumeration for the points type.
 */
public enum PointType {

    POST_OFFER(10),
    RESPOND_OFFER(20),
    CLOSE_OFFER(50);

    private final int value;

    PointType(int value) {
        this.value = value;
    }

    /**
     * Return the value of the PointType.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
