package com.example.lfasd.fuli;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by LFasd on 2017/6/26.
 */

public class NetStatusReceiver extends BroadcastReceiver {

    private OnNetWorkChangeListener mListener;

    public void setListener(OnNetWorkChangeListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()){
            mListener.onNetWorkChange();
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){

            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){

            }
        }
    }

}
