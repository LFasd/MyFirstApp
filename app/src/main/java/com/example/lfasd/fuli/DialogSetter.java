package com.example.lfasd.fuli;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by LFasd on 2017/6/5.
 */

public class DialogSetter {

    //AlertDialog要显示的View
    private View mView;

    //加载图片时的的进度条
    private ProgressBar mProgressBar = null;

    private Context mContext = null;
    private ImageView mImageView = null;

    public DialogSetter(Context context) {
        mContext = context;
    }

    /**
     * 设置AlertDialog的一些功能，绑定事件监听器
     *
     * @param dialog 要进行设置的AlertDialog
     * @param result 数据源
     */
    public void setDialog(final AlertDialog dialog, final Result result) {
        mView = View.inflate(mContext, R.layout.girl, null);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progressbar);
        mImageView = (ImageView) mView.findViewById(R.id.girl);

        Glide.with(mContext).load(result.getUrl())
                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageView.setImageBitmap(resource);
                        //图片加载完毕后，把进度条去掉，显示图片
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        dialog.setView(mView);

        //点击AlertDialog中的View就关闭AlertDialog
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageBitmap(null);
                dialog.dismiss();
            }
        });
    }
}