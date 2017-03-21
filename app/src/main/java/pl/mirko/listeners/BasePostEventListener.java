package pl.mirko.listeners;

import pl.mirko.models.BasePost;

public interface BasePostEventListener {

    void onBasePostChanged(BasePost basePost);
}
