package com.example.user.zeeals.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zeeals.R;
import com.example.user.zeeals.activity.addGroupAndLinkFragmentHost;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.service.UserClient;
import com.example.user.zeeals.util.ServerAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class RecyclerAdapter_Main extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CHILD = 0;
    private static final int PARENT = 1;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerAdapter_Main recyclerAdapter;
    public RecyclerView recyclerView;
    private static List<Zlink> general;
    private static List<Zlink> backup;
    private Zlink mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    public String token;
    private UserClient userClient;
    private Context context;

    RecyclerAdapter_Main(RecyclerView recyclerView, List<Zlink> general, String token, Context context){
        this.recyclerView = recyclerView;
        recyclerAdapter = this;
        RecyclerAdapter_Main.general = new ArrayList<>();
        RecyclerAdapter_Main.general = general;
        this.token = token;
        this.context = context;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ServerAPI.zeealseRESTAPI)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        userClient = retrofit.create(UserClient.class);

    }



    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("StaticFieldLeak")
        static TextView groupTitle;
        @SuppressLint("StaticFieldLeak")
        public ImageView arrow;
        public TextView icon;


        ParentViewHolder(View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.list_item_group_name);
            arrow = itemView.findViewById(R.id.list_item_group_arrow);
            icon = itemView.findViewById(R.id.list_item_group_icon);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand_collapse(arrow);
                }

            });


        }

        void expand_collapse(ImageView v) {
            int id = getLayoutPosition();
            zGroup zGroup = (zGroup) general.get(id);
            List<zSource> zSources = zGroup.getChildLink();
            if (zGroup.isHasChild()) {
                //collapse list
                if (zGroup.isChildrenVisible()) {

                    if(!zGroup.getChildLink().isEmpty()){
                        animateCollapse(v);
                        zGroup.setChildrenVisible(false);
                        if (id + 1 + zSources.size() > id + 1) {
                            general.subList(id + 1, id + 1 + zSources.size()).clear();
                        }

                        /* primitive ways haha, just in case alt+enter above is fckdup */
//                                for (int i = id + 1; i < id + 1 + zSources.size(); i++) {
//                                    general.remove(id + 1);
//                                }


                        recyclerAdapter.notifyItemRangeRemoved(id + 1, zSources.size());
                    }

                } else {
                    //expanding list
                    if(!zGroup.getChildLink().isEmpty()){
                        animateExpand(v);
                        zGroup.setChildrenVisible(true);
                        int index = 0;

                        for (int i = id + 1; i < (id + 1 + zSources.size()); i++) {
                            general.add(i, zSources.get(index));
                            index++;
                        }
                        recyclerAdapter.notifyItemRangeInserted(id + 1, zSources.size());
                    }

                }
            }
        }

        private static void animateExpand(ImageView v) {

            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            v.setAnimation(rotate);
        }

        private static void animateCollapse(ImageView v) {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            v.setAnimation(rotate);
        }
    }


    public  static class ChildsViewHolder extends RecyclerView.ViewHolder {
        TextView sourceName;
        TextView sourceLink;
        FrameLayout layoutLink;

        ChildsViewHolder(View itemView) {
            super(itemView);
            layoutLink = itemView.findViewById(R.id.linkLayout);
            sourceName = itemView.findViewById(R.id.list_item_source_name);
            sourceLink = itemView.findViewById(R.id.list_item_source_link);

        }

    }


    @Override
    public int getItemViewType(int position) {
        if(general.get(position).isParent()){
            return PARENT;
        }else return CHILD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType==CHILD){
            View itemView = inflater.inflate(R.layout.list_item_source,viewGroup,false);
            return new ChildsViewHolder(itemView);
        }else{
            View itemView = inflater.inflate(R.layout.list_item_group,viewGroup,false);
            return new ParentViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (viewHolder.getItemViewType()==CHILD){
            TextView sourceName = ((ChildsViewHolder)viewHolder).sourceName;
            TextView sourceLink= ((ChildsViewHolder)viewHolder).sourceLink;
            FrameLayout linkLayout = ((ChildsViewHolder)viewHolder).layoutLink;
            final zSource zSource = (zSource) general.get(i);
            sourceLink.setText(zSource.getUrl());
            sourceName.setText(zSource.getTitle());
            linkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] id = {zSource.getGroupLinkId(),zSource.getLinkId()};
                    Log.d(TAG, "onClick: Child clicked");
                    context.startActivity(new Intent(context, addGroupAndLinkFragmentHost.class).putExtra("menuType","editLink").putExtra("position",id));
                }
            });

        }else{
            TextView groupName = ParentViewHolder.groupTitle;
            TextView iconGroup = ((ParentViewHolder)viewHolder).icon;
            zGroup zGroup = (zGroup)general.get(i);
            groupName.setText(zGroup.getTitle());

            String icon = new String (Character.toChars(Integer.parseInt(
                    zGroup.getUnicode(), 16)));

            iconGroup.setText(icon);
            groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, addGroupAndLinkFragmentHost.class).putExtra("menuType","editGroup").putExtra("position",i));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return general.size();
    }

    void deleteItem(final int position, final RecyclerView.ViewHolder viewHolder) {

        if(viewHolder.getItemViewType()==PARENT){
            zGroup zGroup = ((zGroup)general.get(position));
            if(zGroup.isChildrenVisible()){
                ((ParentViewHolder) viewHolder).expand_collapse(((ParentViewHolder) viewHolder).arrow);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Hapus Group")
                    .setMessage("Apakah anda yakin menghapus group ini ? ")
                    .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int deletePosition = ((zGroup)general.get(position)).getGroupLinkId();
                            Call<ResponseBody> call = userClient.delete(token,deletePosition);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if(response.isSuccessful()){
                                        general.remove(position);
                                        notifyItemRemoved(position);

                                        Gson gson = new Gson();
                                        String json = gson.toJson(general);
                                        context.getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();
                                    }else{
                                        Toast.makeText(context,"Failed to delete group",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    Toast.makeText(context,"Failed to delete group",Toast.LENGTH_SHORT).show();
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

        }else{

            //TODO FIX THIS
            final zSource zSource = (zSource) general.get(position);
            final int[] groupLinkPosition= searchGroupPosition(zSource.getGroupLinkId(),zSource.getLinkId());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Hapus link")
                    .setMessage("Apakah anda yakin menghapus link ini ? ")
                    .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int deletePosition = zSource.getLinkId();
                            Call<ResponseBody> call = userClient.deleteLink(token,deletePosition);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if(response.isSuccessful()){

                                        String groupJSON = context.getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
                                        Type listType = new TypeToken<List<zGroup>>(){}.getType();
                                        ArrayList<Zlink> zLink;
                                        zLink = new Gson().fromJson(groupJSON,listType);
                                        assert zLink != null;
                                        zGroup zGroup = ((zGroup)zLink.get(groupLinkPosition[0]));
                                        zGroup.getChildLink().remove(groupLinkPosition[1]);

                                        zGroup zGroup2 = ((zGroup)general.get(groupLinkPosition[0]));
                                        zGroup2.getChildLink().remove(groupLinkPosition[1]);
                                        general.remove(position);
                                        notifyItemRangeChanged(0,general.size());
                                        Gson gson = new Gson();
                                        String json = gson.toJson(zLink);
                                        context.getSharedPreferences("TOKEN",MODE_PRIVATE).edit().putString("GROUPLIST",json).apply();
                                    }else{
                                        Toast.makeText(context,"Failed to delete group",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    Toast.makeText(context,"Failed to delete group",Toast.LENGTH_SHORT).show();
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
        }

    }

    private void undoDelete() {
//        general.remove(mRecentlyDeletedItem);
//        notifyItemRemoved(mRecentlyDeletedItemPosition);
//        general.add(mRecentlyDeletedItemPosition,mRecentlyDeletedItem);
//        notifyItemInserted(mRecentlyDeletedItemPosition);
        notifyDataSetChanged();
    }

    private int[] searchGroupPosition(int groupId,int linkId){
        int groupPosition = 1000;
        int linkPosition= 1000;

//        String groupJSON = context.getSharedPreferences("TOKEN",MODE_PRIVATE).getString("GROUPLIST",null);
//
//        Type listType = new TypeToken<List<zGroup>>(){}.getType();
//        zLink = new ArrayList<>();
//        zLink = new Gson().fromJson(groupJSON,listType);

        for(int i = 0; i<general.size(); i++){
            if(general.get(i) instanceof zGroup){
                if(((zGroup)general.get(i)).getGroupLinkId()==groupId){
                    groupPosition=i;
                    break;
                }
            }
        }
        zGroup zGroup = ((zGroup)general.get(groupPosition));
        for(int i =0;i<zGroup.getChildLink().size();i++){
            if(zGroup.getChildLink().get(i).getLinkId()==linkId){
                linkPosition=i;
                break;
            }
        }

        return new int[]{groupPosition,linkPosition};
    }

}
