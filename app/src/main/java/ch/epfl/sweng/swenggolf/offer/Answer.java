package ch.epfl.sweng.swenggolf.offer;

public class Answer {
    private final String userId;
    private final String description;

    public Answer(String userId, String description) {
        this.userId = userId;
        this.description = description;
    }

    //TODO need this? But how is data recovered?
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
