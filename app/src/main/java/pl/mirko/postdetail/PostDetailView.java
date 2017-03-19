package pl.mirko.postdetail;

import java.util.List;

import pl.mirko.base.BasePostView;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

interface PostDetailView extends BasePostView {

    void showProgressBar(boolean show);

    void initDataSet(List<BasePost> commentList);

    void addNewItem(BasePost basePost);

    void updateItem(BasePost basePost);

    void showPostDetails(Post rawPost);
}
