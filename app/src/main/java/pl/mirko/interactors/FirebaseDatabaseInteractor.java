package pl.mirko.interactors;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.models.BasePost;
import pl.mirko.models.Comment;
import pl.mirko.models.Post;
import pl.mirko.models.User;
import timber.log.Timber;

public class FirebaseDatabaseInteractor implements DatabaseInteractor {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private static final String USERS = "users";
    private static final String POSTS = "posts";
    private static final String COMMENTS = "comments";

    @Override
    public void createNewUser(User newUser) {
        databaseReference
                .child(USERS)
                .child(newUser.uid)
                .setValue(newUser);
    }

    @Override
    public void createNewPost(final String content, final BasePostSendingListener basePostSendingListener) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            basePostSendingListener.onBasePostSendingStarted();
            databaseReference
                    .child(USERS)
                    .child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            String postId = UUID.randomUUID().toString();
                            Post newPost = new Post(currentUser.nickname, content, postId);
                            databaseReference
                                    .child(POSTS)
                                    .child(postId)
                                    .setValue(newPost)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            basePostSendingListener.onBasePostSendingFinished();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e(databaseError.getMessage());
                        }
                    });
        }
    }

    @Override
    public void createNewComment(String postId, Comment newComment,
                                 final BasePostSendingListener basePostSendingListener) {
        basePostSendingListener.onBasePostSendingStarted();
        databaseReference
                .child(COMMENTS)
                .push()
                .setValue(newComment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        basePostSendingListener.onBasePostSendingFinished();
                    }
                });
    }

    @Override
    public void fetchPosts(final BasePostFetchingListener basePostFetchingListener) {
        basePostFetchingListener.onBasePostFetchingStarted();
        databaseReference
                .child(POSTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<BasePost> postList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            postList.add(dataItem.getValue(Post.class));
                        }
                        basePostFetchingListener.onBasePostFetchingFinished(postList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void fetchComments(final BasePostFetchingListener basePostFetchingListener) {
        basePostFetchingListener.onBasePostFetchingStarted();
        databaseReference
                .child(COMMENTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<BasePost> commentList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            commentList.add(dataItem.getValue(Comment.class));
                        }
                        basePostFetchingListener.onBasePostFetchingFinished(commentList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }
}
