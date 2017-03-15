package pl.mirko.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import static pl.mirko.interactors.FirebaseDatabaseInteractor.NO_THUMB;

@Parcel
public class BasePost {

    public String author;
    public String content;
    public int score;
    public String id;

    private int scoreColor;
    private String thumb = NO_THUMB;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
