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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static final int WRITE_EXTERNAL_STORAGE = 0;

    public static final long EXITING_TIME = 2500;

    public static final String ANDROID_URL = "http://gank.io/api/data/Android/10/";

    public static final String EXPAND_URL = "http://gank.io/api/data/拓展资源/10/";

    public static final String FORE_URL = "http://gank.io/api/data/前端/10/";

    public static final String IOS_URL = "http://gank.io/api/data/iOS/20/";

    public static final String OTHER_URL = "http://gank.io/api/data/瞎推荐/10/";

    public static final String RELAX_URL = "http://gank.io/api/data/休息视频/10/";

    public static final String APP_URL = "http://gank.io/api/data/App/10/";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private FragmentManager fm = getSupportFragmentManager();

    private BaseFragment isshow;
    private FloatingActionButton backToTop;

    private FuliFragment mFuliFragment;
    private AllFragment mAndroidFragment;
    private AllFragment mIosFragment;
    private AllFragment mExpandFragment;
    private AllFragment mRelaxFragment;
    private AllFragment mAppFragment;
    private AllFragment mForeFragment;
    private AllFragment mOtherFragment;

    private long time;

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
        bar.setTitle("福利");
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
                            mAndroidFragment = new AllFragment(ANDROID_URL, backToTop);
                        }
                        switchFragment(isshow, mAndroidFragment);
                        bar.setTitle("Android");
                        break;
                    case R.id.ios:
                        if (mIosFragment == null) {
                            mIosFragment = new AllFragment(IOS_URL, backToTop);
                        }
                        switchFragment(isshow, mIosFragment);
                        bar.setTitle("ios");
                        break;
                    case R.id.relax:
                        if (mRelaxFragment == null) {
                            mRelaxFragment = new AllFragment(RELAX_URL, backToTop);
                        }
                        switchFragment(isshow, mRelaxFragment);
                        bar.setTitle("休息视频");
                        break;
                    case R.id.app:
                        if (mAppFragment == null) {
                            mAppFragment = new AllFragment(APP_URL, backToTop);
                        }
                        switchFragment(isshow, mAppFragment);
                        bar.setTitle("App");
                        break;
                    case R.id.expand:
                        if (mExpandFragment == null) {
                            mExpandFragment = new AllFragment(EXPAND_URL, backToTop);
                        }
                        switchFragment(isshow, mExpandFragment);
                        bar.setTitle("拓展资源");
                        break;
                    case R.id.other:
                        if (mOtherFragment == null) {
                            mOtherFragment = new AllFragment(OTHER_URL, backToTop);
                        }
                        switchFragment(isshow, mOtherFragment);
                        bar.setTitle("瞎推荐");
                        break;
                    case R.id.fore:
                        if (mForeFragment == null) {
                            mForeFragment = new AllFragment(FORE_URL, backToTop);
                        }
                        switchFragment(isshow, mForeFragment);
                        bar.setTitle("前端");
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
                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE:
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

    private void switchFragment(BaseFragment from, BaseFragment to) {
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

    /**
     * 初始化界面，内容是一个Fragment
     *
     * @param fragment 需要一开始显示的页面
     */
    private void init(BaseFragment fragment) {
        backToTop.hide();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment, fragment).show(fragment).commit();
        isshow = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //如果DrawerLayout已经打开，就把DrawerLayout关闭
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {//否则判断是否要关闭程序
                    if (System.currentTimeMillis() < time) {
                        finish();

                        //杀死进程
                        android.os.Process.killProcess(android.os.Process.myPid());
                    } else {
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        time = System.currentTimeMillis() + EXITING_TIME;
                    }
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                //如果DrawerLayout已经打开，关闭DrawerLayout
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {//否则打开DrawerLayout
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
        }

        return true;
    }
}