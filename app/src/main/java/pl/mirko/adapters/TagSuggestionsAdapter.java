package pl.mirko.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class TagSuggestionsAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> allTags;
    private List<String> tagSuggestions;

    public TagSuggestionsAdapter(List<String> initialTagSuggestions, List<String> allTags, Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line, initialTagSuggestions);
        this.tagSuggestions = initialTagSuggestions;
        this.allTags = allTags;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<String> filteredTags = new ArrayList<>();
                if (constraint != null) {
                    for (String tag : allTags) {
                        if (tag.equals(constraint.toString())) {
                            filteredTags.add(tag);
                        }
                    }
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
