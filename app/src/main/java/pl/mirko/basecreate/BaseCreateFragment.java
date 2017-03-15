package pl.mirko.basecreate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
    private final int REQUEST_PICK_IMAGE = 1;
    protected BaseCreatePresenter baseCreatePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    File imageFile = createImageFile(createImageGallery());
                    baseCreatePresenter.setCurrentImageFile(imageFile);
                    saveImageToTempFile(imageFile, data);
                    baseCreatePresenter.onImageAdded();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        String imageName = "image_" + timeStamp + "_";
        return File.createTempFile(imageName, ".jpg", galleryFolder);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveImageToTempFile(File imageFile, Intent data) throws IOException {
        InputStream inputStream = getContext()
                .getContentResolver().openInputStream(data.getData());
        if (inputStream != null) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            OutputStream outputStream = new FileOutputStream(imageFile);
            outputStream.write(buffer);
        }
    }
}
