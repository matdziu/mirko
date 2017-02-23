package pl.mirko.login;

interface LoginView {

    void showLoginError();

    void showEmptyLoginError();

    void showEmptyPasswordError();

    void hideEmptyLoginError();

    void hideEmptyPasswordError();
}
