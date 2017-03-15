package pl.mirko.createcomment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.base.BaseFragment;
import pl.mirko.base.BaseMultimediaView;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;
import pl.mirko.models.Post;

import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;

public class CreateCommentFragment extends BaseFragment implements CreateCommentView, BaseMultimediaView {

    @BindView(R.id.create_edit_text)
    EditText createCommentEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.create_content_view)
    ViewGroup createCommentContentView;

    @BindView(R.id.add_multimedia_fab)
    FloatingActionButton addMultimediaButton;

    private CreateCommentPresenter createCommentPresenter;
    private Post post;

    private final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        post = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        createCommentPresenter = new CreateCommentPresenter(this, new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor());
        basePresenter = createCommentPresenter;
        basePresenter.setBaseMultimediaView(this);
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION_CODE:
                if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    getActivity().finish();
                }
        }
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
