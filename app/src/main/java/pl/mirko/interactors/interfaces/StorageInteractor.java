package pl.mirko.interactors.interfaces;

import pl.mirko.listeners.BasePostImageSendingListener;

public interface StorageInteractor {

    void uploadBasePostImage(String imageFilePath, String basePostId,
                             BasePostImageSendingListener basePostImageSendingListener);
}