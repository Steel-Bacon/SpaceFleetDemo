package com.example.student.spacefleetdemo.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.spacefleetdemo.DesignFragment_Module;
import com.example.student.spacefleetdemo.Fragment_Research;
import com.example.student.spacefleetdemo.R;

import static com.example.student.spacefleetdemo.MainActivity.setButtonClickable;

public class WeaponDesigner extends AppCompatActivity{
    private Button btn_increaseCaliber ,btn_decreaseCaliber, btn_increaseLength, btn_decreaseLength, btn_improveReload, btn_decreaseReload, btn_cancelWeapon, btn_confirmWeapon;
    private TextView TV_setCaliber, TV_setLength, TV_setReload, TV_previewWeaponCaliber, TV_previewWeaponRange, TV_previewWeaponReloadTime, TV_previewWeaponSize, TV_weaponTechPoint, TV_weaponLV;
    private EditText ET_weaponName;

    private int caliber = 1, range = 1, reloadTime = 1, size = 1, setCaliber = 0, setLength = 0, setReload = 0, weaponTech = Fragment_Research.getTechLv(Fragment_Research.WEAPON), techPoint = weaponTech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moduledesigner_weapon);
        findViewId();
        refresh();
    }

    private void findViewId() {
        btn_increaseCaliber = findViewById(R.id.btn_increaseCaliber);
        btn_increaseCaliber.setOnClickListener(btnOnClickListener);
        btn_decreaseCaliber = findViewById(R.id.btn_decreaseCaliber);
        btn_decreaseCaliber.setOnClickListener(btnOnClickListener);
        btn_increaseLength = findViewById(R.id.btn_increaseLength);
        btn_increaseLength.setOnClickListener(btnOnClickListener);
        btn_decreaseLength = findViewById(R.id.btn_decreaseLength);
        btn_decreaseLength.setOnClickListener(btnOnClickListener);
        btn_improveReload = findViewById(R.id.btn_improveReload);
        btn_improveReload.setOnClickListener(btnOnClickListener);
        btn_decreaseReload = findViewById(R.id.btn_decreaseReload);
        btn_decreaseReload.setOnClickListener(btnOnClickListener);
        btn_cancelWeapon = findViewById(R.id.btn_cancelWeapon);
        btn_cancelWeapon.setOnClickListener(btnOnClickListener);
        btn_confirmWeapon = findViewById(R.id.btn_confirmWeapon);
        btn_confirmWeapon.setOnClickListener(btnOnClickListener);

        TV_setCaliber = findViewById(R.id.TV_setCaliber);
        TV_setLength = findViewById(R.id.TV_setLength);
        TV_setReload = findViewById(R.id.TV_setReload);
        TV_previewWeaponCaliber = findViewById(R.id.TV_previewWeaponCaliber);
        TV_previewWeaponRange = findViewById(R.id.TV_previewWeaponRange);
        TV_previewWeaponReloadTime = findViewById(R.id.TV_previewWeaponReloadTime);
        TV_previewWeaponSize = findViewById(R.id.TV_previewWeaponSize);
        TV_weaponTechPoint = findViewById(R.id.TV_weaponTechPoint);
        TV_weaponLV = findViewById(R.id.TV_weaponLV);

        ET_weaponName = findViewById(R.id.ET_weaponName);
    }

    private Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_increaseCaliber:
                    setCaliber++;
                    caliber++;
                    range = (setCaliber+1)*(setLength+1);
                    size++;
                    techPoint--;
                    break;
                case R.id.btn_decreaseCaliber:
                    setCaliber--;
                    caliber--;
                    range = (setCaliber+1)*(setLength+1);
                    size--;
                    techPoint++;
                    break;
                case R.id.btn_increaseLength:
                    setLength++;
                    range = (setCaliber+1)*(setLength+1);
                    techPoint--;
                    break;
                case R.id.btn_decreaseLength:
                    setLength--;
                    range = (setCaliber+1)*(setLength+1);
                    techPoint++;
                    break;
                case R.id.btn_improveReload:
                    setReload++;
                    techPoint--;
                    break;
                case R.id.btn_decreaseReload:
                    setReload--;
                    techPoint++;
                    break;
                case R.id.btn_cancelWeapon:
                    finish();
                    break;
                case R.id.btn_confirmWeapon:
                    if(ET_weaponName.getText().toString().equals("")){
                        Toast.makeText(WeaponDesigner.this,"請輸入武器名稱",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String name = ET_weaponName.getText().toString();
                    Weapon newDesign = new Weapon(caliber, range, reloadTime, size, name);
                    DesignFragment_Module.addModuleDesign(newDesign);
                    finish();
                    break;
            }
            refresh();
        }
    };

    private void refresh() {
        reloadTime = (setCaliber+1)*(setLength+1)/(setReload+1);

        TV_setCaliber.setText("口徑：" + setCaliber);
        TV_setLength.setText("倍徑：" + setLength);
        TV_setReload.setText("裝填：" + setReload);
        TV_previewWeaponCaliber.setText("口徑：" + caliber);
        TV_previewWeaponRange.setText("射程：" + range);
        TV_previewWeaponReloadTime.setText("裝填時間：" + reloadTime);
        TV_previewWeaponSize.setText("體積：" + size);
        TV_weaponTechPoint.setText("剩餘科技點：" + techPoint);
        TV_weaponLV.setText("武器等級：" + weaponTech);

        setButtonClickable(btn_increaseCaliber,true);
        setButtonClickable(btn_decreaseCaliber,true);
        setButtonClickable(btn_increaseLength,true);
        setButtonClickable(btn_decreaseLength,true);
        setButtonClickable(btn_improveReload,true);
        setButtonClickable(btn_decreaseReload,true);
        if(techPoint == 0){
            setButtonClickable(btn_increaseCaliber,false);
            setButtonClickable(btn_increaseLength,false);
            setButtonClickable(btn_improveReload,false);
        }
        if(setCaliber == 0){
            setButtonClickable(btn_decreaseCaliber,false);
        }
        if(setLength == 0){
            setButtonClickable(btn_decreaseLength,false);
        }
        if(setReload == 0){
            setButtonClickable(btn_decreaseReload,false);
        }
        if(reloadTime == 1){
            setButtonClickable(btn_decreaseCaliber,false);
            setButtonClickable(btn_decreaseLength,false);
            setButtonClickable(btn_improveReload,false);
        }
    }
}
