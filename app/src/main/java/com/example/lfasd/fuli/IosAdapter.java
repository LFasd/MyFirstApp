package com.example.lfasd.fuli;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LFasd on 2017/6/3.
 */

public class IosAdapter extends RecyclerView.Adapter<IosAdapter.MyHolder> {

    private Context mContext;
    private List<Result> mResults;

    class MyHolder extends RecyclerView.ViewHolder {

        CardView view;
        TextView mTitle;
        TextView mAuthor;

        public MyHolder(View itemView) {
            super(itemView);
            view = (CardView) itemView;
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAuthor = (TextView) itemView.findViewById(R.id.author);
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.ios_item, parent, false);
        final MyHolder holder = new MyHolder(view);

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
    public void onBindViewHolder(IosAdapter.MyHolder holder, int position) {
        holder.mTitle.setText(mResults.get(position).getDesc());
        if (mResults.get(position).getWho() != null) {
            holder.mAuthor.setText(mResults.get(position).getWho());
        } else {
            holder.mAuthor.setText("匿名");
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
}
