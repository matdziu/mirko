package pl.mirko.createcomment;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.models.Post;

class CreateCommentPresenter extends BasePresenter implements BasePostSendingListener, BasePostImageSendingListener {

    private CreateCommentView createCommentView;
    private DatabaseInteractor databaseInteractor;
    private StorageInteractor storageInteractor;

    private String commentedPostId;

    CreateCommentPresenter(CreateCommentView createCommentView, DatabaseInteractor databaseInteractor,
                           StorageInteractor storageInteractor) {
        this.createCommentView = createCommentView;
        this.databaseInteractor = databaseInteractor;
        this.storageInteractor = storageInteractor;
    }

    void createNewComment(Post post, String content) {
        commentedPostId = post.id;
        databaseInteractor.createNewComment(commentedPostId, content, this);
    }

    @Override
    public void onBasePostSendingStarted() {
        createCommentView.showSoftKeyboard(false);
        createCommentView.showProgressBar(true);
    }

    @Override
    public void onBasePostSendingFinished(String basePostId) {
        if (currentImageFilePath != null) {
            storageInteractor.uploadBasePostImage(currentImageFilePath, basePostId, this);
        } else {
            createCommentView.showProgressBar(false);
            createCommentView.finish();
        }
    }

    @Override
    public void onImageUploaded(String basePostId) {
        databaseInteractor.storeCommentImageName(commentedPostId, basePostId, currentImageName);
        createCommentView.showProgressBar(false);
        createCommentView.finish();
    }
}