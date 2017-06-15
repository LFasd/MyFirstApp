package com.example.lfasd.fuli;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by LFasd on 2017/6/11.
 */
public class SignActivity extends AppCompatActivity {

    private MaterialEditText mEditText;

    /**
     * 启动SignActivity并把数据传给SignActivity
     *
     * @param activity 启动SignActivity的Activity
     * @param old_sign 用户当前的个性签名
     */
    public static void actionStart(Activity activity, String old_sign) {
        Intent intent = new Intent(activity, SignActivity.class);
        intent.putExtra("user_sign", old_sign);
        activity.startActivityForResult(intent, MainActivity.CHANGE_USER_SIGN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("修改签名");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_arrow_left);
        }

        mEditText = (MaterialEditText) findViewById(R.id.sign);

        Intent intent = getIntent();

        mEditText.setText(intent.getStringExtra("user_sign"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save:
                String sign = mEditText.getText().toString();

                //如果字数大于指定的字数限制
                if (sign.length() > mEditText.getMaxCharacters()) {
                    mEditText.setError("字数过多");
                } else if (sign.length() == 0) {
                    //如果字数为0
                    mEditText.setError("不能为空");
                } else {
                    //字数没有错误，才返回用户的修改结果
                    Intent intent = new Intent();
                    intent.putExtra("user_sign", mEditText.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }
}
