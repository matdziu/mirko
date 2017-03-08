package pl.mirko.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.createpost.CreatePostPresenter;

public class TagSuggestionsAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> allTags;
    private List<String> tagSuggestions;
    private CreatePostPresenter createPostPresenter;
    private Context context;

    public TagSuggestionsAdapter(List<String> initialTagSuggestions, List<String> allTags,
                                 Context context, CreatePostPresenter createPostPresenter) {
        super(context, R.layout.item_tag_suggestion, initialTagSuggestions);
        this.tagSuggestions = initialTagSuggestions;
        this.allTags = allTags;
        this.createPostPresenter = createPostPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_tag_suggestion, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tagSuggestionTextView.setText(tagSuggestions.get(position));

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tag_suggestion_text_view)
        TextView tagSuggestionTextView;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    List<String> filteredTags = createPostPresenter
                            .filterTagSuggestions(constraint.toString(), allTags);
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredTags;
                    filterResults.count = filteredTags.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tagSuggestions.clear();
                if (results != null && results.count > 0) {
                    List<?> filteredTags = (List<?>) results.values;
                    for (Object filteredTag : filteredTags) {
                        if (filteredTag instanceof String) {
                            tagSuggestions.add((String) filteredTag);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        };
    }
}
