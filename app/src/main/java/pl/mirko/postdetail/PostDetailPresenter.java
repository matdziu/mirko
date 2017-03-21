package pl.mirko.postdetail;

import java.util.List;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.interfaces.AuthenticationInteractor;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostEventListener;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.PostChangedListener;
import pl.mirko.listeners.ThumbFetchingListener;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;
import rx.functions.Action1;

import static pl.mirko.interactors.FirebaseDatabaseInteractor.DOWN;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.NO_THUMB;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.UP;

class PostDetailPresenter extends BasePresenter implements BasePostFetchingListener,
        PostChangedListener, ThumbFetchingListener, BasePostEventListener {

    private DatabaseInteractor databaseInteractor;
    private PostDetailView postDetailView;
    private String postId;

    PostDetailPresenter(AuthenticationInteractor authenticationInteractor,
                        DatabaseInteractor databaseInteractor,
                        StorageInteractor storageInteractor,
                        PostDetailView postDetailView) {
        super(authenticationInteractor, databaseInteractor, storageInteractor);
        this.databaseInteractor = databaseInteractor;
        this.postDetailView = postDetailView;
    }

    void fetchComments(Post post) {
        databaseInteractor.fetchComments(post, this);
    }

    void addOnPostChangedListener(Post post) {
        databaseInteractor.addOnPostChangedListener(post, this);
    }

    @Override
    public void onBasePostFetchingStarted() {
        postDetailView.showProgressBar(true);
    }

    @Override
    public void onBasePostFetchingFinished(List<BasePost> basePostList) {
        databaseInteractor.fetchCommentsThumbs(postId, basePostList, this);
    }

    @Override
    public void onPostChanged(final Post post) {
        databaseInteractor.fetchSingleBasePostThumbs(post)
                .subscribe(new Action1<BasePost>() {
                    @Override
                    public void call(BasePost basePost) {
                        postDetailView.showPostDetails(post);

                        switch (post.getThumb()) {
                            case UP:
                                postDetailView.showThumbUpView();
                                break;
                            case DOWN:
                                postDetailView.showThumbDownView();
                                break;
                            case NO_THUMB:
                                postDetailView.showNoThumbView();
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onThumbFetchingFinished(List<BasePost> basePostList) {
        postDetailView.showProgressBar(false);
        postDetailView.initDataSet(basePostList);
    }

    void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public void onBasePostChanged(BasePost basePost) {
        postDetailView.updateItem(basePost);
    }

    void addCommentEventListener() {
        databaseInteractor.addCommentEventListener(this, postId);
    }
}
