package pl.mirko.signup;

import pl.mirko.models.User;

public interface SignUpListener {

    void onSignUpSuccessful(User user);

    void onSignUpFailure();
}
