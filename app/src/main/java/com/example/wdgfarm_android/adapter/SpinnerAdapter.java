package com.example.wdgfarm_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wdgfarm_android.R;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    String[] data;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, String[] data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(data != null) return data.length;
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.item_spinner_list, viewGroup, false);
        }
        if(data != null){
            String text = data[i];
            ((TextView)view.findViewById(R.id.tv_spinner_item)).setText(text);
        }
        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = inflater.inflate(R.layout.item_spnniner_dropdown_list, viewGroup, false);
        }

        //데이터세팅
        String text = data[i];


        ((TextView) view.findViewById(R.id.tv_spinner_dropdown)).setText(text);

        return view;
    }
}
