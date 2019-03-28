package com.example.user.zeeals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class groupAdapter extends ExpandableListUtils<GroupViewHolder,SourceViewHolder> {
    private static final String TAG = "groupAdapter";

    public groupAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    interface OnItemClickListener{
        void OnItemClick(int position,CharSequence newName);
    }
    OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnChildClickListener{
        void OnChildClick(int position,CharSequence newSourceName, CharSequence newSourceLink,int groupPosition);
    }
    OnChildClickListener onChildClickListener;
    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener= onChildClickListener;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group,parent,false);
        return new GroupViewHolder(view);

    }

    @Override
    public SourceViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_source,parent,false);
        return new SourceViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final SourceViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        final Source source = ((Group) group).getItems().get(childIndex);
        Log.d(TAG, "onBindChildViewHolder: SourceName: "+source.getSourceName());
        Log.d(TAG, "onBindChildViewHolder: SourceLink: "+source.getSourceLink());
        Log.d(TAG, "onBindChildViewHolder: GroupPosition: "+source.getGroupPosition());
        holder.setSourceName(source.getSourceName());
        holder.setSourceLink(source.getSourceLink());
        TextView childName = holder.getSourceName();
        childName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = source.getSourceName();
                CharSequence link = source.getSourceLink();
                onChildClickListener.OnChildClick(childIndex,text,link,source.getGroupPosition());
                Log.d(TAG, "onClick: PosisiGroup: "+source.getGroupPosition());
            }
        });


    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, final int flatPosition, final ExpandableGroup group) {
        final Source source = ((Group) group).getItems().get(0);
        holder.setGroupName(source.getGroupName());
        final int position = holder.getAdapterPosition();
        final TextView groupName = holder.getGroupName();
        groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Adapter position: "+position);
                Log.d(TAG, "onClick: flatPosition: "+flatPosition);
                CharSequence text = groupName.getText();
                onItemClickListener.OnItemClick(position,text);
            }
        });


    }

    public void addAll(List<Group> groups) {
        notifyGroupDataChanged();
        notifyDataSetChanged();
    }




}
