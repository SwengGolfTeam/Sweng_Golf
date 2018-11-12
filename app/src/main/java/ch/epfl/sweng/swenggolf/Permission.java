package ch.epfl.sweng.swenggolf;

public enum Permission {
    NONE, GPS;

    /**
     * Returns the default value of the enum.
     *
     * @return the default Category value
     */
    public static Permission getDefault() {
        return Permission.NONE;
    }
}
