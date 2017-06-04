package com.example.lfasd.fuli;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private FragmentManager fm = getSupportFragmentManager();

    private MyFragment isshow;
    private FloatingActionButton backToTop;

    private MyFragment mFuliFragment;
    private MyFragment mAndroidFragment;
    private MyFragment mIosFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_database);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        backToTop = (FloatingActionButton) findViewById(R.id.back_to_top);

        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isshow.getRecyclerView().smoothScrollToPosition(0);
            }
        });


        mFuliFragment = new FuliFragment(backToTop);
        init(mFuliFragment);

        mNavigationView.setCheckedItem(R.id.fuli);
        //为NavigationView菜单的点击事件绑定监听器
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.fuli:
                        switchFragment(isshow, mFuliFragment);
                        bar.setTitle("福利");
                        break;
                    case R.id.android:
                        if (mAndroidFragment == null) {
                            mAndroidFragment = new AndroidFragment(backToTop);
                        }
                        switchFragment(isshow, mAndroidFragment);
                        bar.setTitle("Android");
                        break;
                    case R.id.ios:
                        if (mIosFragment == null) {
                            mIosFragment = new IosFragment(backToTop);
                        }
                        switchFragment(isshow, mIosFragment);
                        bar.setTitle("ios");
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


    /**
     * 检查并动态申请内存
     */
    public static boolean checkPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context
                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context
                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                //如果用户没有给予权限，就重复申请
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请给予必要权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private void switchFragment(MyFragment from, MyFragment to) {
        if (isshow != to) {
            isshow = to;
            FragmentTransaction transaction = fm.beginTransaction();

            if (to.isAdded()) {
                transaction.hide(from).show(to).commit();
            } else {
                transaction.hide(from).add(R.id.fragment, to).commit();
            }

        }
    }

    private void init(MyFragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment, fragment).show(fragment).commit();
        isshow = fragment;
    }
}