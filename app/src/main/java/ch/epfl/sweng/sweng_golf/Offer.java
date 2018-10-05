package ch.epfl.sweng.sweng_golf;

public class Offer {
    private final String author, title, description;

    /**
     * Contains the data of an offer
     * @param author the creator of the offer. Should not be empty
     * @param title the title of the offer. Should not be empty
     * @param description the description of the offer. Should not be empty
     *
     */
    public Offer(String author, String title, String description){
        if(author.isEmpty()){
            throw new IllegalArgumentException("Author of the offer can't be empty.");
        }
        else if(title.isEmpty()){
            throw new IllegalArgumentException("Name of the offer can't be empty.");
        }
        else if(description.isEmpty()){
            throw new IllegalArgumentException("Description of the offer can't be empty.");
        }
        this.author = author;
        this.title = title;
        this.description = description;
    }

    /**
     *
     * @return the creator of the offer
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return the name of the offer
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return the description of the offer
     */
    public String getDescription() {
        return description;
    }


}
