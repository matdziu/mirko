package pl.mirko.models;

import java.util.List;

public class Post extends BasePost {

    public List<BasePost> commentList;

    public Post(String author, String post, int score, List<BasePost> commentList) {
        super(author, post, score);
        this.commentList = commentList;
    }
}
