package pl.mirko.interactors;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostImageFetchingListener;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.models.BasePost;

public class FirebaseStorageInteractor implements StorageInteractor {

    private StorageReference storageReference =
            FirebaseStorage.getInstance().getReference();

    @Override
    public void uploadBasePostImage(final String imageFilePath, final BasePost basePost,
                                    final BasePostImageSendingListener basePostImageSendingListener) {
        Uri imageFile = Uri.fromFile(new File(imageFilePath));
        storageReference
                .child(basePost.id)
                .putFile(imageFile)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        basePostImageSendingListener.onImageUploaded(basePost, imageFilePath);
                    }
                });
    }

    @Override
    public void fetchBasePostImageUrl(BasePost basePost,
                                      final BasePostImageFetchingListener basePostImageFetchingListener) {
        storageReference
                .child(basePost.id)
                .child(basePost.imagePath)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        basePostImageFetchingListener.onDownloadUrlFetched(uri.toString());
                    }
                });
    }
}
