package pl.mirko.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class BasePost {

    public String author;
    public String postContent;
    public int score;

    private int scoreColor;

    @ParcelConstructor
    BasePost(String author, String postContent, int score) {
        this.author = author;
        this.postContent = postContent;
        this.score = score;
    }

    public int getScoreColor() {
        return scoreColor;
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
    }

    public void increaseScore() {
        score += 1;
    }

    public void decreaseScore() {
        score -= 1;
    }
}
