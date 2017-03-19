package pl.mirko.listeners;

import pl.mirko.models.BasePost;

public interface BasePostEventListener {

    void onBasePostAdded(BasePost basePost);

    void onBasePostChanged(BasePost basePost);
}
