package pl.mirko.createcomment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.base.BaseFragment;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;
import pl.mirko.models.Post;

import static android.app.Activity.RESULT_OK;
import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;

public class CreateCommentFragment extends BaseFragment implements CreateCommentView {

    @BindView(R.id.create_edit_text)
    EditText createCommentEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.create_content_view)
    ViewGroup createCommentContentView;

    private CreateCommentPresenter createCommentPresenter;
    private Post post;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        post = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        createCommentPresenter = new CreateCommentPresenter(this, new FirebaseDatabaseInteractor(),
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
        createCommentEditText.setHint(getResources().getString(R.string.create_comment_hint));
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                createCommentPresenter.createNewComment(post, createCommentEditText.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showSoftKeyboard(boolean show) {
        CreateCommentActivity createCommentActivity = (CreateCommentActivity) getActivity();
        createCommentActivity.showSoftKeyboard(false);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            createCommentContentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createCommentContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        getActivity().finish();
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
                    createCommentPresenter.setCurrentImageFilePath(imageFile.getPath());
                    createCommentPresenter.setCurrentImageName(imageName);
                    saveImageToTempFile(imageFile, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
