package br.com.allin.mobile.pushnotification.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.service.allin.NotificationService;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("");

        this.setup(this.getIntent().getExtras());
    }

    private void setup(Bundle bundle) {
        if (bundle != null) {
            this.sendNotificationType(bundle);
            this.start(bundle);
        }
    }

    private void sendNotificationType(Bundle bundle) {
        NotificationService service = new NotificationService();
        String date = bundle.getString(PushIdentifier.DATE);

        if (bundle.containsKey(PushIdentifier.ID_CAMPAIGN)) {
            String campaignId = bundle.getString(PushIdentifier.ID_CAMPAIGN);

            if (campaignId != null) {
                service.sendCampaign(Integer.parseInt(campaignId), date);
            }
        } else if (bundle.containsKey(PushIdentifier.ID_SEND)) {
            String sendId = bundle.getString(PushIdentifier.ID_SEND);

            if (sendId != null) {
                service.sendTransactional(Integer.parseInt(sendId), date);
            }
        }
    }

    private void start(Bundle bundle) {
        if (bundle.containsKey(PushIdentifier.URL_SCHEME)) {
            this.startIntentScheme(bundle);
        } else {
            this.startIntentHTML(bundle);
        }
    }

    private void startIntentScheme(Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(PushIdentifier.URL_SCHEME));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        this.finish();
    }

    private void startIntentHTML(Bundle bundle) {
        Intent intent = new Intent(Register.this, AllInWebViewActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        this.finish();
    }
}