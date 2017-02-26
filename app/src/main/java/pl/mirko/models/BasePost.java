package pl.mirko.models;

public abstract class BasePost {

    public String author;
    public String post;
    public int score;

    private int scoreColor;

    BasePost(String author, String post, int score) {
        this.author = author;
        this.post = post;
        this.score = score;
    }

    public int getScoreColor() {
        return scoreColor;
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
    }
}
