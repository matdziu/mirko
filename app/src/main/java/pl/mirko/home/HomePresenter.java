package pl.mirko.home;

import java.util.ArrayList;
import java.util.List;

import pl.mirko.models.Post;

class HomePresenter {

    List<Post> setScoreColor(List<Post> postList) {
        List<Post> outputPostList = new ArrayList<>();
        for (Post post : postList) {
            if (post.score > 0) {
                post.setScoreColor(android.R.color.holo_green_dark);
            } else if (post.score < 0) {
                post.setScoreColor(android.R.color.holo_red_dark);
            } else {
                post.setScoreColor(android.R.color.darker_gray);
            }
            outputPostList.add(post);
        }
        return outputPostList;
    }
}
