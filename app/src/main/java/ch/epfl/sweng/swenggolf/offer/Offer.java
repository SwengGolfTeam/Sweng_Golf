package ch.epfl.sweng.swenggolf.offer;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {

    private static final int DESCRIPTION_LIMIT = 140;

    private final String userId;
    private final String title;
    private final String description;
    private final String linkPicture;
    private final String uuid;

    /**
     * Contains the data of an offer.
     *
     * @param title       the title of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     * @param linkPicture the link of the offer's picture.
     * @param userId      the user id. Should not be empty
     * @param uuid        offer identifier
     */
    public Offer(String userId, String title, String description,
                 String linkPicture, String uuid) {

        if (userId.isEmpty()) {
            throw new IllegalArgumentException("UserId of the offer can't be empty.");
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Name of the offer can't be empty.");
        }
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Description of the offer can't be empty.");
        }
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.linkPicture = linkPicture;
        this.uuid = uuid;
    }

    /**
     * Contains the data of an offer.
     *
     * @param userId      the id of the user.
     * @param title       the title of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     */
    public Offer(String userId, String title, String description) {
        this(userId, title, description, "", "");
    }

    /**
     * Empty builder for the listener of Firebase.
     */
    public Offer() {
        this.userId = "";
        this.title = "";
        this.description = "";
        this.linkPicture = "";
        this.uuid = "createdByEmptyConstructor";
    }

    /**
     * Copy constructor.
     *
     * @param that an offer
     */
    public Offer(Offer that) {
        userId = that.userId;
        title = that.title;
        description = that.description;
        linkPicture = that.linkPicture;
        uuid = that.uuid;
    }

    /**
     * Returns the offer's title.
     *
     * @return the name of the offer
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the offer's description.
     *
     * @return the description of the offer
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the offer's userId.
     *
     * @return the userId of the offer
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the shortened offer's description.
     *
     * @return the shortened description of the offer
     */
    public String getShortDescription() {
        return description.length() > DESCRIPTION_LIMIT
                ? description.substring(0, DESCRIPTION_LIMIT) + "..."
                : description;
    }

    /**
     * Returns the offer's url of the picture.
     *
     * @return the url of the picture of the offer
     */
    public String getLinkPicture() {
        return linkPicture;
    }

    /**
     * Returns the offer's uuid.
     *
     * @return the uuid of the offer
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Creates a new offer in the database using the new picture's link given.
     *
     * @param newLinkPicture the new picture's link
     */
    public Offer updateLinkToPicture(String newLinkPicture) {
        return new Offer(userId, title, description, newLinkPicture, uuid);
    }


    /* Implements Parcelable */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.userId,
                this.title,
                this.description,
                this.linkPicture,
                this.uuid});
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    private Offer(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        this.userId = data[0];
        this.title = data[1];
        this.description = data[2];
        this.linkPicture = data[3];
        this.uuid = data[4];
    }

}
