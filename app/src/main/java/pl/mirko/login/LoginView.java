package pl.mirko.login;

interface LoginView {

    void showLoginError();

    void showEmptyLoginError();

    void showEmptyPasswordError();

    void hideLoginError();

    void hidePasswordError();
}
