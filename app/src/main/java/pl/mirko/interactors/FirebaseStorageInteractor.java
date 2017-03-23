package pl.mirko.interactors;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostImageSendingListener;
import pl.mirko.models.BasePost;
import rx.Observable;
import rx.subjects.PublishSubject;

public class FirebaseStorageInteractor implements StorageInteractor {

    private StorageReference storageReference =
            FirebaseStorage.getInstance().getReference();

    @Override
    public void uploadBasePostImage(final Uri imageUri, final String basePostId,
                                    final BasePostImageSendingListener basePostImageSendingListener) {
        storageReference
                .child(basePostId)
                .putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        basePostImageSendingListener.onImageUploaded(basePostId);
                    }
                });
    }

    @Override
    public Observable<String> fetchBasePostImageUrl(BasePost basePost) {
        final PublishSubject<String> subject = PublishSubject.create();
        storageReference
                .child(basePost.id)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        subject.onNext(uri.toString());
                    }
                });
        return subject;
    }
}
