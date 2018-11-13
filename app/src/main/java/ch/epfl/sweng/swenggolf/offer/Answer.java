package ch.epfl.sweng.swenggolf.offer;

import ch.epfl.sweng.swenggolf.tools.Check;

public class Answer {
    private final String userId;
    private final String description;

    public static final int COMMENT_MAX_LENGTH = 110;
    public static final int COMMENT_MIN_LENGTH = 4;

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

    // firebase needs empty constructor
    public Answer() {
        userId = "";
        description = "";
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }
}
