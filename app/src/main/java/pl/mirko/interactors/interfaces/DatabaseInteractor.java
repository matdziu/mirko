package pl.mirko.interactors.interfaces;

import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.models.Post;
import pl.mirko.models.User;

public interface DatabaseInteractor {

    void createNewUser(User newUser);

    void createNewPost(String content, BasePostSendingListener basePostSendingListener);

    void createNewComment(Post post, String content, BasePostSendingListener basePostSendingListener);

    void fetchPosts(BasePostFetchingListener basePostFetchingListener);

    void fetchComments(Post post, BasePostFetchingListener basePostFetchingListener);
}
