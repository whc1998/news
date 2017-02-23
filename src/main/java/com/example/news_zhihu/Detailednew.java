package com.example.news_zhihu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by WHC on 2017/2/14.
 */

public class Detailednew extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailednew);
        webView = (WebView) findViewById(R.id.wv_online);
        Intent intent = getIntent();
        final String path = intent.getStringExtra("Path");
        //WebSettings对webview进行基本设置
        WebSettings settings = webView.getSettings();
        //显示缩放控制器
        settings.setDisplayZoomControls(true);
        //支持网页缩放
        settings.setSupportZoom(true);
        //设置网页支持javaScript脚本
        settings.setJavaScriptEnabled(true);
        //加载网页
        webView.loadUrl(path);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                webView.loadUrl(path);

                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
}
