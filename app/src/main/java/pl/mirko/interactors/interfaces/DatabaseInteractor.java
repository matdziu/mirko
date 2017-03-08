package pl.mirko.interactors.interfaces;

import java.util.List;

import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.listeners.PostChangedListener;
import pl.mirko.listeners.TagFetchingListener;
import pl.mirko.listeners.ThumbFetchingListener;
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

    void addOnPostChangedListener(Post post, PostChangedListener postChangedListener);

    void fetchPostsThumbs(List<BasePost> postList, ThumbFetchingListener thumbFetchingListener);

    void fetchCommentsThumbs(String commentedPostId, List<BasePost> commentList, ThumbFetchingListener thumbFetchingListener);

    void fetchSinglePostThumbs(Post post, PostChangedListener postChangedListener);

    void fetchTags(TagFetchingListener tagFetchingListener);
}