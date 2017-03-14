package pl.mirko.createpost;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.adapters.TagSuggestionsAdapter;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;

import static android.app.Activity.RESULT_OK;

public class CreatePostFragment extends Fragment implements CreatePostView {

    @BindView(R.id.create_edit_text)
    AutoCompleteTextView createPostEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.create_content_view)
    ViewGroup createPostContentView;

    private CreatePostPresenter createPostPresenter;

    private int REQUEST_PICK_IMAGE = 1;

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
        Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoIntent.setType("image/*");
        startActivityForResult(photoIntent, REQUEST_PICK_IMAGE);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    File imageFile = createImageFile(createImageGallery());
                    createPostPresenter.setCurrentImageFilePath(imageFile.getPath());

                    InputStream inputStream = getContext()
                            .getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        byte[] buffer = new byte[inputStream.available()];
                        inputStream.read(buffer);

                        OutputStream outputStream = new FileOutputStream(imageFile);
                        outputStream.write(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File galleryFolder = new File(storageDirectory, getResources().getString(R.string.app_name));
        if (!galleryFolder.exists()) {
            galleryFolder.mkdirs();
        }
        return galleryFolder;
    }

    private File createImageFile(File galleryFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "image_" + timeStamp + "_";
        createPostPresenter.setCurrentImageName(imageFileName);
        return File.createTempFile(imageFileName, ".jpg", galleryFolder);
    }
}
