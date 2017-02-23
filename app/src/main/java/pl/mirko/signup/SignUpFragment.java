package pl.mirko.signup;

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
import pl.mirko.R;

public class SignUpFragment extends Fragment implements SignUpView {

    @BindView(R.id.login_input_layout)
    TextInputLayout loginInputLayout;

    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.retype_password_input_layout)
    TextInputLayout retypePasswordInputLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showSignUpError() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.sign_up_error_text, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoginEmptyError() {
        loginInputLayout.setError(getString(R.string.empty_login_error_text));
    }

    @Override
    public void showPasswordTooShortError() {
        passwordInputLayout.setError(getString(R.string.password_too_short_error_text));
    }

    @Override
    public void showWrongPasswordError() {
        passwordInputLayout.setError(getString(R.string.wrong_password_error));
    }

    @Override
    public void showRetypePasswordNotMatchingError() {
        retypePasswordInputLayout.setError(getString(R.string.retype_password_not_matching_error_text));
    }

    @Override
    public void hideLoginError() {
        loginInputLayout.setError(null);
    }

    @Override
    public void hidePasswordError() {
        passwordInputLayout.setError(null);
    }

    @Override
    public void hideRetypePasswordError() {
        retypePasswordInputLayout.setError(null);
    }
}
