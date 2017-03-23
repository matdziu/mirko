package pl.mirko.basecreate;

public interface BaseCreateView {

    void showSoftKeyboard(boolean show);

    void showProgressBar(boolean show);

    void finishWithResult(String basePostId);

    void showImageAddedInfo();

    void showImageDeletedInfo();

    void startImagePickActivity();
}
