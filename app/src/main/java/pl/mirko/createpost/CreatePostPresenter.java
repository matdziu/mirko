package pl.mirko.createpost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.listeners.TagFetchingListener;

class CreatePostPresenter implements BasePostSendingListener, TagFetchingListener {

    private CreatePostView createPostView;
    private DatabaseInteractor databaseInteractor;

    CreatePostPresenter(CreatePostView createPostView, DatabaseInteractor databaseInteractor) {
        this.createPostView = createPostView;
        this.databaseInteractor = databaseInteractor;
    }

    void createNewPost(String content) {
        List<String> contentWords = new ArrayList<>(Arrays.asList(content.split("\\s+")));
        List<String> tags = new ArrayList<>();

        for (String word : contentWords) {
            if (word.startsWith("#") && word.length() > 1) {
                tags.add(word.replace("#", ""));
            }
        }

        databaseInteractor.createNewPost(content, tags, this);
    }

    void fetchTags() {
        databaseInteractor.fetchTags(this);
    }

    @Override
    public void onBasePostSendingStarted() {
        createPostView.showSoftKeyboard(false);
        createPostView.showProgressBar(true);
    }

    @Override
    public void onBasePostSendingFinished() {
        createPostView.showProgressBar(false);
        createPostView.finish();
    }

    @Override
    public void onTagFetchingFinished(List<String> tags) {
        List<String> formattedTags = new ArrayList<>();
        for (String rawTag : tags) {
            formattedTags.add("#" + rawTag);
        }
        createPostView.setTagSuggestions(formattedTags);
    }
}
