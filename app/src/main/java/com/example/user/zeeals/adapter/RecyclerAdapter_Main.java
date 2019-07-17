package com.example.user.zeeals.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.user.zeeals.R;
import com.example.user.zeeals.activity.addGroupAndLinkFragmentHost;
import com.example.user.zeeals.model.Zlink;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zSource;
import java.util.ArrayList;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class RecyclerAdapter_Main extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CHILD = 0;
    private static final int PARENT = 1;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerAdapter_Main recyclerAdapter;
    RecyclerView recyclerView;
    private static List<Zlink> general;
    public String token;
    private Context context;

    RecyclerAdapter_Main(RecyclerView recyclerView, List<Zlink> general, String token, Context context){
        this.recyclerView = recyclerView;
        recyclerAdapter = this;
        RecyclerAdapter_Main.general = new ArrayList<>();
        RecyclerAdapter_Main.general = general;
        this.token = token;
        this.context = context;

    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("StaticFieldLeak")
        static TextView groupTitle;
        @SuppressLint("StaticFieldLeak")
        ImageView arrow;
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
        ConstraintLayout layoutLink;
        ImageView link_avatar,status_indicator;
        Context mContext;

        ChildsViewHolder(View itemView) {
            super(itemView);
            link_avatar = itemView.findViewById(R.id.link_avatar_image);
            layoutLink = itemView.findViewById(R.id.linkLayout);
            sourceName = itemView.findViewById(R.id.list_item_source_name);
            status_indicator = itemView.findViewById(R.id.status_inidcator);
//            sourceLink = itemView.findViewById(R.id.list_item_source_link);
            mContext = itemView.getContext();
        }

        void setImage(String url){
            Glide.with(mContext).load(url).into(link_avatar);
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (viewHolder.getItemViewType()==CHILD){
            setFadeAnimation(viewHolder.itemView);
            ConstraintLayout linkLayout = ((ChildsViewHolder)viewHolder).layoutLink;
            TextView sourceName = ((ChildsViewHolder)viewHolder).sourceName;
//            TextView sourceLink= ((ChildsViewHolder)viewHolder).sourceLink;
            zSource zSource = (zSource) general.get(i);
//            sourceLink.setText(zSource.getLinkKey());
            sourceName.setText(zSource.getTitle());
//            new DownloadImageTask(linkAvatar).execute(zSource.getAvatar());
//            linkAvatar.setImageURI(Uri.parse(zSource.getLink_avatar()));
            ((ChildsViewHolder) viewHolder).setImage(zSource.getAvatar());
//            linkLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openEditLink(i);
//                }
//            });
            if(zSource.getStatus()==0) {
                linkLayout.setBackgroundResource(R.drawable.status_off);
                ((ChildsViewHolder) viewHolder).link_avatar.setColorFilter(Color.parseColor("#5FD4D4D4"));
                ((ChildsViewHolder) viewHolder).sourceName.setTextColor(Color.parseColor("#5F000000"));
                ((ChildsViewHolder) viewHolder).status_indicator.setVisibility(View.INVISIBLE);
            }
            else {
                linkLayout.setBackgroundResource(R.drawable.button_shadow_box);
                ((ChildsViewHolder) viewHolder).link_avatar.setColorFilter(null);
                ((ChildsViewHolder) viewHolder).sourceName.setTextColor(Color.parseColor("#FA000000"));
                ((ChildsViewHolder) viewHolder).status_indicator.setVisibility(View.VISIBLE);
            }
            sourceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEditLink(i);
                }
            });
        }else{
            TextView groupName = ParentViewHolder.groupTitle;
            TextView iconGroup = ((ParentViewHolder)viewHolder).icon;
            final ImageView arrow = ((ParentViewHolder)viewHolder).arrow;

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


//            if(i==general.size()-1){
//                ((ParentViewHolder)viewHolder).expand_collapse(arrow);
//            }
        }
    }



    @Override
    public int getItemCount() {
        return general.size();
    }


    private void openEditLink(int i){
        zSource zSource = (zSource) general.get(i);
        int[] id = {zSource.getGroupLinkId(),zSource.getLinkId()};
        context.startActivity(new Intent(context, addGroupAndLinkFragmentHost.class).putExtra("menuType","editLink").putExtra("position",id));
    }


    private void setFadeAnimation(View view) {
        view.animate().x(50f).y(100f);
    }




}
