package pl.mirko.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

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
import pl.mirko.interactors.FirebaseStorageInteractor;
import pl.mirko.models.BasePost;

import static android.app.Activity.RESULT_OK;
import static pl.mirko.basecreate.BaseCreateFragment.BASE_POST_ID;
import static pl.mirko.basecreate.BaseCreateFragment.CREATE_BASE_POST_REQUEST_CODE;

public class HomeFragment extends Fragment implements HomeView {

    @BindView(R.id.home_recycler_view)
    RecyclerView homeRecyclerView;

    @BindView(R.id.home_content_view)
    ViewGroup homeContentView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.home_swipe_refresh)
    SwipeRefreshLayout homeSwipeRefresh;

    private BasePostsAdapter basePostsAdapter;

    private HomePresenter homePresenter;

    private CursorAdapter suggestionsAdapter;

    private boolean searchMode;
    private String currentSearchQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePresenter = new HomePresenter(new FirebaseAuthInteractor(), new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor(), this);
        suggestionsAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_tag_suggestion,
                null, new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1}, new int[]{R.id.tag_suggestion_text_view}, 0);

        basePostsAdapter = new BasePostsAdapter(getContext(), homePresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        homePresenter.fetchPosts(String.valueOf(System.currentTimeMillis()), true);
        homePresenter.fetchTags();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(layoutManager);
        homeRecyclerView.setAdapter(basePostsAdapter);
        homeRecyclerView.addOnScrollListener(new InfiniteScrollListener(3, layoutManager) {

            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                if (!searchMode) {
                    homePresenter.fetchPosts(String.valueOf(Long.valueOf(basePostsAdapter.getLastItemKey()) - 1), false);
                } else {
                    homePresenter.queryPosts(currentSearchQuery, false,
                            String.valueOf(Long.valueOf(basePostsAdapter.getLastItemKey()) - 1));
                }
            }
        });

        homeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!searchMode) {
                    basePostsAdapter.clearDataSet();
                    homePresenter.fetchPosts(String.valueOf(System.currentTimeMillis()), false);
                } else {
                    homeSwipeRefresh.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @OnClick(R.id.add_post_fab)
    public void onAddPostFabClicked() {
        startActivityForResult(new Intent(getContext(), CreatePostActivity.class), CREATE_BASE_POST_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_BASE_POST_REQUEST_CODE && resultCode == RESULT_OK && !searchMode) {
            basePostsAdapter.clearDataSet();
            homePresenter.fetchPosts(data.getStringExtra(BASE_POST_ID), false);
        }
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            homeContentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            homeContentView.setVisibility(View.VISIBLE);
            homeSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void updateDataSet(List<BasePost> postList) {
        basePostsAdapter.updateDataSet(postList);
        homeRecyclerView.setItemViewCacheSize(basePostsAdapter.getItemCount());
        homePresenter.addPostEventListener();
    }

    @Override
    public void updateItem(BasePost basePost) {
        basePostsAdapter.updateItem(basePost);
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
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSuggestionsAdapter(suggestionsAdapter);

        final List<String> filteredTags = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                basePostsAdapter.clearDataSet();
                currentSearchQuery = query;
                homePresenter.queryPosts(query, true, String.valueOf(System.currentTimeMillis()));
                searchMode = true;
                homeSwipeRefresh.setEnabled(false);
                homeContentView.requestFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    suggestionsAdapter.swapCursor(null);
                } else {
                    filteredTags.clear();
                    filteredTags.addAll(homePresenter.filterTagSuggestions(newText));
                    suggestionsAdapter.swapCursor(createNewCursor(filteredTags));
                }
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                basePostsAdapter.clearDataSet();
                homePresenter.fetchPosts(String.valueOf(System.currentTimeMillis()), true);
                searchMode = false;
                homeSwipeRefresh.setEnabled(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                basePostsAdapter.clearDataSet();
                searchView.setQuery(filteredTags.get(position), true);
                searchMode = true;
                homeSwipeRefresh.setEnabled(false);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
        });
    }

    private MatrixCursor createNewCursor(List<String> filteredTags) {
        String[] columns = {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1
        };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (int index = 0; index < filteredTags.size(); index++) {
            String[] row = {Integer.toString(index), filteredTags.get(index)};
            matrixCursor.addRow(row);
        }
        return matrixCursor;
    }
}
