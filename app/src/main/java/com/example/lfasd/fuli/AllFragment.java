package com.example.lfasd.fuli;

import android.content.SharedPreferences;
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

    public static AllFragment newInstance(String url, FloatingActionButton button
            , SharedPreferences sharedPreferences) {
        AllFragment allFragment = new AllFragment();
        BaseFragment.newInstance(url, button, allFragment, sharedPreferences);
        return allFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
