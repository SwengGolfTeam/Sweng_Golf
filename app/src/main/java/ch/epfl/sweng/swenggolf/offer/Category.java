package ch.epfl.sweng.swenggolf.offer;

public enum Category {
    OTHER, FOOD, SERVICE, DRINKS, TEST;

    /**
     * Returns the default value of the enum
     * @return the default Category value
     */
    public static Category getDefault(){
        return Category.OTHER;
    }
}
