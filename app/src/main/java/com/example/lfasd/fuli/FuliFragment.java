package com.example.lfasd.fuli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by LFasd on 2017/6/3.
 */

public class FuliFragment extends BaseFragment {

    public static final String URL = "http://gank.io/api/data/福利/10/";

    public FuliFragment(FloatingActionButton button) {
        super(URL);
        setBackToTop(button);
    }

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.addOnScrollListener(getListener());

        load(URL);

        return view;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
