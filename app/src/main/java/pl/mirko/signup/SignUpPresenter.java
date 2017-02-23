package pl.mirko.signup;

import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;
import pl.mirko.models.User;

class SignUpPresenter implements SignUpListener {

    private SignUpView signUpView;
    private FirebaseAuthInteractor firebaseAuthInteractor;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    SignUpPresenter(SignUpView signUpView,
                    FirebaseAuthInteractor firebaseAuthInteractor,
                    FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.signUpView = signUpView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
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
            firebaseAuthInteractor.createNewAccount(email, password, nickname, this);
        }
    }

    @Override
    public void onSignUpStarted() {
        signUpView.showProgressBar();
    }

    @Override
    public void onSignUpSuccessful(User user) {
        signUpView.hideProgressBar();
        firebaseDatabaseInteractor.createNewUser(user);
        signUpView.navigateToHome();
    }

    @Override
    public void onSignUpFailure() {
        signUpView.hideProgressBar();
        signUpView.showSignUpError();
    }
}
