package pl.mirko.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.base.BasePostView;
import pl.mirko.base.BasePresenter;
import pl.mirko.models.BasePost;
import pl.mirko.models.Comment;
import pl.mirko.models.Post;
import pl.mirko.postdetail.PostDetailActivity;

import static pl.mirko.interactors.FirebaseDatabaseInteractor.DOWN;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.UP;

public class BasePostsAdapter extends RecyclerView.Adapter<BasePostsAdapter.ViewHolder> {

    private List<BasePost> basePostList;
    private Context context;
    private BasePresenter basePresenter;

    public static String POST_KEY = "postContent";

    private static int POST_TYPE = 1;
    private static int COMMENT_TYPE = 0;

    public BasePostsAdapter(List<BasePost> basePostList, Context context, BasePresenter basePresenter) {
        this.basePostList = basePostList;
        this.context = context;
        this.basePresenter = basePresenter;
    }

    @Override
    public int getItemViewType(int position) {
        if (basePostList.get(position) instanceof Post) {
            return POST_TYPE;
        } else if (basePostList.get(position) instanceof Comment) {
            return COMMENT_TYPE;
        } else {
            return -1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_base_post, parent, false);
        if (viewType == POST_TYPE) {
            return new ViewHolder(view, 16);
        } else if (viewType == COMMENT_TYPE) {
            return new ViewHolder(view, 0);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BasePost rawPost = basePostList.get(position);
        final BasePost basePost = basePresenter.setScoreColor(rawPost);

        holder.authorTextView.setText(basePost.author);
        holder.basePostTextView.setText(basePost.content);
        holder.scoreTextView.setText(String.valueOf(basePost.score));
        holder.scoreTextView.setTextColor(ContextCompat.getColor(context, basePost.getScoreColor()));
        holder.thumbUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basePresenter.updateScore(basePost, UP);
            }
        });
        holder.thumbDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basePresenter.updateScore(basePost, DOWN);
            }
        });
        if (basePostList.get(position) instanceof Post) {
            holder.basePostCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(POST_KEY, Parcels.wrap(basePostList.get(holder.getAdapterPosition())));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        basePresenter.setProperThumbView(basePost, holder);

        if (basePost.hasImage) {
            holder.basePostImageView.setVisibility(View.VISIBLE);
            basePresenter.loadImage(basePost, holder);
        }
    }

    public void setNewDataSet(List<BasePost> basePostList) {
        this.basePostList = basePostList;
        notifyDataSetChanged();
    }

    public List<BasePost> getDataSet() {
        return basePostList;
    }

    @Override
    public int getItemCount() {
        return basePostList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements BasePostView {

        @BindView(R.id.author_text_view)
        TextView authorTextView;

        @BindView(R.id.base_post_text_view)
        TextView basePostTextView;

        @BindView(R.id.score_text_view)
        TextView scoreTextView;

        @BindView(R.id.base_post_card_view)
        CardView basePostCardView;

        @BindView(R.id.thumb_up_button)
        ImageButton thumbUpButton;

        @BindView(R.id.thumb_down_button)
        ImageButton thumbDownButton;

        @BindView(R.id.base_post_image_view)
        ImageView basePostImageView;

        private Context context;

        ViewHolder(View itemView, int topMargin) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) basePostCardView.getLayoutParams();
            marginLayoutParams.topMargin = topMargin;
        }

        @Override
        public void showThumbUpView() {
            thumbDownButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey));
            thumbUpButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        @Override
        public void showThumbDownView() {
            thumbUpButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey));
            thumbDownButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
        }

        @Override
        public void showNoThumbView() {
            thumbUpButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey));
            thumbDownButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey));
        }

        @Override
        public void loadImage(String url) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.image_placeholder)
                    .into(basePostImageView);
        }
    }
}
