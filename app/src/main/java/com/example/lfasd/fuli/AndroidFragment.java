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

    public static final String URL = "http://gank.io/api/data/Android/10/";

    public AndroidFragment(FloatingActionButton button) {
        super(URL);
        setBackToTop(button);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        load(URL + getPage());

        return view;
    }

    /**
     * 取消AndroidFragment正在加载的所有图片
     */
    public void clean() {
        ((AndroidAdapter) getAdapter()).clean();
    }
}
