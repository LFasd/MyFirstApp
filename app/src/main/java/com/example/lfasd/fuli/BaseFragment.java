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

/**
 *
 */
public class BaseFragment extends Fragment {

    /**
     * 数据改变标识
     */
    public static final int DATA_SET_CHANGED = 0;

    /**
     * 数据末尾标识
     */
    public static final int END = 1;

    /**
     * 按钮状态改变标识
     */
    public static final int BUTTON_STATE_CHANGED = 2;

    /**
     * 停止滚动后FloatingActionButton需要过多少毫秒后消失
     */
    public static final long BUTTON_WAIT = 800;

    /**
     * Fragment中的RecyclerView是否正在滚动
     */
    private boolean isScrolling = false;

    /**
     * 现在已经加载过的url资源数
     */
    private int page;

    /**
     * 对应RecyclerView中每个Item的数据模型
     */
    private List<Result> mResults;

    /**
     * 回滚到RecyclerView的浮动按钮
     */
    private FloatingActionButton backToTop;

    /**
     * 后台url
     */
    private String url;

    /**
     * RecyclerView显示所需的Adapter
     */
    private RecyclerView.Adapter mAdapter;

    /**
     * 每一个Fragment中用来显示主要内容的RecyclerView
     */
    private RecyclerView recyclerView;

    /**
     *
     * @param url
     * @param button
     * @return
     */
    public static void newInstance(String url, FloatingActionButton button, BaseFragment baseFragment){
        baseFragment.url = url;
        baseFragment.backToTop = button;

        baseFragment.mResults = new ArrayList<>();
        baseFragment.page = 1;

        //如果新建的对象是FuliFragment，就用FuliFragment特有的FuliAdapter
        if (baseFragment instanceof FuliFragment) {
            baseFragment.mAdapter = new FuliAdapter(baseFragment.mResults);
        } else {//否则就用其他Fragment都通用的AllAdapter
            baseFragment.mAdapter = new AllAdapter(baseFragment.mResults);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        //除了FuliFragment以外，其他Fragment的布局都是一样的，这里是其他Fragment通用的RecyclerView设置
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(mListener);

        load();

        return view;
    }

    /**
     * 从url对应的后台加载数据，并把解析后的数据加载到模型集合中，然后通过Handler发送通知数据改变，刷新界面
     */
    protected void load() {

        //启动一条新的线程来进行网络连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.requestURL(url + page, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //当加载后台数据失败后，提示用户
                        Looper.prepare();
                        Toast.makeText(getActivity(), "无法加载数据，请确认网络连接", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //加载后台数据成功，就解析后台返回的数据
                        Return mReturn = Util.resolveJSON(response);

                        if (mReturn.getResults().length > 0) {
                            //把数据模型添加到集合中
                            for (Result result : mReturn.getResults()) {
                                mResults.add(result);
                            }
                            Log.d("handler", "data_set_changed");

                            //通知RecyclerView的Adapter发生了数据改变，使RecyclerView显示的内容也更新
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

    /**
     * ！！！！！！！！！！！！！
     * 实现方法不够完美，暂时还有漏洞
     * <p>
     * 监听RecyclerView滚动事件的监听器
     */
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            //当RecyclerView停止滚动后
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isScrolling = false;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(BUTTON_WAIT);

                            //通知可能需要改变FloatingActionButton的显示状态
                            mHandler.sendEmptyMessage(BUTTON_STATE_CHANGED);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                //如果RecyclerView不能往下滚动，意味着到了底部，可以加载下一个资源的数据了
                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    load();
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
                    //如果正在滚动，把FloatingActionButton显示出来
                    if (isScrolling) {
                        backToTop.show();
                    } else {
                        //否则把FloatActionButton隐藏
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

    /**
     * 给MainActivity中的FloatingActionButton使用的函数，返回当前正在显示的Fragment的RecyclerView
     *
     * @return 前正在显示的Fragment的RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 获取MainActivity中的FloatingActionButton，当RecyclerView发生滑动的时候，可以改变浮动按钮的状态
     *
     * @param backToTop MainActivity中的FloatingActionButton
     */
    protected void setBackToTop(FloatingActionButton backToTop) {
        this.backToTop = backToTop;
    }

}
