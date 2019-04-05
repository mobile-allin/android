package br.com.allin.mobile.pushnotification.notification;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.AlertCallback;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.service.allin.MessageService;
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
            if (bundle.containsKey(PushIdentifier.ACTION)) {
                AllInDelegate delegate = AlliNConfiguration.getInstance().getDelegate();

                if (delegate != null) {
                    delegate.onClickAction(bundle.getString(PushIdentifier.ACTION));
                }
            } else {
                this.sendNotificationType(bundle);
            }

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

        String id = bundle.getString(PushIdentifier.ID);

        if (id != null) {
            MessageService.markAsRead(Integer.parseInt(id));
        }
    }

    private void start(final Bundle bundle) {
        String title = bundle.getString(PushIdentifier.TITLE);
        String body = bundle.getString(PushIdentifier.BODY);
        AllInDelegate delegate = AlliNConfiguration.getInstance().getDelegate();

        if (bundle.containsKey(PushIdentifier.URL_SCHEME)) {
            if (AlliNPush.getInstance().isShowAlertScheme()) {
                if (delegate != null) {
                    boolean alert = delegate.onShowAlert(title, body, new AlertCallback() {
                        @Override
                        public void show() {
                            startIntentScheme(bundle);
                        }
                    });

                    if (!alert) {
                        this.showAlert(bundle, true);
                    }
                } else {
                    this.showAlert(bundle, true);
                }
            } else {
                this.startIntentScheme(bundle);
            }
        } else {
            if (AlliNPush.getInstance().isShowAlertHTML()) {
                if (delegate != null) {
                    boolean alert = delegate.onShowAlert(title, body, new AlertCallback() {
                        @Override
                        public void show() {
                            startIntentHTML(bundle);
                        }
                    });

                    if (!alert) {
                        this.showAlert(bundle, false);
                    }
                } else {
                    this.showAlert(bundle, false);
                }
            } else {
                this.startIntentHTML(bundle);
            }
        }
    }

    private void startIntentScheme(Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(PushIdentifier.URL_SCHEME));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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

    private void showAlert(final Bundle bundle, final boolean scheme) {
        String title = bundle.getString(PushIdentifier.TITLE);
        String body = bundle.getString(PushIdentifier.BODY);
        int resID = this.getResources().getIdentifier("AlliNDialogTheme", "style", this.getPackageName());

        AlertDialog.Builder builder;

        if (resID != -1) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(this, resID));
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(body);
        builder.setMessage(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (scheme) {
                    startIntentScheme(bundle);
                } else {
                    startIntentHTML(bundle);
                }
            }
        });

        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}