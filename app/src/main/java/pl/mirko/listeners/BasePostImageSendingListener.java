package pl.mirko.listeners;

import pl.mirko.models.BasePost;

public interface BasePostImageSendingListener {

    void onImageUploaded(BasePost basePost, String imagePath);
}
