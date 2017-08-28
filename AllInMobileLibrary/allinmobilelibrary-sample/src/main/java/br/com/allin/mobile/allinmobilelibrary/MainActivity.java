package br.com.allin.mobile.allinmobilelibrary;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvAllIn;
    private Switch swAllInNotification;
    private ProgressBar pbLoadNotification;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                MainActivity.this.showNotificationLoad();

                OnRequest onRequest = toggleSwitch(isChecked);

                if (isChecked) {
                    AllInPush.getInstance().enable(onRequest);
                } else {
                    AllInPush.getInstance().disable(onRequest);
                }
            }
        });

        deviceIsEnable();

        initPushAlliN();
    }

    private void initPushAlliN() {
        try {
            String projectId = getString(R.string.project_id);

            NotificationEntity notification = new NotificationEntity(
                    "#000000", R.mipmap.ic_launcher, android.R.drawable.sym_def_app_icon);

            ConfigurationEntity configurationEntity = new ConfigurationEntity(projectId);
            configurationEntity.setNotificationEntity(notification);

            AllInPush.configure(this, new AllInDelegate() {
                @Override
                public void onAction(String action, boolean sentFromServer) {

                }
            }, configurationEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Finalizar os serviços de push
//        AllInPush.finish();
    }

    private OnRequest toggleSwitch(final boolean enable) {
        return new OnRequest<String>() {
            @Override
            public void onFinish(final String value) {
                MainActivity.this.hideNotificationLoad();

                swAllInNotification.setChecked(enable);
            }

            @Override
            public void onError(final Exception exception) {
                MainActivity.this.hideNotificationLoad();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Erro");
                alertDialog.setMessage(exception.getMessage());
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        swAllInNotification.setChecked(!enable);
                    }
                });

                alertDialog.create().show();
            }
        };
    }

    public void deviceIsEnable() {
        MainActivity.this.showNotificationLoad();

        AllInPush.getInstance().deviceIsEnable(new OnRequest<Boolean>() {
            @Override
            public void onFinish(final Boolean value) {
                MainActivity.this.hideNotificationLoad();

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

                progressDialog = ProgressDialog.show(MainActivity.this, null, "Enviando informações");

                String pushId = AllInPush.getInstance().getDeviceId();

                Map<String, String> map = new HashMap<>();
                map.put("id_push", Util.md5(pushId));
                map.put("push_id", pushId);
                map.put("plataforma", "android");
                map.put("dt_ultima_abertura", null);
                map.put("dt_ultimo_clique", null);

                AllInPush.getInstance().sendList("Lista Padrao Push", map, new OnRequest<String>() {
                    @Override
                    public void onFinish(String value) {
                        progressDialog.dismiss();

                        Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception exception) {
                        progressDialog.dismiss();
                    }
                });

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