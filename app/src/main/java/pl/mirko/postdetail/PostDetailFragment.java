package pl.mirko.postdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.BasePostsAdapter;
import pl.mirko.createcomment.CreateCommentActivity;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;

import static android.app.Activity.RESULT_OK;
import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;
import static pl.mirko.basecreate.BaseCreateFragment.BASE_POST_ID;
import static pl.mirko.basecreate.BaseCreateFragment.CREATE_BASE_POST_REQUEST_CODE;
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

    @BindView(R.id.base_post_image_view)
    ImageView basePostImageView;

    @BindView(R.id.post_detail_swipe_refresh)
    SwipeRefreshLayout postDetailSwipeRefresh;

    private BasePostsAdapter basePostsAdapter;

    private PostDetailPresenter postDetailPresenter;

    private Post rawPost;
    private Post post;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postDetailPresenter = new PostDetailPresenter(new FirebaseAuthInteractor(),
                new FirebaseDatabaseInteractor(), new FirebaseStorageInteractor(), this);

        rawPost = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        postDetailPresenter.setPostId(rawPost.id);
        basePostsAdapter = new BasePostsAdapter(getContext(), postDetailPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, view);

        if (rawPost.hasImage) {
            basePostImageView.setVisibility(View.VISIBLE);
            postDetailPresenter.loadImage(rawPost, this);
        }

        showPostDetails(rawPost);
        postDetailPresenter.addOnPostChangedListener(post);
        postDetailPresenter.fetchComments(post, String.valueOf(System.currentTimeMillis()), true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        commentsRecyclerView.setLayoutManager(layoutManager);
        commentsRecyclerView.setAdapter(basePostsAdapter);
        commentsRecyclerView.setNestedScrollingEnabled(false);
        commentsRecyclerView.addOnScrollListener(new InfiniteScrollListener(3, layoutManager) {

            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                postDetailPresenter.fetchComments(post, String.valueOf(Long.valueOf(basePostsAdapter.getLastItemKey()) - 1), false);
            }
        });

        postDetailSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                basePostsAdapter.clearDataSet();
                postDetailPresenter.fetchComments(post, String.valueOf(System.currentTimeMillis()), false);
            }
        });

        return view;
    }

    @OnClick(R.id.thumb_up_button)
    public void onThumbUpButtonClicked() {
        postDetailPresenter.updateScore(post, UP);
    }

    @OnClick(R.id.thumb_down_button)
    public void onThumbDownButtonClicked() {
        postDetailPresenter.updateScore(post, DOWN);
    }

    @OnClick(R.id.add_comment_fab)
    public void onAddCommentFabClicked() {
        Intent intent = new Intent(getContext(), CreateCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(POST_KEY, Parcels.wrap(post));
        intent.putExtras(bundle);
        startActivityForResult(intent, CREATE_BASE_POST_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_BASE_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            basePostsAdapter.clearDataSet();
            postDetailPresenter.fetchComments(post, data.getStringExtra(BASE_POST_ID), false);
        }
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            commentsRecyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            commentsRecyclerView.setVisibility(View.VISIBLE);
            postDetailSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void initDataSet(List<BasePost> commentList) {
        basePostsAdapter.updateDataSet(commentList);
        commentsRecyclerView.setItemViewCacheSize(basePostsAdapter.getItemCount());
        postDetailPresenter.addCommentEventListener();
    }

    @Override
    public void updateItem(BasePost basePost) {
        basePostsAdapter.updateItem(basePost);
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

    @Override
    public void showThumbDownView() {
        if (getContext() != null) {
            thumbUpButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
            thumbDownButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
        }
    }

    @Override
    public void showThumbUpView() {
        if (getContext() != null) {
            thumbDownButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
            thumbUpButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        }
    }

    @Override
    public void showNoThumbView() {
        if (getContext() != null) {
            thumbDownButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
            thumbUpButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
        }
    }

    @Override
    public void loadImage(String url) {
        basePostImageView.layout(0, 0, 0, 0);
        Glide.with(getContext())
                .load(url)
                .into(basePostImageView);
    }
}
