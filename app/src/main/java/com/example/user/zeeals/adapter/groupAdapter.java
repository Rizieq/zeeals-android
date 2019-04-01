package com.example.user.zeeals.adapter;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.model.Group;
import com.example.user.zeeals.model.Source;
import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class groupAdapter extends ExpandableListUtils<GroupViewHolder,SourceViewHolder> {
    private static final String TAG = "groupAdapter";
    private List<? extends ExpandableGroup> listGroup;

    public groupAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
        this.listGroup = groups;

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

    interface getContextListener{
        void gettingContext(Context context);
    }
    getContextListener gettingContext;
    public void setGettingContext(getContextListener Context) {
        this.gettingContext= Context;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group,parent,false);
        return new GroupViewHolder(view);

    }

    @Override
    public SourceViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_source,parent,false);

        final View viewTumbal = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_source,parent,false);
        setGettingContext(new getContextListener() {
            @Override
            public void gettingContext(Context context) {
                gettingContext.gettingContext(viewTumbal.getContext());
            }
        });
        return new SourceViewHolder(viewTumbal);
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


    public void deleteChild(int position) {
        listGroup.remove(position);
        notifyGroupDataChanged();
        notifyItemRemoved(position);
    }

    public void restoreChild(Source source, int parentPosition, int childPosition ){
        listGroup.get(parentPosition).getItems().add(childPosition,source);
    }

    public void addAll(List<Group> groups) {
        notifyGroupDataChanged();
        notifyDataSetChanged();
    }




}
