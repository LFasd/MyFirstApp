package com.example.lfasd.fuli;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LFasd on 2017/6/3.
 */

public class AndroidAdapter extends RecyclerView.Adapter<AndroidAdapter.MyHolder> {

    List<Result> mResults;
    Context mContext;

    private MyHolder holder;

    //用于保存在切换fragment后需要取消Glide网络加载的holder
    private HashMap<Integer, MyHolder> holders = new HashMap<>();

    //一个页面中最多同时拥有7个holder
    public static final int MAX_HOLDERS = 7;


    /**
     * 取消正在加载的所有图片
     */
    public void clean() {
        for (int i = 0; i < holders.size(); i++) {
            Glide.clear(holders.get(i).mImageView);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        RelativeLayout view;
        TextView mTitle;
        TextView mAuthor;
        ImageView mImageView;
        TextView mTime;

        public MyHolder(View itemView) {
            super(itemView);
            view = (RelativeLayout) itemView;
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAuthor = (TextView) itemView.findViewById(R.id.author);
            mImageView = (ImageView) itemView.findViewById(R.id.icon);
            mTime = (TextView) itemView.findViewById(R.id.time);
        }
    }

    public AndroidAdapter(List<Result> results) {
        mResults = results;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.base_item, parent, false);

        holder = new MyHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(mResults.get(position).getUrl()));
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holders.put(position % MAX_HOLDERS, holder);

        Result result = mResults.get(position);

        //设置标题
        holder.mTitle.setText(result.getDesc());

        //当有一个item从RecyclerView的上面划出后，就停止加载那个item的图片
        Glide.clear(holder.mImageView);

        String[] image = result.getImages();
        //如果有图标，就使用Glide加载图片
        if (image != null && image.length > 0) {
            Glide.with(mContext)
                    .load(image[0])
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.ic_action_warning)
                    .thumbnail(0.2f)
                    .into(holder.mImageView);
        } else {
            //如果没有图片，就是用默认的安卓机器人作为图标
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources()
                    , R.mipmap.ic_action_android);
            holder.mImageView.setImageBitmap(bitmap);
        }

        //设置作者
        if (result.getWho() != null) {
            holder.mAuthor.setText(result.getWho());
        } else {
            holder.mAuthor.setText("匿名");
        }

        //设置日期
        holder.mTime.setText(result.getPublishedAt().split("T")[0]);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
}
