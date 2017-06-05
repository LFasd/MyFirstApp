package com.example.lfasd.fuli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LFasd on 2017/6/3.
 */

public class AndroidFragment extends BaseFragment {

    public static final String ANDROID_URL = "http://gank.io/api/data/Android/10/";

    public AndroidFragment(FloatingActionButton button) {
        super(ANDROID_URL);
        backToTop = button;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getAdapter());
        recyclerView.addOnScrollListener(getListener());

        load(ANDROID_URL + page);

        return view;
    }

    /**
     * 取消AndroidFragment正在加载的所有图片
     */
    public void clean() {
        ((AndroidAdapter) getAdapter()).clean();
    }
}
