package pl.mirko.home;

import java.util.List;

import pl.mirko.models.BasePost;

interface HomeView {

    void showProgressBar(boolean show);

    void updateRecyclerView(List<BasePost> postList);

    void showWrongQueryFormatError();

    void showSoftKeyboard(boolean show);

    void setTagSuggestions(List<String> tags);
}
