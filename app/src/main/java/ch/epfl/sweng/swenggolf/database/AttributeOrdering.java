package ch.epfl.sweng.swenggolf.database;

public final class AttributeOrdering {
    private final String attribute;
    private final Ordering ordering;
    private final int numberOfElements;
     private AttributeOrdering(String attribute, Ordering ordering, int numberOfElements) {
        if(attribute == null) {
            throw new IllegalArgumentException("The attribute should not be null.");
        }
        if(attribute.isEmpty()) {
            throw new IllegalArgumentException("The attribute should not be empty.");
        }
        assert(ordering != null);
        if(numberOfElements <= 0) {
            throw new IllegalArgumentException("The number of elements should be positive.");
        }
        this.ordering = ordering;
        this.attribute = attribute;
        this.numberOfElements = numberOfElements;
    }

    public static AttributeOrdering ascendingOrdering(String attribute, int numberOfElements) {
         return new AttributeOrdering(attribute, Ordering.ASCENDING, numberOfElements);
    }

    public static AttributeOrdering descendingOrdering(String attribute, int numberOfElements) {
        return new AttributeOrdering(attribute, Ordering.DESCENDING, numberOfElements);
    }

    public String getAttribute() {
         return attribute;
    }

    public int getNumberOfElements() {
         return numberOfElements;
    }

    public boolean isAscending() {
        return ordering.equals(Ordering.ASCENDING);
    }

    public boolean isDescending() {
        return ordering.equals(Ordering.DESCENDING);
    }

    private enum Ordering {
        ASCENDING, DESCENDING;
    }
}
