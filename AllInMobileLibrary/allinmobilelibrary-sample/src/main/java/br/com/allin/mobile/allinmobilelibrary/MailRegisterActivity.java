package br.com.allin.mobile.allinmobilelibrary;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.interfaces.ConfigurationListener;

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

        String email = AllInPush.getUserEmail(MailRegisterActivity.this);

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
        progressDialog = ProgressDialog.show(this, null, "Cadastrando e-mail");

        AllInPush.updateUserEmail(etEmail.getText().toString(), new ConfigurationListener<String>() {
            @Override
            public void onFinish(String value) {
                progressDialog.dismiss();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MailRegisterActivity.this);
                alertDialog.setMessage(value);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        finish();
                    }
                });

                alertDialog.create().show();
            }

            @Override
            public void onError(Exception exception) {
                progressDialog.dismiss();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MailRegisterActivity.this);
                alertDialog.setTitle("Erro");
                alertDialog.setMessage(exception.getMessage());
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create().show();
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}
