package com.example.lfasd.fuli;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by LFasd on 2017/6/14.
 */

/**
 * 里面提供了两个回调接口，用于处理用户的删除Item事件
 * @param <T>
 */
public class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private OnLoadMoreListener mLoadMoreListener;

    private OnDeleteListener mOnDeleteListener;

    /**
     * 用户删除Item后Item数量不够监听器
     */
    interface OnLoadMoreListener {
        /**
         * 需要加载更多Item时回调该方法
         */
        void OnLoadMore();
    }

    /**
     * 用户删除Item监听器
     */
    interface OnDeleteListener {
        /**
         * 用户删除Item后回调该方法
         *
         * @param id 用户删除Item的id
         */
        void OnDelete(String id);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    /**
     * 触发删除事件，回调方法
     * @param id
     */
    protected void deleteItem(String id) {
        if(mOnDeleteListener != null) {
            mOnDeleteListener.OnDelete(id);
        }
    }

    /**
     * 触发剩余Item数量不够事件，回调方法
     */
    protected void loadMore() {
        if(mLoadMoreListener != null) {
            mLoadMoreListener.OnLoadMore();
        }
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
