package com.example.lfasd.fuli;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by LFasd on 2017/6/2.
 */

public class FuliAdapter extends RecyclerView.Adapter<FuliAdapter.MyHolder> {

    private List<Result> mResults = null;
    private Context mContext = null;

    //每一个FuliItem都是一个CardView，里面包含了一个ImageView和一个TextView
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

        setListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.mTextView.setText(mResults.get(position).getWho());
        //使用 Glide 直接加载 url 对应的图片到 ImageView
        Glide.with(mContext).load(mResults.get(position).getUrl())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }


    /**
     * 设置并显示对话框
     *
     * @param position 第几个 CardView
     */
    private void showDialog(int position) {

        AlertDialog dialog = new AlertDialog.Builder(mContext).create();

        new DialogSetter(mContext).setDialog(dialog, mResults.get(position));

        dialog.show();
    }

    private void setListener(final MyHolder holder) {

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                showDialog(position);
            }
        });

        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                AlertDialog dialog = builder.create();

                builder.setSingleChoiceItems(new String[]{"保存图片", "分享", "不喜欢"}
                        , -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = holder.getAdapterPosition();

                                switch (which) {
                                    case 0:
                                        saveImage(position);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        mResults.remove(position);
                                        notifyItemRemoved(position);
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }).show();

                return true;
            }
        });

    }

    private void saveImage(final int position) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(mResults.get(position).getUrl());
                        InputStream is = url.openStream();

                        String path = Environment.getExternalStorageDirectory().getPath() + "/福利/";

                        File file = new File(path);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        FileOutputStream fos = new FileOutputStream(path +
                                mResults.get(position).get_id() + ".jpg");

                        int len;
                        byte[] flush = new byte[1024];

                        while ((len = is.read(flush)) != -1) {
                            fos.write(flush, 0, len);
                        }

                        fos.close();
                        is.close();

                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();

            Toast.makeText(mContext, "图片已保存到/福利/" + mResults.get(position).get_id() + ".jpg"
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "无法使用内部储存器", Toast.LENGTH_SHORT).show();
        }
    }
}
