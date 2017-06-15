package com.example.lfasd.fuli;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by LFasd on 2017/6/15.
 */

public class GirlActivity extends AppCompatActivity {

    private ImageView mImageView = null;
    //加载图片时的的进度条
    private ProgressBar mProgressBar = null;

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, GirlActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girl);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mImageView = (ImageView) findViewById(R.id.girl);

        Intent intent = getIntent();

        Glide.with(this).load(intent.getStringExtra("url"))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageView.setImageBitmap(resource);
                        //图片加载完毕后，把进度条去掉，显示图片
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
