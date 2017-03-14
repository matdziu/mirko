package pl.mirko.base;

import pl.mirko.R;
import pl.mirko.interactors.interfaces.AuthenticationInteractor;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.models.BasePost;

import static pl.mirko.interactors.FirebaseDatabaseInteractor.DOWN;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.NO_THUMB;
import static pl.mirko.interactors.FirebaseDatabaseInteractor.UP;

public class BasePresenter {

    private AuthenticationInteractor authenticationInteractor;
    private DatabaseInteractor databaseInteractor;

    protected String currentImageFilePath;
    protected String currentImageName;

    public BasePresenter(AuthenticationInteractor authenticationInteractor,
                         DatabaseInteractor databaseInteractor) {
        this.authenticationInteractor = authenticationInteractor;
        this.databaseInteractor = databaseInteractor;
    }

    protected BasePresenter() {
        // default constructor
    }

    void logout() {
        authenticationInteractor.logout();
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
            databaseInteractor.updateScore(basePost, basePost.getScore() - 1);
            databaseInteractor.sendThumb(NO_THUMB, basePost);
        } else if (previousThumb.equals(DOWN) && thumbClicked.equals(DOWN)) {
            databaseInteractor.updateScore(basePost, basePost.getScore() + 1);
            databaseInteractor.sendThumb(NO_THUMB, basePost);
        } else if (previousThumb.equals(UP) && thumbClicked.equals(DOWN)) {
            databaseInteractor.updateScore(basePost, basePost.getScore() - 2);
            databaseInteractor.sendThumb(DOWN, basePost);
        } else if (previousThumb.equals(DOWN) && thumbClicked.equals(UP)) {
            databaseInteractor.updateScore(basePost, basePost.getScore() + 2);
            databaseInteractor.sendThumb(UP, basePost);
        } else if (previousThumb.equals(NO_THUMB) && thumbClicked.equals(UP)) {
            databaseInteractor.updateScore(basePost, basePost.getScore() + 1);
            databaseInteractor.sendThumb(UP, basePost);
        } else if (previousThumb.equals(NO_THUMB) && thumbClicked.equals(DOWN)) {
            databaseInteractor.updateScore(basePost, basePost.getScore() - 1);
            databaseInteractor.sendThumb(DOWN, basePost);
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

    void setCurrentImageFilePath(String currentImageFilePath) {
        this.currentImageFilePath = currentImageFilePath;
    }

    void setCurrentImageName(String currentImageName) {
        this.currentImageName = currentImageName;
    }
}
