package ch.epfl.sweng.swenggolf.offer;

import android.view.View;

import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;

public class ListOwnOfferActivity extends ListOfferActivity {
    @Override
    protected void prepareOfferData(View inflated,
                                    DatabaseOfferConsumer dbConsumer, List<Category> categories) {
        super.prepareOfferData(inflated, new DatabaseOfferConsumer() {
            @Override
            public void accept(Database db, List<Category> categories, ValueListener<List<Offer>> listener) {
                db.readOffers(listener, categories, Config.getUser().getUserId());
            }
        }, categories);
    }
}

