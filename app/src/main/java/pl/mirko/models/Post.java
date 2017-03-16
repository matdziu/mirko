package pl.mirko.models;

import org.parceler.Parcel;

@Parcel
public class Post extends BasePost {

    Post() {
        // default constructor for Firebase
    }

    public Post(String author, String postContent, String postId, boolean hasImage) {
        super(author, postContent, postId, hasImage);
    }
}
