package pl.mirko.base;

import pl.mirko.interactors.FirebaseAuthInteractor;

class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;

    BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void logout() {
        firebaseAuthInteractor.logout();
    }
}
