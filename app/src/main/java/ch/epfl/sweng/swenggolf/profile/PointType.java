package ch.epfl.sweng.swenggolf.profile;

/**
 * Enumeration for the points type.
 */
public enum PointType {

    POST_OFFER(3),
    RESPOND_OFFER(10),
    CLOSE_OFFER(0),
    ADD_PICTURE(1),
    ADD_LOCALISATION(1),
    FOLLOW(2);


    private final int value;

    /**
     * Constructor of a Point type.
     * @param value
     */
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
