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

    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.nickname_password_input_layout)
    TextInputLayout nicknameInputLayout;

    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

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
            Snackbar.make(getView(), R.string.sign_up_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showEmptyEmailError() {
        emailInputLayout.setError(getString(R.string.empty_email_error));
    }

    @Override
    public void showPasswordTooShortError() {
        passwordInputLayout.setError(getString(R.string.password_too_short_error));
    }

    @Override
    public void showWrongPasswordError() {
        passwordInputLayout.setError(getString(R.string.wrong_password_error));
    }

    @Override
    public void showEmptyNicknameError() {
        nicknameInputLayout.setError(getString(R.string.empty_nickname_error));
    }

    @Override
    public void showWrongNicknameError() {
        nicknameInputLayout.setError(getString(R.string.wrong_nickname_error));
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
    public void hideNicknameError() {
        nicknameInputLayout.setError(null);
    }
}
