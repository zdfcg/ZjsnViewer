package me.crafter.android.zjsnviewer.ui.web;

import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;

public class WebActivity extends BaseFragmentActivity{

    @BindView(R.id.web_view)
    WebView webView;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        ButterKnife.bind(this);

        setToolbarTitle("web");
//        toolbar.setTitle("web");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        String url = getIntent().getStringExtra("URL");
//        final String js = getIntent().getStringExtra("JS");
//
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setDatabaseEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setDefaultTextEncodingName("UTF-8");
//
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                webView.loadUrl(js);
//            }
//        });
//
//        webView.loadUrl(url);
    }
}
