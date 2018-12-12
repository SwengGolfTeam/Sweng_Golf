package ch.epfl.sweng.swenggolf.statistics;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.HashMap;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * A class that allows to manage the statistics of a User.
 */
public class UserStats {
    public static final Integer INITIAL_VALUE = 0;
    private static final String LOG_KEY_STATISTICS = "USER_STATS";
    private HashMap<String, Integer> stats;

    /**
     * Builds the stats with the default value for all of them.
     */
    public UserStats(){
        setMap(new HashMap<String, Integer>());
        for(Stats stat: Stats.values()){
            put(stat, INITIAL_VALUE);
        }
    }

    /**
     * Adds a value for a specific stats in the Map.
     * @param stat The stat that we want to add/change
     * @param value The Integer value
     */
    public void put(Stats stat, Integer value){
        stats.put(stat.toString(), value);
    }

    /**
     * A getter of the Map that is needed by Firebase.
     * @return The Map
     */
    public HashMap<String, Integer> getMap(){
        return stats;
    }

    /**
     * Returns the i-th key of the Map.
     * @param i the index of the wanted element
     * @return the corresponding key
     */
    @Exclude
    public Stats getKey(Integer i){
        return Stats.valueOf((String)stats.keySet().toArray()[i]);
    }

    /**
     * A setter that is needed for the Firebase read function.
     * @param map the HashMap with the stats
     */
    public void setMap(HashMap<String, Integer> map){
        stats = map;
    }

    /**
     * Returns the value associated to a statistic.
     * @param stat the specific Stat
     * @return the value of it
     */
    @Exclude
    public Integer getValue(Stats stat){
        return stats.get(stat.toString());
    }

    /**
     * Gets the size of the Map that stores the statistics.
     * @return the size
     */
    public int size(){
        return stats.size();
    }

    /**
     * Writes the stats of a specific User to the database.
     * @param userId the id of the specific user
     */
    public void write(String userId) {
        Log.d(LOG_KEY_STATISTICS, "write stats for user "+userId);
        Database.getInstance()
                .write(Database.STATISTICS_USERS_PATH, userId, this);
    }

    /**
     * Reads the stats of a specific User from the database.
     * @param listener the ValueListener associated to the operation
     * @param userId the id of the User
     */
    public static void read(final ValueListener<UserStats> listener, String userId) {
        Log.d(LOG_KEY_STATISTICS, "read stats for user "+userId);
        Database.getInstance()
                .read(Database.STATISTICS_USERS_PATH, userId, listener, UserStats.class);
    }

    /**
     * Increments (by a specific amount) a statistic for a specific User.
     * @param stat the specific Stat to increment
     * @param userId the id of the user
     * @param nb The amount to increment
     */
    public static void updateStat(final Stats stat, final String userId, final int nb){
        ValueListener<UserStats> listener = new ValueListener<UserStats>() {
            @Override
            public void onDataChange(UserStats stats) {
                Log.d(LOG_KEY_STATISTICS, "Updated "+stat.toString()+" for user " + userId);
                int oldValue = stats.getValue(stat);
                stats.put(stat, oldValue+nb);
                stats.write(userId);
            }

            @Override
            public void onCancelled(DbError error) {
                checkBackwardsCompatibility(error, userId);
            }
        };

        read(listener, userId);
    }

    /**
     * Handles the case where it it an old User without statistics in the database yet.
     * @param error the error returned by the database
     * @param userId the id of the specific User
     */
    public static void checkBackwardsCompatibility(DbError error, String userId) {
        if (error == DbError.DATA_DOES_NOT_EXIST){
            UserStats initStats = new UserStats();
            initStats.write(userId);
            Log.d(LOG_KEY_STATISTICS, "Stats generated for user "+userId);
        } else {
            Log.e(LOG_KEY_STATISTICS, "Failed to load stats for user " + userId);
        }
    }

    public enum Stats {
        OFFERS_CREATED("Offers created"),
        OFFERS_CLOSED("Offers closed"),
        OFFERS_DELETED("Offers deleted"),
        OFFERS_READ("Offers read"),
        OFFERS_TOTAL_VIEWS("Total views of own offers"),
        MESSAGES_SENT("Direct messages sent"),
        MESSAGES_RECEIVED("Direct messages received"),
        ANSWERS_POSTED("Answers posted"),
        ANSWERS_ACCEPTED("Own answers accepted"),
        LOGIN("Total logins");

        private final String text;

        Stats(String text){
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

