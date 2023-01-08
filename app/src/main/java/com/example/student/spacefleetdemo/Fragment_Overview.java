package com.example.student.spacefleetdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Overview extends Fragment {
    private ViewPager overviewViewPager;
    private TabLayout tabLayoutOV;
    private OverviewFragment_Overview overviewFragment = new OverviewFragment_Overview();
    private OverviewFragment_Fleet fleetFragment = new OverviewFragment_Fleet();
    private OverviewFragment_Battle battleFragment = new OverviewFragment_Battle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPager();
        setTabLayout();
    }

    private void setViewPager() {
        overviewViewPager = getView().findViewById(R.id.viewPager_overview);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(overviewFragment);
        fragmentList.add(fleetFragment);
        fragmentList.add(battleFragment);
        overviewViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        tabLayoutOV = getView().findViewById(R.id.tabLayoutOV);
        tabLayoutOV.setupWithViewPager(overviewViewPager);
        tabLayoutOV.getTabAt(0).setText("總覽");
        tabLayoutOV.getTabAt(1).setText("艦隊");
        tabLayoutOV.getTabAt(2).setText("戰鬥");
    }

    public OverviewFragment_Overview getOverviewFragment() {
        return overviewFragment;
    }

    public OverviewFragment_Fleet getFleetFragment() {
        return fleetFragment;
    }

    public OverviewFragment_Battle getBattleFragment() {
        return battleFragment;
    }
}
