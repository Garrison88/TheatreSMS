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
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    BroadcastReceiver receiver;
    private int volume_level = 0;
    private AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        setContentView(R.layout.layout_settings);

        Switch toggle_settings = (Switch) findViewById(R.id.activation_switch);

        toggle_settings.setOnCheckedChangeListener(this);

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                    Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                    SmsMessage[] msgs = null;
                    String msgFrom;
                    if (bundle != null){
                        //---retrieve the SMS message received---
                        try{
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            msgs = new SmsMessage[pdus.length];
                            for(int i=0; i<msgs.length; i++){
                                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                                msgFrom = msgs[i].getOriginatingAddress();
                                String msgBody = msgs[i].getMessageBody();
                                Intent sendSMSIntent = new Intent(context, SendSmsActivity.class);
                                sendSMSIntent.putExtra("Enabled", true);
                                sendSMSIntent.putExtra("msgFrom", msgFrom);
                                sendSMSIntent.putExtra("msgBody", msgBody);
                                sendSMSIntent.putExtra("timeStamp", DateHelper.
                                        getDateTimeFormattedFromMilliseconds(System.currentTimeMillis()));
                                startActivity(sendSMSIntent);


                            }
                        }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }

        };


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            volume_level= am.getStreamVolume(AudioManager.STREAM_RING);
//            Toast.makeText(this, volume_level+"", Toast.LENGTH_SHORT).show();

            Util.manipulatePhone(am, getWindow(), true);



            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");



            registerReceiver(receiver, filter);



        } else{
            Util.manipulatePhone(am, getWindow(), false);
            this.finish();
        }

        }

    public void onBackPressed() {

        Switch toggle = (Switch) findViewById(R.id.activation_switch);
        if(toggle.isChecked()) {
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

        }else{
            this.finish();
        }
    }

public void onDestroy(){

    super.onDestroy();
    try {
        unregisterReceiver(receiver);

    } catch(Exception e){
        e.printStackTrace();
    }
    am.setStreamVolume(
            AudioManager.STREAM_RING,
            volume_level,
            0);

}
    }

