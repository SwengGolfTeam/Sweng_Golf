package ch.epfl.sweng.sweng_golf;

public class Offer {
    private final String username, name, description;

    /**
     * Contains the data of an offer
     * @param username the creator of the offer. Should not be empty
     * @param name the name of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     *
     */
    public Offer(String username, String name, String description){
        if(username.isEmpty()){
            throw new IllegalArgumentException("Username can't be empty.");
        }
        else if(name.isEmpty()){
            throw new IllegalArgumentException("Name of the offer can't be empty.");
        }
        else if(description.isEmpty()){
            throw new IllegalArgumentException("Decription of the offer can't be empty.");
        }
        this.username = username;
        this.name = name;
        this.description = description;
    }

    // Empty constructor for the listeners of Firebase
    public Offer(){
        this.username = "";
        this.name = "";
        this.description = "";
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
