package pl.mirko.basecreate;

import android.net.Uri;

import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.listeners.BasePostSendingListener;

public class BaseCreatePresenter implements BasePostImageSendingListener, BasePostSendingListener {

    private StorageInteractor storageInteractor;
    private BaseCreateView baseCreateView;
    private Uri currentImageUri;
    protected boolean hasImage;

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
            hasImage = false;
        }
    }

    @Override
    public void onImageUploaded() {
        baseCreateView.showProgressBar(false);
        baseCreateView.finish();
    }

    void onImageAdded() {
        baseCreateView.showImageAddedInfo();
        hasImage = true;
    }
}
