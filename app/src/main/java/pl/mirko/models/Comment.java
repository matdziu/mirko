package pl.mirko.models;

public class Comment extends BasePost {

    public String commentedPostId;

    @SuppressWarnings("unused")
    public Comment() {
        // default constructor for Firebase
    }

    public Comment(String author, String content, String id, String commentedPostId, boolean hasImage) {
        super(author, content, id, hasImage);
        this.commentedPostId = commentedPostId;
    }
}
