package com.example.user.zeeals.adapter;

import android.view.View;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SourceViewHolder extends ChildViewHolder {

    private TextView sourceName;
    private TextView sourceLink;


    public SourceViewHolder(View itemView) {
        super(itemView);
        sourceName = itemView.findViewById(R.id.list_item_source_name);
        sourceLink = itemView.findViewById(R.id.list_item_source_link);
    }

    public void setSourceName(String source){
        sourceName.setText(source);
    }

    public void setSourceLink(String source) {
        this.sourceLink.setText(source);
    }

    public TextView getSourceName() {
        return sourceName;
    }

    public TextView getSourceLink() {
        return sourceLink;
    }
}
