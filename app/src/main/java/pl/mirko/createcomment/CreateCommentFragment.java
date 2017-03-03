package pl.mirko.createcomment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mirko.R;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.models.Post;

import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;

public class CreateCommentFragment extends Fragment implements CreateCommentView {

    @BindView(R.id.create_comment_edit_text)
    EditText createCommentEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CreateCommentPresenter createCommentPresenter;
    private Post post;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        post = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        createCommentPresenter = new CreateCommentPresenter(this, new FirebaseDatabaseInteractor());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_comment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
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
            createCommentEditText.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createCommentEditText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        getActivity().finish();
    }
}
