package com.garrisonthomas.theatresms;

import android.media.AudioManager;
import android.view.Window;
import android.view.WindowManager;

public class Util extends BaseActivity {

    public static void manipulatePhone(AudioManager audio, Window window, boolean enabled) {

        WindowManager.LayoutParams layout = window.getAttributes();

        if (enabled) {
            // set phone to 'priority mode'

            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            // dim screen when app starts

            layout.screenBrightness = 0F;
            window.setAttributes(layout);
        } else {

            // return phone to 'normal mode'

            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            // return phone to previous brightness

            layout.screenBrightness = 1;
            window.setAttributes(layout);
        }

    }

}
