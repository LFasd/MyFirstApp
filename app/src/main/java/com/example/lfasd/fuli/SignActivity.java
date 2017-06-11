package com.example.lfasd.fuli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by LFasd on 2017/6/11.
 */
public class SignActivity extends AppCompatActivity {

    public static void actionStart(Activity activity, String data1) {
        Intent intent = new Intent(activity, SignActivity.class);
        intent.putExtra("sign", data1);
        activity.startActivityForResult(intent, MainActivity.CHANGE_USER_SIGN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        final EditText editText = (EditText) findViewById(R.id.sign);

        final Intent intent = getIntent();

        editText.setText(intent.getStringExtra("sign"));

        ImageView save = (ImageView) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.putExtra("sign", editText.getText().toString());
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }
}
