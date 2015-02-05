package com.garrisonthomas.theatresms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SendSmsActivity extends Activity {

//    public ListView msgList;
//    public ArrayList<ListModel> list;
//    public ListviewAdapter lvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);

        Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(100);

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

    public void send_msg(View view) {

        EditText etMessageDescription = (EditText)findViewById(R.id.edit_text_send_sms);
        TextView senderPhoneNumberTextView = (TextView) findViewById(R.id.sender_phone_number);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(senderPhoneNumberTextView.getText().toString(), null,
                etMessageDescription.getText().toString(), null, null);

        TextView userTime = (TextView) findViewById(R.id.time_stamp);
        userTime.setText(DateHelper.getDateTimeFormattedFromMilliseconds(System.currentTimeMillis()));

        TextView phoneNumber = (TextView) findViewById(R.id.phone_number);
        phoneNumber.setText(getString(R.string.user_name));

        TextView userMessageBody = (TextView) findViewById(R.id.message_body);
        userMessageBody.setText(etMessageDescription.getText());

        RelativeLayout userLayout = (RelativeLayout) findViewById(R.id.user_message_layout);
        userLayout.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etMessageDescription.getWindowToken(), 0);

        etMessageDescription.setText("");

        }

    public void onNewIntent(Intent intent){

        String senderNumber = intent.getStringExtra("msgFrom");
        String senderMessage = intent.getStringExtra("msgBody");
        String senderTimeStamp = intent.getStringExtra("timeStamp");

        boolean enabled = intent.getBooleanExtra("Enabled", true);
        if (enabled) {
            Util.manipulatePhone((AudioManager)getSystemService(Context.AUDIO_SERVICE), getWindow(), true);
        }

        TextView senderBody_tv = (TextView)findViewById(R.id.sender_message_body);
        senderBody_tv.setText(senderMessage);
        TextView senderPhoneNumber_tv = (TextView)findViewById(R.id.sender_phone_number);
        senderPhoneNumber_tv.setText(senderNumber);
        TextView senderTimeStamp_tv = (TextView)findViewById(R.id.sender_time_stamp);
        senderTimeStamp_tv.setText(senderTimeStamp);

        RelativeLayout userLayout = (RelativeLayout) findViewById(R.id.user_message_layout);
        userLayout.setVisibility(View.INVISIBLE);
    }
    //TODO: create onResume and put vibration code there
    public void onResume() {



    }
}
