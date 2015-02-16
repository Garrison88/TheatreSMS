package com.garrisonthomas.theatresms;

public class ListModel {

    String smsBody;
    String timeStamp;

    public ListModel (String text1, String text2) {

        smsBody = text1;
        timeStamp = text2;

    }

    public String get_text() {
        return smsBody;
    }

    public String get_timestamp() {
        return timeStamp;
    }
}
