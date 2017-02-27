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
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.models.BasePost;
import pl.mirko.models.Comment;
import pl.mirko.models.Post;

public class HomeFragment extends Fragment {

    @BindView(R.id.home_recycler_view)
    RecyclerView homeRecyclerView;

    private BasePostsAdapter basePostsAdapter;

    private HomePresenter homePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePresenter = new HomePresenter();

        List<BasePost> commentList = new ArrayList<>();
        Comment comment1 = new Comment("mateusz_d", "siema", 5);
        Comment comment2 = new Comment("mateusz_d", "siema", -5);
        commentList.add(comment1);
        commentList.add(comment2);

        List<BasePost> postList = new ArrayList<>();
        postList.add(new Post("michal_b", "siema\nusuncie konta", 5, commentList));
        postList.add(new Post("oscarek", "mama auto\nrejestracja XDDDDD", -4, commentList));
        postList.add(new Post("janusz", "jestem fanatykiem wedkarstwa", 0, commentList));

        basePostsAdapter = new BasePostsAdapter(homePresenter.setScoreColor(postList), getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecyclerView.setAdapter(basePostsAdapter);

        return view;
    }
}
