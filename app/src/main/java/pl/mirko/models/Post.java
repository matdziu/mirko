package pl.mirko.models;

public class Post {

    public String author;
    public String post;
    public int score;

    private int scoreColor;

    @SuppressWarnings("unused")
    public Post() {
        // default constructor for Firebase
    }

    public Post(String author, String post, int score) {
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
