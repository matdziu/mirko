package pl.mirko.createpost;

import java.util.List;

import pl.mirko.basecreate.BaseCreateView;

interface CreatePostView extends BaseCreateView {

    void finishWithResult(String basePostId);

    void setTagSuggestions(List<String> tags);

    void setContent(String content);

    String getCurrentPostContent();

    int getCurrentCursorPosition();

    void setCursorPosition(int position);
}
