package com.example.lfasd.fuli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LFasd on 2017/6/5.
 */

public class AllFragment extends BaseFragment {

    private String url;

    protected AllFragment(String url, FloatingActionButton button) {
        super(url);
        this.url = url;
        setBackToTop(button);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        load(url);

        return view;
    }
}
