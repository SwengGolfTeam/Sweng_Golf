package ch.epfl.sweng.swenggolf.notification;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Notification {
    private final NotificationType type;
    private final String concernedOfferName;
    private final String concernedUserId;
    private final String uuid;

    public Notification(NotificationType type, @Nullable String concernedUserId, @Nullable String concernedOfferName) {
        this.type = type;
        this.concernedUserId = concernedUserId;
        this.concernedOfferName = concernedOfferName;
        uuid = UUID.randomUUID().toString();
    }

    // empty constructor for firebase
    public Notification() {
        type = null;
        concernedUserId = "";
        concernedOfferName = "";
        uuid = "";
    }

    public NotificationType getType() {
        return type;
    }

    public String getConcernedUserId() {
        return concernedUserId;
    }

    public String getConcernedOfferName() {
        return concernedOfferName;
    }

    public String getUuid() {
        return uuid;
    }
}
