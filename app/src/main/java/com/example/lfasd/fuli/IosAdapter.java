package com.example.lfasd.fuli;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by LFasd on 2017/6/3.
 */

public class IosAdapter extends RecyclerView.Adapter<IosAdapter.MyHolder> {

    private Context mContext;
    private List<Result> mResults;

    class MyHolder extends RecyclerView.ViewHolder {

        RelativeLayout view;
        TextView mTitle;
        TextView mAuthor;
        ImageView mImageView;

        public MyHolder(View itemView) {
            super(itemView);
            view = (RelativeLayout) itemView;
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAuthor = (TextView) itemView.findViewById(R.id.author);
            mImageView = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public IosAdapter(List<Result> results) {
        mResults = results;
    }

    @Override
    public IosAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.base_item, parent, false);
        final MyHolder holder = new MyHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                //使用浏览器打开url
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(mResults.get(position).getUrl()));
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(IosAdapter.MyHolder holder, int position) {

        Result result = mResults.get(position);

        //设置标题
        holder.mTitle.setText(result.getDesc());

        //设置图标
        String[] image = result.getImages();
        if (image != null && image.length > 0) {
            Glide.with(mContext).load(image[0]).into(holder.mImageView);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources()
                    , R.mipmap.ic_action_android);
            holder.mImageView.setImageBitmap(bitmap);
        }

        //设置作者名
        if (result.getWho() != null) {
            holder.mAuthor.setText(result.getWho());
        } else {
            holder.mAuthor.setText("匿名");
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
}
