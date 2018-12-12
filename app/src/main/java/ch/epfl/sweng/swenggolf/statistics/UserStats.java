package ch.epfl.sweng.swenggolf.statistics;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.HashMap;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

public class UserStats {
    private static final Integer INITIAL_VALUE = 0;
    private static final String LOG_KEY_STATISTICS = "USER_STATS";
    private HashMap<String, Integer> stats;

    public UserStats(){
        setMap(new HashMap<String, Integer>());
        for(Stats stat: Stats.values()){
            put(stat, INITIAL_VALUE);
        }
    }

    public void put(Stats stat, Integer value){
        stats.put(stat.toString(), value);
    }

    //For Firebase write
    public HashMap<String, Integer> getMap(){
        return stats;
    }

    @Exclude
    public Collection<Integer> getValues(){
        return stats.values();
    }

    @Exclude
    public Stats getKey(Integer i){
        return Stats.valueOf((String)stats.keySet().toArray()[i]);
    }

    // for Firebase read
    public void setMap(HashMap<String, Integer> map){
        stats = map;
    }

    @Exclude
    public Integer getValue(Stats stat){
        return stats.get(stat.toString());
    }

    public int size(){
        return stats.size();
    }

    public void write(String userId) {
        Log.d(LOG_KEY_STATISTICS, "write stats for user "+userId);
        Database.getInstance()
                .write(Database.STATISTICS_USERS_PATH, userId, this);
    }

    public static void read(final ValueListener<UserStats> listener, String userId) {
        Log.d(LOG_KEY_STATISTICS, "read stats for user "+userId);
        Database.getInstance()
                .read(Database.STATISTICS_USERS_PATH, userId, listener, UserStats.class);
    }


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

