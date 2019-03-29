package com.example.user.zeeals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
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
            }
        });


    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, final int flatPosition, final ExpandableGroup group) {
        final Source source = ((Group) group).getItems().get(0);
        holder.setGroupName(source.getGroupName());
        final int position = holder.getAdapterPosition();
        final EditText groupName = holder.getGroupName();

            groupName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String text = groupName.getText().toString();
                        onItemClickListener.OnItemClick(position,text);
                        Log.d(TAG, "onEditorAction: "+position);
                        groupName.clearFocus();
                        handled = true;
                    }
                    return handled;
                }
            });
        



    }

    public void addAll(List<Group> groups) {
        notifyGroupDataChanged();
        notifyDataSetChanged();
    }




}
