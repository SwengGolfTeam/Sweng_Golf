package ch.epfl.sweng.swenggolf;

public abstract class Database {

    public abstract void addUser(User user);

    public abstract void containsUser(final ExistsOnData listener, User user);

}
