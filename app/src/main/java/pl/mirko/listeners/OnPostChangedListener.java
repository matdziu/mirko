package pl.mirko.listeners;

import pl.mirko.models.Post;

public interface OnPostChangedListener {

    void onPostChanged(Post post);

    void onPostThumbsFetched(Post post);
}
