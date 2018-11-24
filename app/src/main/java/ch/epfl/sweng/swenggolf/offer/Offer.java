package ch.epfl.sweng.swenggolf.offer;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import ch.epfl.sweng.swenggolf.profile.PointType;

import static ch.epfl.sweng.swenggolf.tools.Check.checkString;

/**
 * Class which represents an offer.
 */
public class Offer implements Parcelable {
    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 100;
    public static final int DESCRIPTION_MIN_LENGTH = 1;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;
    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
    public static final String OFFER = "ch.epfl.sweng.swenggolf.offer";
    private static final int DESCRIPTION_LIMIT = 140;
    private final String uuid;
    private final String title;
    private final String description;
    private final long creationDate;
    private final long endDate;
    private final String linkPicture;
    private final String userId;
    private final Category tag;
    private final double longitude;
    private final double latitude;

    private Offer(String uuid, String title, String description, long creationDate, long endDate,
                  String linkPicture, String userId, Category tag,
                  double longitude, double latitude) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.linkPicture = linkPicture;
        this.userId = userId;
        this.tag = tag;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Empty constructor for the listener of Firebase.
     */
    public Offer() {
        creationDate = 0;
        endDate = 0;
        description = "";
        linkPicture = "";
        title = "";
        userId = "";
        tag = Category.getDefault();
        longitude = 0;
        latitude = 0;
        uuid = "";
    }

    private Offer(Parcel in) {
        String[] data = new String[10];

        in.readStringArray(data);
        this.userId = data[0];
        this.title = data[1];
        this.description = data[2];
        this.linkPicture = data[3];
        this.uuid = data[4];
        this.tag = Category.valueOf(data[5]);
        this.creationDate = Long.parseLong(data[6]);
        this.endDate = Long.parseLong(data[7]);
        this.latitude = Double.parseDouble(data[8]);
        this.longitude = Double.parseDouble(data[9]);
    }

    //Objects.requireNonNull API level is 19
    private static <T> T checkNullity(T toCheck, String erroredAttribute) {
        if (toCheck == null) {
            throw new IllegalArgumentException(erroredAttribute + " cannot be null");
        }
        return toCheck;
    }

    private static String checkStringValidity(String toCheck, String erroredAttribute) {
        checkNullity(toCheck, erroredAttribute);
        if (toCheck.isEmpty()) {
            throw new IllegalArgumentException(erroredAttribute + " cannot be empty");
        }
        return toCheck;
    }

    /**
     * Creates the format for the Date.
     *
     * @return the date format
     */
    public static DateFormat dateFormat() {
        return new SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.US);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Offer) {
            Offer other = (Offer) obj;
            return equalsDate(other)
                    && equalsExtras(other)
                    && equalsDescription(other)
                    && this.uuid.equals(other.uuid);
        }
        return false;
    }

    private boolean equalsDescription(Offer other) {
        return this.description.equals(other.description)
                && this.title.equals(other.title)
                && this.userId.equals(other.userId)
                && this.tag.equals(other.tag);
    }

    private boolean equalsExtras(Offer other) {
        return this.longitude == other.longitude
                && this.latitude == other.latitude
                && this.linkPicture.equals(other.linkPicture);
    }

    private boolean equalsDate(Offer other) {
        return this.creationDate == other.creationDate
                && this.endDate == other.endDate;
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
        return getDescription().length() > DESCRIPTION_LIMIT
                ? getDescription().substring(0, DESCRIPTION_LIMIT) + "..."
                : getDescription();
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
     * Returns the offer's locationLatitude.
     *
     * @return the locationLatitude of the offer
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the offer's locationLongitude.
     *
     * @return the locationLongitude of the offer
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Creates a new offer in the database using the new picture's link given.
     *
     * @param newLinkPicture the new picture's link
     */
    public Offer updateLinkToPicture(String newLinkPicture) {
        Offer.Builder builder = new Offer.Builder(this);
        builder.setLinkPicture(newLinkPicture);
        return builder.build();
    }

    /**
     * Sets the location of the offer.
     *
     * @param latitude  the locationLatitude
     * @param longitude the locationLongitude
     */
    public Offer setLocation(double latitude, double longitude) {
        Offer.Builder builder = new Offer.Builder(this);
        builder.setLatitude(latitude);
        builder.setLongitude(longitude);
        return builder.build();
    }

    /* Implements Parcelable */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                getUserId(),
                getTitle(),
                getDescription(),
                getLinkPicture(),
                getUuid(),
                getTag().toString(),
                Long.toString(getCreationDate()),
                Long.toString(getEndDate()),
                Double.toString(getLatitude()),
                Double.toString(getLongitude())});
    }

    /**
     * The points yielded by the offer creation.
     *
     * @return the score of the offer
     */
    public int offerValue() {
        int value = PointType.POST_OFFER.getValue();
        if (!this.linkPicture.isEmpty()) {
            value += PointType.ADD_PICTURE.getValue();
        }
        if (this.latitude != 0 || this.longitude != 0) {
            value += PointType.ADD_LOCALISATION.getValue();
        }
        return value;
    }

    /**
     * The difference in points yielded by the modification of this offer to another offer.
     *
     * @param modifiedOffer the result of this offer being modified
     * @return the points difference
     * @throws IllegalArgumentException if the modifiedOffer is null
     */
    public int offerValueDiff(Offer modifiedOffer) {
        if (modifiedOffer == null) {
            throw new IllegalArgumentException("The offer cannot be modified to null");
        }
        return modifiedOffer.offerValue() - this.offerValue();
    }

    /**
     * Class used to create an offer.
     * If the fields are strings they are initially set to an empty string.
     * Location values are set to  0.
     * Date value are set to the current time.
     * Tag is set to default.
     */
    public static final class Builder {
        private long creationDate;
        private String linkPicture;
        private long endDate;
        private String description;
        private String title;
        private String userId;
        private Category tag;
        private double longitude;
        private double latitude;
        private String uuid;

        /**
         * Creates a new Builder with his fields initialized to the ones of another offer.
         * @param offer the source offer to serve as base values for the new builder.
         */
        public Builder(Offer offer) {
            this.creationDate = offer.creationDate;
            this.endDate = offer.endDate;
            this.description = offer.description;
            this.title = offer.title;
            this.userId = offer.userId;
            this.linkPicture = offer.linkPicture;
            this.tag = offer.tag;
            this.longitude = offer.longitude;
            this.latitude = offer.latitude;
            this.uuid = offer.uuid;
        }

        /**
         * Creates a default builder.
         * The strings are set to empty.
         * The dates are set to the current date.
         * The location values are set to 0.
         * The tag is set to default.
         * The uuid is set to a new uuid.
         */
        public Builder() {
            creationDate = Calendar.getInstance().getTimeInMillis();
            endDate = Calendar.getInstance().getTimeInMillis();
            description = "";
            title = "";
            userId = "";
            tag = Category.getDefault();
            longitude = 0;
            latitude = 0;
            linkPicture = "";
            uuid = UUID.randomUUID().toString();
        }

        public Category getTag() {
            return this.tag;
        }

        public Builder setTag(Category tag) {
            this.tag = tag;
            return this;
        }

        public String getUserId() {
            return this.userId;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getTitle() {
            return this.title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return this.description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getLinkPicture() {
            return this.linkPicture;
        }

        public Builder setLinkPicture(String linkPicture) {
            this.linkPicture = linkPicture;
            return this;
        }

        public String getUuid() {
            return uuid;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        /**
         * Performs identically as calling setLongitude and setLatitude.
         * latitude and longitude are extracted from a location.
         *
         * @param location the location that gives latitude and longitude
         * @return this
         */
        public Builder setLocation(Location location) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            return this;
        }

        public long getCreationDate() {
            return this.creationDate;
        }

        public Builder setCreationDate(long creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public long getEndDate() {
            return this.endDate;
        }

        public Builder setEndDate(long endDate) {
            this.endDate = endDate;
            return this;
        }

        /**
         * Creates a new Offer which fields value are equal to the ones of the builder.
         * @throws IllegalArgumentException if the fields are null or don't comply to the format.
         * @return a new Offer object.
         */
        public Offer build() {
            if (creationDate < 0 || endDate < 0 || creationDate > endDate) {
                throw new IllegalArgumentException("CreationDate, endDate must be positive and"
                        + " creation must precede end");
            }
            return new Offer(
                    checkNullity(uuid, "UUID"),
                    checkString(title, "title", TITLE_MIN_LENGTH, TITLE_MAX_LENGTH),
                    checkString(description, "description", DESCRIPTION_MIN_LENGTH,
                            DESCRIPTION_MAX_LENGTH),
                    creationDate, endDate,
                    checkNullity(linkPicture, "picture link"),
                    checkStringValidity(userId, "user ID"),
                    checkNullity(tag, "tag"),
                    longitude, latitude
            );
        }

    }

}
