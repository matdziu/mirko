package pl.mirko.base;

import java.util.ArrayList;
import java.util.List;

import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.models.BasePost;

public class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;

    BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    public BasePresenter() {
        // default constructor
    }

    void logout() {
        firebaseAuthInteractor.logout();
    }

    public List<BasePost> setScoreColor(List<BasePost> basePostList) {
        List<BasePost> outputPostList = new ArrayList<>();
        for (BasePost basePost : basePostList) {
            if (basePost.score > 0) {
                basePost.setScoreColor(android.R.color.holo_green_dark);
            } else if (basePost.score < 0) {
                basePost.setScoreColor(android.R.color.holo_red_dark);
            } else {
                basePost.setScoreColor(android.R.color.darker_gray);
            }
            outputPostList.add(basePost);
        }
        return outputPostList;
    }
}
