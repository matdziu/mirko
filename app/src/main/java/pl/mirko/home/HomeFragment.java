package pl.mirko.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.adapters.PostsAdapter;
import pl.mirko.models.Post;

public class HomeFragment extends Fragment {

    @BindView(R.id.home_recycler_view)
    RecyclerView homeRecyclerView;

    private PostsAdapter postsAdapter;

    private HomePresenter homePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePresenter = new HomePresenter();

        List<Post> postList = new ArrayList<>();
        postList.add(new Post("michal_b", "Siema witam na mirko!\nUsuncie konto iks de de de de", 5));
        postList.add(new Post("michal_b", "Siema witam na mirko!\nUsuncie konto iks de de de de", -2));
        postList.add(new Post("michal_b", "Siema witam na mirko!\nUsuncie konto iks de de de de", 0));

        postsAdapter = new PostsAdapter(homePresenter.setScoreColor(postList), getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecyclerView.setAdapter(postsAdapter);

        return view;
    }
}
