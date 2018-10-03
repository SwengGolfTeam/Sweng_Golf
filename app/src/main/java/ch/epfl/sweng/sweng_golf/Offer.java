package ch.epfl.sweng.sweng_golf;

public class Offer {
    private final String username, name, description;

    /**
     * Contains the data of an offer
     * @param username the creator of the offer
     * @param name the name of the offer
     * @param description the description of the offer
     *
     */
    public Offer(String username, String name, String description){
        this.username = username;
        this.name = name;
        this.description = description;
    }

    /**
     *
     * @return the creator of the offer
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return the name of the offer
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the description of the offer
     */
    public String getDescription() {
        return description;
    }


}
