package ch.epfl.sweng.swenggolf.offer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;


public class Offer implements Parcelable {

    private static final int DESCRIPTION_LIMIT = 140;

    private final String userId;
    private final String title;
    private final String description;
    private final String linkPicture;
    private final String uuid;
    private final List<Answer> answers;

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
        this.answers = new LinkedList<>();
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
        this.answers = new LinkedList<>();
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
     * Returns the offer's list of answers
     *
     * @return the answers of the offer
     */
    public List<Answer> getAnswers() {
        return answers;
    }


    /* Implements Parcelable */
    @Override
    public int describeContents() {
        return 0;
    }

    // TODO put answers in parcel too
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = 5 + 2*answers.size();
        dest.writeInt(size);
        String[] bundle = new String[size];
        bundle[0] = this.userId;
        bundle[1] = this.title;
        bundle[2] = this.description;
        bundle[3] = this.linkPicture;
        bundle[4] = this.uuid;

        for (int i = 0; i < answers.size(); ++i) {
            bundle[2*i+5] = answers.get(i).getUserId();
            bundle[2*i+6] = answers.get(i).getDescription();
        }
        dest.writeStringArray(bundle);
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
        // TODO how can we know the size of the parcel? (without making it more a mess than it already is)
        int size = in.readInt();
        String[] data = new String[size];

        in.readStringArray(data);
        this.userId = data[0];
        this.title = data[1];
        this.description = data[2];
        this.linkPicture = data[3];
        this.uuid = data[4];
        // TODO change this to ArrayList (?)
        this.answers = new LinkedList<>();
        for (int i = 5; i < data.length; i += 2) {
            answers.add(new Answer(data[i], data[i+1]));
        }
    }
}
