package pl.mirko.base;

import pl.mirko.adapters.BasePostsAdapter;

public interface BasePostView {

    void showThumbUpView(BasePostsAdapter.ViewHolder viewHolder);

    void showThumbDownView(BasePostsAdapter.ViewHolder viewHolder);

    void showNoThumbView(BasePostsAdapter.ViewHolder viewHolder);
}