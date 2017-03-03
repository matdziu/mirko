package pl.mirko.createpost;

import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.listeners.BasePostSendingListener;

class CreatePostPresenter implements BasePostSendingListener {

    private CreatePostView createPostView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    CreatePostPresenter(CreatePostView createPostView, FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.createPostView = createPostView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void createNewPost(String content) {
        firebaseDatabaseInteractor.createNewPost(content, this);
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
