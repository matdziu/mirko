package pl.mirko.models;

public class Comment extends BasePost {

    @SuppressWarnings("unused")
    public Comment() {
        // default constructor for Firebase
    }

    public Comment(String author, String content, String postId) {
        super(author, content, postId);
    }
}
