package pl.mirko.base;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.mirko.R;

import static android.app.Activity.RESULT_OK;

public class BaseFragment extends Fragment {

    protected int REQUEST_PICK_IMAGE = 1;
    protected BasePresenter basePresenter;

    protected void startImagePickActivity() {
        Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoIntent.setType("image/*");
        startActivityForResult(photoIntent, REQUEST_PICK_IMAGE);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected File createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File galleryFolder = new File(storageDirectory, getResources().getString(R.string.app_name));
        if (!galleryFolder.exists()) {
            galleryFolder.mkdirs();
        }
        return galleryFolder;
    }

    protected File createImageFile(File galleryFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageName = "image_" + timeStamp + "_";
        return File.createTempFile(imageName, ".jpg", galleryFolder);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void saveImageToTempFile(File imageFile, Intent data) throws IOException {
        InputStream inputStream = getContext()
                .getContentResolver().openInputStream(data.getData());
        if (inputStream != null) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            OutputStream outputStream = new FileOutputStream(imageFile);
            outputStream.write(buffer);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    File imageFile = createImageFile(createImageGallery());
                    basePresenter.setCurrentImageFilePath(imageFile.getPath());
                    basePresenter.setCurrentImageName(imageFile.getName());
                    saveImageToTempFile(imageFile, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
