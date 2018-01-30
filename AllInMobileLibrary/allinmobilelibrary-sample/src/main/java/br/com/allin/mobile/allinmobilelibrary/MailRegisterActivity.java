package br.com.allin.mobile.allinmobilelibrary;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

import br.com.allin.mobile.pushnotification.AlliNPush;

/**
 * Created by lucasrodrigues on 4/8/16.
 */
public class MailRegisterActivity extends AppCompatActivity {
    private EditText etEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mail_register);

        etEmail = (EditText) findViewById(R.id.etEmail);

        String email = AlliNPush.getInstance().getEmail();

        if (email == null || TextUtils.isEmpty(email)) {
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    etEmail.setText(account.name);

                    break;
                }
            }
        } else {
            etEmail.setText(email);
        }

        etEmail.setSelectAllOnFocus(true);
    }

    public void register(View view) {
        AlliNPush.getInstance().registerEmail(etEmail.getText().toString());
    }

    public void cancel(View view) {
        finish();
    }
}
