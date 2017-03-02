package pl.mirko.base;

import java.util.List;

import pl.mirko.models.BasePost;

public interface BasePostListener {

    void onBasePostFetchingStarted();

    void onBasePostFetchingFinished(List<BasePost> basePostList);
}
