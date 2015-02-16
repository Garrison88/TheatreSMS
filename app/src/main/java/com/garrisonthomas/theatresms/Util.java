package com.garrisonthomas.theatresms;

import android.media.AudioManager;
import android.view.Window;
import android.view.WindowManager;

public class Util {

    public static void manipulatePhone(AudioManager audio, Window window, boolean enabled){

        if (enabled) {
            // set phone to 'silent mode'

            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            // dim screen when app starts

            WindowManager.LayoutParams layout = window.getAttributes();
            layout.screenBrightness = 0F;
            window.setAttributes(layout);
        } else{
            // do nothing
        }


    }

}
