package ch.epfl.sweng.swenggolf.notification;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * Class which represents a notification.
 */
public class Notification {
    private final NotificationType type;
    private String userName;
    private String userId;
    private String offerName;
    private String offerId;
    private final String uuid;

    /**
     * Create a new notification, in the local sense.
     *
     * @param type           the type of the notification
     * @param concernedUser  the user that performed an action worth sending a notification
     * @param concernedOffer the offer on which that action was performed, if any
     */
    public Notification(NotificationType type, @Nullable User concernedUser,
                        @Nullable Offer concernedOffer) {
        this.type = type;
        if (concernedUser != null) {
            this.userName = concernedUser.getUserName();
            this.userId = concernedUser.getUserId();
        }
        if (concernedOffer != null) {
            this.offerName = concernedOffer.getTitle();
            this.offerId = concernedOffer.getUuid();
        }
        uuid = UUID.randomUUID().toString();
    }

    /**
     * Empty constructor for firebase.
     */
    public Notification() {
        type = null;
        uuid = "";
        userName = null;
        userId = null;
        offerName = null;
        offerId = null;
    }

    /**
     * Get the type of the notification.
     * @return the type of notification
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Get the notification's user name.
     * @return the notification's user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the notification's user id.
     * @return the notification's user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Get the notification's offer.
     * @return the notification's offer
     */
    public String getOfferName() {
        return offerName;
    }

    /**
     * Get the notification's offer id.
     * @return the notification's offer id
     */
    public String getOfferId() {
        return offerId;
    }

    /**
     * Get the notification's user unique id.
     * @return the notification's user unique id
     */
    public String getUuid() {
        return uuid;
    }
}
