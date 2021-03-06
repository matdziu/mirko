package pl.mirko.interactors.interfaces;

import java.util.List;

import pl.mirko.listeners.BasePostEventListener;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.listeners.PostChangedListener;
import pl.mirko.listeners.TagFetchingListener;
import pl.mirko.listeners.ThumbFetchingListener;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;
import pl.mirko.models.User;
import rx.Observable;

public interface DatabaseInteractor {

    void createNewUser(User newUser);

    void createNewPost(String content, List<String> tags, BasePostSendingListener basePostSendingListener,
                       boolean hasImage);

    void createNewComment(String commentedPostId, String content,
                          BasePostSendingListener basePostSendingListener, boolean hasImage);

    void fetchPosts(BasePostFetchingListener basePostFetchingListener, String fetchingStartPoint, boolean progressBar);

    void fetchComments(Post post, BasePostFetchingListener basePostFetchingListener, String fetchingStartPoint, boolean progressBar);

    void updateScore(BasePost basePost, int updatedScore);

    void sendThumb(String thumb, BasePost basePost);

    void addOnPostChangedListener(Post post, PostChangedListener postChangedListener);

    void fetchPostsThumbs(List<BasePost> postList, ThumbFetchingListener thumbFetchingListener);

    void fetchCommentsThumbs(String commentedPostId, List<BasePost> commentList, ThumbFetchingListener thumbFetchingListener);

    void fetchTags(TagFetchingListener tagFetchingListener);

    void updateTags(List<String> tags, String postId);

    void queryPosts(String tag, BasePostFetchingListener basePostFetchingListener,
                    boolean progressBar, String fetchingStartPoint);

    void addPostEventListener(BasePostEventListener basePostEventListener);

    void addCommentEventListener(BasePostEventListener basePostEventListener, String commentedPostId);

    Observable<BasePost> fetchSingleBasePostThumbs(BasePost basePost);
}