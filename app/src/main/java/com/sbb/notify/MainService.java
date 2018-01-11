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
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sbb.notify.share.CommonShared;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        try {
            if (intent.getAction().equals(START_SERVICE)) {
                start();
            } else if (intent.getAction().equals(POST_SERVICE)) {
                post();
            } else if (intent.getAction().equals(STOP_SERVICE)) {
                stop();
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    BasicClientCookie genCookie(String name, String value) {
        BasicClientCookie newCookie = new BasicClientCookie(name, value);
        newCookie.setDomain("bbs.macd.cn");
        newCookie.setPath("/");
        return newCookie;
    }


    public void post() {

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (calendar.get(Calendar.DAY_OF_WEEK) > 1 && calendar.get(Calendar.DAY_OF_WEEK) < 7) {

            if ((calendar.get(Calendar.HOUR_OF_DAY) > 9 && calendar.get(Calendar.HOUR_OF_DAY) < 15)) {

                String open = MobclickAgent.getConfigParams(MainService.this, "open");
                if (open.equals("on")) {
                    final AsyncHttpClient httpClient = new AsyncHttpClient();

                    PersistentCookieStore myCookieStore = new PersistentCookieStore(this);

//            myCookieStore.addCookie(genCookie("rVTN_4d0a_checkpm", "1"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_connect_is_bind", "0"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_lastact", "1503048757%09home.php%09space"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_lastcheckfeed", "4047955%7C1503048748"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_lastvisit", "1503045019"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_lip", "101.254.183.73%2C1503048599"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_saltkey", "NNNSq91h"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_security_cookiereport", "bacf9SVNwcdPFuCramSbukd8ECJCVm%2FvEQE9cwl4QvgYo36Zwa75"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_sendmail", "1"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_sid", "LQiBQK"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_ulastactivity", "f803vKg6%2BfVn9jjZuuM54R7Hm9lkyO1kPwRnuXo99iGDApD%2FFRhH"));
//            myCookieStore.addCookie(genCookie("vClickLastTime", "a%3A8%3A%7Bi%3A0%3Bb%3A0%3Bi%3A2815027%3Bi%3A1502985600%3Bi%3A2821398%3Bi%3A1502985600%3Bi%3A2815507%3Bi%3A1502985600%3Bi%3A2816687%3Bi%3A1502985600%3Bi%3A2612471%3Bi%3A1502985600%3Bi%3A2822333%3Bi%3A1502985600%3Bi%3A2820739%3Bi%3A1502985600%3B%7D"));
//            myCookieStore.addCookie(genCookie("pgv_info", "ssi=s8769423572"));
//            myCookieStore.addCookie(genCookie("pgv_pvi", "7517162152"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_auth", "1c50b7ge6wzW1qx8W5zPekVp8XB5Gt7hpx%2Be9%2B2lepDXIlyx%2FnpFX2SxIhcAFuopSI%2FOz8vaO41NHuHFLiaKCNiiFvtQ"));
//            myCookieStore.addCookie(genCookie("rVTN_4d0a_checkfollow", "1"));

                    httpClient.setCookieStore(myCookieStore);

                    httpClient.get(this, "http://bbs.macd.cn/forum.php?mod=viewthread&tid=2835264&extra=&authorid=3833326", null, new TextHttpResponseHandler("gbk") {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {


                            Pattern pattern = Pattern.compile("page=\\d{1}");

                            Matcher matcher = pattern.matcher(responseString);

                            List<Integer> pages = new ArrayList<>();

                            while (matcher.find()) {
                                String str1 = matcher.group();
                                pages.add(Integer.valueOf(str1.substring(5)));
                            }

                            int page = 0;

                            for (int i = 0; i < pages.size(); i++) {
                                if (pages.get(i) > page) {
                                    page = pages.get(i);
                                }
                            }

                            httpClient.get(MainService.this, "http://bbs.macd.cn/forum.php?mod=viewthread&tid=2835264&extra=&authorid=3833326&page=" + page, null, new TextHttpResponseHandler("gbk") {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    Pattern pattern = Pattern.compile("postmessage_\\d+\">");
                                    Matcher matcher = pattern.matcher(responseString);


                                    String msg = "";

                                    while (matcher.find()) {
                                        String str1 = matcher.group();
                                        int start = responseString.indexOf(str1);
                                        String responseString1 = responseString.substring(start + str1.length());
                                        int end = responseString1.indexOf("</td>");
                                        msg = responseString1.substring(0, end).trim();
                                    }

                                    if (!cs.getTopic().equals(msg)) {
                                        cs.setTopic(msg);
                                        notify_(getString(R.string.topic), msg);
                                    }
                                }
                            });


//                    while (responseString.indexOf("<div class=\"flw_quotenote xs2 pbw\">") > -1) {
//                        int start = responseString.indexOf("<div class=\"flw_quotenote xs2 pbw\">");
//                        responseString = responseString.substring(start + "<div class=\"flw_quotenote xs2 pbw\">".length());
//                        int end = responseString.indexOf("</div>");
//
//                        String msg = responseString.substring(0, end);
//
//                        System.out.println(msg);
//                        responseString = responseString.substring(end);
//
//                        if (!cs.getTopic().equals(msg)) {
//                            cs.setTopic(msg);
//                            notify_(getString(R.string.topic), msg);
//                        }
//
//                        break;
//                    }
                        }
                    });

                } else {
                    stop();
                    stopSelf();
                }
            }
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
//        Intent intent = new Intent(this, ViewPagerActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        manager.notify((int) System.currentTimeMillis(), notification);
    }
}
