package pl.mirko.base;

import android.support.annotation.Nullable;

import pl.mirko.adapters.BasePostsAdapter;

public interface BasePostView {

    void setThumbUpView(@Nullable BasePostsAdapter.ViewHolder viewHolder);

    void setThumbDownView(@Nullable BasePostsAdapter.ViewHolder viewHolder);
}