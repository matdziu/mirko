package pl.mirko.postdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.models.BasePost;
import pl.mirko.models.Comment;

public class PostDetailFragment extends Fragment {

    @BindView(R.id.author_text_view)
    TextView authorTextView;

    @BindView(R.id.base_post_text_view)
    TextView postTextView;

    @BindView(R.id.score_text_view)
    TextView scoreTextView;

    @BindView(R.id.comments_recycler_view)
    RecyclerView commentsRecyclerView;

    private BasePostsAdapter basePostsAdapter;

    private PostDetailPresenter postDetailPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postDetailPresenter = new PostDetailPresenter();

        List<BasePost> commentList = new ArrayList<>();
        Comment comment1 = new Comment("mateusz_d", "siema", 5);
        Comment comment2 = new Comment("mateusz_d", "siema", -5);
        commentList.add(comment1);
        commentList.add(comment2);

        basePostsAdapter = new BasePostsAdapter(postDetailPresenter.setScoreColor(commentList), getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, view);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(basePostsAdapter);

        return view;
    }
}
