package pl.mirko.interactors.interfaces;

public interface AuthenticationInteractor {

    void createNewAccount(String email, String password);

    void login(String email, String password);

    void logout();
}
