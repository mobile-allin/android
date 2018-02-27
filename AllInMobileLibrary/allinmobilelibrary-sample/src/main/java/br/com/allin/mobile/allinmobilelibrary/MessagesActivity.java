package br.com.allin.mobile.allinmobilelibrary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AlMessage;

/**
 * Created by lucasrodrigues on 24/04/17.
 */

public class MessagesActivity extends AppCompatActivity {
    private ListView lvMessages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messages);

        lvMessages = (ListView) findViewById(R.id.lvMessages);
        lvMessages.setAdapter(new Adapter(AlliNPush.getInstance().getMessages(this)));
    }

    private class Adapter extends BaseAdapter {
        private List<AlMessage> messages;

        Adapter(List<AlMessage> messages) {
            Collections.reverse(messages);

            this.messages = messages;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public AlMessage getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = LayoutInflater.from(MessagesActivity.this)
                    .inflate(R.layout.adapter_message, parent, false);

            final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            final TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);

            tvTitle.setText(getItem(position).getSubject());
            tvMessage.setText(getItem(position).getDescription());

            if (!getItem(position).isRead()) {
                tvTitle.setTextColor(Color.parseColor("#FF0000"));
                tvMessage.setTextColor(Color.parseColor("#FF0000"));
            }

            return view;
        }
    }
}
