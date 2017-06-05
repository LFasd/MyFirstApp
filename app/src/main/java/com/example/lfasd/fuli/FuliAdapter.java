package com.example.lfasd.fuli;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

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

        //点击 CardView 就打开一个显示原图的对话框
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                Log.d("test", position + "");
                showDialog(position);
            }
        });

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

}
