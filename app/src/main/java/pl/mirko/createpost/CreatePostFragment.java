package pl.mirko.createpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import pl.mirko.R;
import pl.mirko.adapters.TagSuggestionsAdapter;
import pl.mirko.basecreate.BaseCreateFragment;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;

public class CreatePostFragment extends BaseCreateFragment implements CreatePostView {

    private CreatePostPresenter createPostPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPostPresenter = new CreatePostPresenter(this, new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor(), this);
        baseCreatePresenter = createPostPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createEditText.setHint(getResources().getString(R.string.create_post_hint));
        createPostPresenter.fetchTags();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                createPostPresenter.createNewPost(createEditText.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTagSuggestions(List<String> tags) {
        TagSuggestionsAdapter tagSuggestionsAdapter
                = new TagSuggestionsAdapter(new ArrayList<String>(), tags, getContext(), createPostPresenter);
        createEditText.setAdapter(tagSuggestionsAdapter);
    }

    @Override
    public void setContent(String content) {
        createEditText.setText(content);
    }


    @Override
    public String getCurrentPostContent() {
        return createEditText.getText().toString();
    }

    @Override
    public int getCurrentCursorPosition() {
        return createEditText.getSelectionStart();
    }

    @Override
    public void setCursorPosition(int position) {
        createEditText.setSelection(position);
        createEditText.dismissDropDown();
    }
}
