package com.garrisonthomas.theatresms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SendSmsActivity extends BaseActivity {

    private EditText etSendMsg;
    private TextView userTimeStamp, userMessageBody, incomingBody, incomingPhoneNumber,
            incomingTimeStamp;
    private SmsManager sms;

    public static final String SENDER_ID = "SENDER_ID";
    public static final String SENDER_MSG = "SENDER_MSG";
    public static final String SENDER_TIME = "SENDER_TIME";
    public static final String USER_ID = "USER_ID";
    public static final String USER_MSG = "USER_MSG";
    public static final String USER_TIME = "USER_TIME";

    String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);

        sms = SmsManager.getDefault();

        etSendMsg = (EditText) findViewById(R.id.edit_text_send_sms);

        userTimeStamp = (TextView) findViewById(R.id.user_time_stamp);
        userMessageBody = (TextView) findViewById(R.id.user_message_body);

        incomingBody = (TextView) findViewById(R.id.incoming_message_body);
        incomingPhoneNumber = (TextView) findViewById(R.id.incoming_phone_number);
        incomingTimeStamp = (TextView) findViewById(R.id.incoming_time_stamp);

        if (savedInstanceState == null) {

            String senderId = ("");
            String senderMsg = ("");


        }

        onNewIntent(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_send_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMsg(View view) {

        if (!TextUtils.isEmpty(etSendMsg.getText())) {

            sms.sendTextMessage(String.valueOf(incomingPhoneNumber.getText()), null,
                    etSendMsg.getText().toString(), null, null);

            userTimeStamp.setText(DateHelper.getDateTimeFormattedFromMilliseconds(System.currentTimeMillis()));

            userMessageBody.setText(etSendMsg.getText());

            hideKeyboard();

            etSendMsg.setText("");
        }
    }

    private String getContactNameFromNumber(String number) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        Cursor cursor = SendSmsActivity.this.getContentResolver().query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) {
            senderName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        return senderName;

    }

    public void onNewIntent(Intent intent) {

        String senderNumber = intent.getStringExtra("msgFrom");
        String senderMessage = intent.getStringExtra("msgBody");
        String senderTimeStamp = intent.getStringExtra("timeStamp");

        boolean enabled = intent.getBooleanExtra("Enabled", true);
        if (enabled) {
            Util.manipulatePhone((AudioManager) getSystemService(Context.AUDIO_SERVICE), getWindow(), true);
        }

        incomingBody.setText(senderMessage);
        incomingPhoneNumber.setText(getContactNameFromNumber(senderNumber) + "\n(" + senderNumber + ")");
        incomingTimeStamp.setText(senderTimeStamp);

    }
}
