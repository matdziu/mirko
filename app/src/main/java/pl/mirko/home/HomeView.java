package pl.mirko.home;

import java.util.List;

import pl.mirko.models.BasePost;

interface HomeView {

    void showProgressBar(boolean show);

    void initDataSet(List<BasePost> postList);

    void addNewItem(BasePost basePost);

    void updateItem(BasePost basePost);

    void showWrongQueryFormatError();

    void showSoftKeyboard(boolean show);
}
