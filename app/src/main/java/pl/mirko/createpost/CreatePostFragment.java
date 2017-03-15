package pl.mirko.createpost;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.TagSuggestionsAdapter;
import pl.mirko.base.BaseFragment;
import pl.mirko.base.BaseMultimediaView;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;

public class CreatePostFragment extends BaseFragment implements CreatePostView, BaseMultimediaView {

    @BindView(R.id.create_edit_text)
    AutoCompleteTextView createPostEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.create_content_view)
    ViewGroup createPostContentView;

    @BindView(R.id.add_multimedia_fab)
    FloatingActionButton addMultimediaButton;

    private CreatePostPresenter createPostPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPostPresenter = new CreatePostPresenter(this, new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor());
        basePresenter = createPostPresenter;
        basePresenter.setBaseMultimediaView(this);
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        createPostEditText.setHint(getResources().getString(R.string.create_post_hint));

        createPostPresenter.fetchTags();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                createPostPresenter.createNewPost(createPostEditText.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            createPostContentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createPostContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showSoftKeyboard(boolean show) {
        CreatePostActivity createPostActivity = (CreatePostActivity) getActivity();
        createPostActivity.showSoftKeyboard(show);
    }

    @Override
    public void setTagSuggestions(List<String> tags) {
        TagSuggestionsAdapter tagSuggestionsAdapter
                = new TagSuggestionsAdapter(new ArrayList<String>(), tags, getContext(), createPostPresenter);
        createPostEditText.setAdapter(tagSuggestionsAdapter);
    }

    @Override
    public void setContent(String content) {
        createPostEditText.setText(content);
    }


    @Override
    public String getCurrentPostContent() {
        return createPostEditText.getText().toString();
    }

    @Override
    public int getCurrentCursorPosition() {
        return createPostEditText.getSelectionStart();
    }

    @Override
    public void setCursorPosition(int position) {
        createPostEditText.setSelection(position);
        createPostEditText.dismissDropDown();
    }

    @OnClick(R.id.add_multimedia_fab)
    public void onAddMultimediaFabClicked() {
        basePresenter.onAddImageFabClicked();
    }

    @Override
    public void showImageAddedInfo() {
        Toast.makeText(getContext(), R.string.photo_added_text, Toast.LENGTH_SHORT).show();
        Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation_add_image);
        rotateAnimation.setFillAfter(true);
        addMultimediaButton.startAnimation(rotateAnimation);
        addMultimediaButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorRed)));
    }

    @Override
    public void showImageDeletedInfo() {
        Toast.makeText(getContext(), R.string.photo_deleted_text, Toast.LENGTH_SHORT).show();
        Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation_delete_image);
        rotateAnimation.setFillAfter(true);
        addMultimediaButton.startAnimation(rotateAnimation);
        addMultimediaButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorAccent)));
    }
}
