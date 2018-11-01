package ch.epfl.sweng.swenggolf.offer;

import java.util.List;

public class Answers {
    private int favoritePos;
    private List<Answer> answerList;

    public Answers(List<Answer> answerList, int favoritePos) {
        this.answerList = answerList;
        this.favoritePos = favoritePos;
    }

    // firebase needs empty constructor
    public Answers() {}

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
