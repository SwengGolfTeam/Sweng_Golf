package ch.epfl.sweng.swenggolf.offer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents answers.
 */
public class Answers {
    private int favoritePos;
    private List<Answer> answerList;
    public static final int NO_FAVORITE = -1;

    /**
     * Empty constructor required by Firebase.
     */
    public Answers() {
    }

    /**
     * Create an answer.
     * @param answerList the list of answers
     * @param favoritePos the favorite answer position
     */
    public Answers(List<Answer> answerList, int favoritePos) {
        this.answerList = new ArrayList<>(answerList);
        this.favoritePos = favoritePos;
    }

    /**
     * Get the favorite answer position.
     * @return the position of the favorite answer
     */
    public int getFavoritePos() {
        return favoritePos;
    }

    /**
     * Set the favorite answer position.
     * @param newPos the new position
     */
    public void setFavoritePos(int newPos) {
        favoritePos = newPos;
    }

    /**
     * Get the list of answers.
     * @return the list of answers
     */
    public List<Answer> getAnswerList() {
        return answerList;
    }
}
