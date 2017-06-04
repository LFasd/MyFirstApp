package com.example.lfasd.fuli;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LFasd on 2017/6/2.
 */

public class FuliAdapter extends RecyclerView.Adapter<FuliAdapter.MyHolder> {

    private List<Result> mResults;
    private Context mContext;

    class MyHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private ImageView mImageView;
        private TextView mTextView;

        public MyHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.girl);
            mTextView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    FuliAdapter(List<Result> results) {
        mResults = results;
    }


    @Override
    public MyHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.fuli_item, parent, false);
        final MyHolder holder = new MyHolder(view);

        //点击 CardView 就打开一个显示原图的对话框
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();

                final View girl = View.inflate(mContext, R.layout.girl, null);
                final ImageView imageView = (ImageView) girl.findViewById(R.id.girl);

                Glide.with(mContext).load(mResults.get(position).getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        showDialog(position, girl, resource);
                    }
                });

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.mTextView.setText(mResults.get(position).getWho());
        //使用 Glide 直接加载 url 对应的图片到 ImageView
        Glide.with(mContext).load(mResults.get(position).getUrl()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }


    /**
     * 保存图片到本地
     *
     * @param position 第几个 CardView
     * @param bitmap   需要保存的位图
     */
    private void savePicture(int position, Bitmap bitmap) {

        String path = Environment.getExternalStorageDirectory().getPath() + "/福利/";
        String target = mResults.get(position).get_id() + ".jpg";

        File file = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(path);

            //检查是否存在文件夹，不存在就创建
            if (!file.exists()) {
                file.mkdir();
            }

            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path + target);
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
                Toast.makeText(mContext, "图片已保存到外部储存器/福利/" + target + "中"
                        , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "无法使用外部储存器", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 设置并显示对话框
     *
     * @param position 第几个 CardView
     * @param view     需要 Dialog 显示出来的View
     * @param bitmap   保存到本地所需的 Bitmap
     */
    private void showDialog(final int position, View view, final Bitmap bitmap) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.setView(view);
        dialog.setTitle(mResults.get(position).getWho());
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "保存图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //在保存图片之前先检查一下有没有权限
                if (MainActivity.checkPermission(mContext)) {
                    savePicture(position, bitmap);
                }
            }
        });
        dialog.show();

        //点击图片后直接关闭 Dialog
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
