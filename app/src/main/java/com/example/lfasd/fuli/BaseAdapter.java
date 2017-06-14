package com.example.lfasd.fuli;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by LFasd on 2017/6/14.
 */

public class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private LoadMore mLoadMore;

    interface LoadMore {
        void loadMore();
    }

    protected void loadMore(){
        mLoadMore.loadMore();
    }

    public void setLoadMore(LoadMore loadMore){
        mLoadMore = loadMore;
    }


    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
