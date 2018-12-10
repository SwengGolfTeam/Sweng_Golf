package ch.epfl.sweng.swenggolf.statistics;

import android.util.Log;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;

public class OfferStats {
    private static final int INITIAL_NB_VIEWS = 0;

    // hidden constructor as we only have one field for these stats
    private OfferStats () {}
    // TODO when delete offer -> delete stats as well

    // TODO with completion/Value Listeners?
    public static void initializeNbViews(Offer offer){
        Database.getInstance().write(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), INITIAL_NB_VIEWS);
    }

    private static void writeNbViews(Offer offer, Integer nb){
        Database.getInstance().write(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), nb);
    }

    public static void getNbViews(final ValueListener<Integer> listener, Offer offer){
        Database.getInstance().read(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), listener, Integer.class);
    }

    public static void updateNbViews(final Offer offer){
        ValueListener<Integer> listener = new ValueListener<Integer>() {
            @Override
            public void onDataChange(Integer nb) {
                Log.d("STATS","Updated views for offer " + offer.getUuid());
                writeNbViews(offer, nb+1);
            }

            @Override
            public void onCancelled(DbError error) {
                Log.e("STATS","Failed to update views for offer " + offer.getUuid());
            }
        };

        getNbViews(listener, offer);
    }

    public static void removeNbViews(final Offer offer){
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                Log.d("STATS", "Deleted stats for offer " + offer.getUuid());
            }
            // TODO onFail() ?
        };
        Database.getInstance().remove(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), listener);
    }
}
