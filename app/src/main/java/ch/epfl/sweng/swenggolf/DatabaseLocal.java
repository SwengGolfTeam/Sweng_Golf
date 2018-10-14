package ch.epfl.sweng.swenggolf;

import java.util.List;

public class DatabaseLocal extends Database {

    private final List<User> users;

    public DatabaseLocal(List<User> users) {
        this.users = users;
    }


    @Override
    public void addUser(User user) {
        users.add(user);

    }

    @Override
    public void containsUser(final DataUser listener, User user) {
        listener.onSuccess(users.contains(user), user);
    }

}
