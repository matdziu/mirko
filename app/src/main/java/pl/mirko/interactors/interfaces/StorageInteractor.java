package pl.mirko.interactors.interfaces;

import java.io.File;

import pl.mirko.listeners.BasePostImageSendingListener;

public interface StorageInteractor {

    void uploadBasePostImage(File imageFile, String basePostId,
                             BasePostImageSendingListener basePostImageSendingListener);
}