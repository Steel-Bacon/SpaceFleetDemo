package com.example.student.spacefleetdemo.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> viewList;
    private View layoutView;
    private Context context;
    private int position;

    public ViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        this.context = context;
        layoutView = itemView;
        viewList = new SparseArray<View>();
    }


    public static ViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,false);
        return new ViewHolder(context, itemView, parent);
    }

    public <T extends View> T getView(int viewId) {
        View view = viewList.get(viewId);
        if (view == null){
            view = layoutView.findViewById(viewId);
            viewList.put(viewId, view);
        }
        return (T) view;
    }

    public void updatePosition(int position) {
        this.position = position;
    }

    public int getItemPosition() {
        return position;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}