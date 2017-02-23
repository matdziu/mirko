package pl.mirko.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mirko.R;
import pl.mirko.home.HomeActivity;
import pl.mirko.interactors.FirebaseAuthInteractor;
import pl.mirko.interactors.FirebaseDatabaseInteractor;

public class SignUpFragment extends Fragment implements SignUpView {

    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.nickname_password_input_layout)
    TextInputLayout nicknameInputLayout;

    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.email_edit_text)
    TextInputEditText emailEditText;

    @BindView(R.id.nickname_edit_text)
    TextInputEditText nicknameEditText;

    @BindView(R.id.password_edit_text)
    TextInputEditText passwordEditText;

    private SignUpPresenter signUpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpPresenter = new SignUpPresenter(this, new FirebaseAuthInteractor(), new FirebaseDatabaseInteractor());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.create_account_button)
    public void onCreateAccountButtonClicked() {
        signUpPresenter.createAccount(emailEditText.getText().toString(),
                nicknameEditText.getText().toString(), passwordEditText.getText().toString());
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

    @Override
    public void navigateToHome() {
        getActivity().finish();
        startActivity(new Intent(getContext(), HomeActivity.class));
    }
}
