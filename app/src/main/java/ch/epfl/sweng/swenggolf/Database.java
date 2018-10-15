package ch.epfl.sweng.swenggolf;

public abstract class Database {

    public abstract void addUser(User user);

    public abstract void containsUser(final UserListener listener, User user);

}
