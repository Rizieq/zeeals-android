package com.example.user.zeeals.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.zeeals.R;

import java.util.ArrayList;

public class IconAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> icon;

    public IconAdapter(Context c, ArrayList<String> i) {
        context=c;
        icon=i;
    }

    @Override
    public int getCount() {
        return icon.size();
    }

    @Override
    public Object getItem(int position) {
        return icon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconHolder holder;
        if(convertView==null){
            convertView=View.inflate(context,R.layout.list_icon,null);
            holder = new IconHolder();
            holder.text=convertView.findViewById(R.id.tv_icon);
            convertView.setTag(holder);
        }else{
            holder = (IconHolder)convertView.getTag();
        }
        holder.text.setText(new String (Character.toChars(Integer.parseInt(
                icon.get(position), 16))));
        return convertView;
    }

    public class IconHolder {
        TextView text;
    }

}
