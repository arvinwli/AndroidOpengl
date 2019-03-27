package com.wangheart.androidopengl.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wangheart.androidopengl.R;
import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.utils.LogUtils;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/27
 */
public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl;
    private String mTitle;

    public static void launch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
    }

    private void initData() {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(mTitle))
            setTitle(mTitle);
        mWebView.loadUrl(mUrl);
    }

    private void initView() {
        mWebView=findViewById(R.id.webview);
        WebSettings setting = mWebView.getSettings();
        /**支持Js**/
        setting.setJavaScriptEnabled(true);

        /**设置自适应屏幕，两者合用**/
        //将图片调整到适合webview的大小
        setting.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        setting.setLoadWithOverviewMode(true);

        /**缩放操作**/
        // 是否支持画面缩放，默认不支持
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        // 是否显示缩放图标，默认显示
        setting.setDisplayZoomControls(false);
        // 设置网页内容自适应屏幕大小
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        /**设置允许JS弹窗**/
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);

        /**关闭webview中缓存**/
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        /**设置可以访问文件 **/
        setting.setAllowFileAccess(true);
        setting.setAllowFileAccessFromFileURLs(true);
        setting.setAllowUniversalAccessFromFileURLs(true);

        mWebView.setWebViewClient(new WebViewClient() {
            /*
            网络连接错误时调用
            */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogUtils.d("onProgressChanged %d",newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            /*
            网络开始加载时调用
            */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.d("onPageStarted %s",url);
            }

            /*
            网络加载结束时调用
            */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.d("onPageFinished %s",url);
            }
        });
    }
}
