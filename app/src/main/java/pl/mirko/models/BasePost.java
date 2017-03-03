package pl.mirko.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class BasePost {

    public String author;
    public String content;
    public int score;
    public String postId;

    private int scoreColor;

    public BasePost() {
        // default constructor for Firebase
    }

    @ParcelConstructor
    BasePost(String author, String content, String postId) {
        this.author = author;
        this.content = content;
        this.postId = postId;
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
