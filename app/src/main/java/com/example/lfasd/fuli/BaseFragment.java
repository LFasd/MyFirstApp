package com.example.lfasd.fuli;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LFasd on 2017/6/4.
 */

public class BaseFragment extends Fragment {

    public static final int DATA_SET_CHANGED = 0;

    public static final int END = 1;

    public static final int BUTTON_STATE_CHANGED = 2;

    public static final long BUTTON_WAIT = 800;

    protected boolean isScrolling = false;

    private int page = 1;

    private List<Result> mResults = new ArrayList<>();

    private FloatingActionButton backToTop;

    private String url;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView recyclerView;

    protected BaseFragment(String url) {
        this.url = url;

        if (this instanceof FuliFragment) {
            mAdapter = new FuliAdapter(mResults);
        } else {
            mAdapter = new AllAdapter(mResults);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getAdapter());
        recyclerView.addOnScrollListener(getListener());

        return view;
    }

    protected void load(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.requestURL(url + page, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "无法加载数据，请确认网络连接", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Return mReturn = Util.resolveJSON(response);
                        if (mReturn.getResults().length > 0) {
                            for (Result result : mReturn.getResults()) {
                                mResults.add(result);
                            }
                            Log.d("handler", "data_set_changed");
                            mHandler.sendEmptyMessage(DATA_SET_CHANGED);
                        } else {
                            Log.d("handler", "end");
                            mHandler.sendEmptyMessage(END);
                        }
                        call.cancel();
                    }
                });
            }
        }).start();
    }

    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                isScrolling = false;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(BUTTON_WAIT);
                            if (!isScrolling) {
                                mHandler.sendEmptyMessage(BUTTON_STATE_CHANGED);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    load(url);
                }
            } else {
                isScrolling = true;
                Log.d("handler", "button_state_changed");
                mHandler.sendEmptyMessage(BUTTON_STATE_CHANGED);
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_SET_CHANGED:
                    mAdapter.notifyDataSetChanged();
                    break;
                case END:
                    Toast.makeText(getActivity(), "已经到最后啦", Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_STATE_CHANGED:
                    if (isScrolling) {
                        backToTop.show();
                    } else {
                        backToTop.hide();
                    }
                    break;
            }
        }
    };

    protected RecyclerView.OnScrollListener getListener() {
        return mListener;
    }

    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    protected void setBackToTop(FloatingActionButton backToTop) {
        this.backToTop = backToTop;
    }

}
