package pl.mirko.login;

interface LoginView {

    void showLoginError();

    void showEmptyEmailError();

    void showEmptyPasswordError();

    void hideEmailError();

    void hidePasswordError();
}
