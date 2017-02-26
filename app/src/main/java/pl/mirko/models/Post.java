package pl.mirko.models;

public class Post {

    public String author;
    public String post;

    @SuppressWarnings("unused")
    public Post() {
        // default constructor for Firebase
    }

    public Post(String author, String post) {
        this.author = author;
        this.post = post;
    }
}
