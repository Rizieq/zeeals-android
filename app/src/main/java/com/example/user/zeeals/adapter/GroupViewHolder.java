package com.example.user.zeeals.adapter;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.user.zeeals.R;
import com.example.user.zeeals.fragment.editGroupFragment;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class GroupViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder implements editGroupFragment.OnFragmentInteractionListener{

    private EditText groupName;
    private ImageView arrow;
    private ImageView icon;

    public GroupViewHolder(View itemView) {
        super(itemView);
        arrow =  itemView.findViewById(R.id.list_item_group_arrow);
        icon =  itemView.findViewById(R.id.list_item_group_icon);
        groupName = itemView.findViewById(R.id.list_item_group_name);
    }

    public void setGroupName(String group){
        groupName.setText(group);
    }

    public EditText getGroupName() {
        return groupName;
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
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
    public void onFragmentInteraction(String uri) {
        groupName.setText(uri);
    }


}
