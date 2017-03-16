package pl.mirko.home;

import java.util.ArrayList;
import java.util.List;

import pl.mirko.base.BasePresenter;
import pl.mirko.interactors.interfaces.AuthenticationInteractor;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.interactors.interfaces.StorageInteractor;
import pl.mirko.listeners.BasePostFetchingListener;
import pl.mirko.listeners.TagFetchingListener;
import pl.mirko.listeners.ThumbFetchingListener;
import pl.mirko.models.BasePost;

class HomePresenter extends BasePresenter implements BasePostFetchingListener,
        ThumbFetchingListener, TagFetchingListener {

    private DatabaseInteractor databaseInteractor;
    private HomeView homeView;
    private List<String> tagSuggestions;

    HomePresenter(AuthenticationInteractor authenticationInteractor,
                  DatabaseInteractor databaseInteractor,
                  StorageInteractor storageInteractor,
                  HomeView homeView) {
        super(authenticationInteractor, databaseInteractor, storageInteractor);
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
        tagSuggestions = tags;
    }

    List<String> filterTagSuggestions(String newText) {
        List<String> filteredTags = new ArrayList<>();
        for (String tag : tagSuggestions) {
            if (tag.startsWith(newText)) filteredTags.add(tag);
        }
        return filteredTags;
    }
}
