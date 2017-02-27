package pl.mirko.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public class Post extends BasePost {

    public List<BasePost> commentList;

    @ParcelConstructor
    public Post(String author, String postContent, int score, List<BasePost> commentList) {
        super(author, postContent, score);
        this.commentList = commentList;
    }
}
