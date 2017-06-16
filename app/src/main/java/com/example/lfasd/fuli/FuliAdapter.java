package com.example.lfasd.fuli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by LFasd on 2017/6/2.
 */

public class FuliAdapter extends BaseAdapter<FuliAdapter.MyHolder> {

    private List<Result> mResults = null;
    private Context mContext = null;

    //每一个FuliItem都是一个CardView，里面包含了一个ImageView和一个TextView
    class MyHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private ImageView mImageView;

        public MyHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.girl);
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
        //使用 Glide 直接加载 url 对应的图片到 ImageView
        Glide.with(mContext).load(mResults.get(position).getUrl())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    /**
     * 为每个Item的点击事件和长按事件绑定监听器
     *
     * @param holder
     */
    private void setListener(final MyHolder holder) {

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                GirlActivity.actionStart(mContext, mResults.get(position).getUrl());
            }
        });

        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog dialog = builder.create();

                View view = View.inflate(mContext, R.layout.fuli_select, null);

                TextView save = (TextView) view.findViewById(R.id.save);
                TextView share = (TextView) view.findViewById(R.id.share);
                TextView unlike = (TextView) view.findViewById(R.id.unlike);

                final int position = holder.getAdapterPosition();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainActivity.checkPermission(mContext)) {
                            saveImage(position);
                        }
                        dialog.dismiss();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                unlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果当前的总图片数小于7个，就加载下一页的数据
                        if (getItemCount() < 9) {
                            loadMore();
                        }

                        //先获取需要删除的Item的id
                        deleteItem(mResults.get(position).get_id());
                        //再将需要删除的Item所对应的对象移除
                        mResults.remove(position);
                        //显示删除动画效果
                        notifyItemRemoved(position);
                        //通知RecyclerView数据发生了修改
                        notifyItemRangeChanged(position, 1);

                        dialog.dismiss();
                    }
                });

                dialog.setView(view);
                dialog.show();

//                builder.setSingleChoiceItems(new String[]{"保存图片", "分享（未实现）", "不喜欢"}
//                        , -1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                int position = holder.getAdapterPosition();
//
//                                switch (which) {
//                                    case 0:
//                                        saveImage(position);
//                                        dialog.dismiss();
//                                        break;
//                                    case 1:
//                                        dialog.dismiss();
//                                        break;
//                                    case 2:
//                                        //如果当前的总图片数小于7个，就加载下一页的数据
//                                        if (getItemCount() < 9) {
//                                            loadMore();
//                                        }
//
//                                        //先获取需要删除的Item的id
//                                        deleteItem(mResults.get(position).get_id());
//                                        //再将需要删除的Item所对应的对象移除
//                                        mResults.remove(position);
//                                        //显示删除动画效果
//                                        notifyItemRemoved(position);
//                                        //通知RecyclerView数据发生了修改
//                                        notifyItemRangeChanged(position, 1);
//
//                                        dialog.dismiss();
//                                        break;
//                                }
//                            }
//                        }).show();

                return true;
            }
        });
    }

    /**
     * 使用输入输出流将图片储存到文件中
     *
     * @param position 图片对应的集合下标
     */
    private void saveImage(final int position) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //使用url直接获取InputStream
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
