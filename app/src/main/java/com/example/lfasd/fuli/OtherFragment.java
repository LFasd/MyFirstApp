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

public class OtherFragment extends BaseFragment {

    public static final String URL = "http://gank.io/api/data/瞎推荐/10/";

    protected OtherFragment(FloatingActionButton button) {
        super(URL);
        setBackToTop(button);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        load(URL + getPage());

        return view;
    }
}
