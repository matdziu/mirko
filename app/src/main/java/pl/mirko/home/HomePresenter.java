package pl.mirko.home;

import java.util.List;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.interfaces.AuthenticationInteractor;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.TagFetchingListener;
import pl.mirko.listeners.ThumbFetchingListener;
import pl.mirko.models.BasePost;

class HomePresenter extends BasePresenter implements BasePostFetchingListener,
        ThumbFetchingListener, TagFetchingListener {

    private DatabaseInteractor databaseInteractor;
    private HomeView homeView;

    HomePresenter(AuthenticationInteractor authenticationInteractor,
                  DatabaseInteractor databaseInteractor, HomeView homeView) {
        super(authenticationInteractor, databaseInteractor);
        this.databaseInteractor = databaseInteractor;
        this.homeView = homeView;
    }

    void fetchPosts() {
        databaseInteractor.fetchPosts(this);
    }

    @Override
    public void onBasePostFetchingStarted() {
        homeView.showProgressBar(true);
    }

    @Override
    public void onBasePostFetchingFinished(List<BasePost> postList) {
        databaseInteractor.fetchPostsThumbs(postList, this);
    }

    @Override
    public void onThumbFetchingFinished(List<BasePost> postList) {
        homeView.updateRecyclerView(postList);
        homeView.showProgressBar(false);
    }

    void queryPosts(String tag) {
        if (tag != null && !tag.contains(".") && !tag.contains("#") &&
                !tag.contains("$") && !tag.contains("[") && !tag.contains("]")) {
            databaseInteractor.queryPosts(tag.trim(), this);
            homeView.showSoftKeyboard(false);
        } else {
            homeView.showWrongQueryFormatError();
        }
    }

    void fetchTags() {
        databaseInteractor.fetchTags(this);
    }

    @Override
    public void onTagFetchingFinished(List<String> tags) {
        homeView.setTagSuggestions(tags);
    }
}
