package com.example.lfasd.fuli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by LFasd on 2017/6/5.
 */

public class WebViewActivity extends AppCompatActivity {

    private String url;
    private WebView mWebView;
    private ProgressDialog mProgressDialog;

    /**
     * 启动WebViewActivity并把要加载的url传过去
     *
     * @param context 启动Activity的上下文
     * @param url     WebView需要加载的url
     */
    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        url = getIntent().getStringExtra("url");

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        mWebView.setWebViewClient(mWebViewClient);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //为ProgressDialog设置进度
                mProgressDialog.setProgress(newProgress);
            }
        });

        mWebView.loadUrl(url);

    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mWebView.isShown()) {
                mProgressDialog.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressDialog.hide();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mProgressDialog.hide();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //如果WebView可以回退，就回退到上级页面
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    //如果不能回退，就退出该Activity
                    finish();
                }
                break;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        mProgressDialog.dismiss();
        mWebView.clearHistory();
        super.onDestroy();
    }
}
