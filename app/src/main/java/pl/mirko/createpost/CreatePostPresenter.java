package pl.mirko.createpost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.listeners.BasePostSendingListener;
import pl.mirko.listeners.TagFetchingListener;

public class CreatePostPresenter implements BasePostSendingListener, TagFetchingListener {

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
            if (word.startsWith("#") && word.length() > 1 && !word.contains(".") &&
                    !word.contains("$") && !word.contains("[") && !word.contains("]")) {
                String formattedTag = word.replace("#", "");
                if (!formattedTag.equals("")) {
                    tags.add(word.replace("#", ""));
                }
            }
        }
        databaseInteractor.createNewPost(content, tags, this);
    }

    void fetchTags() {
        databaseInteractor.fetchTags(this);
    }

    public List<String> filterTagSuggestions(String postContent, List<String> allTags) {
        List<String> filteredTags = new ArrayList<>();
        StringBuilder contentStringBuilder = new StringBuilder(postContent);
        int currentCursorPosition = createPostView.getCurrentCursorPosition();

        for (int index = currentCursorPosition - 1; index >= 0; index--) {
            if (contentStringBuilder.charAt(index) == '#') {
                for (String tag : allTags) {
                    if (tag.startsWith(contentStringBuilder.substring(index, currentCursorPosition))) {
                        filteredTags.add(tag);
                    }
                }
            }
        }
        return filteredTags;
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

    public void appendTag(String tag) {
        StringBuilder contentStringBuilder = new StringBuilder(createPostView.getCurrentPostContent());
        int currentCursorPosition = createPostView.getCurrentCursorPosition();

        for (int index = currentCursorPosition - 1; index >= 0; index--) {
            if (contentStringBuilder.charAt(index) == '#') {
                contentStringBuilder.replace(index, currentCursorPosition, tag);
                createPostView.setContent(contentStringBuilder.toString());
                createPostView.setCursorPosition(index + tag.length());
                break;
            }
        }
    }
}
