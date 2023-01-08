package com.example.student.spacefleetdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager gameViewPager;
    private TabLayout tabLayout;
    private Fragment_Overview fragmentOverview = new Fragment_Overview();
    private Fragment_Design fragmentDesign = new Fragment_Design();
    private Fragment_Research fragmentResearch = new Fragment_Research();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViewPager();
        setTabLayout();
    }

    private void setViewPager() {
        gameViewPager = findViewById(R.id.ViewPager_game);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(fragmentOverview);
        fragmentList.add(fragmentDesign);
        fragmentList.add(fragmentResearch);
        gameViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        });
    }

    private void setTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(gameViewPager);
        tabLayout.getTabAt(0).setText("總覽");
        tabLayout.getTabAt(1).setText("設計");
        tabLayout.getTabAt(2).setText("研究");
    }

    public static void setButtonClickable(Button button, boolean clickable){
        button.setClickable(clickable);
        if(clickable){
            button.setTextColor(0xff000000);
        }else{
            button.setTextColor(0x44000000);
        }
    }

    public Fragment_Overview getFragmentOverview() {
        return fragmentOverview;
    }

    public Fragment_Design getFragmentDesign() {
        return fragmentDesign;
    }

    public Fragment_Research getFragmentResearch() {
        return fragmentResearch;
    }
}
