package pl.mirko.createcomment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import pl.mirko.R;
import pl.mirko.basecreate.BaseCreateFragment;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.interactors.FirebaseStorageInteractor;
import pl.mirko.models.Post;

import static pl.mirko.adapters.BasePostsAdapter.POST_KEY;

public class CreateCommentFragment extends BaseCreateFragment implements CreateCommentView {

    private CreateCommentPresenter createCommentPresenter;
    private Post post;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        post = Parcels.unwrap(getActivity()
                .getIntent()
                .getExtras()
                .getParcelable(POST_KEY));

        createCommentPresenter = new CreateCommentPresenter(new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor(), this);
        baseCreatePresenter = createCommentPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createEditText.setHint(getResources().getString(R.string.create_comment_hint));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                createCommentPresenter.createNewComment(post, createEditText.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
