package pl.mirko.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.models.BasePost;
import pl.mirko.models.Post;
import pl.mirko.postdetail.PostDetailActivity;

public class BasePostsAdapter extends RecyclerView.Adapter<BasePostsAdapter.ViewHolder> {

    private List<BasePost> basePostList;
    private Context context;

    public BasePostsAdapter(List<BasePost> basePostList, Context context) {
        this.basePostList = basePostList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_base_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.authorTextView.setText(basePostList.get(position).author);
        holder.basePostTextView.setText(basePostList.get(position).post);
        holder.scoreTextView.setText(String.valueOf(basePostList.get(position).score));
        holder.scoreTextView.setTextColor(ContextCompat.getColor(context, basePostList.get(position).getScoreColor()));
        if (basePostList.get(position) instanceof Post) {
            holder.basePostCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PostDetailActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return basePostList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author_text_view)
        TextView authorTextView;

        @BindView(R.id.base_post_text_view)
        TextView basePostTextView;

        @BindView(R.id.score_text_view)
        TextView scoreTextView;

        @BindView(R.id.base_post_card_view)
        CardView basePostCardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}