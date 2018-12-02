package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

public abstract class FakeDatabaseListHandler extends Database {

    private static void handleError(String attribute) {
        throw new IllegalArgumentException("The attribute " + attribute + " doesn't exist");
    }

    private static String getGetter(String attribute) {
        return "get" + Character.toUpperCase(attribute.charAt(0))
                + attribute.substring(1, attribute.length());
    }

    @NonNull
    private static <T> Comparator<T> getComparator(final Method method) {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                Object attribute1 = invokeGetter(method, o1);
                Object attribute2 = invokeGetter(method, o2);

                return compareAttributes(attribute1, attribute2);
            }
        };
    }

    private static <T> Object invokeGetter(Method method, T invokedOn) {
        try {
            return method.invoke(invokedOn);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't access the attribute");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot call method on generic parameter T");
        }
    }

    private static <T> int compareAttributes(T attribute1, T attribute2) {
        if (attribute1 instanceof Comparable && attribute2 instanceof Comparable) {
            return ((Comparable) attribute1).compareTo(attribute2);
        }
        throw new IllegalArgumentException("The attribute is not comparable");
    }

    @Override
    public void readOffers(@NonNull final ValueListener<List<Offer>> listener,
                           final List<Category> categories) {
        List<Offer> offers = getList(Database.OFFERS_PATH);

        if (isWorking()) {
            offers = removeOffersWrongCategories(offers, categories);
            listener.onDataChange(offers);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    private List<Offer> removeOffersWrongCategories(List<Offer> offers, List<Category> categories) {
        List<Offer> list = new ArrayList<>();
        for (Offer o : offers) {
            if (categories.contains(o.getTag())) {
                list.add(o);
            }
        }
        return list;
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c) {
        if (isWorking()) {
            List<T> list = getList(path);
            listener.onDataChange(list);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c, AttributeFilter filter) {
        if (isWorking()) {

            List<T> list = getList(path);

            List<T> newList = filterList(c, filter.getAttribute(), filter.getValue(), list);
            listener.onDataChange(newList);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @Override
    public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener,
                             @NonNull Class<T> c, AttributeOrdering ordering) {
        if (isWorking()) {
            List<T> unsortedList = getList(path);
            List<T> list = sortList(c, ordering, unsortedList);
            listener.onDataChange(list);
        } else {
            listener.onCancelled(DbError.UNKNOWN_ERROR);
        }
    }

    @NonNull
    private <T> List<T> sortList(@NonNull Class<T> c, AttributeOrdering ordering,
                                 List<T> unsortedList) {
        final Method method;
        String getterName = getGetter(ordering.getAttribute());
        try {
            method = c.getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The getter for the attribute does not exist");
        }
        Comparator<T> comparator = getComparator(method);
        Collections.sort(unsortedList, comparator);
        if (ordering.isDescending()) {
            Collections.reverse(unsortedList);
        }
        int minSize = Math.min(ordering.getNumberOfElements(), unsortedList.size());
        return unsortedList.subList(0, minSize);
    }

    private <T> List<T> filterList(@NonNull Class<T> c, String attribute, String value,
                                   List<T> list) {
        List<T> filtered = new ArrayList<>();
        try {

            //Use reflection to check the attribute
            Method method = c.getDeclaredMethod(getGetter(attribute));
            filtered = createFilteredList(method, list, value);

        } catch (IllegalAccessException e) {
            handleError(attribute);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No getter for " + attribute + " attribute");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Generic type T has no getter for this attribute");
        }
        return filtered;
    }

    private <T> List<T> createFilteredList(Method getter, List<T> list, String value)
            throws InvocationTargetException, IllegalAccessException {
        List<T> newList = new ArrayList<>();
        for (T object : list) {
            if (getter.invoke(object).equals(value)) {
                newList.add(object);
            }
        }
        return newList;
    }

    protected abstract <T> List<T> getList(@NonNull String path);

    protected abstract boolean isWorking();
}
