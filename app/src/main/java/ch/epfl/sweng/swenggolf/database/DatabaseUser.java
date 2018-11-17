package ch.epfl.sweng.swenggolf.database;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.profile.User;

import static ch.epfl.sweng.swenggolf.database.DbError.NONE;

public class DatabaseUser {

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

    public static void addPointsToUserId(int scoreToAdd, String userId) {
        if (userId.equals(Config.getUser().getUserId())) {
            addPointsToCurrentUser(scoreToAdd);
        } else {
            addPointsToAppropriateUser(scoreToAdd, userId, null);
        }
    }

    private static void addPointsToAppropriateUser(final int scoredPoints, final String userId,
                                                   final CompletionListener complete) {
        Database.getInstance().read(Database.USERS_PATH + "/" + userId, "score",
                new ValueListener<Integer>() {
                    @Override
                    public void onDataChange(Integer value) {
                        value = (value == null) ? 0 : value;
                        if (complete == null) {
                            Database.getInstance().write(Database.USERS_PATH + "/" + userId,
                                    "score", value + scoredPoints);
                        } else {
                            Database.getInstance().write(Database.USERS_PATH + "/" + userId,
                                    "score", value + scoredPoints, complete);
                        }
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        //TODO what to do when score fails to update ?
                    }
                }, Integer.class);
    }

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
