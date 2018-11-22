package br.com.allin.mobile.pushnotification.webview;

import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;

/**
 * Activity to search the HTML and display it or redirects
 * to the scheme according to the sent by the server
 */
public class AllInWebViewActivity extends AppCompatActivity {
    private ProgressBar pbAllIn;
    private WebView wvAllIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initViews());

        setup();
        start();
    }

    private View initViews() {
        Toolbar toolbar = createToolbar();

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(PushIdentifier.TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout linearLayout = new LinearLayout(AllInWebViewActivity.this);
        linearLayout.setLayoutParams(
                new LinearLayoutCompat.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(toolbar);
        linearLayout.addView(createViewsBelow());

        return linearLayout;
    }

    private Toolbar createToolbar() {
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });

        Toolbar toolbar = new Toolbar(this);
        RelativeLayout.LayoutParams toolBarParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) styledAttributes.getDimension(0, 0)
        );

        toolbar.setVisibility(View.VISIBLE);
        toolbar.setLayoutParams(toolBarParams);

        return toolbar;
    }

    private RelativeLayout createViewsBelow() {
        RelativeLayout relativeLayout = new RelativeLayout(AllInWebViewActivity.this);

        // WEB VIEW ================================================================================
        wvAllIn = new WebView(AllInWebViewActivity.this);
        RelativeLayout.LayoutParams layoutParamsWebView = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        relativeLayout.addView(wvAllIn, layoutParamsWebView);

        // PROGRESS BAR ============================================================================
        float density = getResources().getDisplayMetrics().density;
        int size = (int) (60 * density + 0.5f);

        RelativeLayout.LayoutParams layoutParamsProgress = new RelativeLayout.LayoutParams(size, size);
        layoutParamsProgress.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        pbAllIn = new ProgressBar(AllInWebViewActivity.this);

        relativeLayout.addView(pbAllIn, layoutParamsProgress);

        return relativeLayout;
    }

    private void setup() {
        this.wvAllIn.setWebViewClient(new AllInWebViewClient(this, this.pbAllIn));
        this.wvAllIn.getSettings().setLoadsImagesAutomatically(true);

        if (Build.VERSION.SDK_INT > 21) {
            this.wvAllIn.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    private void start() {
        boolean isScheme = getIntent().getStringExtra(PushIdentifier.URL_SCHEME) != null;
        boolean isTransactional = getIntent().getStringExtra(PushIdentifier.ID_LOGIN) != null;
        boolean isCampaign = getIntent().getStringExtra(PushIdentifier.ID_CAMPAIGN) != null;

        String url = null;

        if (isScheme) {
            url = getIntent().getStringExtra(PushIdentifier.URL_SCHEME);
        } else if (isTransactional) {
            String urlTransactional = HttpConstant.URL_TEMPLATE_TRANSACTIONAL;
            String idLogin = getIntent().getStringExtra(PushIdentifier.ID_LOGIN);
            String idSend = getIntent().getStringExtra(PushIdentifier.ID_SEND);
            String date = getIntent().getStringExtra(PushIdentifier.DATE);
            url = String.format("%s/%s/%s/%s", urlTransactional, date, idLogin, idSend);
        } else if (isCampaign) {
            String urlCampaign = HttpConstant.URL_TEMPLATE_CAMPAIGN;
            String idCampaign = getIntent().getStringExtra(PushIdentifier.ID_CAMPAIGN);
            String idPush = Util.md5(AlliNPush.getInstance().getDeviceToken());
            url = String.format("%s/%s/%s?type=mobile", urlCampaign, idPush, idCampaign);
        }

        wvAllIn.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return back();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    return back();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean back() {
        if (wvAllIn.canGoBack()) {
            wvAllIn.goBack();
        } else {
            finish();
        }

        return true;
    }
}
