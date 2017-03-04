package pl.mirko.models;

import org.parceler.Parcel;

@Parcel
public class Post extends BasePost {

    @SuppressWarnings("unused")
    Post() {
        // default constructor for Firebase
    }

    public Post(String author, String postContent, String postId) {
        super(author, postContent, postId);
    }
}
