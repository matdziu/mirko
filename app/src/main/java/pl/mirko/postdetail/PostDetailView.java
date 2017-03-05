package pl.mirko.postdetail;

import java.util.List;

import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

interface PostDetailView {

    void showProgressBar(boolean show);

    void updateRecyclerView(List<BasePost> commentList);

    void showPostDetails(Post rawPost);

    void showThumbDownView();

    void showThumbUpView();
}
