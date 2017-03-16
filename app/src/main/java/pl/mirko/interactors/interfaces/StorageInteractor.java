package pl.mirko.interactors.interfaces;

import android.net.Uri;

import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.models.BasePost;
import rx.Observable;

public interface StorageInteractor {

    void uploadBasePostImage(Uri imageUri, String basePostId,
                             BasePostImageSendingListener basePostImageSendingListener);

    Observable<String> fetchBasePostImageUrl(BasePost basePost);
}