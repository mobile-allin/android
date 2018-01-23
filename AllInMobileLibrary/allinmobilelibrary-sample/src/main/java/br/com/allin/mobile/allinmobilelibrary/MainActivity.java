package br.com.allin.mobile.allinmobilelibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AllInDelegate {

    private ListView lvAllIn;
    private Switch swAllInNotification;
    private ProgressBar pbLoadNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlliNPush.getInstance().registerForPushNotifications(this);

        Log.d("DEBUG", AlliNPush.getInstance().getIdentifier());

        setContentView(R.layout.activity_main);

        lvAllIn = (ListView) findViewById(R.id.lvAllIn);
        swAllInNotification = (Switch) findViewById(R.id.swAllInNotification);
        pbLoadNotification = (ProgressBar) findViewById(R.id.pbLoadNotification);

        lvAllIn.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                Arrays.asList(getResources().getStringArray(R.array.allin_list))));
        lvAllIn.setOnItemClickListener(MainActivity.this);

        swAllInNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showNotificationLoad();

                swAllInNotification.setChecked(isChecked);

                if (isChecked) {
                    AlliNPush.getInstance().enable();
                } else {
                    AlliNPush.getInstance().disable();
                }

                deviceIsEnable();
            }
        });

        deviceIsEnable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Finalizar os serviços de push
        AlliNPush.getInstance().finish();
    }

    @Override
    public void onAction(String action, boolean sentFromServer) {

    }

    public void deviceIsEnable() {
        showNotificationLoad();

        AlliNPush.getInstance().deviceIsEnable(new OnRequest<Boolean>() {
            @Override
            public void onFinish(final Boolean value) {
                hideNotificationLoad();

                swAllInNotification.setChecked(value);
            }

            @Override
            public void onError(Exception exception) {
                MainActivity.this.hideNotificationLoad();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, MailRegisterActivity.class));
                break;

            case 1:
                if (!swAllInNotification.isChecked()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Aviso");
                    alertDialog.setMessage("Para enviar a lista, é necessário estar com as notificações habilitadas.");
                    alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.create().show();

                    return;
                }

                String pushId = AlliNPush.getInstance().getDeviceToken();

                Map<String, String> map = new HashMap<>();
                map.put("id_push", Util.md5(pushId));
                map.put("push_id", pushId);
                map.put("plataforma", "android");
                map.put("dt_ultima_abertura", null);
                map.put("dt_ultimo_clique", null);

                AlliNPush.getInstance().sendList("Lista Padrao Push", map);

                break;

            case 2:

                startActivity(new Intent(this, MessagesActivity.class));

                break;

            default:
                break;
        }
    }

    private void showNotificationLoad() {
        swAllInNotification.setVisibility(View.GONE);
        pbLoadNotification.setVisibility(View.VISIBLE);
    }

    private void hideNotificationLoad() {
        swAllInNotification.setVisibility(View.VISIBLE);
        pbLoadNotification.setVisibility(View.GONE);
    }
}