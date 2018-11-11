package ch.epfl.sweng.swenggolf.offer;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

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

    public static String categoriesToSingleString(List<Category> categories){
        List<String> list = new ArrayList<>();

        for (Category cat : Category.values()){
            if (categories.contains(cat))
                list.add("true");
            else
                list.add("false");
        }

        return TextUtils.join(",", list);
    }

    public static List<Category> singleStringToCategories(String list){
        String[] array = TextUtils.split(list, ",");
        List<Category> categories = new ArrayList<>();
        Category[] cat = Category.values();
        for (int i=0; i< cat.length; i++){
            if (array[i].equals("true"))
                categories.add(cat[i]);
        }

        return categories;
    }
}
