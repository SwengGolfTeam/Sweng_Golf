package ch.epfl.sweng.swenggolf.offer;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;

public interface DatabaseOfferConsumer {
    void accept(Database db, List<Category> categories, ValueListener<List<Offer>> listener);
}
