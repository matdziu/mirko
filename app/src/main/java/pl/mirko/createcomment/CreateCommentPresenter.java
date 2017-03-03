package pl.mirko.createcomment;

import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.models.Post;

class CreateCommentPresenter implements BasePostSendingListener {

    private CreateCommentView createCommentView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    CreateCommentPresenter(CreateCommentView createCommentView, FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.createCommentView = createCommentView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void createNewComment(Post post, String content) {
        firebaseDatabaseInteractor.createNewComment(post, content, this);
    }

    @Override
    public void onBasePostSendingStarted() {
        createCommentView.showSoftKeyboard(false);
        createCommentView.showProgressBar(true);
    }

    @Override
    public void onBasePostSendingFinished() {
        createCommentView.showProgressBar(false);
        createCommentView.finish();
    }
}