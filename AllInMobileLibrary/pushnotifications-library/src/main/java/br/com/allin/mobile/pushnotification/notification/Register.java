package br.com.allin.mobile.pushnotification.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.service.allin.NotificationService;

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
            this.startIntentScheme(bundle);
        }
    }

    private void sendNotificationType(Bundle bundle) {
        NotificationService.sendView(bundle.getString(PushIdentifier.VIEW_ID));
    }

    private void startIntentScheme(Bundle bundle) {
        try {
            Uri uri = Uri.parse(bundle.getString(PushIdentifier.URL_SCHEME));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            this.finish();
        } catch (Exception e) {
            Log.d("ALLIN", "Invalid URL Scheme");
        }
    }
}