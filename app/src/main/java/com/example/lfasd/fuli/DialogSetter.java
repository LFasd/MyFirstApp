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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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

    //保存图片所需的位图
    private Bitmap mBitmap = null;

    //图片是否已经加载完
    private boolean allReady = false;

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
                        //图片加载完毕后，把进度条去掉，显示图片，
                        mBitmap = resource;
                        mImageView.setImageBitmap(mBitmap);
                        allReady = true;
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        dialog.setView(mView);
        dialog.setTitle(result.getWho());
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "保存图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //在保存图片之前先检查一下有没有权限
                if (MainActivity.checkPermission(mContext)) {
                    //如果图片加载完毕，才保存图片
                    if (allReady) {
                        savePicture(mBitmap, result.get_id() + ".jpg");
                    } else {
                        Toast.makeText(mContext, "图片正在加载中", Toast.LENGTH_SHORT).show();
                    }
                }
                mBitmap = null;
            }
        });


        //点击AlertDialog中的View就关闭AlertDialog
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出前先把Bitmap设为null，方便垃圾回收器回收内存
                mImageView.setImageBitmap(null);
                mBitmap = null;
                dialog.dismiss();
            }
        });
    }

    /**
     * 把传进来的Bitmap表示的图片保存在SD卡福利文件夹中
     *
     * @param bitmap   需要保存的位图
     * @param fileName 图片名字
     */
    private void savePicture(Bitmap bitmap, String fileName) {

        //外部储存器的福利文件夹路径
        String path = Environment.getExternalStorageDirectory().getPath() + "/福利/";
        File file = null;

        //如果外部存储器可用，才保存图片
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
