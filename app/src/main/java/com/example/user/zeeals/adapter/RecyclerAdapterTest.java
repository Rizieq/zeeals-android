package com.example.user.zeeals.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
//import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecyclerAdapterTest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CHILD = 0;
    public static final int PARENT = 1;
    static List<zSource> datas = new ArrayList<>();
    static List<Integer> children = new ArrayList<>();
    static RecyclerAdapterTest recyclerAdapter;
    public RecyclerView recyclerView;
    static List<Zlink> general;

    public RecyclerAdapterTest(RecyclerView recyclerView,List<Zlink> general){
        this.recyclerView = recyclerView;
        recyclerAdapter = this;
        this.general = new ArrayList<>();
        this.general = general;

        for(int i =0;i<general.size();i++){
            Log.d(TAG, "RecyclerAdapterTest: "+general.get(0).isParent());
        }
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public EditText groupTitle;


        public ParentViewHolder(View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.list_item_group_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = getLayoutPosition();
            int adapterPos = getAdapterPosition();

            zGroup zGroup = (zGroup) general.get(id);
            zSource[] zSources = zGroup.getzSource();

            if (zGroup.getzSource().length != 0) {
                //collapse list
                if (zGroup.isChildrenVisible()) {
                    zGroup.setChildrenVisible(false);
                    for (int i = id + 1; i < id + 1 + zSources.length; i++) {
                        general.remove(id + 1);
                    }
                    recyclerAdapter.notifyItemRangeRemoved(id + 1, zSources.length);
                } else {
                    //expanding list
                    zGroup.setChildrenVisible(true);
                    int index = 0;

                    for (int i = id + 1; i < (id + 1 + zSources.length); i++) {
                        general.add(i, zSources[index]);
                        index++;
                    }
                    recyclerAdapter.notifyItemRangeInserted(id + 1, zSources.length);
                }

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerAdapter.recyclerView.
                        getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if ((id + 1) < general.size()) {
                    if ((id + 1) > lastVisibleItemPosition) {
                        recyclerAdapter.recyclerView.scrollToPosition(id + 1);
                    }
                }

            }
        }
    }


    public  static class ChildsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView sourceName;
        public TextView sourceLink;

        public ChildsViewHolder(View itemView) {
            super(itemView);

            sourceName = itemView.findViewById(R.id.list_item_source_name);
            sourceLink = itemView.findViewById(R.id.list_item_source_link);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: Child name: "+ sourceName.getText().toString());
            Log.d(TAG, "onClick: Child link: "+ sourceLink.getText().toString());
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(general.get(position).isParent()){
            return PARENT;
        }else return CHILD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        //i disini adalah viewType
        if(i==CHILD){
            Log.d(TAG, "onCreateViewHolder: CHILD "+i);
            View itemView = inflater.inflate(R.layout.list_item_source,viewGroup,false);
            return new ChildsViewHolder(itemView);
        }else{
            Log.d(TAG, "onCreateViewHolder: PARENT "+i);
            View itemView = inflater.inflate(R.layout.list_item_group,viewGroup,false);
            return new ParentViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType()==CHILD){
            Log.d(TAG, "onBindViewHolder: CHILD "+viewHolder.getItemViewType());
            TextView sourceName = ((ChildsViewHolder)viewHolder).sourceName;
            TextView sourceLink = ((ChildsViewHolder)viewHolder).sourceLink;
            zSource zSource = (zSource) general.get(i);
            sourceName.setText(zSource.getSourceName());
            sourceLink.setText(zSource.getSourceLink());
        }else{

            Log.d(TAG, "onBindViewHolder: PARENT "+viewHolder.getItemViewType());
            EditText groupName = ((ParentViewHolder)viewHolder).groupTitle;
            zGroup zGroup = (zGroup)general.get(i);
            groupName.setText(zGroup.getName());
        }
    }

    @Override
    public int getItemCount() {
        return general.size();
    }
}
