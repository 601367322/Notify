package com.sbb.notify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.sbb.notify.share.CommonShared;
import com.umeng.analytics.MobclickAgent;

import net.imageloader.tools.br.imakdg;
import net.imageloader.tools.br.imandg;
import net.imageloader.tools.imafdg;
import net.imageloader.tools.st.imbydg;
import net.imageloader.tools.st.imbzdg;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imafdg.getInstance(this).init("ee066029d422e628",
                "12f79f9cc0e475ad", false);

        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);

        final CommonShared cs = new CommonShared(this);
        if (cs.getSwitch() == CommonShared.ON) {
            button.setChecked(true);
        } else {
            button.setChecked(false);
        }

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
        });


        // 实例化LayoutParams(重要)
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
        // 实例化广告条
        imandg adView = new imandg(this, imakdg.FIT_SCREEN);
        // 调用Activity的addContentView函数

        ((ViewGroup) findViewById(R.id.content)).addView(adView);

        showAd();

        findViewById(R.id.topic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewPagerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showAd() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imbzdg.isaypl(MainActivity.this).iscxpl(
                        MainActivity.this, new imbydg() {
                            @Override
                            public void isbqpl() {
                                Log.i("YoumiAdDemo", "展示成功");
                            }

                            @Override
                            public void isbppl() {
                                Log.i("YoumiAdDemo", "展示失败");
                                showAd();
                            }

                            @Override
                            public void isbrpl() {
                                Log.i("YoumiAdDemo", "展示关闭");
                            }

                        });
            }
        }, 3000);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
