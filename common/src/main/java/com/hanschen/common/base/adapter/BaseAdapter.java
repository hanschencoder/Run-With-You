package com.hanschen.common.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    protected Context      mContext    = null;
    protected List<T>      mData       = null;
    protected Set<Integer> mSelections = null;
    private   int          mLayoutId   = 0;

    public BaseAdapter(Context context, int layoutId) {
        this(context, layoutId, null);
    }

    public BaseAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mData = data;
        mLayoutId = layoutId;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public void setSelections(Set<Integer> selections) {
        mSelections = selections;
        notifyDataSetChanged();
    }

    public void clearSelections() {
        if (mSelections != null) {
            mSelections.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent, mLayoutId, position);
        bindData(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    abstract void bindData(ViewHolder viewHolder, T data);
}
