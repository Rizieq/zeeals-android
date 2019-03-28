package com.example.user.zeeals;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
        this.adapterCount+=1;
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

        sourceName=et_sourceName.getText().toString();
        sourceLink=et_sourceLink.getText().toString();


        et_sourceName.addTextChangedListener(sourceWatch);
        et_sourceLink.addTextChangedListener(sourceWatch);

        et_sourceName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sourceName=et_sourceName.getText().toString();
                }
            }
        });
        et_sourceLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sourceLink=et_sourceLink.getText().toString();
                    onTextSavedListener.onTextSaved(new Source(sourceName,sourceLink));
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return adapterCount;
    }

    private TextWatcher sourceWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sourceName=et_sourceName.getText().toString();
            sourceLink=et_sourceLink.getText().toString();

            Log.d(TAG, "onBindViewHolder: Name"+sourceName);
            Log.d(TAG, "onBindViewHolder: Link"+sourceLink);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

}
