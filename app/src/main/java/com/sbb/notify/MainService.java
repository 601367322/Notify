package com.sbb.notify;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sbb.notify.share.CommonShared;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sbb on 2015/5/19.
 */
public class MainService extends Service {

    public static final String START_SERVICE = "com.sbb.notify.start_service.action";
    public static final String POST_SERVICE = "com.sbb.notify.post_service.action";
    public static final String STOP_SERVICE = "com.sbb.notify.stop_service.action";

    CommonShared cs;
    private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        cs = new CommonShared(this);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(START_SERVICE)) {
            start();
        } else if (intent.getAction().equals(POST_SERVICE)) {
            post();
        } else if (intent.getAction().equals(STOP_SERVICE)) {
            stop();
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void start() {
        Intent i = new Intent();
        i.setClass(this, MainService.class);
        i.setAction(POST_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 15 * 1000, pi);
    }

    public void post() {
        String open = MobclickAgent.getConfigParams(MainService.this, "open");
        if (open.equals("on")) {
            AsyncHttpClient httpClient = new AsyncHttpClient();

            httpClient.post(this, "http://iguba.eastmoney.com/action.aspx?action=getuserreply&uid=5384013884094322&rnd=1432015773727", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    JSONArray array = response.optJSONArray("re");
                    if (array != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.optJSONObject(i);
                            String te = jo.optString("te").replace("<br>", "");
                            if (i == 0) {
                                if (!cs.getReply().equals(te)) {
                                    cs.setReply(te);
                                    notify_(getString(R.string.reply), te);
                                }
                            }
                            sb.append(te + "\n");
                        }
                    }
                }
            });

            httpClient.post(this, "http://iguba.eastmoney.com/5384013884094322", null, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    String sub = responseString.substring(responseString.indexOf("var itemdata =") + "var itemdata =".length()).trim();
                    String sub1 = sub.substring(0, sub.indexOf("};") + 1);
                    try {
                        JSONObject response = new JSONObject(sub1);
                        JSONArray array = response.optJSONArray("re");
                        if (array != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.optJSONObject(i);
                                String tt = jo.optString("tt").replace("<br>", "");
                                if (i == 0) {
                                    if (!cs.getTopic().equals(tt)) {
                                        cs.setTopic(tt);
                                        notify_(getString(R.string.topic), tt);
                                    }
                                }
                                sb.append(tt + "\n");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(sub1);
                }
            });
        }else{
            stop();
            stopSelf();
        }
    }

    public void stop() {
        Intent i = new Intent();
        i.setClass(this, MainService.class);
        i.setAction(POST_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    public void notify_(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setOnlyAlertOnce(false).setNumber(1).setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);

        builder.setContentText(content);
        builder.setDefaults(Notification.DEFAULT_ALL);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        manager.notify((int) System.currentTimeMillis(), notification);
    }
}
