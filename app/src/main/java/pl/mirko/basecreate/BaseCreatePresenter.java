package pl.mirko.basecreate;

import java.io.File;

import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.listeners.BasePostSendingListener;

public class BaseCreatePresenter implements BasePostImageSendingListener, BasePostSendingListener {

    private StorageInteractor storageInteractor;
    private BaseCreateView baseCreateView;
    protected File currentImageFile;

    public BaseCreatePresenter(StorageInteractor storageInteractor, BaseCreateView baseCreateView) {
        this.storageInteractor = storageInteractor;
        this.baseCreateView = baseCreateView;
    }

    @Override
    public void onBasePostSendingStarted() {
        baseCreateView.showSoftKeyboard(false);
        baseCreateView.showProgressBar(true);
    }

    @Override
    public void onBasePostSendingFinished(String basePostId) {
        if (currentImageFile != null) {
            storageInteractor.uploadBasePostImage(currentImageFile, basePostId, this);
        } else {
            baseCreateView.showProgressBar(false);
            baseCreateView.finish();
        }
    }

    void setCurrentImageFile(File currentImageFile) {
        this.currentImageFile = currentImageFile;
    }

    void onAddImageFabClicked() {
        if (currentImageFile == null) {
            baseCreateView.startImagePickActivity();
        } else {
            currentImageFile = null;
            baseCreateView.showImageDeletedInfo();
        }
    }

    @Override
    public void onImageUploaded(String basePostId) {
        baseCreateView.showProgressBar(false);
        baseCreateView.finish();
    }

    void onImageAdded() {
        baseCreateView.showImageAddedInfo();
    }
}
