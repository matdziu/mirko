package pl.mirko.signup;

interface SignUpView {

    void showSignUpError();

    void showLoginEmptyError();

    void showPasswordTooShortError();

    void showWrongPasswordError();

    void showRetypePasswordNotMatchingError();

    void hideLoginError();

    void hidePasswordError();

    void hideRetypePasswordError();
}
