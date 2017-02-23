package pl.mirko.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.signup.SignUpActivity;

public class LoginFragment extends Fragment implements LoginView {

    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.sign_up_button)
    public void onSignUpButtonClicked() {
        startActivity(new Intent(getContext(), SignUpActivity.class));
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
}
