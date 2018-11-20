package ch.epfl.sweng.swenggolf.offer;

import java.util.ArrayList;
import java.util.List;

public class Answers {
    private int favoritePos;
    private List<Answer> answerList;
    public static final int NO_FAVORITE = -1;

    public Answers(List<Answer> answerList, int favoritePos) {
        this.answerList = new ArrayList<>(answerList);
        this.favoritePos = favoritePos;
    }

    // firebase needs empty constructor
    public Answers() {
    }

    public int getFavoritePos() {
        return favoritePos;
    }

    public void setFavoritePos(int newPos) {
        favoritePos = newPos;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }
}
