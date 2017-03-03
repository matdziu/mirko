package pl.mirko.postdetail;

import java.util.List;

import pl.mirko.models.BasePost;

interface PostDetailView {

    void showProgressBar(boolean show);

    void updateRecyclerView(List<BasePost> commentList);
}
