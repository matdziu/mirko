package pl.mirko.createpost;

import java.util.List;

interface CreatePostView {

    void showProgressBar(boolean show);

    void finish();

    void showSoftKeyboard(boolean show);

    void setTagSuggestions(List<String> tags);
}
