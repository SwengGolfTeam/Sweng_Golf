package ch.epfl.sweng.swenggolf.offer.list;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

/**
 * This class is used to provide some flexibility on the different read operation of the database.
 * By implementing this interface you can choose which operation to do on the database.
 */
public interface DatabaseOfferConsumer {
    /**
     * Receive a database and execute a read operation on it.
     *
     * @param db         the database
     * @param categories the categories we want to get
     * @param listener   the listener to get the offers
     */
    void accept(Database db, List<Category> categories, ValueListener<List<Offer>> listener);
}
