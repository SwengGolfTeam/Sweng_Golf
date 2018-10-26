package ch.epfl.sweng.swenggolf.offer;

public enum Category {
    None, Food, Service, Drinks;

    /**
     * Converts a string to a Category instance
     *
     * @param tag the string to convert
     */
    public static Category stringToTag(String tag){
        switch(tag){
            case "Food": return Category.Food;
            case "Service": return Category.Service;
            case "Drinks": return Category.Drinks;
            default: return Category.None;
        }
    }
}
