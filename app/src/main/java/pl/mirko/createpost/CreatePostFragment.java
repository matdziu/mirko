package pl.mirko.createpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.TagSuggestionsAdapter;
import pl.mirko.base.BaseFragment;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;

import static android.app.Activity.RESULT_OK;

public class CreatePostFragment extends BaseFragment implements CreatePostView {

    @BindView(R.id.create_edit_text)
    AutoCompleteTextView createPostEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.create_content_view)
    ViewGroup createPostContentView;

    private CreatePostPresenter createPostPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPostPresenter = new CreatePostPresenter(this, new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor());
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
        startImagePickActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    File imageFile = createImageFile(createImageGallery());
                    createPostPresenter.setCurrentImageFilePath(imageFile.getPath());
                    createPostPresenter.setCurrentImageName(imageName);
                    saveImageToTempFile(imageFile, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
