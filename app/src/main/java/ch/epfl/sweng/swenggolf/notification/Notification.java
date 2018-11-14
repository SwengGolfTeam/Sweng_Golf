package ch.epfl.sweng.swenggolf.notification;

import org.jetbrains.annotations.Nullable;

public class Notification {
    private final NotificationType type;
    private final String concernedUserName;
    private final String concernedOfferName;

    public Notification(NotificationType type, @Nullable String concernedUserName, @Nullable String concernedOfferName) {
        this.type = type;
        this.concernedUserName = concernedUserName;
        this.concernedOfferName = concernedOfferName;
    }

    public NotificationType getType() {
        return type;
    }

    public String getConcernedOfferName() {
        return concernedOfferName;
    }

    public String getConcernedUserName() {
        return concernedUserName;
    }
}
