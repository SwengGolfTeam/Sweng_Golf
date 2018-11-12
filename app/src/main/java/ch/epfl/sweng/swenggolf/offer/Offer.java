package ch.epfl.sweng.swenggolf.offer;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Calendar;
import static ch.epfl.sweng.swenggolf.tools.Check.checkString;

public class Offer implements Parcelable {
    private static final int DESCRIPTION_LIMIT = 140;
    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 100;
    public static final int DESCRIPTION_MIN_LENGTH = 1;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;


    private final Category tag;
    private final String userId;
    private final String title;
    private final String description;
    private final String linkPicture;
    private final String uuid;

    private  long creationDate;
    private  long endDate;

    /**
     * Contains the data of an offer.
     *
     * @param title       the title of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     * @param linkPicture the link of the offer's picture
     * @param userId      the user id. Should not be empty
     * @param uuid        offer identifier
     * @param tag         the category of the offer
     */
    public Offer(String userId, String title, String description,
                 String linkPicture, String uuid, Category tag,
                 long creationDate, long endDate) {

        if (userId.isEmpty()) {
            throw new IllegalArgumentException("UserId of the offer can't be empty.");
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Name of the offer can't be empty.");
        }
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Description of the offer can't be empty.");
        }
        if (tag == null) {
            throw new IllegalArgumentException("Tag must be indicated or use other constructor");
        }


        this.tag = tag;
        this.userId = userId;
        this.title = checkString(title, "title", TITLE_MIN_LENGTH, TITLE_MAX_LENGTH);
        this.description = checkString(description, "description", DESCRIPTION_MIN_LENGTH,
                DESCRIPTION_MAX_LENGTH);
        this.linkPicture = linkPicture;
        this.uuid = uuid;
        this.creationDate = creationDate;
        this.endDate = endDate;
    }

    public Offer(String userId, String title, String description,
                 String linkPicture, String uuid, Category tag,
                 Calendar creationDate, Calendar endDate) {
        this(userId,title,description,linkPicture,uuid,tag,
                creationDate.getTimeInMillis(),
                endDate.getTimeInMillis());
    }
    /**
     * Contains the data of an offer.
     *
     * @param title       the title of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     * @param linkPicture the link of the offer's picture
     * @param userId      the user id. Should not be empty
     * @param uuid        offer identifier
     */
    public Offer(String userId, String title, String description,
                 String linkPicture, String uuid) {
        this(userId, title, description, linkPicture, uuid, Category.getDefault(), Calendar.getInstance(), Calendar.getInstance());
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
        this.tag = Category.getDefault();
        creationDate = Calendar.getInstance().getTimeInMillis();
        endDate = Calendar.getInstance().getTimeInMillis();
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
        tag = that.tag;
        creationDate = that.creationDate;
        endDate = that.endDate;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Offer) {
            Offer other = (Offer) obj;
            boolean userIdEquality = userId.equals(other.userId);
            boolean titleEquality = title.equals(other.title);
            boolean descriptionEquality = description.equals(other.description);
            boolean linkPictureEquality = linkPicture.equals(other.linkPicture);
            boolean uuidEquality = uuid.equals(other.uuid);
            boolean tagEquality = tag.equals(other.tag);
            return userIdEquality && titleEquality && descriptionEquality
                    && linkPictureEquality && uuidEquality && tagEquality;
        }
        return false;
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
     * Returns the offer's tag.
     *
     * @return the category of the offer
     */
    public Category getTag() {
        return tag;
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
     * Returns the offer's creation date.
     *
     * @return the offer's creation date
     */
    public long getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the offer's end date.
     *
     * @return the offer's end date
     */
    public long getEndDate() {
        return endDate;
    }

    /**
     * Creates a new offer in the database using the new picture's link given.
     *
     * @param newLinkPicture the new picture's link
     */
    public Offer updateLinkToPicture(String newLinkPicture) {
        return new Offer(userId, title, description, newLinkPicture, uuid, tag, Calendar.getInstance(), Calendar.getInstance());
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
                this.uuid,
                this.tag.toString(),
                Long.toString(this.creationDate),
                Long.toString(this.endDate)
        });
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
        String[] data = new String[8];
        in.readStringArray(data);
        this.userId = data[0];
        this.title = data[1];
        this.description = data[2];
        this.linkPicture = data[3];
        this.uuid = data[4];
        this.tag = Category.valueOf(data[5]);
        this.creationDate = Long.parseLong(data[6]);
        this.endDate = Long.parseLong(data[7]);
    }

}
