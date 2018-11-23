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
    private static final int DESCRIPTION_LIMIT = 140;
    public static final String OFFER = "ch.epfl.sweng.swenggolf.offer";

    private static void checkNullity(Object toCheck, String erroredAttribute) {
        if(toCheck == null) {
            throw new IllegalArgumentException(erroredAttribute + " cannot be null");
        }
    }

    private static void checkStringValidity(String toCheck , String erroredAttribute) {
        checkNullity(toCheck, erroredAttribute);
        if(toCheck.isEmpty()) {
            throw new IllegalArgumentException(erroredAttribute + " cannot be empty");
        }
    }

    private static final class Date {
        private final long dateOfCreation;
        private final long dateOfDeletion;

        private Date(long dateOfCreation, long dateOfDeletion) {
            this.dateOfCreation = dateOfCreation;
            this.dateOfDeletion = dateOfDeletion;
        }

        private static final class Builder {
            private long dateOfCreation;
            private long dateOfDeletion;

            private Builder(Date dates) {
                this.dateOfCreation = dates.dateOfCreation;
                this.dateOfDeletion = dates.dateOfDeletion;
            }

            private Builder() {
                dateOfCreation = dateOfDeletion = Calendar.getInstance().getTimeInMillis();
            }

            private Date build() {
                if(dateOfCreation < 0) {
                    throw new IllegalArgumentException("creation date should be positive");
                }
                if(dateOfDeletion < 0) {
                    throw new IllegalArgumentException("end date should be positive");
                }
                if (dateOfCreation > dateOfDeletion) {
                    throw new IllegalArgumentException("creation date must be before the end date");
                }
                return new Date(dateOfCreation, dateOfDeletion);
            }
        }
    }

    private static final class Description {
        private final String description;
        private final String title;
        private final Category tag;
        private final String userId;

        private Description(String description, String title, Category tag, String userId) {
            this.description = description;
            this.title = title;
            this.tag = tag;
            this.userId = userId;
        }

        private static final class Builder {
            private String description;
            private String title;
            private String userId;
            private Category tag;

            private Builder(Description description) {
                this.description = description.description;
                this.title = description.title;
                this.userId = description.userId;
                this.tag = description.tag;
            }

            private Builder() {
                description = "";
                title = "";
                userId = "";
                tag = Category.getDefault();
            }

            @Override
            public boolean equals(@Nullable Object obj) {
                if(obj instanceof Description) {
                    Description other = (Description) obj;
                    return this.description.equals(other.description)
                            && this.title.equals(other.title)
                            && this.userId.equals(other.userId)
                            && this.tag.equals(other.tag);
                }
                return false;
            }

            private Description build() {
                checkNullity(tag, "category");
                checkStringValidity(title, "title");
                checkString(title, "title", TITLE_MIN_LENGTH, TITLE_MAX_LENGTH);
                checkStringValidity(description, "description");
                checkString(description, "description", DESCRIPTION_MIN_LENGTH,
                        DESCRIPTION_MAX_LENGTH);
                checkStringValidity(userId, "user ID");
                return new Description(description, title, tag, userId);
            }
        }
    }

    private static final class Extras {
        private final String linkPicture;
        private final Location location;

        private Extras(String linkPicture, Location location) {
            this.linkPicture = linkPicture;
            this.location = location;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if(obj instanceof Extras) {
                Extras other = (Extras) obj;
                return this.linkPicture.equals(other.linkPicture)
                        && this.location.equals(other.location);
            }
            return false;
        }

        private static final class Builder {
            private String linkPicture;
            private Extras.Location.Builder location;

            private Builder() {
                linkPicture = "";
                location = new Extras.Location.Builder();
            }

            private Builder(Extras extras) {
                this.linkPicture = extras.linkPicture;
                this.location = new Extras.Location.Builder(extras.location);
            }

            private Extras build() {
                return new Extras(linkPicture, location.build());
            }
        }

        private static final class Location {
            private final double locationLatitude;
            private final double locationLongitude;

            private Location(double locationLatitude, double locationLongitude) {
                this.locationLatitude = locationLatitude;
                this.locationLongitude = locationLongitude;
            }

            @Override
            public boolean equals(@Nullable Object obj) {
                if(obj instanceof Location) {
                    Location other = (Location) obj;
                    return this.locationLongitude == other.locationLongitude
                            && this.locationLatitude == other.locationLatitude;
                }
                return false;
            }

            private static final class Builder {
                private double locationLatitude;
                private double locationLongitude;

                private Builder(Extras.Location location) {
                    this.locationLatitude = location.locationLatitude;
                    this.locationLongitude = location.locationLongitude;
                }

                private Builder() {
                    locationLatitude = 0;
                    locationLongitude = 0;
                }

                private Extras.Location build() {
                    return new Extras.Location(locationLatitude, locationLongitude);
                }
            }
        }
    }

    /**
     * Class used to create an offer.
     * If the fields are strings they are initially set to an empty string.
     * Location values are set to  0.
     * Date value are set to the current time.
     * Tag is set to default.
     */
    public static final class Builder {
        private Date.Builder dates;
        private Description.Builder description;
        private Extras.Builder extras;
        private String uuid;

        public Builder(Offer offer) {
            this.dates = new Date.Builder(offer.dates);
            this.description = new Description.Builder(offer.description);
            this.extras = new Extras.Builder(offer.extras);
            this.uuid = offer.uuid;
        }

        public Builder() {
            this.dates = new Date.Builder();
            this.description = new Description.Builder();
            this.extras = new Extras.Builder();
            this.uuid = UUID.randomUUID().toString();
        }

        public Category getTag() {
            return description.tag;
        }

        public Builder setTag(Category tag) {
            this.description.tag = tag;
            return this;
        }

        public String getUserId() {
            return this.description.userId;
        }

        public Builder setUserId(String userId) {
            this.description.userId = userId;
            return this;
        }

        public String getTitle() {
            return this.description.title;
        }

        public Builder setTitle(String title) {
            this.description.title = title;
            return this;
        }

        public String getDescription() {
            return this.description.description;
        }

        public Builder setDescription(String description) {
            this.description.description = description;
            return this;
        }

        public String getLinkPicture() {
            return this.extras.linkPicture;
        }

        public Builder setLinkPicture(String linkPicture) {
            this.extras.linkPicture = linkPicture;
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
            return this.extras.location.locationLatitude;
        }

        public Builder setLatitude(double latitude) {
            this.extras.location.locationLatitude = latitude;
            return this;
        }

        public double getLongitude() {
            return this.extras.location.locationLongitude;
        }

        public Builder setLongitude(double longitude) {
            this.extras.location.locationLongitude = longitude;
            return this;
        }

        public Builder setLocation(Location location) {
            this.extras.location.locationLatitude = location.getLatitude();
            this.extras.location.locationLongitude = location.getLongitude();
            return this;
        }

        public long getCreationDate() {
            return this.dates.dateOfCreation;
        }

        public Builder setCreationDate(long creationDate) {
            this.dates.dateOfCreation = creationDate;
            return this;
        }

        public long getEndDate() {
            return this.dates.dateOfDeletion;
        }

        public Builder setEndDate(long endDate) {
            this.dates.dateOfDeletion = endDate;
            return this;
        }

        public Offer build() {
            checkNullity(uuid, "UUID");
            return new Offer(dates.build(), description.build(), extras.build(), uuid);
        }

    }

    private final Date dates;
    private final Description description;
    private final Extras extras;
    private final String uuid;

    private Offer(Date date, Description description, Extras extras, String uuid) {
        this.dates = date;
        this.description = description;
        this.extras = extras;
        this.uuid = uuid;
    }

    /**
     * Empty constructor for the listener of Firebase.
     */
    public Offer() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        dates = new Date(currentTime, currentTime);
        description = new Description("","",Category.getDefault(),"");
        extras = new Extras("",new Extras.Location(0,0));
        this.uuid = "createdByEmptyConstructor";
    }

    private Offer(Parcel in) {
        String[] data = new String[10];

        in.readStringArray(data);

        Date.Builder dateBuilder = new Date.Builder();
        dateBuilder.dateOfCreation = Long.parseLong(data[6]);
        dateBuilder.dateOfDeletion = Long.parseLong(data[7]);

        Extras.Location.Builder locationBuilder = new Extras.Location.Builder();
        locationBuilder.locationLongitude = Double.parseDouble(data[9]);
        locationBuilder.locationLatitude = Double.parseDouble(data[8]);

        Extras.Builder extrasBuilder = new Extras.Builder();
        extrasBuilder.linkPicture = data[3];
        extrasBuilder.location = locationBuilder;

        Description.Builder descriptionBuilder = new Description.Builder();
        descriptionBuilder.userId = data[0];
        descriptionBuilder.title = data[1];
        descriptionBuilder.description = data[2];
        descriptionBuilder.tag = Category.valueOf(data[5]);

        this.dates = dateBuilder.build();
        this.description = descriptionBuilder.build();
        this.extras = extrasBuilder.build();
        this.uuid = data[4];
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
            return this.dates.equals(other.dates)
                    && this.description.equals(other.description)
                    && this.extras.equals(other.extras)
                    && this.uuid.equals(other.uuid);
        }
        return false;
    }

    /**
     * Returns the offer's title.
     *
     * @return the name of the offer
     */
    public String getTitle() {
        return description.title;
    }

    /**
     * Returns the offer's description.
     *
     * @return the description of the offer
     */
    public String getDescription() {
        return description.description;
    }

    /**
     * Returns the offer's userId.
     *
     * @return the userId of the offer
     */
    public String getUserId() {
        return description.userId;
    }

    /**
     * Returns the offer's tag.
     *
     * @return the category of the offer
     */
    public Category getTag() {
        return description.tag;
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
        return extras.linkPicture;
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
        return dates.dateOfCreation;
    }

    /**
     * Returns the offer's end date.
     *
     * @return the offer's end date
     */
    public long getEndDate() {
        return dates.dateOfDeletion;
    }

    /**
     * Returns the offer's locationLatitude.
     *
     * @return the locationLatitude of the offer
     */
    public double getLatitude() {
        return extras.location.locationLatitude;
    }

    /**
     * Returns the offer's locationLongitude.
     *
     * @return the locationLongitude of the offer
     */
    public double getLongitude() {
        return extras.location.locationLongitude;
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
                this.description.userId,
                this.description.title,
                this.description.description,
                this.extras.linkPicture,
                this.uuid,
                this.description.tag.toString(),
                Long.toString(this.dates.dateOfCreation),
                Long.toString(this.dates.dateOfDeletion),
                Double.toString(this.extras.location.locationLatitude),
                Double.toString(this.extras.location.locationLongitude)});
    }

    /**
     * The points yielded by the offer creation.
     *
     * @return the score of the offer
     */
    public int offerValue() {
        int value = PointType.POST_OFFER.getValue();
        if (!this.extras.linkPicture.isEmpty()) {
            value += PointType.ADD_PICTURE.getValue();
        }
        if (this.extras.location.locationLatitude != 0 || this.extras.location.locationLongitude != 0) {
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

}
