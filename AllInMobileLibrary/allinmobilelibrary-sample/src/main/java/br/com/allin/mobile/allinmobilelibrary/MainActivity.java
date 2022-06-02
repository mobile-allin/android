package br.com.allin.mobile.allinmobilelibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        EditText email = findViewById(R.id.email);
        Button register = findViewById(R.id.register);
        Button copy = findViewById(R.id.copy);

        register.setOnClickListener(view -> {
            if (isValidEmail(email.getText().toString())) {
                List<AIValues> values = new ArrayList<>();
                values.add(new AIValues("email", email.getText().toString()));

                AlliNPush.getInstance().sendList("Synapse_List", values);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sucesso");
                builder.setMessage("E-mail inválido");
                builder.setPositiveButton("OK", (arg0, arg1) -> arg0.dismiss());
                builder.create().show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Erro");
                builder.setMessage("E-mail inválido");
                builder.setPositiveButton("OK", (arg0, arg1) -> arg0.dismiss());
                builder.create().show();
            }
        });

        Log.d("DEBUG", AlliNPush.getInstance().getDeviceToken());

        copy.setOnClickListener(view -> setClipboard(view.getContext(), AlliNPush.getInstance().getDeviceToken()));
    }

    private void setClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, "Push ID copiado", Toast.LENGTH_LONG).show();
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}