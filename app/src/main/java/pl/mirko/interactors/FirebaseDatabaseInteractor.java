package pl.mirko.interactors;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.mirko.interactors.interfaces.DatabaseInteractor;
import pl.mirko.models.User;

public class FirebaseDatabaseInteractor implements DatabaseInteractor {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private static final String USERS = "users";

    @Override
    public void createNewUser(User newUser) {
        databaseReference
                .child(USERS)
                .child(newUser.uid)
                .setValue(newUser);
    }
}
