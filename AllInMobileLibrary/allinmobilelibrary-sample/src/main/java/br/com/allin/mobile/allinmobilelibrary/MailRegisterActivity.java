package br.com.allin.mobile.allinmobilelibrary;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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

        etEmail = findViewById(R.id.etEmail);
        etEmail.setSelectAllOnFocus(true);
    }

    public void cancel(View view) {
        finish();
    }
}
