package pl.mirko.models;

public class User {

    public String uid;
    public String nickname;

    public User() {
        // default constructor for Firebase
    }

    public User(String uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }
}
