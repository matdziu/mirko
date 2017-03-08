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

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.listeners.PostChangedListener;
import pl.mirko.listeners.TagFetchingListener;
import pl.mirko.listeners.ThumbFetchingListener;
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
    private static final String THUMBS = "thumbs";
    private static final String SCORE_FIELD_NAME = "score";
    private static final String TAGS = "tags";

    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String NO_THUMB = "noThumb";

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
                            String postId = databaseReference
                                    .child(POSTS)
                                    .push().getKey();

                            User currentUser = dataSnapshot.getValue(User.class);
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
    public void createNewComment(final Post post, final String content,
                                 final BasePostSendingListener basePostSendingListener) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            basePostSendingListener.onBasePostSendingStarted();
            databaseReference
                    .child(USERS)
                    .child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String commentId = databaseReference
                                    .child(COMMENTS)
                                    .child(post.id)
                                    .push().getKey();

                            User currentUser = dataSnapshot.getValue(User.class);
                            Comment newComment = new Comment(currentUser.nickname, content,
                                    commentId, post.id);

                            databaseReference
                                    .child(COMMENTS)
                                    .child(post.id)
                                    .child(commentId)
                                    .setValue(newComment)
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
    public void fetchPosts(final BasePostFetchingListener basePostFetchingListener) {
        basePostFetchingListener.onBasePostFetchingStarted();
        databaseReference
                .child(POSTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<BasePost> postList = new ArrayList<>();
                        for (final DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            postList.add(0, dataItem.getValue(Post.class));
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
    public void fetchComments(Post post, final BasePostFetchingListener basePostFetchingListener) {
        basePostFetchingListener.onBasePostFetchingStarted();
        databaseReference
                .child(COMMENTS)
                .child(post.id)
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

    @Override
    public void updateScore(BasePost basePost, int updatedScore) {
        DatabaseReference basePostReference = null;

        if (basePost instanceof Post) {
            basePostReference = databaseReference
                    .child(POSTS);
        } else if (basePost instanceof Comment) {
            Comment comment = (Comment) basePost;
            basePostReference = databaseReference
                    .child(COMMENTS)
                    .child(comment.commentedPostId);
        }

        if (basePostReference != null) {
            basePostReference
                    .child(basePost.id)
                    .child(SCORE_FIELD_NAME)
                    .setValue(updatedScore);
        }
    }

    @Override
    public void sendThumb(String thumb, BasePost basePost) {
        DatabaseReference basePostReference = null;
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (basePost instanceof Post) {
            basePostReference = databaseReference
                    .child(POSTS);
        } else if (basePost instanceof Comment) {
            Comment comment = (Comment) basePost;
            basePostReference = databaseReference
                    .child(COMMENTS)
                    .child(comment.commentedPostId);
        }

        if (basePostReference != null && firebaseUser != null) {
            basePostReference
                    .child(basePost.id)
                    .child(THUMBS)
                    .child(firebaseUser.getUid())
                    .setValue(thumb);
        }
    }

    @Override
    public void addOnPostChangedListener(Post post, final PostChangedListener postChangedListener) {
        databaseReference
                .child(POSTS)
                .child(post.id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        postChangedListener.onPostChanged(dataSnapshot.getValue(Post.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void fetchPostsThumbs(final List<BasePost> postList,
                                 final ThumbFetchingListener thumbFetchingListener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            databaseReference
                    .child(POSTS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (BasePost post : postList) {
                                if (dataSnapshot.child(post.id)
                                        .child(THUMBS)
                                        .hasChild(firebaseUser.getUid())) {
                                    post.setThumb(dataSnapshot.child(post.id)
                                            .child(THUMBS)
                                            .child(firebaseUser.getUid())
                                            .getValue(String.class));
                                }
                            }
                            thumbFetchingListener.onThumbFetchingFinished(postList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e(databaseError.getMessage());
                        }
                    });
        }
    }

    @Override
    public void fetchCommentsThumbs(String commentedPostId,
                                    final List<BasePost> commentList,
                                    final ThumbFetchingListener thumbFetchingListener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            databaseReference
                    .child(COMMENTS)
                    .child(commentedPostId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (BasePost comment : commentList) {
                                if (dataSnapshot.child(comment.id)
                                        .child(THUMBS)
                                        .hasChild(firebaseUser.getUid())) {
                                    comment.setThumb(dataSnapshot.child(comment.id)
                                            .child(THUMBS)
                                            .child(firebaseUser.getUid())
                                            .getValue(String.class));
                                }
                            }
                            thumbFetchingListener.onThumbFetchingFinished(commentList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e(databaseError.getMessage());
                        }
                    });
        }
    }

    @Override
    public void fetchSinglePostThumbs(final Post post, final PostChangedListener postChangedListener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            databaseReference
                    .child(POSTS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(post.id)
                                    .child(THUMBS)
                                    .hasChild(firebaseUser.getUid())) {
                                post.setThumb(dataSnapshot.child(post.id)
                                        .child(THUMBS)
                                        .child(firebaseUser.getUid())
                                        .getValue(String.class));
                            }
                            postChangedListener.onPostThumbsFetched(post);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e(databaseError.getMessage());
                        }
                    });
        }

    }

    @Override
    public void fetchTags(final TagFetchingListener tagFetchingListener) {
        databaseReference
                .child(TAGS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> tags = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            tags.add(dataItem.getKey());
                        }
                        tagFetchingListener.onTagFetchingFinished(tags);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }
}
