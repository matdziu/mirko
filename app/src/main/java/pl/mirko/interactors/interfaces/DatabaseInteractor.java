package pl.mirko.interactors.interfaces;

import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.listeners.OnPostChangedListener;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;
import pl.mirko.models.User;

public interface DatabaseInteractor {

    void createNewUser(User newUser);

    void createNewPost(String content, BasePostSendingListener basePostSendingListener);

    void createNewComment(Post post, String content, BasePostSendingListener basePostSendingListener);

    void fetchPosts(BasePostFetchingListener basePostFetchingListener);

    void fetchComments(Post post, BasePostFetchingListener basePostFetchingListener);

    void updateScore(BasePost basePost, int updatedScore);

    void sendThumb(String thumb, BasePost basePost);

    void addOnPostChangedListener(Post post, OnPostChangedListener onPostChangedListener);
}
