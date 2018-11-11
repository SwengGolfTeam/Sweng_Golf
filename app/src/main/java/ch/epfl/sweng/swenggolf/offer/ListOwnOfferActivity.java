package ch.epfl.sweng.swenggolf.offer;

import android.view.View;

import com.google.android.gms.common.util.BiConsumer;

import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;

public class ListOwnOfferActivity extends ListOfferActivity {
    @Override
    protected void prepareOfferData(View inflated,
                                    BiConsumer<Database, ValueListener<List<Offer>>> dbConsumer) {
        super.prepareOfferData(inflated, new BiConsumer<Database, ValueListener<List<Offer>>>() {
            @Override
            public void accept(Database database, ValueListener<List<Offer>> listener) {
                database.readList(Database.OFFERS_PATH, listener, Offer.class, "userId",
                        Config.getUser().getUserId());
            }
        });
    }
}
