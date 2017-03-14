package pl.mirko.listeners;

public interface BasePostSendingListener {

    void onBasePostSendingStarted();

    void onBasePostSendingFinished(String basePostId);
}
