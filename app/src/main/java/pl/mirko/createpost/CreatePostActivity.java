package pl.mirko.createpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import pl.mirko.R;
import pl.mirko.base.BaseActivity;

public class CreatePostActivity extends BaseActivity {

    private boolean showSendMenuItem = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSoftKeyboard(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.create_post_fragment_container, new CreatePostFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_create_post;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showSendMenuItem) {
            getMenuInflater().inflate(R.menu.create_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void hideSendMenuItem() {
        showSendMenuItem = false;
        ActivityCompat.invalidateOptionsMenu(this);
    }
}
