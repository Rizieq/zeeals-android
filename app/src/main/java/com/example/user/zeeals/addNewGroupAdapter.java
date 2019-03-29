package com.example.user.zeeals;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


public class addNewGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    EditText et_sourceName;
    EditText et_sourceLink;
    int adapterCount;
    String sourceName,sourceLink;

    public addNewGroupAdapter(int adapterCount) {
        this.adapterCount = adapterCount;
    }

    public void addCount(){
        this.adapterCount++;
    }

    interface onTextSavedListener{
        void onTextSaved(Source source);
    }
    onTextSavedListener onTextSavedListener;
    public void setOnTextSaved(onTextSavedListener onTextSavedListener) {
        this.onTextSavedListener= onTextSavedListener ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
            et_sourceName = itemView.findViewById(R.id.edit_save_add_new_source_name);
            et_sourceLink = itemView.findViewById(R.id.edit_save_add_new_source_link);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_new_source,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
                et_sourceName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus){
                            sourceName = et_sourceName.getText().toString();
                        }
                        
                    }
                });

                et_sourceLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus){
                            sourceLink = et_sourceLink.getText().toString();
                            onTextSavedListener.onTextSaved(new Source(sourceName,sourceLink));
                            Log.d(TAG, "onFocusChange: LOST FOCUS");
                        }else{
                            Log.d(TAG, "onFocusChange: Has Ofcus TRU");
                        }
                    }
                });

                et_sourceLink.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            Log.d(TAG, "onEditorAction: FOCUS LOST");
                            et_sourceLink.clearFocus();
                            handled = true;
                        }
                        return handled;
                    }
                });
            



    }

    @Override
    public int getItemCount() {
        return adapterCount;
    }

    public int getAdapterCount(){
        return adapterCount;
    }


}
