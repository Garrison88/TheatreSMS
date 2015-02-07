package com.garrisonthomas.theatresms;

import android.media.AudioManager;
import android.view.Window;
import android.view.WindowManager;

public class Util {

    public static void manipulatePhone(AudioManager audio, Window window, boolean enabled){

        if (enabled) {
            // set phone to 'vibrate mode'

            audio.setRingerMode(1);

            // dim screen when app starts

            WindowManager.LayoutParams layout = window.getAttributes();
            layout.screenBrightness = 0F;
            window.setAttributes(layout);
        } else{
            //TODO: figure out users current state of volume and set it back here (brightness auto changed)
        }


    }

}
