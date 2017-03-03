package pl.mirko.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.home.HomeActivity;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.signup.SignUpFragment;

public class LoginFragment extends Fragment implements LoginView {

    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.email_edit_text)
    TextInputEditText emailEditText;

    @BindView(R.id.password_edit_text)
    TextInputEditText passwordEditText;

    @BindView(R.id.login_content_view)
    ViewGroup loginContentView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter(this, new FirebaseAuthInteractor());
        loginPresenter.initAuthStateListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loginPresenter.addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        loginPresenter.removeAuthStateListener();
    }

    @OnClick(R.id.sign_up_button)
    public void onSignUpButtonClicked() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment_container, new SignUpFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        loginPresenter.login(emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void showLoginError() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.login_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showEmptyEmailError() {
        emailInputLayout.setError(getString(R.string.empty_email_error));
    }

    @Override
    public void showEmptyPasswordError() {
        passwordInputLayout.setError(getString(R.string.empty_password_error));
    }

    @Override
    public void hideEmailError() {
        emailInputLayout.setError(null);
    }

    @Override
    public void hidePasswordError() {
        passwordInputLayout.setError(null);
    }

    @Override
    public void navigateToHome() {
        getActivity().finish();
        startActivity(new Intent(getContext(), HomeActivity.class));
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            loginContentView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            loginContentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
