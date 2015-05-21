package com.sbb.notify.share;

import android.content.Context;

public class CommonShared {
    private SharedDataUtil sp;
    private SharedDataUtil.SharedDataEditor editor;
    private final String TOPIC = "topic";
    private final String REPLY = "reply";
    private final String SWITCH = "switch";

    public static final int ON = 1;
    public static final int OFF = 0;

    public CommonShared(Context context) {
        sp = SharedDataUtil.getInstance(context);
        editor = sp.getSharedDataEditor();
    }

    public void setTopic(String s) {
        editor.putString(TOPIC, s);
        editor.commit();
    }

    public String getTopic() {
        return sp.getString(TOPIC, "");
    }

    public void setReply(String s) {
        editor.putString(REPLY, s);
        editor.commit();
    }

    public String getReply() {
        return sp.getString(REPLY, "");
    }

    public void setSwitch(int swich) {
        editor.putInt(SWITCH, swich);
        editor.commit();
    }

    public int getSwitch() {
        return sp.getInt(SWITCH, OFF);
    }
}
