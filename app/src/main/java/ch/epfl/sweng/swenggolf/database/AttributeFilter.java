package ch.epfl.sweng.swenggolf.database;

/**
 * Filter used to compare an attribute of a class with it's value.
 */
public final class AttributeFilter {
    private final String attribute;
    private final String value;

    /**
     * Constructor.
     *
     * @param attribute the class attribute
     * @param value     the value to compare
     */
    public AttributeFilter(String attribute, String value) {
        if (attribute == null || value == null) {
            throw new IllegalArgumentException("The arguments can't be null.");
        }
        if (attribute.isEmpty() || value.isEmpty()) {
            throw new IllegalArgumentException("The arguments can't be empty.");
        }
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Get the attribute we choose to compare.
     *
     * @return the attribute we choose to compare
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Get the value to compare.
     *
     * @return the value to compare
     */
    public String getValue() {
        return value;
    }

}
