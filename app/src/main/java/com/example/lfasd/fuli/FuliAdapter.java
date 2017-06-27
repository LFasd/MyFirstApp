package com.example.lfasd.fuli;

import android.Manifest;
import android.content.Context;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LFasd on 2017/6/2.
 */

public class FuliAdapter extends BaseAdapter<FuliAdapter.MyHolder> {

    private List<Result> mResults = null;
    private Context mContext = null;
    private Map<Integer, Integer> mHeights = new HashMap<>();

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
    public void onBindViewHolder(final MyHolder holder, final int position) {
//        final ImageView imageView = holder.mImageView;
        final ViewGroup.LayoutParams params = holder.mImageView.getLayoutParams();

        String url = mResults.get(position).getUrl();

        if (getItemCount() - position == 2) {
            loadMore();
        }


        Integer height = mHeights.get(position);

        if (height != null) {
            params.height = height;
            holder.mImageView.setLayoutParams(params);
        } else {
            //使用Glide加载出Bitmap，并根据Bitmap的宽高，设置ImageView的宽高
            // 并将高度存进Map中，下次加载图片的时候能够直接根据position获取高度
            Glide.with(mContext).load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            int width = holder.mImageView.getWidth();
                            if (width != 0) {
                                params.height = (int) (1.0 * resource.getHeight() / resource.getWidth() * width);
                            }else{
                                //这个498是在我手机上的ImageView的实际宽度
                                params.height = (int) (1.0 * resource.getHeight() / resource.getWidth() * 498);
                            }

                            mHeights.put(position, params.height);
                            holder.mImageView.setLayoutParams(params);
                        }
                    });
        }


        //使用 Glide 直接加载 url 对应的图片到 ImageView
        Glide.with(mContext).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
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

                View view = View.inflate(mContext, R.layout.select_dialog, null);

                TextView save = (TextView) view.findViewById(R.id.text1);
                TextView share = (TextView) view.findViewById(R.id.text2);
                TextView unlike = (TextView) view.findViewById(R.id.text3);

                save.setText("保存图片");
                share.setText("分享图片");
                unlike.setText("不喜欢");

                final int position = holder.getAdapterPosition();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainActivity.checkPermission(mContext
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
