package pl.mirko.createpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.interactors.FirebaseDatabaseInteractor;

public class CreatePostFragment extends Fragment implements CreatePostView {

    @BindView(R.id.create_post_edit_text)
    EditText createPostEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CreatePostPresenter createPostPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPostPresenter = new CreatePostPresenter(this, new FirebaseDatabaseInteractor());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
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
            createPostEditText.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createPostEditText.setVisibility(View.VISIBLE);
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
}
