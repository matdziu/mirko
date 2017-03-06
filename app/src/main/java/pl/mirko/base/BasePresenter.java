package pl.mirko.base;

import pl.mirko.R;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.models.BasePost;

import static pl.mirko.interactors.FirebaseDatabaseInteractor.DOWN;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.NO_THUMB;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.UP;

public class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    public BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor,
                         FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void logout() {
        firebaseAuthInteractor.logout();
    }

    public BasePost setScoreColor(BasePost basePost) {
        if (basePost.score > 0) {
            basePost.setScoreColor(R.color.colorGreen);
        } else if (basePost.score < 0) {
            basePost.setScoreColor(R.color.colorRed);
        } else {
            basePost.setScoreColor(R.color.colorGrey);
        }
        return basePost;
    }

    public void updateScore(BasePost basePost, String thumbClicked) {
        String previousThumb = basePost.getThumb();
        if (previousThumb.equals(UP) && thumbClicked.equals(UP)) {
            firebaseDatabaseInteractor.updateScore(basePost, basePost.getScore() - 1);
            firebaseDatabaseInteractor.sendThumb(NO_THUMB, basePost);
        } else if (previousThumb.equals(DOWN) && thumbClicked.equals(DOWN)) {
            firebaseDatabaseInteractor.updateScore(basePost, basePost.getScore() + 1);
            firebaseDatabaseInteractor.sendThumb(NO_THUMB, basePost);
        } else if (previousThumb.equals(UP) && thumbClicked.equals(DOWN)) {
            firebaseDatabaseInteractor.updateScore(basePost, basePost.getScore() - 2);
            firebaseDatabaseInteractor.sendThumb(DOWN, basePost);
        } else if (previousThumb.equals(DOWN) && thumbClicked.equals(UP)) {
            firebaseDatabaseInteractor.updateScore(basePost, basePost.getScore() + 2);
            firebaseDatabaseInteractor.sendThumb(UP, basePost);
        } else if (previousThumb.equals(NO_THUMB) && thumbClicked.equals(UP)) {
            firebaseDatabaseInteractor.updateScore(basePost, basePost.getScore() + 1);
            firebaseDatabaseInteractor.sendThumb(UP, basePost);
        } else if (previousThumb.equals(NO_THUMB) && thumbClicked.equals(DOWN)) {
            firebaseDatabaseInteractor.updateScore(basePost, basePost.getScore() - 1);
            firebaseDatabaseInteractor.sendThumb(DOWN, basePost);
        }
    }

    public void setProperThumbView(BasePost basePost, BasePostView basePostView) {
        switch (basePost.getThumb()) {
            case UP:
                basePostView.showThumbUpView();
                break;
            case DOWN:
                basePostView.showThumbDownView();
                break;
            case NO_THUMB:
                basePostView.showNoThumbView();
                break;
        }
    }
}
