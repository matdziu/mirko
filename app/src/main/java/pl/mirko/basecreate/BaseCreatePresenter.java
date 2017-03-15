package pl.mirko.basecreate;

import android.net.Uri;

import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.listeners.BasePostSendingListener;

public class BaseCreatePresenter implements BasePostImageSendingListener, BasePostSendingListener {

    private StorageInteractor storageInteractor;
    private BaseCreateView baseCreateView;
    private Uri currentImageUri;

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
        if (currentImageUri != null) {
            storageInteractor.uploadBasePostImage(currentImageUri, basePostId, this);
        } else {
            baseCreateView.showProgressBar(false);
            baseCreateView.finish();
        }
    }

    void setCurrentImageUri(Uri currentImageUri) {
        this.currentImageUri = currentImageUri;
    }

    void onAddImageFabClicked() {
        if (currentImageUri == null) {
            baseCreateView.startImagePickActivity();
        } else {
            currentImageUri = null;
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
