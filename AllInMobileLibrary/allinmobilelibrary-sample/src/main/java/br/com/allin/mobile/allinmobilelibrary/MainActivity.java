package br.com.allin.mobile.allinmobilelibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.helper.Util;


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

        Log.d("DEBUG 2", AlliNPush.getInstance().getDeviceToken());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        switch (pos) {
            case 0:
                startActivity(new Intent(this, MailRegisterActivity.class));
                break;

            case 1:
                List<AIValues> list = new ArrayList<>();
//                list.add(new AIValues("push_id", AlliNPush.getInstance().getDeviceToken()));
//                list.add(new AIValues("plataforma", "android"));
                list.add(new AIValues("email", "lrodriguesteste@gmail.com"));
                list.add(new AIValues("estado", "SP"));
                list.add(new AIValues("cel", "11987482000"));

                AlliNPush.getInstance().sendList("Mobile_10_Jan_V1", list);

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