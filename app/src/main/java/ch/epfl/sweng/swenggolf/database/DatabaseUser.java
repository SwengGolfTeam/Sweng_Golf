package ch.epfl.sweng.swenggolf.database;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.profile.User;

import static ch.epfl.sweng.swenggolf.database.DbError.NONE;

/**
 * Class that regroups all the Users related database code.
 */
public class DatabaseUser {

    public static final String POINTS = "points";

    private DatabaseUser() {
    }

    /**
     * Add a User to the Database.
     *
     * @param user the User to add
     */
    public static void addUser(User user) {
        Database.getInstance().write(Database.USERS_PATH, user.getUserId(), user);
    }


    /**
     * Get a User in the Database.
     *
     * @param listener the listener which will provide the corresponding user
     * @param userId   the id of the user we want to find
     */
    public static void getUser(final ValueListener<User> listener, String userId) {
        Database.getInstance().read(Database.USERS_PATH, userId, listener, User.class);
    }

    /**
     * Get a User in the Database.
     *
     * @param listener the listener which will provide the corresponding user
     * @param user     the user we want to find
     */
    public static void getUser(final ValueListener<User> listener, User user) {
        getUser(listener, user.getUserId());
    }

    private static void addPointsToAppropriateUser(final int scoredPoints, final String userId,
                                                   final CompletionListener complete) {
        final String userPath = Database.USERS_PATH + "/" + userId;
        Database.getInstance().read(userPath, POINTS,
                new ValueListener<Integer>() {
                    @Override
                    public void onDataChange(Integer value) {
                        writePointsToDatabase(userPath, value+scoredPoints, complete);
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        if (error == DbError.DATA_DOES_NOT_EXIST){
                            writePointsToDatabase(userPath, scoredPoints, complete);
                        } else {
                            if (complete != null) {
                                complete.onComplete(error);
                            }
                        }
                    }
                }, Integer.class);
    }

    private static void writePointsToDatabase(String userPath, Integer value,
                                              CompletionListener complete){
        if (complete == null){
            Database.getInstance().write(userPath,
                    POINTS, value);
        } else {
            Database.getInstance().write(userPath,
                    POINTS, value, complete);
        }
    }

    /**
     * Calls the database to update the points of a user according to his ID.
     *
     * @param scoreToAdd the points to add to the user current score.
     * @param userId     the ID of the user that has his points increased.
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
     * @param user        the user that has his points increased.
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

    /**
     * Add Points to current User.
     *
     * @param points the amount of point we want to add
     */
    public static void addPointsToCurrentUser(final int points) {
        addPointsToUser(points, Config.getUser());
    }

}
