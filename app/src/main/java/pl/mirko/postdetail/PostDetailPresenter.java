package pl.mirko.postdetail;

import java.util.List;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.OnPostChangedListener;
import pl.mirko.listeners.ThumbFetchingListener;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

class PostDetailPresenter extends BasePresenter implements BasePostFetchingListener,
        OnPostChangedListener, ThumbFetchingListener {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private PostDetailView postDetailView;
    private String postId;

    PostDetailPresenter(FirebaseAuthInteractor firebaseAuthInteractor,
                        FirebaseDatabaseInteractor firebaseDatabaseInteractor,
                        PostDetailView postDetailView) {
        super(firebaseAuthInteractor, firebaseDatabaseInteractor);
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.postDetailView = postDetailView;
    }

    void fetchComments(Post post) {
        firebaseDatabaseInteractor.fetchComments(post, this);
    }

    void addOnPostChangedListener(Post post) {
        firebaseDatabaseInteractor.addOnPostChangedListener(post, this);
    }

    @Override
    public void onBasePostFetchingStarted() {
        postDetailView.showProgressBar(true);
    }

    @Override
    public void onBasePostFetchingFinished(List<BasePost> basePostList) {
        firebaseDatabaseInteractor.fetchCommentsThumbs(postId, basePostList, this);
    }

    @Override
    public void onPostChanged(Post post) {
        postDetailView.showPostDetails(post);
    }

    @Override
    public void onThumbFetchingFinished(List<BasePost> basePostList) {
        postDetailView.showProgressBar(false);
        postDetailView.updateRecyclerView(basePostList);
    }

    void setPostId(String postId) {
        this.postId = postId;
    }
}
