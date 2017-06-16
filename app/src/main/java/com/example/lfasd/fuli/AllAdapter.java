package com.example.lfasd.fuli;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by LFasd on 2017/6/5.
 */

public class AllAdapter extends BaseAdapter<AllAdapter.MyHolder> {

    private List<Result> mResults;
    private Context mContext;


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

    public AllAdapter(List<Result> results) {
        mResults = results;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.base_item, parent, false);

        final MyHolder holder = new MyHolder(view);

        setListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
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
            holder.mImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mImageView.setVisibility(View.GONE);
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

    private void setListener(final MyHolder holder) {

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                WebViewActivity.actionStart(mContext, mResults.get(position).getUrl());
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog dialog = builder.create();

                View view = View.inflate(mContext, R.layout.select_dialog, null);

                TextView like = (TextView) view.findViewById(R.id.text1);
                TextView share = (TextView) view.findViewById(R.id.text2);
                TextView unlike = (TextView) view.findViewById(R.id.text3);

                like.setText("收藏");
                share.setText("分享");
                unlike.setText("不喜欢");

                final int position = holder.getAdapterPosition();

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                        //如果当前的总Item数小于7个，就加载下一页的数据
                        if (getItemCount() < 7) {
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

}
