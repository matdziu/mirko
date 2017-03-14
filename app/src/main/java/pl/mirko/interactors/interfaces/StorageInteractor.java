package pl.mirko.interactors.interfaces;

import pl.mirko.listeners.BasePostImageFetchingListener;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.models.BasePost;

public interface StorageInteractor {

    void uploadBasePostImage(String imageFilePath, BasePost basePost,
                             BasePostImageSendingListener basePostImageSendingListener);

    void fetchBasePostImageUrl(BasePost basePost,
                               BasePostImageFetchingListener basePostImageFetchingListener);
}