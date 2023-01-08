package com.example.student.spacefleetdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Design extends Fragment{
    private ViewPager designViewPager;
    private TabLayout tabLayoutDS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_design,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPager();
        setTabLayout();
    }

    private void setViewPager() {
        designViewPager = getView().findViewById(R.id.ViewPager_Design);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new DesignFragment_Ship());
        fragmentList.add(new DesignFragment_Module());
        designViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        tabLayoutDS = getView().findViewById(R.id.tabLayoutDS);
        tabLayoutDS.setupWithViewPager(designViewPager);
        tabLayoutDS.getTabAt(0).setText("船艦設計");
        tabLayoutDS.getTabAt(1).setText("模組設計");
    }
}
