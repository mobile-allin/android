package br.com.allin.mobile.pushnotification.webview;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.Notification;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Activity to search the HTML and display it or redirects
 * to the scheme according to the sent by the server
 */
public class AllInWebViewActivity extends AppCompatActivity {
    private ProgressBar pbAllIn;
    private WebView wvAllIn;
    private AllInWebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initViews());

        if (this.mWebViewClient == null) {
            this.mWebViewClient = new AllInWebViewClient(AllInWebViewActivity.this, pbAllIn);
        }

        this.wvAllIn.setWebViewClient(mWebViewClient);

        init();
    }

    private View initViews() {
        Toolbar toolbar = createToolbar();

        setSupportActionBar(toolbar);

        configureActionBar();

        LinearLayout linearLayout = new LinearLayout(AllInWebViewActivity.this);
        linearLayout.setLayoutParams(
                new LinearLayoutCompat.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(toolbar);
        linearLayout.addView(createViewsBelow());

        return linearLayout;
    }

    private void configureActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(Notification.SUBJECT));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        RelativeLayout.LayoutParams layoutParamsProgress =
                new RelativeLayout.LayoutParams(getPx(60), getPx(60));
        layoutParamsProgress.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        pbAllIn = new ProgressBar(AllInWebViewActivity.this);

        relativeLayout.addView(pbAllIn, layoutParamsProgress);

        return relativeLayout;
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

    private int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;

        return (int) (dimensionDp * density + 0.5f);
    }

    private void init() {
        if (getIntent().hasExtra(Notification.URL_SCHEME)) {
            wvAllIn.loadUrl(getIntent().getStringExtra(Notification.URL_SCHEME));
        } else if (getIntent().hasExtra(Notification.ID_LOGIN)) {
            String urlTransactional = getIntent().getStringExtra(Notification.URL_TRANSACTIONAL);
            String idLogin = getIntent().getStringExtra(Notification.ID_LOGIN);
            String idSend = getIntent().getStringExtra(Notification.ID_SEND);
            String date = getIntent().getStringExtra(Notification.DATE_NOTIFICATION);
            String url = urlTransactional + "/" + date + "/" + idLogin + "/" + idSend;

            wvAllIn.loadUrl(url);
        } else {
            loadHTML(getIntent().getExtras());
        }
    }

    private void loadHTML(Bundle bundle) {
        String idCampaignString = bundle.getString(Notification.ID_CAMPAIGN);

        if (idCampaignString == null) {
            return;
        }

        int idCampaign = Integer.valueOf(idCampaignString);

        try {
            AllInPush.getInstance().getHtmlTemplate(idCampaign, new OnRequest<String>() {
                @Override
                public void onFinish(String value) {
                    wvAllIn.loadData(value.replace("##id_push##",
                            Util.md5(AllInPush.getInstance().getDeviceId())),
                            "text/html; charset=utf-8", null);
                }

                @Override
                public void onError(Exception exception) {
                    Log.d(AllInWebViewActivity.class.toString(), exception.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    back();

                    return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        if (wvAllIn.canGoBack()) {
            wvAllIn.goBack();
        } else {
            finish();
        }
    }
}
