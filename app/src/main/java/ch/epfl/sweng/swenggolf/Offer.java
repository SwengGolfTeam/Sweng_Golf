package ch.epfl.sweng.swenggolf;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {

    private static final int DESCRIPTION_LIMIT = 140;

    private final String author;
    private final String title;
    private final String description;

    /**
     * Contains the data of an offer.
     *
     * @param author      the creator of the offer. Should not be empty
     * @param title       the title of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     */
    public Offer(String author, String title, String description) {
        if (author.isEmpty()) {
            throw new IllegalArgumentException("Author of the offer can't be empty.");
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Name of the offer can't be empty.");
        }
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Description of the offer can't be empty.");
        }
        this.author = author;
        this.title = title;
        this.description = description;
    }

    /**
     * Empty builder for the listener of Firebase.
     */
    public Offer() {
        this.author = "";
        this.title = "";
        this.description = "";
    }

    /**
     * Returns the offer's author's name.
     *
     * @return the creator of the offer
     */
    public String getAuthor() {
        return author;
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
     * Returns the shortened offer's description.
     *
     * @return the shortened description of the offer
     */
    public String getShortDescription() {
        return description.length() > DESCRIPTION_LIMIT ?
                description.substring(0, DESCRIPTION_LIMIT) + "..." : description;
    }


    /* Implements Parcelable */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.author,
                this.title,
                this.description});
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
        String[] data = new String[3];

        in.readStringArray(data);
        this.author = data[0];
        this.title = data[1];
        this.description = data[2];
    }
}
