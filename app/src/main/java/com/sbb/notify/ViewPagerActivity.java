package com.sbb.notify;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar;

import com.umeng.analytics.MobclickAgent;

import net.imageloader.tools.st.imbydg;
import net.imageloader.tools.st.imbzdg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbb on 2015/5/21.
 */
public class ViewPagerActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<Fragment> views = new ArrayList<Fragment>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    public void showAd() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imbzdg.isaypl(ViewPagerActivity.this).iscxpl(
                        ViewPagerActivity.this, new imbydg() {
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
        }, 15000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        final ActionBar.Tab tab = actionBar
                .newTab()
                .setText("发表的帖子")
                .setTabListener(new ActionBar.TabListener() {
                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                        if (viewPager != null)
                            viewPager.setCurrentItem(0);
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                    }
                });
        actionBar.addTab(tab);
        final ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("回复的帖子")
                .setTabListener(new ActionBar.TabListener() {
                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                        viewPager.setCurrentItem(1);
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                    }
                });
        actionBar.addTab(tab1);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        views.add(new TopicListFragment());
        TopicListFragment fragment = new TopicListFragment();
        Bundle b = new Bundle();
        b.putBoolean("isTopic", false);
        fragment.setArguments(b);
        views.add(fragment);
        viewPager.setAdapter(new mPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    actionBar.selectTab(tab);
                } else {
                    actionBar.selectTab(tab1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        showAd();
    }

    class mPagerAdapter extends FragmentPagerAdapter {

        public mPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return views.get(arg0);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            ((LoaderImpl) views.get(position)).refresh();
            super.setPrimaryItem(container, position, object);
        }

    }
}
