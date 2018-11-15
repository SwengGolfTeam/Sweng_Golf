package ch.epfl.sweng.swenggolf.database;

/**
 * This class is used to sort data on a attribute name. We can sort by ascending or descending order
 * and limit the number of elements.
 */
public final class AttributeOrdering {
    private final String attribute;
    private final Ordering ordering;
    private final int numberOfElements;

    private AttributeOrdering(String attribute, Ordering ordering, int numberOfElements) {
        if (attribute == null) {
            throw new IllegalArgumentException("The attribute should not be null.");
        }
        if (attribute.isEmpty()) {
            throw new IllegalArgumentException("The attribute should not be empty.");
        }
        assert (ordering != null);
        if (numberOfElements <= 0) {
            throw new IllegalArgumentException("The number of elements should be positive.");
        }
        this.ordering = ordering;
        this.attribute = attribute;
        this.numberOfElements = numberOfElements;
    }

    /**
     * Return a new AttributeOrdering in ascending order.
     *
     * @param attribute        the attribute to order on
     * @param numberOfElements the number of elements to take
     * @return an AttributeOrdering in ascending order
     */
    public static AttributeOrdering ascendingOrdering(String attribute, int numberOfElements) {
        return new AttributeOrdering(attribute, Ordering.ASCENDING, numberOfElements);
    }


    /**
     * Return a new AttributeOrdering in descending order.
     *
     * @param attribute        the attribute to order on
     * @param numberOfElements the number of elements to take
     * @return an AttributeOrdering in descending order
     */
    public static AttributeOrdering descendingOrdering(String attribute, int numberOfElements) {
        return new AttributeOrdering(attribute, Ordering.DESCENDING, numberOfElements);
    }

    /**
     * Return the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Return the number of elements.
     *
     * @return the number of elements
     */
    public int getNumberOfElements() {
        return numberOfElements;
    }

    /**
     * Return true if the ordering is ascending, false otherwise.
     *
     * @return true if the ordering is ascending, false otherwise
     */
    public boolean isAscending() {
        return ordering.equals(Ordering.ASCENDING);
    }

    /**
     * Return true if the ordering is descending, false otherwise.
     *
     * @return true if the ordering is descending, false otherwise
     */
    public boolean isDescending() {
        return ordering.equals(Ordering.DESCENDING);
    }

    private enum Ordering {
        ASCENDING, DESCENDING;
    }
}
