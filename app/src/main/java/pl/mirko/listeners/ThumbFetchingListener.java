package pl.mirko.listeners;

import java.util.List;

import pl.mirko.models.BasePost;

public interface ThumbFetchingListener {

    void onThumbFetchingFinished(List<BasePost> basePostList);
}
