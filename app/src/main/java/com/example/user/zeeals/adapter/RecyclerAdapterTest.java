package com.example.user.zeeals.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.loginScreen;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.ServerAPI;
//import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;
import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class RecyclerAdapterTest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CHILD = 0;
    public static final int PARENT = 1;
    static RecyclerAdapterTest recyclerAdapter;
    public RecyclerView recyclerView;
    static List<Zlink> general;
    public Zlink mRecentlyDeletedItem;
    public int mRecentlyDeletedItemPosition;
    public View mActivity;
    public String token;
    UserClient userClient;
    Context context;

    public RecyclerAdapterTest(RecyclerView recyclerView, List<Zlink> general,View v,String token,Context context){
        this.recyclerView = recyclerView;
        recyclerAdapter = this;
        this.general = new ArrayList<>();
        this.general = general;
        this.mActivity = v;
        this.token = token;
        this.context = context;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ServerAPI.zeealseRESTAPI)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        userClient = retrofit.create(UserClient.class);

    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groupTitle;
        public ImageView arrow;


        public ParentViewHolder(View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.list_item_group_name);
            itemView.setOnClickListener(this);
            arrow = itemView.findViewById(R.id.list_item_group_arrow);
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        @Override
        public void onClick(View v) {
            int id = getLayoutPosition();

            zGroup zGroup = (zGroup) general.get(id);
            Log.d(TAG, "onClick: "+id);
            List<zSource> zSources = zGroup.getzSource();
            if (!zGroup.isHasNoChild()) {

                //collapse list
                if (zGroup.isChildrenVisible()) {

                    if(!zGroup.getzSource().isEmpty()){
                        animateCollapse();
                        zGroup.setChildrenVisible(false);
                        for (int i = id + 1; i < id + 1 + zSources.size(); i++) {
                            general.remove(id + 1);
                        }
                        recyclerAdapter.notifyItemRangeRemoved(id + 1, zSources.size());
                    }

                } else {
                    //expanding list
                    if(!zGroup.getzSource().isEmpty()){
                        animateExpand();
                        zGroup.setChildrenVisible(true);
                        int index = 0;

                        for (int i = id + 1; i < (id + 1 + zSources.size()); i++) {
                            general.add(i, zSources.get(index));
                            index++;
                        }
                        recyclerAdapter.notifyItemRangeInserted(id + 1, zSources.size());
                    }

                }

//                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerAdapter.recyclerView.
//                        getLayoutManager()).findLastCompletelyVisibleItemPosition();
//
//                if ((id + 1) < general.size()) {
//                    if ((id + 1) > lastVisibleItemPosition) {
//                        recyclerAdapter.recyclerView.scrollToPosition(id + 1);
//                    }
//                }

            }else{
                if(zGroup.isChildrenVisible()){
                    animateCollapse();
                    zGroup.setChildrenVisible(false);
                }else {
                    animateExpand();
                    zGroup.setChildrenVisible(true);
                }
            }
        }
    }


    public  static class ChildsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView sourceName;

        public ChildsViewHolder(View itemView) {
            super(itemView);

            sourceName = itemView.findViewById(R.id.list_item_source_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: Child name: "+ sourceName.getText().toString());
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
            TextView sourceName = ((ChildsViewHolder)viewHolder).sourceName;
            zSource zSource = (zSource) general.get(i);
            sourceName.setText(zSource.getSourceName());
        }else{
            TextView groupName = ((ParentViewHolder)viewHolder).groupTitle;
            zGroup zGroup = (zGroup)general.get(i);
            groupName.setText(zGroup.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return general.size();
    }

    public void deleteItem(final int position,RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "deleteItem: "+position);
        Log.d(TAG, "deleteItem: "+general.get(position));
        if(viewHolder.getItemViewType()==PARENT){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Hapus Group")
                    .setMessage("Apakah anda yakin menghapus group ini ? ")
                    .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Call<ResponseBody> call = userClient.delete(token,((zGroup)general.get(position)).getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.d(TAG, "onResponse: "+response);
                                    general.remove(position);
                                    notifyItemRemoved(position);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRecentlyDeletedItem = general.get(position);
                            mRecentlyDeletedItemPosition = position;
                            undoDelete();
                        }
                    }).setCancelable(false);
            AlertDialog alertOut = builder.create();
            alertOut.show();




//            showUndoSnackbar(((zGroup)general.get(position)).getId());

        }

    }

    private void showUndoSnackbar(final int position) {
        View view = mActivity;

        Snackbar snackbar = Snackbar.make(view, "Group Deleted",Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.rgb(199, 149, 109));
        snackbar.setAction("UNDO?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });

        snackbar.show();

        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {


            }
        });


    }

    public void undoDelete() {
        general.remove(mRecentlyDeletedItem);
        notifyItemRemoved(mRecentlyDeletedItemPosition);

        general.add(mRecentlyDeletedItemPosition,mRecentlyDeletedItem);
//        zGroup g = (zGroup) mRecentlyDeletedItem;

//        Call<zGroup> call = userClient.create(token,g);
//        call.enqueue(new Callback<zGroup>() {
//            @Override
//            public void onResponse(Call<zGroup> call, Response<zGroup> response) {
//                Log.d(TAG, "onResponse: "+response);
//            }
//
//            @Override
//            public void onFailure(Call<zGroup> call, Throwable t) {
//
//            }
//        });

        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}
