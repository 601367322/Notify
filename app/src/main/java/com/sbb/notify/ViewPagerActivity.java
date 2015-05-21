package com.sbb.notify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbb on 2015/5/21.
 */
public class ViewPagerActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<Fragment> views = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        views.add(new TopicListFragment());
        viewPager.setAdapter(new mPagerAdapter(getSupportFragmentManager()));
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
