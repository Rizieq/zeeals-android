package com.example.user.zeeals.adapter;

import android.support.v7.widget.RecyclerView;

interface SwipeToDeleteCallbackListener {
    void onSwiped(RecyclerView.ViewHolder holder,int direction, int position);
}
