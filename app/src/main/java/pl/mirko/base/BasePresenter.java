package pl.mirko.base;

import java.util.ArrayList;
import java.util.List;

import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.models.BasePost;

public class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    public BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor,
                  FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    protected BasePresenter() {
        // default constructor
    }

    void logout() {
        firebaseAuthInteractor.logout();
    }

    public List<BasePost> setScoreColor(List<BasePost> basePostList) {
        List<BasePost> outputPostList = new ArrayList<>();
        for (BasePost basePost : basePostList) {
            outputPostList.add(setScoreColor(basePost));
        }
        return outputPostList;
    }

    public BasePost setScoreColor(BasePost basePost) {
        if (basePost.score > 0) {
            basePost.setScoreColor(android.R.color.holo_green_dark);
        } else if (basePost.score < 0) {
            basePost.setScoreColor(android.R.color.holo_red_dark);
        } else {
            basePost.setScoreColor(android.R.color.darker_gray);
        }
        return basePost;
    }

    public void updateScore(BasePost basePost, int updatedScore) {
        firebaseDatabaseInteractor.updateScore(basePost, updatedScore);
    }
}
