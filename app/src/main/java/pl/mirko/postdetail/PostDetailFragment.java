package pl.mirko.postdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;

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
    private Post post;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postDetailPresenter = new PostDetailPresenter();

        Post rawPost = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        post = (Post) postDetailPresenter.setScoreColor(rawPost);

        basePostsAdapter = new BasePostsAdapter(postDetailPresenter.setScoreColor(post.commentList),
                getContext(), postDetailPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, view);

        authorTextView.setText(post.author);
        postTextView.setText(post.postContent);
        scoreTextView.setText(String.valueOf(post.score));
        scoreTextView.setTextColor(ContextCompat.getColor(getContext(), post.getScoreColor()));

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(basePostsAdapter);

        return view;
    }

    @OnClick(R.id.thumb_up_button)
    public void onThumbUpButtonClicked() {
        post.increaseScore();
        updateScoreView(post);
    }

    @OnClick(R.id.thumb_down_button)
    public void onThumbDownButtonClicked() {
        post.decreaseScore();
        updateScoreView(post);
    }

    private void updateScoreView(BasePost basePost) {
        BasePost formattedPost = postDetailPresenter.setScoreColor(basePost);
        scoreTextView.setText(String.valueOf(formattedPost.score));
        scoreTextView.setTextColor(ContextCompat.getColor(getContext(), formattedPost.getScoreColor()));
    }
}
