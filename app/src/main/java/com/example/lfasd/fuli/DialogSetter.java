package com.example.lfasd.fuli;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.List;

/**
 * Created by LFasd on 2017/6/5.
 */

public class DialogSetter {

    private View mView;
    private Bitmap mBitmap = null;
    private Context mContext = null;
    private boolean allReady = false;
    private ProgressBar mProgressBar = null;
    private ImageView mImageView = null;

    public DialogSetter(Context context) {
        mContext = context;
    }

    public void setDialog(final AlertDialog dialog, final List<Result> mResults, final int position) {
        mView = View.inflate(mContext, R.layout.girl, null);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progressbar);
        mImageView = (ImageView) mView.findViewById(R.id.girl);

        Glide.with(mContext).load(mResults.get(position).getUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBitmap = resource;
                        mImageView.setImageBitmap(mBitmap);
                        allReady = true;
                        mProgressBar.setVisibility(View.GONE);
                        Glide.clear(mImageView);
                    }
                });

        dialog.setView(mView);
        dialog.setTitle(mResults.get(position).getWho());
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "保存图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //在保存图片之前先检查一下有没有权限
                if (MainActivity.checkPermission(mContext)) {
                    if (allReady) {
                        savePicture(mBitmap, mResults.get(position).get_id() + ".jpg");
                    } else {
                        Toast.makeText(mContext, "图片正在加载中", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageBitmap(null);
                mBitmap = null;
                dialog.dismiss();
            }
        });
    }

    private void savePicture(Bitmap bitmap, String fileName) {

        String path = Environment.getExternalStorageDirectory().getPath() + "/福利/";

        File file = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(path);

            //检查是否存在文件夹，不存在就创建
            if (!file.exists()) {
                file.mkdir();
            }

            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path + fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //把图片完整地保存到指定文件
            boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result) {
                Toast.makeText(mContext, "图片已保存到外部储存器/福利/" + fileName + "中"
                        , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "无法使用外部储存器", Toast.LENGTH_SHORT).show();
        }
    }
}
