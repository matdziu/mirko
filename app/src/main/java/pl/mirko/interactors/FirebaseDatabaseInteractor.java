package pl.mirko.interactors;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.mirko.base.BasePostListener;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.models.BasePost;
import pl.mirko.models.Comment;
import pl.mirko.models.Post;
import pl.mirko.models.User;
import timber.log.Timber;

public class FirebaseDatabaseInteractor implements DatabaseInteractor {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private static final String USERS = "users";
    private static final String POSTS = "posts";

    @Override
    public void createNewUser(User newUser) {
        databaseReference
                .child(USERS)
                .child(newUser.uid)
                .setValue(newUser);
    }

    @Override
    public void createNewPost(Post newPost) {
        String postId = UUID.randomUUID().toString();
        newPost.id = postId;
        databaseReference
                .child(POSTS)
                .child(postId)
                .setValue(newPost);
    }

    @Override
    public void createNewComment(Post post, Comment newComment) {
        post.commentList.add(newComment);
        databaseReference
                .child(POSTS)
                .child(post.id)
                .setValue(post);
    }

    @Override
    public void fetchPosts(final BasePostListener basePostListener) {
        basePostListener.onBasePostFetchingStarted();
        databaseReference
                .child(POSTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<BasePost> basePostList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            basePostList.add(dataItem.getValue(BasePost.class));
                        }
                        basePostListener.onBasePostFetchingFinished(basePostList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }
}
