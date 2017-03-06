package pl.mirko.createcomment;

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.models.Post;

class CreateCommentPresenter implements BasePostSendingListener {

    private CreateCommentView createCommentView;
    private DatabaseInteractor databaseInteractor;

    CreateCommentPresenter(CreateCommentView createCommentView, DatabaseInteractor databaseInteractor) {
        this.createCommentView = createCommentView;
        this.databaseInteractor = databaseInteractor;
    }

    void createNewComment(Post post, String content) {
        databaseInteractor.createNewComment(post, content, this);
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