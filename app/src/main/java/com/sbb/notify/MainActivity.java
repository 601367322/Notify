package com.sbb.notify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.sbb.notify.share.CommonShared;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UmengUpdateAgent.update(this);

        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);

        final CommonShared cs = new CommonShared(this);
        if (cs.getSwitch() == CommonShared.ON) {
            button.setChecked(true);
        } else {
            button.setChecked(false);
        }
        MobclickAgent.updateOnlineConfig(MainActivity.this);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MobclickAgent.updateOnlineConfig(MainActivity.this);
                String open =MobclickAgent.getConfigParams(MainActivity.this,"open");
                if(open.equals("on")) {
                    if (isChecked) {
                        Intent mIntent = new Intent();
                        mIntent.setAction(MainService.START_SERVICE);
                        mIntent.setPackage(getPackageName());
                        startService(mIntent);
                        cs.setSwitch(CommonShared.ON);
                    } else {
                        Intent mIntent = new Intent();
                        mIntent.setAction(MainService.STOP_SERVICE);
                        mIntent.setPackage(getPackageName());
                        startService(mIntent);
                        cs.setSwitch(CommonShared.OFF);
                    }
                }
            }
        });


//        findViewById(R.id.topic_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MobclickAgent.updateOnlineConfig(MainActivity.this);
//                String open = MobclickAgent.getConfigParams(MainActivity.this, "open");
//                if (open.equals("on")) {
//                    Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        PushAgent.getInstance(this).onAppStart();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        String device_token = UmengRegistrar.getRegistrationId(this);
        System.out.println(device_token);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_settings:
//                FeedbackAgent agent = new FeedbackAgent(this);
//                agent.startFeedbackActivity();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
