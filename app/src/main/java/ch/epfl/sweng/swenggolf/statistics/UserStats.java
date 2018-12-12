package ch.epfl.sweng.swenggolf.statistics;

import java.util.HashMap;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

public class UserStats extends HashMap<String, Integer>{
    private static final Integer INITIAL_VALUE = 0;

    UserStats(){
        for(Stats stat: Stats.values()){
            put(stat, INITIAL_VALUE);
        }
    }

    public void put(Stats stat, Integer value){
        super.put(stat.toString(), value);
    }

    public Integer getValue(Stats stat){
        //TODO if map does not contain? handle it!
        return this.get(stat.toString());
    }

    public void write(User user) {
        Database.getInstance()
                .write(Database.STATISTICS_USERS_PATH, user.getUserId(), this);
    }

    public void read(final ValueListener<UserStats> listener, User user) {
        Database.getInstance()
                .read(Database.STATISTICS_USERS_PATH, user.getUserId(), listener, UserStats.class);
    }

    public enum Stats {
        CREATED_OFFERS("Offers created"),
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

