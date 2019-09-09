package br.com.allin.mobile.allinmobilelibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView lvAllIn;
    private Switch swAllInNotification;
    private ProgressBar pbLoadNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ADD SEARCH ================================================
//        List<AISearch> list = new ArrayList<>();
//        list.add(new AISearch("iPhone BAGAÇA"));
//        BTG360.addSearchs("60:1", list);
//
//        BTG360.addSearch("60:1", new AISearch("TESTE SINGLE"));

//        ADD CART ==================================================
//        List<AICart> carts = new ArrayList<>();
//        carts.add(new AICart("131231231"));
//        BTG360.addCarts("60:1", carts);

//        ADD PRODUCTS ==============================================
//        List<AIProduct> products = new ArrayList<>();
//        products.add(new AIProduct("01010101"));
//        BTG360.addProducts("60:1", products);

//        ADD CLIENTS ===============================================
//        List<AIClient> clients = new ArrayList<>();
//        clients.add(new AIClient("lucasbrsilva@gmail.com"));
//        BTG360.addClients("60:1", clients);

//        ADD TRANSACTIONS ==========================================
//        List<AITransaction> transactions = new ArrayList<>();
//        transactions.add(new AITransaction("01010101Trans", "AC1231231"));
//        BTG360.addTransactions("60:1", transactions);

//        ADD WARNS =================================================
//        List<AIWarn> warns = new ArrayList<>();
//        warns.add(new AIWarn("01010101Warn", true));
//        BTG360.addWarnMe("60:1", warns);

//        ADD WARNS =================================================
//        List<AIWish> wishs = new ArrayList<>();
//        wishs.add(new AIWish("01010101Warn", true));
//        BTG360.addWishList("60:1", wishs);

        setContentView(R.layout.activity_main);

        lvAllIn = findViewById(R.id.lvAllIn);
        swAllInNotification = findViewById(R.id.swAllInNotification);
        pbLoadNotification = findViewById(R.id.pbLoadNotification);

        lvAllIn.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                Arrays.asList(getResources().getStringArray(R.array.allin_list))));
        lvAllIn.setOnItemClickListener(MainActivity.this);

        Log.d("DEBUG", AlliNPush.getInstance().getDeviceToken());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        switch (pos) {
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

//                String pushId = AlliNPush.getInstance().getDeviceToken();
//
//                List<AIValues> list = new ArrayList<>();
//                list.add(new AIValues("id_push", Util.md5(pushId)));
//                list.add(new AIValues("push_id", pushId));
//                list.add(new AIValues("plataforma", "android"));
//                list.add(new AIValues("dt_ultima_abertura", null));
//                list.add(new AIValues("dt_ultimo_clique", null));
//
//                AlliNPush.getInstance().sendList("Lista Padrao Push", list);

                final ProgressDialog progressDialog = ProgressDialog.show(this, null, "TESTE");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int position = 0; position < 1000; position++) {
                            String pushId = "PUSH_ID_" + position;

                            List<AIValues> list = new ArrayList<>();
                            list.add(new AIValues("id_push", Util.md5(pushId)));
                            list.add(new AIValues("push_id", pushId));
                            list.add(new AIValues("plataforma", "android"));
                            list.add(new AIValues("request_number", String.valueOf(position)));

                            AlliNPush.getInstance().sendList("stress_test", list);

                            SystemClock.sleep(300);
                        }

                        progressDialog.dismiss();
                    }
                }).start();

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