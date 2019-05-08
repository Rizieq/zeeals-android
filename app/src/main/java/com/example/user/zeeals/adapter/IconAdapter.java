package com.example.user.zeeals.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.list_icon,null);
        }

        TextView iconView = convertView.findViewById(R.id.tv_icon);
//        Typeface fontawesome =  Typeface.createFromAsset(context.getAssets(),"fonts/fontawesomewebfont.ttf");
//        iconView.setTypeface(fontawesome);
        iconView.setText(new String (Character.toChars(Integer.parseInt(
                icon.get(position), 16))));

        return convertView;
    }
}
