package ch.epfl.sweng.swenggolf.notification;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

public class Notification {
    private final NotificationType type;
    private final Offer concernedOffer;
    private final User concernedUser;
    private final String uuid;

    public Notification(NotificationType type, @Nullable User concernedUser, @Nullable Offer concernedOffer) {
        this.type = type;
        this.concernedUser = concernedUser;
        this.concernedOffer = concernedOffer;
        uuid = UUID.randomUUID().toString();
    }

    // empty constructor for firebase
    public Notification() {
        type = null;
        concernedUser = null;
        concernedOffer = null;
        uuid = "";
    }

    public NotificationType getType() {
        return type;
    }

    public User getConcernedUser() {
        return concernedUser;
    }

    public Offer getConcernedOffer() {
        return concernedOffer;
    }

    public String getUuid() {
        return uuid;
    }
}
