package ch.epfl.sweng.swenggolf.notification;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

public class Notification {
    private final NotificationType type;
    private final String userName;
    private final String userId;
    private final String offerName;
    private final String offerId;
    private final String uuid;

    public Notification(NotificationType type, @Nullable User concernedUser, @Nullable Offer concernedOffer) {
        this.type = type;
        if (concernedUser != null) {
            this.userName = concernedUser.getUserName();
            this.userId = concernedUser.getUserId();
        } else {
            this.userName = null;
            this.userId = null;
        }
        if (concernedOffer != null) {
            this.offerName = concernedOffer.getTitle();
            this.offerId = concernedOffer.getUuid();
        } else {
            this.offerName = null;
            this.offerId = null;
        }
        uuid = UUID.randomUUID().toString();
    }

    // empty constructor for firebase
    public Notification() {
        type = null;
        uuid = "";
        userName = null;
        userId = null;
        offerName = null;
        offerId = null;
    }

    public NotificationType getType() {
        return type;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getUuid() {
        return uuid;
    }
}
