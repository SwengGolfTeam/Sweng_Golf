package ch.epfl.sweng.swenggolf.offer;

import ch.epfl.sweng.swenggolf.tools.Check;

/**
 * Class which represents an Answer of an Offer.
 */
public class Answer {
    public static final int COMMENT_MAX_LENGTH = 200;
    public static final int COMMENT_MIN_LENGTH = 4;
    private final String userId;
    private final String description;

    /**
     * Empty Constructor needed by Firebase.
     */
    public Answer() {
        userId = "";
        description = "";
    }

    /**
     * Create a new Answer to an offer.
     *
     * @param userId      the id of the user who answers
     * @param description the message of the answer
     */
    public Answer(String userId, String description) {
        this.userId = userId;
        this.description = Check.checkString(description, "description",
                COMMENT_MIN_LENGTH, COMMENT_MAX_LENGTH);
    }

    /**
     * Get the user id of the Answer.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Get the description of an Answer.
     *
     * @return the description of an Answer
     */
    public String getDescription() {
        return description;
    }
}
