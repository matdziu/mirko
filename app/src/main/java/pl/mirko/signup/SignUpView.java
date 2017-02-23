package pl.mirko.signup;

interface SignUpView {

    void showSignUpError();

    void showEmptyEmailError();

    void showPasswordTooShortError();

    void showWrongPasswordError();

    void showEmptyNicknameError();

    void showWrongNicknameError();

    void hideEmailError();

    void hidePasswordError();

    void hideNicknameError();
}
