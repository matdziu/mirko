package pl.mirko.basecreate;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.base.BaseActivity;

import static android.app.Activity.RESULT_OK;

public class BaseCreateFragment extends Fragment implements BaseCreateView {

    @BindView(R.id.create_edit_text)
    protected AutoCompleteTextView createEditText;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    @BindView(R.id.create_content_view)
    protected ViewGroup createContentView;

    @BindView(R.id.add_multimedia_fab)
    protected FloatingActionButton addMultimediaButton;

    private final int REQUEST_PICK_IMAGE = 1;
    protected BaseCreatePresenter baseCreatePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                baseCreatePresenter.setCurrentImageUri(data.getData());
                baseCreatePresenter.onImageAdded();
            }
        }
    }

    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            createContentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createContentView.setVisibility(View.VISIBLE);
        }
    }

    public void finish() {
        getActivity().finish();
    }

    public void showSoftKeyboard(boolean show) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.showSoftKeyboard(show);
    }

    @OnClick(R.id.add_multimedia_fab)
    public void onAddMultimediaFabClicked() {
        baseCreatePresenter.onAddImageFabClicked();
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

    @Override
    public void startImagePickActivity() {
        Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoIntent.setType("image/*");
        startActivityForResult(photoIntent, REQUEST_PICK_IMAGE);
    }
}
