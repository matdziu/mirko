package pl.mirko.models;

public class Comment extends BasePost {

    public String commentedPostId;

    @SuppressWarnings("unused")
    public Comment() {
        // default constructor for Firebase
    }

    public Comment(String author, String content, String id, String commentedPostId) {
        super(author, content, id);
        this.commentedPostId = commentedPostId;
    }
}
