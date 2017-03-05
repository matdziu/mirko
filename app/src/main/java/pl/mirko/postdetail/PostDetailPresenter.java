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

import static pl.mirko.interactors.FirebaseDatabaseInteractor.DOWN;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.UP;

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
        firebaseDatabaseInteractor.fetchSinglePostThumbs(post, this);
    }

    @Override
    public void onPostThumbsFetched(Post post) {
        postDetailView.showPostDetails(post);

        if (post.getThumb().equals(UP)) {
            postDetailView.showThumbUpView();
        } else if (post.getThumb().equals(DOWN)) {
            postDetailView.showThumbDownView();
        }
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
