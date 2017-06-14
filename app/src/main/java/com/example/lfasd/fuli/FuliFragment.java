package com.example.lfasd.fuli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lib.homhomlib.design.SlidingLayout;


/**
 * Created by LFasd on 2017/6/3.
 */

public class FuliFragment extends BaseFragment {

    public static final String URL = "http://gank.io/api/data/福利/10/";

    private RecyclerView mRecyclerView;

    public static FuliFragment newInstance(FloatingActionButton button) {
        FuliFragment fuliFragment = new FuliFragment();
        BaseFragment.newInstance(URL, button, fuliFragment);
        return fuliFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        //FuliFragment显示的效果是一行有两个CardView，使用网络布局管理器
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.addOnScrollListener(getListener());

        SlidingLayout slidingLayout = (SlidingLayout) view.findViewById(R.id.slidinglayout);
        setSlidingLayout(slidingLayout);

        load();

        return view;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
