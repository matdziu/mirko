package pl.mirko.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.createpost.CreatePostActivity;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.models.BasePost;

public class HomeFragment extends Fragment implements HomeView {

    @BindView(R.id.home_recycler_view)
    RecyclerView homeRecyclerView;

    @BindView(R.id.home_content_view)
    ViewGroup homeContentView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private BasePostsAdapter basePostsAdapter;

    private HomePresenter homePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePresenter = new HomePresenter(new FirebaseDatabaseInteractor(), this);
        basePostsAdapter = new BasePostsAdapter(new ArrayList<BasePost>(), getContext(), homePresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        homePresenter.fetchPosts();

        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecyclerView.setAdapter(basePostsAdapter);

        return view;
    }

    @OnClick(R.id.add_post_fab)
    public void onAddPostFabClicked() {
        startActivity(new Intent(getContext(), CreatePostActivity.class));
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            homeContentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            homeContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateRecyclerView(List<BasePost> postList) {
        basePostsAdapter.setNewData(postList);
    }
}
