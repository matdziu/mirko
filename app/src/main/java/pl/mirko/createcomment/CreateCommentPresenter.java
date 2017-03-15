package pl.mirko.createcomment;

import pl.mirko.basecreate.BaseCreatePresenter;
import pl.mirko.basecreate.BaseCreateView;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.models.Post;

class CreateCommentPresenter extends BaseCreatePresenter {

    private DatabaseInteractor databaseInteractor;
    private String commentedPostId;

    CreateCommentPresenter(DatabaseInteractor databaseInteractor,
                           StorageInteractor storageInteractor, BaseCreateView baseCreateView) {
        super(storageInteractor, baseCreateView);
        this.databaseInteractor = databaseInteractor;
    }

    void createNewComment(Post post, String content) {
        this.commentedPostId = post.id;
        databaseInteractor.createNewComment(commentedPostId, content, this);
    }

    @Override
    public void onImageUploaded(String basePostId) {
        databaseInteractor.storeCommentImageName(commentedPostId, basePostId, currentImageFile.getName());
        super.onImageUploaded(basePostId);
    }
}