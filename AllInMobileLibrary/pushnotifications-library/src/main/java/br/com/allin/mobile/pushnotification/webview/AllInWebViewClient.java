package br.com.allin.mobile.pushnotification.webview;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * WebClient callbacks of webview
 */
public class AllInWebViewClient extends WebViewClient {
    private Activity activity;
    private ProgressBar mProgressBar;

    public AllInWebViewClient(Activity activity, ProgressBar mProgressBar) {
        this.activity = activity;
        this.mProgressBar = mProgressBar;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!(url.contains("http://") || url.contains("https://"))) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
                activity.finish();
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);

    }

}
