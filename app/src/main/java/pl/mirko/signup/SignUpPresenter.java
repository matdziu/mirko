package pl.mirko.signup;

import pl.mirko.interactors.interfaces.AuthenticationInteractor;
import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.models.User;

class SignUpPresenter implements SignUpListener {

    private SignUpView signUpView;
    private AuthenticationInteractor authenticationInteractor;
    private DatabaseInteractor databaseInteractor;

    SignUpPresenter(SignUpView signUpView,
                    AuthenticationInteractor authenticationInteractor,
                    DatabaseInteractor firebaseDatabaseInteractor) {
        this.signUpView = signUpView;
        this.authenticationInteractor = authenticationInteractor;
        this.databaseInteractor = firebaseDatabaseInteractor;
    }

    void createAccount(String email, String nickname, String password) {
        boolean isEmailCorrect;
        boolean isPasswordCorrect;
        boolean isNicknameCorrect;

        if (email.trim().isEmpty()) {
            signUpView.showEmptyEmailError();
            isEmailCorrect = false;
        } else {
            signUpView.hideEmailError();
            isEmailCorrect = true;
        }

        if (password.length() < 6) {
            signUpView.showPasswordTooShortError();
            isPasswordCorrect = false;
        } else {
            signUpView.hidePasswordError();

            if (password.contains(" ")) {
                signUpView.showWrongPasswordError();
                isPasswordCorrect = false;
            } else {
                signUpView.hidePasswordError();
                isPasswordCorrect = true;
            }
        }

        if (nickname.trim().isEmpty()) {
            signUpView.showEmptyNicknameError();
            isNicknameCorrect = false;
        } else {
            signUpView.hideNicknameError();

            if (nickname.contains(" ")) {
                signUpView.showWrongNicknameError();
                isNicknameCorrect = false;
            } else {
                signUpView.hideNicknameError();
                isNicknameCorrect = true;
            }
        }

        if (isEmailCorrect && isNicknameCorrect && isPasswordCorrect) {
            authenticationInteractor.createNewAccount(email, password, nickname, this);
        }
    }

    @Override
    public void onSignUpStarted() {
        signUpView.showProgressBar(true);
    }

    @Override
    public void onSignUpSuccessful(User user) {
        signUpView.showProgressBar(false);
        databaseInteractor.createNewUser(user);
        signUpView.navigateToHome();
    }

    @Override
    public void onSignUpFailure() {
        signUpView.showProgressBar(false);
        signUpView.showSignUpError();
    }
}
