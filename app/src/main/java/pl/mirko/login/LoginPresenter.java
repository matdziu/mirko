package pl.mirko.login;

import pl.mirko.interactors.interfaces.AuthenticationInteractor;

class LoginPresenter implements LoginListener {

    private LoginView loginView;
    private AuthenticationInteractor authenticationInteractor;

    LoginPresenter(LoginView loginView, AuthenticationInteractor authenticationInteractor) {
        this.loginView = loginView;
        this.authenticationInteractor = authenticationInteractor;
    }

    void login(String email, String password) {
        boolean isPasswordCorrect;
        boolean isEmailCorrect;

        if (email.trim().isEmpty()) {
            loginView.showEmptyEmailError();
            isEmailCorrect = false;
        } else {
            loginView.hideEmailError();
            isEmailCorrect = true;
        }

        if (password.trim().isEmpty()) {
            loginView.showEmptyPasswordError();
            isPasswordCorrect = false;
        } else {
            loginView.hidePasswordError();
            isPasswordCorrect = true;
        }

        if (isEmailCorrect && isPasswordCorrect) {
            authenticationInteractor.login(email, password, this);
        }
    }

    void initAuthStateListener() {
        authenticationInteractor.initAuthStateListener(this);
    }

    void addAuthStateListener() {
        authenticationInteractor.addAuthStateListener();
    }

    void removeAuthStateListener() {
        authenticationInteractor.removeAuthStateListener();
    }

    @Override
    public void onLoginStarted() {
        loginView.showProgressBar(true);
    }

    @Override
    public void onLoginSuccessful() {
        loginView.showProgressBar(false);
        loginView.navigateToHome();
    }

    @Override
    public void onLoginFailure() {
        loginView.showProgressBar(false);
        loginView.showLoginError();
    }
}
