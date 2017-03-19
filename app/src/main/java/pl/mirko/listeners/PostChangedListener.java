package pl.mirko.listeners;

import pl.mirko.models.Post;

public interface PostChangedListener {

    void onPostChanged(Post post);
}
