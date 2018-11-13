package ch.epfl.sweng.swenggolf.database;

public final class AttributeFilter {
    private final String attribute;
    private final String value;

    /**
     * This is used to compare an attribute of a class with it's value.
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
     * Return the attribute we choose to compare.
     *
     * @return the attribute we choose to compare
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Return the value to compare.
     *
     * @return the value to compare
     */
    public String getValue() {
        return value;
    }

}
