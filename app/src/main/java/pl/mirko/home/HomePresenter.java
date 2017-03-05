package pl.mirko.home;

import java.util.List;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.ThumbFetchingListener;
import pl.mirko.models.BasePost;

class HomePresenter extends BasePresenter implements BasePostFetchingListener, ThumbFetchingListener {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private HomeView homeView;

    HomePresenter(FirebaseAuthInteractor firebaseAuthInteractor,
                  FirebaseDatabaseInteractor firebaseDatabaseInteractor, HomeView homeView) {
        super(firebaseAuthInteractor, firebaseDatabaseInteractor);
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.homeView = homeView;
    }

    void fetchPosts() {
        firebaseDatabaseInteractor.fetchPosts(this);
    }

    @Override
    public void onBasePostFetchingStarted() {
        homeView.showProgressBar(true);
    }

    @Override
    public void onBasePostFetchingFinished(List<BasePost> postList) {
        firebaseDatabaseInteractor.fetchPostsThumbs(postList, this);
    }

    @Override
    public void onThumbFetchingFinished(List<BasePost> postList) {
        homeView.updateRecyclerView(postList);
        homeView.showProgressBar(false);
    }
}
