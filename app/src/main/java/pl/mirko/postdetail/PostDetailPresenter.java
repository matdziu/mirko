package pl.mirko.postdetail;

import java.util.List;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

class PostDetailPresenter extends BasePresenter implements BasePostFetchingListener {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private PostDetailView postDetailView;

    PostDetailPresenter(FirebaseDatabaseInteractor firebaseDatabaseInteractor, PostDetailView postDetailView) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.postDetailView = postDetailView;
    }

    void fetchComments(Post post) {
        firebaseDatabaseInteractor.fetchComments(post, this);
    }

    @Override
    public void onBasePostFetchingStarted() {
        postDetailView.showProgressBar(true);
    }

    @Override
    public void onBasePostFetchingFinished(List<BasePost> basePostList) {
        postDetailView.showProgressBar(false);
        postDetailView.updateRecyclerView(basePostList);
    }
}
