package pl.mirko.login;

public interface LoginListener {

    void onLoginStarted();

    void onLoginSuccessful();

    void onLoginFailure();
}
