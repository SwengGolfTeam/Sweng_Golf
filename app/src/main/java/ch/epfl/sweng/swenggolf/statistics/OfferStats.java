package ch.epfl.sweng.swenggolf.statistics;

import android.util.Log;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;

/**
 * A class that allows to manage the number of views of the offers.
 */
public class OfferStats {
    public static final int INITIAL_NB_VIEWS = 0;
    private static final String LOG_KEY_STATISTICS = "STATS";
    private static final String LOG_KEY_RETRO = "RETRO-COMPATIBILITY";

    private OfferStats() {
    }

    /**
     * Sets the number of views for a new offer to the default value.
     *
     * @param offer the specific offer
     */
    public static void initializeNbViews(Offer offer) {
        Database.getInstance()
                .write(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), INITIAL_NB_VIEWS);
    }

    private static void writeNbViews(Offer offer, Integer nb) {
        Database.getInstance()
                .write(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), nb);
    }

    /**
     * Recovers the number of views for a specific offer in the database.
     *
     * @param listener The value listener where to return the value
     * @param offer    the specific offer
     */
    public static void getNbViews(final ValueListener<Integer> listener, Offer offer) {
        Database.getInstance()
                .read(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), listener, Integer.class);
    }

    /**
     * Increments the number of views for an offer in the database.
     *
     * @param offer the specific offer
     */
    public static void updateNbViews(final Offer offer) {
        ValueListener<Integer> listener = new ValueListener<Integer>() {
            @Override
            public void onDataChange(Integer nb) {
                Log.d(LOG_KEY_STATISTICS, "Updated views for offer " + offer.getUuid());
                writeNbViews(offer, nb + 1);
            }

            @Override
            public void onCancelled(DbError error) {
                manageRetrocompatibility(error, offer);
            }
        };

        getNbViews(listener, offer);
    }

    /**
     * Deletes the number of views from an Offer in the database.
     *
     * @param offer the specific offer
     */
    public static void removeNbViews(final Offer offer) {
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                Log.d(LOG_KEY_STATISTICS, "Deleted stats for offer " + offer.getUuid());
            }
        };
        Database.getInstance().remove(Database.STATISTICS_OFFERS_PATH, offer.getUuid(), listener);
    }

    /**
     * Handles the case where it it an old Offer without the statistics.
     * @param error the error returned by the database
     * @param offer the specific offer
     */
    public static void manageRetrocompatibility(DbError error, Offer offer){
        if (error == DbError.DATA_DOES_NOT_EXIST){
            initializeNbViews(offer);
            Log.d(LOG_KEY_RETRO, "Stats generated for old offer "+offer.getUuid());
        } else {
            Log.e(LOG_KEY_STATISTICS, "Failed to load views for offer " + offer.getUuid());
        }
    }
}
