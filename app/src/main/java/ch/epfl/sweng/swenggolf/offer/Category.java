package ch.epfl.sweng.swenggolf.offer;

public enum Category {
    OTHER, FOOD, SERVICE, DRINKS, TEST;

    /**
     * Returns the default value of the enum.
     *
     * @return the default Category value
     */
    public static Category getDefault() {
        return Category.OTHER;
    }

    private int value;

    static {
        OTHER.value = 0;
        FOOD.value = 1;
        SERVICE.value = 2;
        DRINKS.value = 3;
        TEST.value = 4;
    }

    public int toInt() {
        return value;
    }
}
