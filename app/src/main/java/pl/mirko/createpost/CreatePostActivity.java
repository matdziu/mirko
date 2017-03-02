package pl.mirko.createpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import pl.mirko.R;
import pl.mirko.base.BaseActivity;

public class CreatePostActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forceSoftKeyboard();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.create_post_fragment_container, new CreatePostFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_create_post;
    }
}
