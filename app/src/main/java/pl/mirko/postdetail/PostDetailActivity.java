package pl.mirko.postdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import pl.mirko.R;
import pl.mirko.base.BaseActivity;

public class PostDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.post_detail_fragment_container, new PostDetailFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_detail;
    }
}
