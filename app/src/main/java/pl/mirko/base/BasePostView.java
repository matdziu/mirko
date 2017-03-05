package pl.mirko.base;

import android.support.annotation.Nullable;

import pl.mirko.adapters.BasePostsAdapter;

public interface BasePostView {

    void showThumbUpView(@Nullable BasePostsAdapter.ViewHolder viewHolder);

    void showThumbDownView(@Nullable BasePostsAdapter.ViewHolder viewHolder);
}