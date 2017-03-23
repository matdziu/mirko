package pl.mirko.listeners;

import java.util.List;

import pl.mirko.models.BasePost;

public interface BasePostFetchingListener {

    void onBasePostFetchingStarted(boolean progressBar);

    void onBasePostFetchingFinished(List<BasePost> basePostList);
}
