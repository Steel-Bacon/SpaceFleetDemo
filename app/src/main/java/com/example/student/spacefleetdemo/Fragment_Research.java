package com.example.student.spacefleetdemo;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_Research extends Fragment {
    private static int researchPoint = 5;
    private TextView tv_researchPoint, tv_shipBodyRs, tv_weaponRs, tv_engineRs;
    private Button btn_shipBody, btn_weapon, btn_engine;
    private static int[] techLv = {0,0,0};
    final public static int SHIPBODY = 0, WEAPON = 1, ENGINE = 2;

    private View.OnClickListener researchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(researchPoint > 0) {
                switch (v.getId()) {
                    case R.id.btn_shipBodyRs:
                        techLv[SHIPBODY]++;
                        break;
                    case R.id.btn_weaponRs:
                        techLv[WEAPON]++;
                        break;
                    case R.id.btn_engineRs:
                        techLv[ENGINE]++;
                        break;
                }
                researchPoint--;
                refresh();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_research,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        findViewId();
    }

    private void findViewId() {
        tv_researchPoint = getView().findViewById(R.id.tv_researchpoint);
        tv_shipBodyRs = getView().findViewById(R.id.tv_shipBodyRs);
        tv_weaponRs = getView().findViewById(R.id.tv_weaponRs);
        tv_engineRs = getView().findViewById(R.id.tv_engineRs);

        btn_shipBody = getView().findViewById(R.id.btn_shipBodyRs);
        btn_shipBody.setOnClickListener(researchOnClickListener);
        btn_weapon = getView().findViewById(R.id.btn_weaponRs);
        btn_weapon.setOnClickListener(researchOnClickListener);
        btn_engine = getView().findViewById(R.id.btn_engineRs);
        btn_engine.setOnClickListener(researchOnClickListener);

        refresh();
    }

    private void refresh(){
        tv_researchPoint.setText("研究點：" + researchPoint);
        tv_shipBodyRs.setText("艦體 LV." + techLv[SHIPBODY]);
        tv_weaponRs.setText("武器 LV." + techLv[WEAPON]);
        tv_engineRs.setText("引擎 LV." + techLv[ENGINE]);
        if(researchPoint == 0){
            btn_shipBody.setClickable(false);
            btn_shipBody.setTextColor(0x55000000);
            btn_weapon.setClickable(false);
            btn_weapon.setTextColor(0x55000000);
            btn_engine.setClickable(false);
            btn_engine.setTextColor(0x55000000);
        }else{
            btn_shipBody.setClickable(true);
            btn_shipBody.setTextColor(0xff000000);
            btn_weapon.setClickable(true);
            btn_weapon.setTextColor(0xff000000);
            btn_engine.setClickable(true);
            btn_engine.setTextColor(0xff000000);
        }
    }

    public static int getTechLv(int techType) {
        return techLv[techType];
    }

    public static void addResearchPoint(int researchPoint){
        Fragment_Research.researchPoint += researchPoint;
    }
}
