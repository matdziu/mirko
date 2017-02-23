package pl.mirko.login;

import pl.mirko.interactors.FirebaseAuthInteractor;

class LoginPresenter implements LoginListener {

    private LoginView loginView;
    private FirebaseAuthInteractor firebaseAuthInteractor;

    LoginPresenter(LoginView loginView, FirebaseAuthInteractor firebaseAuthInteractor) {
        this.loginView = loginView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
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
            firebaseAuthInteractor.login(email, password, this);
        }
    }

    void initAuthStateListener() {
        firebaseAuthInteractor.initAuthStateListener(this);
    }

    void addAuthStateListener() {
        firebaseAuthInteractor.addAuthStateListener();
    }

    void removeAuthStateListener() {
        firebaseAuthInteractor.removeAuthStateListener();
    }

    @Override
    public void onLoginSuccessful() {
        loginView.navigateToHome();
    }

    @Override
    public void onLoginFailure() {
        loginView.showLoginError();
    }
}
