package com.example.student.spacefleetdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.student.spacefleetdemo.RecyclerView.ShipClassInfoAdapter;
import com.example.student.spacefleetdemo.ship.ShipDesign;
import com.example.student.spacefleetdemo.ship.shipDesigner;

import java.util.ArrayList;

public class DesignFragment_Ship extends Fragment{
    private Button btn_newShipDesign;
    private RecyclerView RV_shipDesign;
    private static ArrayList<ShipDesign> shipDesignList = new ArrayList<>();
    private static ShipClassInfoAdapter shipClassInfoAdapter = new ShipClassInfoAdapter(shipDesignList);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.designfragment_ship,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewId();
    }

    private void findViewId() {
        btn_newShipDesign = getView().findViewById(R.id.btn_newShipDesign);
        btn_newShipDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), shipDesigner.class);
                startActivity(intent);
            }
        });

        RV_shipDesign = getView().findViewById(R.id.RV_shipDesign);
        RV_shipDesign.setAdapter(shipClassInfoAdapter);
        RV_shipDesign.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static void addShipDesign(ShipDesign shipDesign){
        shipDesignList.add(shipDesign);
        shipClassInfoAdapter.notifyDataSetChanged();
    }

    public static ArrayList<ShipDesign> getShipDesignList() {
        return shipDesignList;
    }
}
