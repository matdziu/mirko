package pl.mirko.createpost;

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostSendingListener;

class CreatePostPresenter implements BasePostSendingListener {

    private CreatePostView createPostView;
    private DatabaseInteractor databaseInteractor;

    CreatePostPresenter(CreatePostView createPostView, DatabaseInteractor databaseInteractor) {
        this.createPostView = createPostView;
        this.databaseInteractor = databaseInteractor;
    }

    void createNewPost(String content) {
        databaseInteractor.createNewPost(content, this);
    }

    @Override
    public void onBasePostSendingStarted() {
        createPostView.showSoftKeyboard(false);
        createPostView.showProgressBar(true);
    }

    @Override
    public void onBasePostSendingFinished() {
        createPostView.showProgressBar(false);
        createPostView.finish();
    }
}
