package pl.mirko.postdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.createcomment.CreateCommentActivity;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.DOWN;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.UP;

public class PostDetailFragment extends Fragment implements PostDetailView {

    @BindView(R.id.author_text_view)
    TextView authorTextView;

    @BindView(R.id.base_post_text_view)
    TextView postTextView;

    @BindView(R.id.score_text_view)
    TextView scoreTextView;

    @BindView(R.id.comments_recycler_view)
    RecyclerView commentsRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.thumb_up_button)
    ImageButton thumbUpButton;

    @BindView(R.id.thumb_down_button)
    ImageButton thumbDownButton;

    private BasePostsAdapter basePostsAdapter;

    private PostDetailPresenter postDetailPresenter;

    private Post rawPost;
    private Post post;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postDetailPresenter = new PostDetailPresenter(new FirebaseAuthInteractor(),
                new FirebaseDatabaseInteractor(), this);

        rawPost = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        postDetailPresenter.setPostId(rawPost.id);

        basePostsAdapter = new BasePostsAdapter(new ArrayList<BasePost>(), getContext(), postDetailPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, view);

        showPostDetails(rawPost);
        postDetailPresenter.addOnPostChangedListener(post);
        postDetailPresenter.fetchComments(post);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(basePostsAdapter);

        return view;
    }

    @OnClick(R.id.thumb_up_button)
    public void onThumbUpButtonClicked() {
        postDetailPresenter.sendThumb(UP, post);
        int updatedScore = post.getScore() + 1;
        postDetailPresenter.updateScore(post, updatedScore);
        thumbDownButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
        thumbUpButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        thumbDownButton.setEnabled(true);
        thumbUpButton.setEnabled(false);
    }

    @OnClick(R.id.thumb_down_button)
    public void onThumbDownButtonClicked() {
        postDetailPresenter.sendThumb(DOWN, post);
        int updatedScore = post.getScore() - 1;
        postDetailPresenter.updateScore(post, updatedScore);
        thumbUpButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
        thumbDownButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
        thumbDownButton.setEnabled(false);
        thumbUpButton.setEnabled(true);
    }

    @OnClick(R.id.add_comment_fab)
    public void onAddCommentFabClicked() {
        Intent intent = new Intent(getContext(), CreateCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(POST_KEY, Parcels.wrap(post));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            commentsRecyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            commentsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateRecyclerView(List<BasePost> commentList) {
        basePostsAdapter.setNewData(commentList);
    }

    @Override
    public void showPostDetails(Post rawPost) {
        this.post = (Post) postDetailPresenter.setScoreColor(rawPost);
        authorTextView.setText(post.author);
        postTextView.setText(post.content);
        scoreTextView.setText(String.valueOf(post.score));
        if (getContext() != null) {
            scoreTextView.setTextColor(ContextCompat.getColor(getContext(), post.getScoreColor()));
        }
    }
}
