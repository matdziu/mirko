package pl.mirko.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public class Post extends BasePost {

    public List<BasePost> commentList;
    public String id;

    @ParcelConstructor
    public Post(String author, String postContent, int score, List<BasePost> commentList, String id) {
        super(author, postContent, score);
        this.commentList = commentList;
        this.id = id;
    }
}
