package pl.mirko.interactors.interfaces;

import pl.mirko.login.LoginListener;
import pl.mirko.signup.SignUpListener;

public interface AuthenticationInteractor {

    void createNewAccount(String email, String password, String nickname, SignUpListener signUpListener);

    void login(String email, String password, LoginListener loginListener);

    void logout();

    void initAuthStateListener(LoginListener loginListener);

    void addAuthStateListener();

    void removeAuthStateListener();
}
