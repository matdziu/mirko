package pl.mirko.interactors.interfaces;

import pl.mirko.base.BasePostListener;
import pl.mirko.models.Comment;
import pl.mirko.models.Post;
import pl.mirko.models.User;

public interface DatabaseInteractor {

    void createNewUser(User newUser);

    void createNewPost(Post newPost);

    void createNewComment(Post post, Comment newComment);

    void fetchPosts(BasePostListener basePostListener);
}
