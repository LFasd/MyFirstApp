package com.example.lfasd.fuli;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
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
     * 修改用户头像标识
     */
    public static final int CHANGE_USER_ICON = 1;

    /**
     * 修改背景图标标识
     */
    public static final int CHANGE_USER_BACKGROUND = 2;

    /**
     * 修改个性签名标识
     */
    public static final int CHANGE_USER_SIGN = 3;

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
    private CircleImageView user_icon;
    private ImageView background;
    private TextView user_sign;

    private SharedPreferences sign_SharedPreferences;
    private SharedPreferences unlike_SharedPreferences;

    private Toolbar mToolbar;

    /**
     * 控制Fragment切换的FragmentManager
     */
    private FragmentManager fm;

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
            //在ActionBar上显示菜单按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_database);
        }

        fm = getSupportFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        backToTop = (FloatingActionButton) findViewById(R.id.back_to_top);

        //这里有个坑
        View nav_head = getLayoutInflater().inflate(R.layout.nav_head, null, false);

        user_icon = (CircleImageView) nav_head.findViewById(R.id.user_picture);
        initUser();

        user_sign = (TextView) nav_head.findViewById(R.id.user_sign);
        initUserSign();

        background = (ImageView) nav_head.findViewById(R.id.background);
        initBackground();

        //为
        mNavigationView.addHeaderView(nav_head);


        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回滚到当前Fragment中RecyclerView的顶部
                isshow.getRecyclerView().smoothScrollToPosition(0);
            }
        });

        unlike_SharedPreferences = getSharedPreferences("unlike", Context.MODE_PRIVATE);

        mFuliFragment = FuliFragment.newInstance(backToTop, unlike_SharedPreferences);
        init(mFuliFragment);

        mNavigationView.setCheckedItem(R.id.fuli);
        //为NavigationView菜单的点击事件绑定监听器
        mNavigationView.setNavigationItemSelectedListener(mListener);
    }

    /**
     *
     */
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
                                mAndroidFragment = AllFragment.newInstance(ANDROID_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mAndroidFragment);
                            mToolbar.setTitle("Android");
                            break;
                        case R.id.ios:
                            if (mIosFragment == null) {
                                mIosFragment = AllFragment.newInstance(IOS_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mIosFragment);
                            mToolbar.setTitle("ios");
                            break;
                        case R.id.relax:
                            if (mRelaxFragment == null) {
                                mRelaxFragment = AllFragment.newInstance(RELAX_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mRelaxFragment);
                            mToolbar.setTitle("休息视频");
                            break;
                        case R.id.app:
                            if (mAppFragment == null) {
                                mAppFragment = AllFragment.newInstance(APP_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mAppFragment);
                            mToolbar.setTitle("App");
                            break;
                        case R.id.expand:
                            if (mExpandFragment == null) {
                                mExpandFragment = AllFragment.newInstance(EXPAND_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mExpandFragment);
                            mToolbar.setTitle("拓展资源");
                            break;
                        case R.id.other:
                            if (mOtherFragment == null) {
                                mOtherFragment = AllFragment.newInstance(OTHER_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mOtherFragment);
                            mToolbar.setTitle("瞎推荐");
                            break;
                        case R.id.fore:
                            if (mForeFragment == null) {
                                mForeFragment = AllFragment.newInstance(FORE_URL
                                        , backToTop, unlike_SharedPreferences);
                            }
                            switchFragment(isshow, mForeFragment);
                            mToolbar.setTitle("前端");
                            break;
                    }
                    //选择完成后把滑动菜单关闭
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

                to.getAdapter().setOnDeleteListener(new BaseAdapter.OnDeleteListener() {
                    @Override
                    public void OnDelete(String id) {
                        unlike_SharedPreferences.edit().putString(id, id).commit();
                    }
                });
            }

        }
    }

    /**
     * 初始化界面，内容是一个Fragment
     *
     * @param fragment 需要一开始显示的Fragment
     */
    private void init(BaseFragment fragment) {
//        backToTop.hide();

        fragment.getAdapter().setOnDeleteListener(new BaseAdapter.OnDeleteListener() {
            @Override
            public void OnDelete(String id) {
                unlike_SharedPreferences.edit().putString(id, id).commit();
            }
        });

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment, fragment).show(fragment).commit();

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

    /**
     * 打开应用后初始化滑动菜单的用户头像
     */
    private void initUser() {

        File file = new File(getCacheDir() + "/user/user.jpg");

        //如果图片缓存存在，直接使用Glide加载图片
        if (file.exists()) {
            Glide.with(this).load(file)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop().into(user_icon);
        } else {
            //否则使用默认头像
            Glide.with(this).load(R.mipmap.ic_action_user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(user_icon);
        }

        //为用户头像设置监听器，点击头像打开相册选择新头像
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(MainActivity.this)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, CHANGE_USER_ICON);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHANGE_USER_ICON:
                if (resultCode == Activity.RESULT_OK) {
                    saveImage(data.getData(), "user.jpg");
                    showImage(data.getData(), user_icon);
                }
                break;
            case CHANGE_USER_BACKGROUND:
                if (resultCode == Activity.RESULT_OK) {
                    saveImage(data.getData(), "background.jpg");
                    showImage(data.getData(), background);
                }
                break;
            case CHANGE_USER_SIGN:
                if (resultCode == Activity.RESULT_OK) {
                    String sign = data.getStringExtra("user_sign");
                    user_sign.setText(sign);
                    SharedPreferences.Editor editor = sign_SharedPreferences.edit();
                    editor.putString("user_sign", sign);
                    editor.commit();
                }
                break;
        }
    }

    /**
     * 把用户选择的图片保存在缓存中，下次打开应用使用缓存中的图片
     *
     * @param uri    图片的Uri信息
     * @param target 图片的名字
     */
    private void saveImage(Uri uri, final String target) {
        Glide.with(this).load(uri)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                String path = getCacheDir() + "/user/";

                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }

                OutputStream fos = null;
                try {
                    fos = new FileOutputStream(path + target, false);
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

    /**
     * 将图片加载到指定的ImageView中
     *
     * @param uri  图片的Uri信息
     * @param view 需要加载图片的ImageView
     */
    private void showImage(Uri uri, ImageView view) {
        Glide.with(this).load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(view);
    }

    /**
     * 打开应用后初始化用户的个性签名
     */
    private void initUserSign() {
        if (sign_SharedPreferences == null) {
            sign_SharedPreferences = getPreferences(Context.MODE_PRIVATE);
        }
        String sign = sign_SharedPreferences.getString("user_sign", "点击修改个性签名");
        user_sign.setText(sign);

        //点击个性签名修改个性签名
        user_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignActivity.actionStart(MainActivity.this, user_sign.getText().toString());
            }
        });
    }

    /**
     * 打开应用后初始化滑动菜单中的用户背景墙
     */
    private void initBackground() {
        File file = new File(getCacheDir() + "/user/background.jpg");

        //如果图片缓存存在，就加载图片
        if (file.exists()) {
            Glide.with(this).load(file)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(background);
        }

        //为背景墙绑定事件监听器，用户点击后打开相册选择图片
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(MainActivity.this)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, CHANGE_USER_BACKGROUND);
                }
            }
        });
    }
}