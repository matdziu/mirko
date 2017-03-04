package pl.mirko.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class BasePost {

    public String author;
    public String content;
    public int score;
    public String id;

    private int scoreColor;

    public BasePost() {
        // default constructor for Firebase
    }

    @ParcelConstructor
    BasePost(String author, String content, String id) {
        this.author = author;
        this.content = content;
        this.id = id;
    }

    public int getScoreColor() {
        return scoreColor;
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
