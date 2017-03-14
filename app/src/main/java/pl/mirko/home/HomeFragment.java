package pl.mirko.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.createpost.CreatePostActivity;
import pl.mirko.interactors.FirebaseAuthInteractor;
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

        homePresenter = new HomePresenter(new FirebaseAuthInteractor(), new FirebaseDatabaseInteractor(), this);
        basePostsAdapter = new BasePostsAdapter(new ArrayList<BasePost>(), getContext(), homePresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

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
        basePostsAdapter.setNewDataSet(postList);
    }

    @Override
    public void showWrongQueryFormatError() {
        Toast.makeText(getContext(), R.string.wrong_tag_format, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSoftKeyboard(boolean show) {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.showSoftKeyboard(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.home_menu, menu);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                homePresenter.queryPosts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                homePresenter.fetchPosts();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });
    }
}
