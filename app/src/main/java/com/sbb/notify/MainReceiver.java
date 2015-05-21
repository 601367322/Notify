package com.sbb.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sbb on 2015/5/19.
 */
public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println(intent.getAction());
    }
}
