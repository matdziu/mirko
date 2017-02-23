package pl.mirko.interactors;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.mirko.interactors.interfaces.AuthenticationInteractor;
import pl.mirko.login.LoginListener;
import pl.mirko.signup.SignUpListener;

public class FirebaseAuthInteractor implements AuthenticationInteractor {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public void createNewAccount(String email, String password, final SignUpListener signUpListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signUpListener.onSignUpSuccessful();
                        } else {
                            signUpListener.onSignUpFailure();
                        }
                    }
                });
    }

    @Override
    public void login(String email, String password, final LoginListener loginListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginListener.onLoginSuccessful();
                        } else {
                            loginListener.onLoginFailure();
                        }
                    }
                });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public void initAuthStateListener(final LoginListener loginListener) {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    loginListener.onLoginSuccessful();
                } else {
                    loginListener.onLoginFailure();
                }
            }
        };
    }

    @Override
    public void addAuthStateListener() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void removeAuthStateListener() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
