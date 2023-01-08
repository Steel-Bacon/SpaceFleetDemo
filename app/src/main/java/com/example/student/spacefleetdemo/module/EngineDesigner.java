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

public class EngineDesigner extends AppCompatActivity{
    private Button btn_reducePower, btn_addPower, btn_increaseSize, btn_reduceSize, btn_cancelEngine, btn_confirmEngine;
    private TextView TV_setPower, TV_setSize, TV_previewEnginePower, TV_previewEngineSize, TV_engineTechPoint, TV_engineLV;
    private EditText ET_engineName;

    private int power = 1, size = 1, powerSet = 0, sizeSet = 0, engineTech = Fragment_Research.getTechLv(Fragment_Research.ENGINE), techPoint = engineTech;
    private String name ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moduledesigner_engine);
        findViewId();
        refresh();
    }

    private void findViewId() {
        btn_reducePower = findViewById(R.id.btn_reducePower);
        btn_reducePower.setOnClickListener(btnOnClickListener);
        btn_addPower = findViewById(R.id.btn_addPower);
        btn_addPower.setOnClickListener(btnOnClickListener);
        btn_reduceSize = findViewById(R.id.btn_reduceSize);
        btn_reduceSize.setOnClickListener(btnOnClickListener);
        btn_increaseSize = findViewById(R.id.btn_increaseSize);
        btn_increaseSize.setOnClickListener(btnOnClickListener);
        btn_cancelEngine = findViewById(R.id.btn_cancelEngine);
        btn_cancelEngine.setOnClickListener(btnOnClickListener);
        btn_confirmEngine = findViewById(R.id.btn_confirmEngine);
        btn_confirmEngine.setOnClickListener(btnOnClickListener);

        TV_setPower = findViewById(R.id.TV_setPower);
        TV_setSize = findViewById(R.id.TV_setSize);
        TV_previewEnginePower = findViewById(R.id.TV_previewEnginePower);
        TV_previewEngineSize = findViewById(R.id.TV_previewEngineSize);
        TV_engineTechPoint = findViewById(R.id.TV_engineTechPoint);
        TV_engineLV = findViewById(R.id.TV_engineLV);
        ET_engineName = findViewById(R.id.ET_engineName);
    }

    private Button.OnClickListener btnOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_addPower:
                    powerSet++;
                    size++;
                    techPoint--;
                    break;
                case R.id.btn_reducePower:
                    size--;
                    techPoint++;
                    powerSet--;
                    break;
                case R.id.btn_reduceSize:
                    sizeSet++;
                    size--;
                    techPoint--;
                    break;
                case R.id.btn_increaseSize:
                    size++;
                    techPoint++;
                    sizeSet--;
                    break;
                case R.id.btn_cancelEngine:
                    finish();
                    break;
                case R.id.btn_confirmEngine:
                    if(ET_engineName.getText().toString().equals("")){
                        Toast.makeText(EngineDesigner.this,"請輸入引擎名稱",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String name = ET_engineName.getText().toString();
                    Engine newDesign = new Engine(power,size,name);
                    DesignFragment_Module.addModuleDesign(newDesign);
                    finish();
                    break;
            }
            refresh();
        }
    };

    private void refresh(){
        power = (powerSet+1)*(powerSet+2)/2 - (sizeSet)*(sizeSet+1)/2;
        power = power*(size+5)/5;//engine size bonus

        TV_setPower.setText("引擎出力：" + powerSet);
        TV_setSize.setText("引擎小型化：" + sizeSet);
        TV_previewEnginePower.setText("出力：" + power);
        TV_previewEngineSize.setText("體積：" + size);
        TV_engineTechPoint.setText("剩餘科技點：" + techPoint);
        TV_engineLV.setText("引擎科技：" + engineTech);

        setButtonClickable(btn_addPower,true);
        setButtonClickable(btn_reducePower,true);
        setButtonClickable(btn_reduceSize,true);
        setButtonClickable(btn_increaseSize,true);
        if(techPoint == 0) {
            setButtonClickable(btn_addPower,false);
            setButtonClickable(btn_reduceSize,false);
        }
        if(powerSet == sizeSet){
            setButtonClickable(btn_reducePower,false);
            setButtonClickable(btn_reduceSize,false);
        }
        if(powerSet == 0) {
            setButtonClickable(btn_reducePower,false);
        }
        if(sizeSet == 0){
            setButtonClickable(btn_increaseSize,false);
        }
    }
}
