package com.example.lfasd.fuli;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    /**
     * 申请写入外部存储器权限的标识
     */
    public static final int WRITE_EXTERNAL_STORAGE = 0;

    /**
     * 打开相册的标识
     */
    public static final int CHOOSE_PHONE = 1;

    /**
     * 在这么多时间间隔内连续按两下返回键就能退出应用程序
     */
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
    private CircleImageView mCircleImageView;

    private Toolbar mToolbar;

    /**
     * 控制Fragment切换的FragmentManager
     */
    private FragmentManager fm = getSupportFragmentManager();

    /**
     * 记录当前显示的是哪个Fragment
     */
    private BaseFragment isshow;

    /**
     * 用于回滚到正在显示的Fragment中RecyclerView的顶部的浮动按钮
     */
    private FloatingActionButton backToTop;

    private FuliFragment mFuliFragment;
    private AllFragment mAndroidFragment;
    private AllFragment mIosFragment;
    private AllFragment mExpandFragment;
    private AllFragment mRelaxFragment;
    private AllFragment mAppFragment;
    private AllFragment mForeFragment;
    private AllFragment mOtherFragment;

    /**
     * 记录退出应用程序时间
     */
    private long time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("福利");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_database);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        backToTop = (FloatingActionButton) findViewById(R.id.back_to_top);

        View nav_head = getLayoutInflater().inflate(R.layout.nav_head, null, false);
        mCircleImageView = (CircleImageView) nav_head.findViewById(R.id.user_picture);
        initUser();

        mNavigationView.addHeaderView(nav_head);


        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回滚到当前Fragment中RecyclerView的顶部
                isshow.getRecyclerView().smoothScrollToPosition(0);
            }
        });


        mFuliFragment = new FuliFragment(backToTop);
        init(mFuliFragment);

        mNavigationView.setCheckedItem(R.id.fuli);
        //为NavigationView菜单的点击事件绑定监听器
        mNavigationView.setNavigationItemSelectedListener(mListener);
    }


    private NavigationView.OnNavigationItemSelectedListener mListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.fuli:
                            switchFragment(isshow, mFuliFragment);
                            mToolbar.setTitle("福利");
                            break;
                        case R.id.android:
                            if (mAndroidFragment == null) {
                                mAndroidFragment = new AllFragment(ANDROID_URL, backToTop);
                            }
                            switchFragment(isshow, mAndroidFragment);
                            mToolbar.setTitle("Android");
                            break;
                        case R.id.ios:
                            if (mIosFragment == null) {
                                mIosFragment = new AllFragment(IOS_URL, backToTop);
                            }
                            switchFragment(isshow, mIosFragment);
                            mToolbar.setTitle("ios");
                            break;
                        case R.id.relax:
                            if (mRelaxFragment == null) {
                                mRelaxFragment = new AllFragment(RELAX_URL, backToTop);
                            }
                            switchFragment(isshow, mRelaxFragment);
                            mToolbar.setTitle("休息视频");
                            break;
                        case R.id.app:
                            if (mAppFragment == null) {
                                mAppFragment = new AllFragment(APP_URL, backToTop);
                            }
                            switchFragment(isshow, mAppFragment);
                            mToolbar.setTitle("App");
                            break;
                        case R.id.expand:
                            if (mExpandFragment == null) {
                                mExpandFragment = new AllFragment(EXPAND_URL, backToTop);
                            }
                            switchFragment(isshow, mExpandFragment);
                            mToolbar.setTitle("拓展资源");
                            break;
                        case R.id.other:
                            if (mOtherFragment == null) {
                                mOtherFragment = new AllFragment(OTHER_URL, backToTop);
                            }
                            switchFragment(isshow, mOtherFragment);
                            mToolbar.setTitle("瞎推荐");
                            break;
                        case R.id.fore:
                            if (mForeFragment == null) {
                                mForeFragment = new AllFragment(FORE_URL, backToTop);
                            }
                            switchFragment(isshow, mForeFragment);
                            mToolbar.setTitle("前端");
                            break;
                    }
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            };


    /**
     * 动态申请权限
     *
     * @param context 需要申请权限的上下文
     * @return 如果有权限直接返回true，没有权限，先申请权限并返回false
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

    /**
     * 切换正在显示的Fragment
     *
     * @param from 现在正在显示的Fragment
     * @param to   需要显示的Fragment
     */
    private void switchFragment(BaseFragment from, BaseFragment to) {
        if (isshow != to) {
            isshow = to;
            FragmentTransaction transaction = fm.beginTransaction();

            //如果需要显示的Fragment已经在FragmentManager中了，直接显示出来
            if (to.isAdded()) {
                transaction.hide(from).show(to).commit();
            } else {
                //如果要显示的Fragment不在FragmentManager中，把它添加到FragmentManager中
                transaction.hide(from).add(R.id.fragment, to).commit();
            }

        }
    }

    /**
     * 初始化界面，内容是一个Fragment
     *
     * @param fragment 需要一开始显示的Fragment
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

    private void initUser() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/user/user.jpg");
        if (file.exists()) {
            Glide.with(this).load(file)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter().into(mCircleImageView);
        } else {
            Glide.with(this).load(R.mipmap.ic_action_user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mCircleImageView);
        }

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(MainActivity.this)) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, CHOOSE_PHONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHONE:
                if (resultCode == Activity.RESULT_OK) {
                    Glide.with(this).load(data.getData())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .fitCenter().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mCircleImageView.setImageBitmap(resource);

                            String path = Environment.getExternalStorageDirectory().getPath() + "/user/";

                            File file = new File(path);
                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStream fos = null;
                            try {
                                fos = new FileOutputStream(path + "user.jpg", false);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
        }
    }

}