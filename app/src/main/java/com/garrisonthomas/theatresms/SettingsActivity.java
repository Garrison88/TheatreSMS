package com.garrisonthomas.theatresms;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v7.widget.SwitchCompat;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    BroadcastReceiver receiver;
    private int volume_level;
    private AudioManager am;
    private TextView tv;
    private SwitchCompat toggle_settings;
    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_settings);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        tv = (TextView) findViewById(R.id.textview_enjoy_movie);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        toggle_settings = (SwitchCompat) findViewById(R.id.activation_switch);

        toggle_settings.setOnCheckedChangeListener(this);

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                    SmsMessage[] msgs = null;
                    String msgFrom;
                    if (bundle != null) {
                        //---retrieve the SMS message received---
                        try {
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            msgs = new SmsMessage[pdus.length];
                            for (int i = 0; i < msgs.length; i++) {
                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                msgFrom = msgs[i].getOriginatingAddress();
                                String msgBody = msgs[i].getMessageBody();
                                Intent sendSMSIntent = new Intent(context, SendSmsActivity.class);
                                sendSMSIntent.putExtra("Enabled", true);
                                sendSMSIntent.putExtra("msgFrom", msgFrom);
                                sendSMSIntent.putExtra("msgBody", msgBody);
                                sendSMSIntent.putExtra("timeStamp", DateHelper.
                                        getDateTimeFormattedFromMilliseconds(System.
                                                currentTimeMillis()));
                                startActivity(sendSMSIntent);
                                vibe.vibrate(100);


                            }
                        } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

//            volume_level = am.getStreamVolume(AudioManager.RINGER_MODE_SILENT);

            Util.manipulatePhone(am, getWindow(), true);

            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");

            registerReceiver(receiver, filter);

            tv.setText("Enjoy the show!");

        } else {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            this.finish();

        }

    }

    public void onBackPressed() {

        if (toggle_settings.isChecked()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_exit_title))
                    .setMessage(getString(R.string.dialog_exit_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            SettingsActivity.this.finish();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } else {
            this.finish();
        }
    }

    public void onDestroy() {

        super.onDestroy();

        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        try {
            unregisterReceiver(receiver);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

