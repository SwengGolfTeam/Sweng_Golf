package ch.epfl.sweng.swenggolf.offer;

import java.util.ArrayList;
import java.util.List;

public class Answers {
    private int favoritePos;
    private List<Answer> answers;

    public Answers(List<Answer> answers, int favoritePos) {
        this.answers = answers;
        this.favoritePos = favoritePos;
    }

    // for firebase
    public Answers() {
        answers = new ArrayList<>();
        favoritePos = -1;
    }

    public int getFavoritePos() {
        return favoritePos;
    }

    public void setFavoritePos(int newPos) {
        favoritePos = newPos;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
