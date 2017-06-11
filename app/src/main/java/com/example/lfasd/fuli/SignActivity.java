package com.example.lfasd.fuli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by LFasd on 2017/6/11.
 */

public class SignActivity extends AppCompatActivity {

    public static void actionStart(Context context, String data1) {
        Intent intent = new Intent(context, SignActivity.class);
        intent.putExtra("sign", data1);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        EditText editText = (EditText) findViewById(R.id.sign);

        Intent intent = getIntent();

        editText.setText(intent.getStringExtra("sign"));
    }
}
