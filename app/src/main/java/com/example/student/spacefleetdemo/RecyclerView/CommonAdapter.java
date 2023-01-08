package com.example.student.spacefleetdemo.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>{
    protected Context context;
    protected int layoutId;
    protected List<T> dataList;
    protected LayoutInflater inflater;

    public CommonAdapter(Context context, int layoutId, List<T> dataList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        return ViewHolder.get(context, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(dataList.isEmpty()){
            setNoItemView(holder);
        }else {
            holder.updatePosition(position);
            setItemToHolder(holder, dataList.get(position));
        }
    }

    public abstract void setItemToHolder(ViewHolder holder, T t);

    public abstract void setNoItemView(ViewHolder holder);

    @Override
    public int getItemCount() {
        if(dataList.isEmpty()){
            return 1;
        }else{
            return dataList.size();
        }
    }
}
