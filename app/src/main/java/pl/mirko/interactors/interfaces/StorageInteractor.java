package pl.mirko.interactors.interfaces;

import android.net.Uri;

import pl.mirko.listeners.BasePostImageSendingListener;

public interface StorageInteractor {

    void uploadBasePostImage(Uri imageUri, String basePostId,
                             BasePostImageSendingListener basePostImageSendingListener);
}