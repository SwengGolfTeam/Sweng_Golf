package ch.epfl.sweng.swenggolf.database;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.profile.User;

import static ch.epfl.sweng.swenggolf.database.DbError.NONE;

public class DatabaseUser {

    public static final String POINTS = "points";

    private DatabaseUser() {
    }

    public static void addUser(User user) {
        Database.getInstance().write(Database.USERS_PATH, user.getUserId(), user);
    }


    public static void getUser(final ValueListener<User> listener, User user) {
        getUser(listener, user.getUserId());
    }

    public static void getUser(final ValueListener<User> listener, String userId) {
        Database.getInstance().read(Database.USERS_PATH, userId, listener, User.class);
    }

    private static void addPointsToAppropriateUser(final int scoredPoints, final String userId,
                                                   final CompletionListener complete) {
        Database.getInstance().read(Database.USERS_PATH + "/" + userId, POINTS,
                new ValueListener<Integer>() {
                    @Override
                    public void onDataChange(Integer value) {
                        value = (value == null) ? 0 : value;
                        if (complete == null) {
                            Database.getInstance().write(Database.USERS_PATH + "/" + userId,
                                    POINTS, value + scoredPoints);
                        } else {
                            Database.getInstance().write(Database.USERS_PATH + "/" + userId,
                                    POINTS, value + scoredPoints, complete);
                        }
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        //TODO what to do when score fails to update ?
                    }
                }, Integer.class);
    }

    /**
     * Calls the database to update the points of a user according to his ID.
     *
     * @param scoreToAdd the points to add to the user current score.
     * @param userId the ID of the user that has his points increased.
     */
    public static void addPointsToUserId(int scoreToAdd, String userId) {
        if (userId.equals(Config.getUser().getUserId())) {
            addPointsToCurrentUser(scoreToAdd);
        } else {
            addPointsToAppropriateUser(scoreToAdd, userId, null);
        }
    }

    /**
     * Calls the database to update the points of a user according to his object.
     * Adds the points to the object when the write to the database is completed.
     *
     * @param scorePoints the points scored by the user.
     * @param user the user that has his points increased.
     */
    public static void addPointsToUser(final int scorePoints, final User user) {
        addPointsToAppropriateUser(scorePoints, user.getUserId(),
                new CompletionListener() {
                    @Override
                    public void onComplete(DbError error) {
                        if (error == NONE) {
                            user.addPoints(scorePoints);
                        }
                    }
                });
    }

    public static void addPointsToCurrentUser(final int points) {
        addPointsToUser(points, Config.getUser());
    }

}
